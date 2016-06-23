package gov.nasa.jpf.inspector.client.commands;

import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.BreakpointState;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;

import java.io.PrintStream;

/**
 * Represents the "change breakpoint", "enable breakpoint" and "disable breakpoint" commands which change
 * a breakpoint's state.
 */
public final class CmdBreakpointChange extends ClientCommand {
  private String breakpointId;
  private BreakpointState newState;
  private String normalizedCommand;

  private CmdBreakpointChange(String breakpointId, BreakpointState newState, String normalizedCommand) {
    this.breakpointId = breakpointId;
    this.newState = newState;
    this.normalizedCommand = normalizedCommand;
  }

  public static CmdBreakpointChange createEnableBreakpointCommand(String id) {
    return new CmdBreakpointChange(id, BreakpointState.ENABLED, "enable breakpoint " + id);
  }
  public static CmdBreakpointChange createDisableBreakpointCommand(String id) {
    return new CmdBreakpointChange(id, BreakpointState.DISABLED, "disable breakpoint " + id);
  }
  public static CmdBreakpointChange createChangeBreakpointCommand(String id, BreakpointState newState) {
    String stateWord = null;
    switch (newState) {
      case DISABLED: stateWord = "disable"; break;
      case ENABLED: stateWord = "enable"; break;
      case LOGGING: stateWord = "log"; break;
    }
    return new CmdBreakpointChange(id, newState, "change breakpoint " + id + " state = " + stateWord);
  }


  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    try {
      int breakpointIdInteger = Integer.parseInt(breakpointId);
      final boolean rc = inspector.changeBreakpointState(breakpointIdInteger, newState);
      if (!rc) {
        outStream.println("Breakpoint with ID " + breakpointIdInteger + " does not exist.");
      } else {
        String newStatus = "enabled";
        if (newState == BreakpointState.LOGGING) {
          newStatus = "a logging breakpoint";
        }
        if (newState == BreakpointState.DISABLED) {
          newStatus = "disabled";
        }
        outStream.println("Breakpoint with ID " + breakpointIdInteger + " is now " + newStatus + ".");
      }
    } catch (NumberFormatException e) {
      outStream.println("Malformed breakpoint identifier - \"" + breakpointId + "\" is not an integer (maybe it is out of bounds).");
    }
  }

  @Override
  public String getNormalizedCommand() {
    return normalizedCommand;
  }
}
