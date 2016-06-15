package gov.nasa.jpf.inspector.client.commands;

import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;

import java.io.PrintStream;

/**
 * Represents a custom, user-defined command, or an alias.
 * This class uses configuration heavily. It uses the properties:
 * - jpf-inspector.alias.[key] = [value]
 * - jpf-inspector.custom_commands
 */
public class CmdCustomCommand extends ClientCommand {

  private final String commandName;
  private final String arguments;

  public CmdCustomCommand(String commandName, String arguments) {
    this.commandName = commandName;
    this.arguments = arguments;
  }

  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    outStream.println("ERR: '" + commandName + "' is not recognized as an internal command, alias or a custom command.");
  }

  @Override
  public String getNormalizedCommand() {
    return commandName + " " + arguments;
  }
}
