//
// Copyright (C) 2011 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
//
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
//
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//
package gov.nasa.jpf.inspector.client;

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.JPF.Status;
import gov.nasa.jpf.inspector.JPFInspectorFacade;
import gov.nasa.jpf.inspector.client.parser.CommandParserFactory;
import gov.nasa.jpf.inspector.client.parser.CommandParserInterface;
import gov.nasa.jpf.inspector.common.ConsoleInformation;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorParsingErrorException;
import gov.nasa.jpf.inspector.frontends.swing.AuxiliaryInspectorPanel;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.inspector.utils.InspectorConfiguration;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the JPF Inspector client.
 * All the commands use this concrete class rather than the interface.
 * This is the only implementatino of {@link JPFInspectorClientInterface}.
 */
public class JPFInspectorClient implements JPFInspectorClientInterface {
  private final PrintStream outputStream;
  private final JPFInspectorBackEndInterface inspector;
  private final CallbackExecutionDecorator cbExecutionDecorator;

  private final CommandRecorder recorder;
  private final List<AuxiliaryInspectorPanel> listeners = new ArrayList<>();

  public JPFInspectorClient (String target, PrintStream outStream) {
    this(target, outStream, outStream);
  }
  public JPFInspectorClient (String target, PrintStream outStream, PrintStream callbackOutStream) {

    if (outStream == null) {
      throw new IllegalArgumentException("Output stream not specified (null).");
    }

    this.recorder = new CommandRecorder(target, outStream);

    // Create the fully decorated callbacks handler.
    JPFClientCallbackHandler callbacks = new JPFClientCallbackHandler(listeners, callbackOutStream);
    CallbackRecordingDecorator cbRecDecorator = new CallbackRecordingDecorator(callbacks, recorder);
    this.cbExecutionDecorator = new CallbackExecutionDecorator(recorder, cbRecDecorator, System.out);

    this.outputStream = outStream;
    this.inspector = JPFInspectorFacade.getInspectorBackend(cbExecutionDecorator);
  }

  @Override
  public void executeCommand (String cmdStr, ExecutionContext context) {
    CommandParserInterface parser = CommandParserFactory.getClientCommandParser();
    ClientCommandInterface cmd = parseCommand(cmdStr, parser, context);
    executeCommand(cmd, context);
  }

  public void executeCommandOrCallback (String cmdStr) {
    CommandParserInterface parser = CommandParserFactory.getRecordCommandParser();
    ClientCommandInterface cmd = parseCommand(cmdStr, parser, ExecutionContext.FROM_SWING_TERMINAL);
    executeCommand(cmd, ExecutionContext.FROM_SWING_TERMINAL);
    // TODO possible refactoring: ExecutionContext can be an InspectorClient field, it is set only once anyway.
  }

  private ClientCommandInterface parseCommand(String cmdStr, CommandParserInterface parser, ExecutionContext context) {

    // Trim left white space
    cmdStr = trimLeftWhitspace(cmdStr);
    if (cmdStr == null) {
      return null;
    }

    // Ignore comments
    if (cmdStr.charAt(0) == '#') {
      // Remove "# " if present
      if ((cmdStr.length() > 1) && (cmdStr.charAt(1) == ' ')) {
        cmdStr = cmdStr.substring(2);
      }
      recordComment(cmdStr);
      return null;
    }

    // Parse the command and process errors
    ClientCommandInterface cmd = null;
    try {
      cmd = parser.parseCommand(cmdStr);
    } catch (JPFInspectorParsingErrorException e) {
      if (context == ExecutionContext.FROM_SWING_TERMINAL) {
        outputStream.println("cmd>" + cmdStr);
      }
      outputStream.println(e.getMessage());
      outputStream.println(e.expressError(ConsoleInformation.MAX_ERROR_LINE_LENGTH));

      recordComment("ERR:  Error parsing the command \"" + cmdStr + "\".");

      recordComment(e.getMessage());
      recordComment(e.expressError(JPFInspectorParsingErrorException.DEFAULT_LINE_LENGTH));
    }
    return cmd;
  }

  public static String trimLeftWhitspace(String cmdStr) {
    int i = 0;
    int maxLen = cmdStr.length();
    while ((i < maxLen) && isWhiteSpace(cmdStr.charAt(i))) {
      i++;
    }
    if (i == maxLen) {
      return null;
    }
    cmdStr = cmdStr.substring(i);
    return cmdStr;
  }

  private void executeCommand(ClientCommandInterface cmd, ExecutionContext context) {
    if (cmd == null) {
      return;
    }

    // Previously, the following try-catch block was enclosed in a "synchronized (recorder)" block.
    // However, any utility of this block was dubious for the following reasons:
    //  1. The JPF thread and commands don't usually run at the same time because safe mode prevents it.
    //  2. The only important command that doesn't obey this rule is "break" and you don't usually want
    //     to reexecute records with "break" because they wouldn't be deterministic anyway.
    //  3. Now that we permit custom commands, commands may take arbitrarily long and the synchronized block
    //     was susceptible to cause deadlocks.
    // If the removal of this block causes problems, it may be reinstated.

    try {
      // In Swing, we must echo the prompt.
      if (!cmd.isHiddenCommand() && (context == ExecutionContext.FROM_SWING_TERMINAL)) {
        outputStream.println("cmd>" + cmd.getNormalizedCommand());
      }

      // Record.
      cmd.recordCommand(recorder);

      // Execute.
      if (isSafe(cmd)) {
        cmd.execute(this, inspector, outputStream);
      }

    } catch (Throwable e) {
      outputStream.println("ERR: Generic error while processing a command:");
      e.printStackTrace(outputStream);
      recordComment("ERR: Generic error while processing a command:");
      recordComment(e.getMessage());
    }

    for (AuxiliaryInspectorPanel panel : listeners) {
      panel.fireCommandExecutedOrCallbackReceived();
    }
  }

  /**
   * Returns true if safe mode is disabled or if the command is safe. Otherwise prints an error message.
   *
   * This is not very thread-safe because after this check is over, the Inspector may be already paused.
   * That is okay, however, as the "break" command is the only one that doesn't make sense when paused and it
   * does nothing when pause is already in progress. And, there is no way to get out of pause except by launching
   * an instruction, and this thread is the only one capable of launching instructions.
   *
   * @param cmd Command instance.
   */
  private boolean isSafe(ClientCommandInterface cmd) {
    if (InspectorConfiguration.getInstance().isSafeModeActive()) {
      boolean paused = inspector.isPaused();
      if (!paused && !cmd.isSafeToExecuteWhenNotPaused()) {
        outputStream.println("ERR: This command may only be used when JPF has started and is paused. If JPF is not already started, then start it using 'run' and if it is already running, then pause it using 'break'. Then you will be able to execute this command. Alternatively, you may disable safe mode in the configuration file by setting 'jpf-inspector.safe-mode = false'.");
        recordComment("The previous command failed because safe mode disabled it.");
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean isPaused() {
    return inspector.isPaused();
  }

  @Override
  public void connect2JPF (JPF jpf) throws JPFInspectorGenericErrorException {
    // Recording cannot be used in the method, so far, because it would cause a deadlock with the "recorder" sync object.

    if (jpf == null) {
      throw new IllegalArgumentException("JPF parameter cannot be null.");
    }

    if (jpf.getStatus() != Status.NEW) {
      throw new IllegalArgumentException("Invalid JPF state. JPF is running or terminated model checking.");
    }

    try {
      inspector.bindWithJPF(jpf);
    } catch (JPFInspectorGenericErrorException e) {
      outputStream.println(e.getMessage());

      throw e;
    }
  }

  @Override
  public void addInspectorListener(AuxiliaryInspectorPanel auxiliaryInspectorPanel) {
    listeners.add(auxiliaryInspectorPanel);
  }

  @Override
  public JPFInspectorBackEndInterface getServer() {
    return this.inspector;
  }

  /**
   * Adds comment into list of executed commands. (Append comment to last executed command of callbacks)
   */
  public void recordComment (String message) {
    recorder.addComment(message);
  }

  /**
   * Gets the command recorder.
   *
   * The command recorder is created once, in the constructor.
   */
  public CommandRecorder getCommandRecorder () {
    return recorder;
  }

  // Get currently used callbacks
  public CallbackExecutionDecorator getDecoratedCallbacks () {
    return cbExecutionDecorator;
  }

  private static boolean isWhiteSpace (char c) {
    return (c == ' ') || (c == '\t') || (c == '\n');
  }

}
