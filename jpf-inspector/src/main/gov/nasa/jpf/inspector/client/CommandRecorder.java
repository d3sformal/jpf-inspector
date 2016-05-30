package gov.nasa.jpf.inspector.client;

import gov.nasa.jpf.inspector.client.CallbackExecutionDecorator.WORKING_MODE;
import gov.nasa.jpf.inspector.client.commands.CmdBreakpointCreate;
import gov.nasa.jpf.inspector.client.commands.CmdCallback;
import gov.nasa.jpf.inspector.interfaces.BreakPointStatus;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;

import java.io.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Stores and replays executed commands.
 *
 * The command recorder stores only commands, not their results, but it does store comments that are sometimes generated automatically by some commands. The contents of a command recorder may be erased, stores to a file or replayed with commands in the Inspector console.
 *
 * @author Alf
 */
public class CommandRecorder {

  /*
   * List with executed command, callbacks and comments.
   */
  private final List<String> commands;

  private final Pattern patternLineStart = Pattern.compile("\n"); // Used in recordComment add add "# " at the beginning of each line
  private final String target;

  private ClientCommandInterface lastCommand;
  private int lastCommandIndex;

  private final PrintStream outStream;

  /**
   * 
   * @param outStream Stream where report results of commands
   */
  public CommandRecorder (String target, PrintStream outStream) {
    this.commands = new ArrayList<>();
    this.outStream = outStream;
    this.target = target;
    init();
  }

  private void init () {
    commands.clear();
    lastCommand = null;
    lastCommandIndex = 0;
    DateFormat df = DateFormat.getInstance();
    Date now = new Date();
    String userName = System.getProperty("user.name");

    addComment("Recording starts at " + df.format(now) + (userName != null ? " by " + userName : "") + ".");
    addComment("Target: " + target);
    addComment("");
  }

  /**
   * Writes information on all breakpoints into the record log as comments.
   * This is used for debugging only.
   *
   * @param inspector The Inspector server.
   */
  @SuppressWarnings("unused")
  private void dumpBackendState (JPFInspectorBackEndInterface inspector) {
    // Dumping existing breakpoints
    List<BreakPointStatus> bps = inspector.getBreakPoints();
    for (BreakPointStatus bp : bps) {
      String bpStrCommand = CmdBreakpointCreate.ConsoleBreakpointCreationExpression.getNormalizedExpressionPrefix(bp) + ' ' + bp.getNormalizedBreakpointExpression();
      addComment(bpStrCommand);
    }
  }

  /**
   * Reinitializes the command recorder (forgets all recorded commands and creates a new header for the recorded data, including a new timestamp).
   */
  public synchronized void clearRecordedCommands () {
    init();
    addComment("Recording started because of user-initiated clear command.");
  }

  /**
   * Saves the contents of the current command recorder to a file. If we cannot write to the specified filename (invalid characters, bad permissions, ...), the method returns false.
   * @param fileName Name of the file where the commands should be written to.
   * @return Returns true if the commands were successfully saved to the file; returns false otherwise.
   */
  public synchronized boolean saveRecordedCommmands (String fileName) {
    try {
      PrintStream outFile = new PrintStream(fileName);
      for (String command : commands) {
        outFile.print(command);
      }
      outFile.close();

      return true;

    } catch (FileNotFoundException e) {
      outStream.println("ERR: Could not write to the file '" + fileName + "'. Recording not saved.\n\t" + e.getMessage());
      return false;
    }
  }

  /**
   * Loads the contents of a recording from a file and executes all commands from that recording, in order. If we cannot read the specified file (it does not exist, for example), the method returns false.
   *
   * We read the file as we go. If at any point we lose access to the file, the replay terminates and we return false.
   * @param fileName Filename of the file that contains the recording to play.
   * @param client The current JPFInspectorClient.
   * @return Returns true when all loaded commands are executed. Returns false if the file could not be read.
   */
  public boolean executeCommands (String fileName, JPFInspectorClient client) {
    BufferedReader in;
    try {
      in = new BufferedReader(new FileReader(fileName));
    } catch (FileNotFoundException e) {
      outStream.println("ERR: Could not read the file '" + fileName + "'. Recording will not play.\n\t" + e.getMessage());
      return false;
    }
    try {

      CallbackExecutionDecorator cb = client.getDecoratedCallbacks();
      WORKING_MODE oldWorkMode = cb.setNewMode(WORKING_MODE.WM_EXECUTION_RECORD);
      String line;
      while ((line = in.readLine()) != null) {
        client.executeCommandOrCallback(line);
        // TODO log errors + break on error prevent multiplication in multiple records
      }

      in.close();

      // Restore original mode
      cb.setNewMode(oldWorkMode);
      return true;

    } catch (IOException e) {
      outStream.println("ERR: Could not read the file '" + fileName + "'. Recording will not play.\n\t" + e.getMessage());
      return false;
    }

  }

  /**
   * Dumps all recorded events as a multi-line string.
   */
  public String getRecordedEvents () {
    return toString();
  }

  @Override
  public String toString () {
    StringBuilder sb = new StringBuilder();

    for (String command : commands) {
      sb.append(command);
    }

    return sb.toString();
  }

  // *************************************************************************
  // Commands to log executed events
  // *************************************************************************

  public synchronized void addComment (String comment) {
    if (comment == null) {
      return;
    }

    // Remove trailing '\n'
    while ((comment.length() > 0) && (comment.charAt(comment.length() - 1) == '\n')) {
      comment = comment.substring(0, comment.length() - 1);
    }

    // add # before each new line
    comment = "# " + patternLineStart.matcher(comment).replaceAll("\n# ") + '\n';
    commands.add(comment);
  }

  public synchronized void recordCommand (ClientCommandInterface cmd) {
    assert (cmd != null);
    String cmdStr = cmd.getNormalizedCommand();
    // Add trailing '\n'
    if (cmdStr.charAt(cmdStr.length() - 1) != '\n') {
      cmdStr += '\n';
    }

    commands.add(cmdStr);
    lastCommand = cmd;
    lastCommandIndex = commands.size() - 1;
  }

  // Updates only if it is the last recorded command
  public synchronized void updateCommandRecord (ClientCommandInterface cmd) {
    assert (cmd != null);
    if (lastCommand.equals(cmd)) {

      String cmdStr = cmd.getNormalizedCommand();
      // Add trailing '\n'
      if (cmdStr.charAt(cmdStr.length() - 1) != '\n') {
        cmdStr += '\n';
      }

      // Invalid last command -> ignore modification
      commands.set(lastCommandIndex, cmdStr);
    }
  }

  public synchronized void recordCallback (CmdCallback cmdCB) {
    String cbStr = cmdCB.getNormalizedCommand();
    // Add trailing '\n'
    if (cbStr.charAt(cbStr.length() - 1) != '\n') {
      cbStr += '\n';
    }

    commands.add(cbStr);
  }

}
