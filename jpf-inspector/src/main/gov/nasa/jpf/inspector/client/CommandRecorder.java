package gov.nasa.jpf.inspector.client;

import gov.nasa.jpf.inspector.client.CallbackExecutionDecorator.WORKING_MODE;
import gov.nasa.jpf.inspector.client.commands.CmdBreakpointCreate;
import gov.nasa.jpf.inspector.client.commands.CmdCallback;
import gov.nasa.jpf.inspector.interfaces.BreakPointStatus;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Stores and replays executed commands.
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

  private boolean deterministicBehaviour; // Is recorded execution deterministics (all commands are sent if JPF is not running)
  private boolean jpfStartRecorded; // Begins record with start of the JPF (not record while in the middle of the execution)
  private ClientCommandInterface lastCommand;
  private int lastCommandIndex;

  private int commandCount; // Number of recorded commands

  private final PrintStream outStream;

  /**
   * 
   * @param outStream Stream where report results of commands
   */
  public CommandRecorder (String target, PrintStream outStream) {
    this.commands = new ArrayList<String>();
    this.outStream = outStream;
    this.target = target;
    init();
  }

  private void init () {
    commands.clear();
    deterministicBehaviour = true;
    jpfStartRecorded = false;
    lastCommand = null;
    lastCommandIndex = 0;
    commandCount = 0;
    DateFormat df = DateFormat.getInstance();
    Date now = new Date();
    String userName = System.getProperty("user.name");

    addComment("Recorded at " + df.format(now) + (userName != null ? " by " + userName : "") + ".");
    addComment("Target: " + target);
    addComment("");
  }

  private void dumpBackendState (JPFInspectorBackEndInterface inspector) {
    // Dumping existing breakpoints
    List<BreakPointStatus> bps = inspector.getBreakPoints();
    for (BreakPointStatus bp : bps) {
      String bpStrCommand = CmdBreakpointCreate.ConsoleBreakpointCreate.getNormalizedExpressionPrefix(bp) + ' ' + bp.getNormalizedBreakpointExpression();
      addComment(bpStrCommand);
    }

    // Dumping notification state

  }

  public synchronized void clearRecordedCommands () {
    init();

    addComment("Cleared");
  }

  public synchronized boolean saveRecordedCommmands (String fileName) {
    try {
      PrintStream outFile = new PrintStream(fileName);
      Iterator<String> it = commands.iterator();
      while (it.hasNext()) {
        outFile.print(it.next());
      }
      outFile.close();

      return true;
    } catch (FileNotFoundException e) {
      outStream.println("ERR: Cannot write to " + fileName + " file. \n\t" + e.getMessage());
      return false;
    }
  }

  public boolean executeCommands (String fileName, JPFInspectorClient client) {
    BufferedReader in = null;
    try {
      in = new BufferedReader(new FileReader(fileName));
    } catch (FileNotFoundException e) {
      outStream.println("ERR: Cannot found " + fileName + " file. \n\t" + e.getMessage());
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
      outStream.println("ERR: Cannot read " + fileName + " file. \n\t" + e.getMessage());
      return false;
    }

  }

  /**
   * @return Dumps all recorded events
   */
  public String getRecordedEvents () {
    return toString();
  }

  @Override
  public String toString () {
    StringBuilder sb = new StringBuilder();
    sb.append("# CommandRecorder - executed commands\n");

    Iterator<String> it = commands.iterator();
    while (it.hasNext()) {
      sb.append(it.next());
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
    commandCount++;
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
