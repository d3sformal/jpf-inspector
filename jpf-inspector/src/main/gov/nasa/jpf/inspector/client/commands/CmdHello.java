package gov.nasa.jpf.inspector.client.commands;

import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;

import java.io.PrintStream;

/**
 * Represents the "hello" command that is used to test whether JPF Inspector is working.
 */
public class CmdHello extends ClientCommand {
  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    outStream.println("Hello to you, too, dear user! The JPF Inspector command loop is working!");
  }

  @Override
  public String getNormalizedCommand() {
    return "hello";
  }
}
