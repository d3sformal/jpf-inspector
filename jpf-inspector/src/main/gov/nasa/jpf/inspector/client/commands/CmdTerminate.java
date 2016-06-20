package gov.nasa.jpf.inspector.client.commands;

import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Represents the "terminate" command that causes the JPF to terminate its execution and then waits until JPF
 * actually becomes terminated.
 */
public class CmdTerminate extends ClientCommand {
  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    inspector.requestTermination();
    try {
      inspector.start();
    } catch (JPFInspectorException e) {
      outStream.println("ERR: Termination request but JPF could not be resumed to begin the termination.");
      outStream.println(e.getMessage());
      client.recordComment(e.getMessage());
      return;
    }
    inspector.waitUntilStopped();
    inspector.getServerCallbacks().waitUntilCallbackQueueIsEmpty();
  }

  @Override
  public String getNormalizedCommand() {
    return "terminate";
  }

  @Override
  public boolean isSafeToExecuteWhenNotPaused() {
    return false; // First, use "break", then "terminate".
  }
}
