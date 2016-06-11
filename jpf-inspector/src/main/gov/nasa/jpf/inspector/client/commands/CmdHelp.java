package gov.nasa.jpf.inspector.client.commands;

import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the "help" command that prints available commands.
 */
public class CmdHelp extends ClientCommand {
  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    Map<String, List<CommandHelpInfo>> categories = new HashMap<>();

    ArrayList<CommandHelpInfo> informationalCommands = new ArrayList<>();
    informationalCommands.add(new CommandHelpInfo("hello", null, "Prints a simple hello message."));
    informationalCommands.add(new CommandHelpInfo("help", null, "Prints this message."));
    informationalCommands.add(new CommandHelpInfo("thread [id]", "ti", "Prints info about a thread.."));
    informationalCommands.add(new CommandHelpInfo("thread_pc [id]", "thpc", "Prints current instruction of a thread."));
    informationalCommands.add(new CommandHelpInfo("print [expression]", null, "Evaluates an expression."));
    categories.put("Informational commands", informationalCommands);

    ArrayList<CommandHelpInfo> execution = new ArrayList<>();
    execution.add(new CommandHelpInfo("quit", "exit", "Closes the shell and terminates the process."));
    execution.add(new CommandHelpInfo("continue", "run, cont", "Starts or resumes execution."));
    execution.add(new CommandHelpInfo("break", null, "Suspends execution."));
    execution.add(new CommandHelpInfo("wait", null, "Blocks until JPF becomes stopped."));
    execution.add(new CommandHelpInfo("set [target] = [value]", null, "Modifies a variable's value."));
    categories.put("Execution commands", execution);

    ArrayList<CommandHelpInfo> breakpoints = new ArrayList<>();
    breakpoints.add(new CommandHelpInfo("create breakpoint [properties...] [hit condition]", "cr bp", "Creates a new breakpoint."));
    breakpoints.add(new CommandHelpInfo("show breakpoint", "sw bp", "Prints all breakpoints."));
    breakpoints.add(new CommandHelpInfo("delete breakpoint [id]", "del bp", "Deletes the breakpoint."));
    breakpoints.add(new CommandHelpInfo("assert [file]:[line] [condition]", null, "Creates a dynamic assertion."));
    categories.put("Breakpoint-related commands", breakpoints);

    ArrayList<CommandHelpInfo> recording = new ArrayList<>();
    recording.add(new CommandHelpInfo("record clear", "rec clear", "Clears the log."));
    recording.add(new CommandHelpInfo("record print", "rec print", "Prints the log."));
    recording.add(new CommandHelpInfo("record execute [filename]", "rec ex", "Executes the stored recording."));
    recording.add(new CommandHelpInfo("record save [filename]", "rec save", "Saves the recording into a file."));
    categories.put("Recording commands", recording);

    ArrayList<CommandHelpInfo> stepping = new ArrayList<>();
    stepping.add(new CommandHelpInfo("step_instruction", "sins", "Executes one instruction."));
    stepping.add(new CommandHelpInfo("step_over", "so", "Executes current line."));
    stepping.add(new CommandHelpInfo("step_out", "sout", "Executes until end of method."));
    stepping.add(new CommandHelpInfo("step_in", "si", "Executes one line or until a method is entered."));
    stepping.add(new CommandHelpInfo("step_transition", "st", "Executes until next choice."));

    stepping.add(new CommandHelpInfo("back_step_instruction", "bsins", "Undoes one instruction."));
    stepping.add(new CommandHelpInfo("back_step_over", "bso", "Undoes current source line."));
    stepping.add(new CommandHelpInfo("back_step_out", "bsout", "Undoes instructions until we return to caller."));
    stepping.add(new CommandHelpInfo("back_step_in", "bsi", "Undoes this and previous line or until a method call is undone."));
    stepping.add(new CommandHelpInfo("back_step_transition", "bst", "Undoes until a choice point is reached."));
    categories.put("Stepping-related commands", stepping);

    ArrayList<CommandHelpInfo> choiceGenerators = new ArrayList<>();
    choiceGenerators.add(new CommandHelpInfo("[enable/disable] [data/sched] [ask/print] cg", null, "Sets what notifications about choice generators should be given to the user."));
    choiceGenerators.add(new CommandHelpInfo("cg select [index]", null, "Selects a choice for the current choice generator."));
    choiceGenerators.add(new CommandHelpInfo("used choice_generators", "used cg", "Prints all choice generators in the transition path."));
    categories.put("Choice generators", choiceGenerators);


    outStream.println("You may use the following commands in this console:\n");
    for(Map.Entry<String, List<CommandHelpInfo>> entry : categories.entrySet()) {
      outStream.println(entry.getKey() + ": ");
      int maxCommandLength = 0;
      for(CommandHelpInfo info : entry.getValue()) {
        if (info.syntax.length() > maxCommandLength) {
          maxCommandLength = info.syntax.length();
        }
      }
      for(CommandHelpInfo info : entry.getValue()) {
        outStream.println(pad(info.syntax, maxCommandLength + 3) + (info.abbreviation != null ? ("[" + info.abbreviation + "] ") : "") + info.shortDescription);
      }
      outStream.println();
    }
  }

  private String pad(String syntax, int length) {
    int spaces = length - syntax.length();
    String spaceString = "";
    for (int i = 0; i < spaces; i++)
      spaceString += " ";
    return syntax + spaceString;
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
