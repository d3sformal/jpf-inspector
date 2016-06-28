package gov.nasa.jpf.inspector.client.commands;

import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.shell.ShellManager;

import java.io.PrintStream;

/**
 * Represents the "wait" command that blocks until the JPF becomes suspended and until all callbacks are processed.
 */
public class CmdWait extends ClientCommand {
  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    inspector.waitUntilStopped();
    inspector.getServerCallbacks().waitUntilCallbackQueueIsEmpty();
  }

  @Override
  public String getNormalizedCommand() {
    return "wait";
  }

}
