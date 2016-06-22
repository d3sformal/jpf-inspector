package gov.nasa.jpf.inspector.client.commands;

import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;

import java.io.PrintStream;

/**
 * Represents the "change breakpoint", "enable breakpoint" and "disable breakpoint" commands which change
 * a breakpoint's state.
 */
public class CmdBreakpointChange extends ClientCommand {
  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
   // TODO
  }

  @Override
  public String getNormalizedCommand() {
    return "change breakpoint";
  }
}
