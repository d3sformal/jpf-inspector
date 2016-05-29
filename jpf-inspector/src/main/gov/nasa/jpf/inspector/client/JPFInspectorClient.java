package gov.nasa.jpf.inspector.client;

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.JPF.Status;
import gov.nasa.jpf.inspector.JPFInspectorFacade;
import gov.nasa.jpf.inspector.client.parser.CommandParserFactory;
import gov.nasa.jpf.inspector.client.parser.CommandParserInterface;
import gov.nasa.jpf.inspector.interfaces.InspectorCallBacks;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorParsingErrorException;

import java.io.PrintStream;

public class JPFInspectorClient implements JPFInspectorClientInterface {
  private final PrintStream outputStream;
  private final JPFInspectorBackEndInterface inspector;
  private final CallbackExecutionDecorator cbExecutionDecorator;

  private final CommandRecorder recorder;

  public JPFInspectorClient (String target, PrintStream outStream) {
    this(target, outStream, new JPFClientCallbackHandler(outStream));
  }

  public JPFInspectorClient (String target, PrintStream outStream, InspectorCallBacks callbacks) {
    if (outStream == null) {
      throw new IllegalArgumentException("Output stream not specified (null)");
    }

    if (callbacks == null) {
      throw new IllegalArgumentException("callbacks not specified (null)");
    }

    this.outputStream = outStream;
    recorder = new CommandRecorder(target, outStream);

    // Decorate the callbacks
    CallbackRecordingDecorator cbRecDecorator = new CallbackRecordingDecorator(callbacks, recorder);
    cbExecutionDecorator = new CallbackExecutionDecorator(recorder, cbRecDecorator, System.out);
    inspector = JPFInspectorFacade.getInspectorBackend(cbExecutionDecorator);
  }

  @Override
  public void executeCommand (String cmdStr) {
    CommandParserInterface parser = CommandParserFactory.getClientCommandParser();
    ClientCommandInterface cmd = parseCommand(cmdStr, parser);
    executeCommand(cmd);
  }

  public void executeCommandOrCallback (String cmdStr) {
    CommandParserInterface parser = CommandParserFactory.getRecordCommandParser();
    ClientCommandInterface cmd = parseCommand(cmdStr, parser);
    executeCommand(cmd);
  }

  public ClientCommandInterface parseCommand (String cmdStr, CommandParserInterface parser) {
    // Prepare the input

    // Trim left white space
    int i = 0;
    int maxLen = cmdStr.length();
    while (i < maxLen && isWhiteSpace(cmdStr.charAt(i))) {
      i++;
    }
    if (i == maxLen) {
      return null; // Empty line (only white chars)
    }
    cmdStr = cmdStr.substring(i);

    // Ignore comments
    if (cmdStr.charAt(0) == '#') {
      // Remove "# " if present
      if (cmdStr.length() > 1 && cmdStr.charAt(1) == ' ') {
        cmdStr = cmdStr.substring(2);
      }
      recordComment(cmdStr);
      return null;
    }

    // Parse the command and process errors
    ClientCommandInterface cmd = null;
    try {
      cmd = parser.parseCommands(cmdStr);
    } catch (JPFInspectorParsingErrorException e) {
      outputStream.println("cmd>" + cmdStr);
      outputStream.println(e.getMessage());

      // TODO - Extend/replace outStream to be able to report line length - not use magic constant 50
      outputStream.println(e.expressError(50));

      recordComment("ERR:  Error parsing \"" + cmdStr + "\" command");

      recordComment(e.getMessage());
      recordComment(e.expressError(JPFInspectorParsingErrorException.DEFAULT_LINE_LENGTH));
    }
    return cmd;
  }

  public void executeCommand (ClientCommandInterface cmd) {
    if (cmd == null) {
      return;
    }
    // To serialize recordingof executed commands and commands related to command execution
    synchronized (recorder) {
      try {
        if (cmd.isHiddenCommand() == false) {
          outputStream.println("cmd>" + cmd.getNormalizedCommand());
        }
        cmd.recordCommand(recorder);
        cmd.executeCommands(this, inspector, outputStream);
      } catch (Throwable e) {
        outputStream.println("ERR: Generic error while processing command");
        e.printStackTrace(outputStream);

        recordComment("ERR: Generic error while processing command");
        recordComment(e.getMessage());
      }
    }
  }

  @Override
  public void connect2JPF (JPF jpf) throws JPFInspectorGenericErrorException {
    if (jpf == null) {
      throw new IllegalArgumentException("JPF parameter cannot be null");
    }

    if (jpf.getStatus() != Status.NEW) {
      throw new IllegalArgumentException("Invalied JPF state. JPF is running or terminated model checking.");
    }

    // TODO - Record current state of JPF (breakpoints and CG notifications)

    try {
      inspector.bindWithJPF(jpf);
    } catch (JPFInspectorGenericErrorException e) {
      outputStream.println(e.getMessage());

      recordComment("Error while connection JPF");
      recordComment(e.getMessage());

      throw e;
    }
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
