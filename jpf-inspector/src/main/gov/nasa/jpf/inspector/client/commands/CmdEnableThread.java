package gov.nasa.jpf.inspector.client.commands;

import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;

import java.io.PrintStream;

/**
 * Represents the "enable thread" and "disable thread" commands which determine whether a thread is eligible to
 * be scheduled.
 */
public class CmdEnableThread extends ClientCommand {
  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    // TODO
  }

  @Override
  public String getNormalizedCommand() {
    return "enable thread";
  }
}
