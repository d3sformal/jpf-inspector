package gov.nasa.jpf.inspector.client.commands;

import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.commands.VerifyCommand;

import java.io.PrintStream;
import java.util.List;

/**
 * Represents the "finish" command that waits until the current JPF thread terminates.
 */
public class CmdFinish extends ClientCommand {
  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    // TODO use verify command listener instead of active waiting
    // TODO include "wait" functionality here

    inspector.waitUntilStopped();
    inspector.getServerCallbacks().waitUntilCallbackQueueIsEmpty();

    final List<VerifyCommand> vcList = ShellManager.getManager().getCommands(VerifyCommand.class);
    final VerifyCommand vc = vcList.get(0);
    while (vc.isVerifying()) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  @Override
  public String getNormalizedCommand() {
    return "finish";
  }
}
