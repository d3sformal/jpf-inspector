package gov.nasa.jpf.inspector.client.commands;

import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;

import java.io.PrintStream;

public class CmdQuit extends ClientCommand {

  @Override
  public void executeCommands(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    outStream.println("JPF Inspector will now terminate its system process.");
    System.exit(0);
  }

  @Override
  public String getNormalizedCommand() {
    return "quit";
  }
}
