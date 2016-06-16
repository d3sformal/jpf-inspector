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
  private ClientCommandInterface aliasedCommand;
  private boolean isFailedCommand;
  private String errorMessage;

  // TODO (elsewhere): after I understand loadReferences lazily, see why "print #stackSlot[0]" fails (in ExpressionTest.java, both before and after calling the instance method)

  // TODO solve cyclic dependency
  public CmdCustomCommand(String commandName, String arguments) {
    this.commandName = commandName;
    this.arguments = arguments;
    alias = InspectorConfiguration.getInstance().getAliasIfAny(commandName);
    customCommand = InspectorConfiguration.getInstance().getCustomCommandIfAny(commandName);
    if (alias != null) {
      CommandParserInterface parser = CommandParserFactory.getClientCommandParser();
      String realCommand = alias.getValue();
      realCommand = JPFInspectorClient.trimLeftWhitspace(realCommand);
      String[] argumentsArray = arguments.trim().split("\\W+");
      int numberOfArguments = argumentsArray.length;
      if (arguments.trim().length() == 0) {
        numberOfArguments = 0;
      }
      if (alias.getNumberOfRequiredParameters() > numberOfArguments) {
        isFailedCommand = true;
        errorMessage = "ERR: The alias '" + commandName + "' requires " + alias.getNumberOfRequiredParameters() +
                " arguments but only " + numberOfArguments + " were provided.\nThe alias should expand to this: '" +
                alias.getValue() + "'.";
        return;
      }
      if (!alias.usesFullArgumentLine() && alias.getNumberOfRequiredParameters() != numberOfArguments) {
        // If the user gave more arguments than needed or less than needed, then he probably
        // misunderstands the alias and the command should not go through.
        // However, if the alias uses the full argument line, then we can't really say anything.
        isFailedCommand = true;
        errorMessage = "ERR: You have provided " + numberOfArguments + " arguments which is more than what the alias '" + commandName + "' requires (" + alias.getNumberOfRequiredParameters() +
                "). This may not be what you intended.\nThe alias should expand to this: '" +
                alias.getValue() + "'.";
        return;
      }
      assert  realCommand != null;
      realCommand = replaceMacroStyle(realCommand, alias.getNumberOfRequiredParameters(), arguments, argumentsArray);
      try {
        aliasedCommand = parser.parseCommand(realCommand);
      } catch (JPFInspectorParsingErrorException e) {
        isFailedCommand = true;
        errorMessage =
                "ERR: The alias '" + commandName + "' was expanded but its expansion could not be parsed.\n" +
                "The expansion was: '" + realCommand + "'.\n" +
                "Error: " + e.getMessage() + ".\n";
      }
    }
  }

  private String replaceMacroStyle(String realCommand, int numberOfRequiredParameters, String arguments, String[] argumentsArray) {
    String result = realCommand.replace(CommandAlias.FULL_ARGUMENT_PATTERN, arguments);
    for (int i = 0; i < numberOfRequiredParameters; i++) {
      result = result.replace("{" + i + "}", argumentsArray[i]);
    }
    return result;
  }

  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    if (isFailedCommand) {
      outStream.println(errorMessage);
      return;
    }
    // Search aliases first
    if (alias != null) {
      aliasedCommand.execute(client, inspector, outStream);
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
    if (isFailedCommand) {
      // A failed command just prints a message so it's okay.
      return true;
    }
    if (alias != null) {
      return aliasedCommand.isSafeToExecuteWhenNotPaused();
    }
    if (customCommand != null) {
      return customCommand.isSafeToExecuteWhenNotPaused();
    }
    return true;
  }
}
