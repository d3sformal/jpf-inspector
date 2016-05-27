package gov.nasa.jpf.inspector.client.commands;

import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CmdHelp extends ClientCommand {
  @Override
  public void executeCommands(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    Map<String, List<CommandHelpInfo>> categories = new HashMap<>();
    ArrayList<CommandHelpInfo> informationalCommands = new ArrayList<>();
    informationalCommands.add(new CommandHelpInfo("hello", null, "Prints a simple hello message."));
    informationalCommands.add(new CommandHelpInfo("help", null, "Prints this message."));
    informationalCommands.add(new CommandHelpInfo("quit", "exit", "Closes the shell and terminates the process."));

    categories.put("Informational commands", informationalCommands);
    ArrayList<CommandHelpInfo> breakpointCommands = new ArrayList<>();
    breakpointCommands.add(new CommandHelpInfo("create breakpoint [properties...] [hit condition]", "cr bp", "Creates a new breakpoint."));
    categories.put("Breakpoint-related commands", breakpointCommands);
    categories.put("Stepping-related commands", new ArrayList<>());

    outStream.println("You may use the following commands in this console:\n");
    for(Map.Entry<String, List<CommandHelpInfo>> entry : categories.entrySet()) {
      outStream.println(entry.getKey() + ": ");
      for(CommandHelpInfo info : entry.getValue()) {
        outStream.println(info.syntax + "\t" + (info.abbreviation != null ? ("[" + info.abbreviation + "] ") : "") + info.shortDescription);
      }
      outStream.println();
    }
  }

  @Override
  public String getNormalizedCommand() {
    return "help";
  }

  private static class CommandHelpInfo {
    private final String syntax;
    private final String abbreviation;
    private final String shortDescription;

    public CommandHelpInfo(String syntax, String abbreviation, String description) {
      this.syntax = syntax;
      this.abbreviation = abbreviation;
      this.shortDescription = description;
    }
  }
}
