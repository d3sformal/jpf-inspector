package gov.nasa.jpf.inspector.client.commands;

import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.ClientCommandInterface;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.client.parser.CommandParserFactory;
import gov.nasa.jpf.inspector.client.parser.CommandParserInterface;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorParsingErrorException;
import gov.nasa.jpf.inspector.interfaces.CustomCommand;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.inspector.utils.CommandAlias;
import gov.nasa.jpf.inspector.utils.InspectorConfiguration;

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
  private final CustomCommand customCommand;
  private final CommandAlias alias;
  private ClientCommandInterface actualCommand;
  private JPFInspectorParsingErrorException parsingErrorException = null;

  public CmdCustomCommand(String commandName, String arguments) {
    this.commandName = commandName;
    this.arguments = arguments;
    alias = InspectorConfiguration.getInstance().getAliasIfAny(commandName);
    customCommand = InspectorConfiguration.getInstance().getCustomCommandIfAny(commandName);
    if (alias != null) {
      CommandParserInterface parser = CommandParserFactory.getClientCommandParser();
      String realCommand = alias.getValue();
      // TODO replace parameteres in the style of MessageFormat
      realCommand = JPFInspectorClient.trimLeftWhitspace(realCommand);
      assert  realCommand != null;
      try {
        actualCommand = parser.parseCommand(realCommand);
      } catch (JPFInspectorParsingErrorException e) {
        parsingErrorException = e;
      }
    }
  }

  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    // Search aliases first
    if (alias != null) {
      if (parsingErrorException != null) {
        outStream.println("ERR: The alias '" + commandName + "' was expanded but its expansion could not be parsed.");
        outStream.println(parsingErrorException.getMessage());
      } else {
        actualCommand.execute(client, inspector, outStream);
      }
    } else if (customCommand != null) {
      customCommand.execute(arguments, client, inspector, outStream);
    } else {
      // It's neither an alias nor a custom command.
      outStream.println(
              "ERR: '" + commandName + "' is not recognized as an internal command, alias or a custom command.");
    }
  }

  @Override
  public String getNormalizedCommand() {
    return commandName + " " + arguments;
  }

  @Override
  public boolean isSafeToExecuteWhenNotPaused() {
    if (alias != null) {
      if (parsingErrorException != null) {
        // If we just throw an exception... well, we can do that at any time.
        return true;
      }
      return actualCommand.isSafeToExecuteWhenNotPaused();
    }
    if (customCommand != null) {
      return customCommand.isSafeToExecuteWhenNotPaused();
    }
    return true;
  }
}
