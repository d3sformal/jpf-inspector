//
// Copyright (C) 2016 Petr Hudeček
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

package gov.nasa.jpf.inspector.client.commands;

import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.CustomCommand;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.inspector.utils.CommandAlias;
import gov.nasa.jpf.inspector.utils.InspectorConfiguration;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the "help" command that prints available commands.
 */
public class CmdHelp extends ClientCommand {
  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    printCommands(outStream);
    printHitConditions(outStream);
  }

  private static void printHitConditions(PrintStream outStream) {
    outStream.println();
    outStream.println("You may use the following hit conditions: ");
    outStream.println("garbage_collection = begin/end/both\n" +
                              "choice_generator = data/sched/both\n" +
                              "instruction_type = any/invoke/return/field_access/field_read/field_write/condition/lock/array/local_access/local_read/local_write\n" +
                              "position = filename:linenumber\n" +
                              "property_violated\n" +
                              "thread_scheduled = in/out/both : threadnumber\n" +
                              "field_access = classname:fieldname\n" +
                              "field_read = classname:fieldname\n" +
                              "field_write = classname:fieldname\n" +
                              "local_access = name\n" +
                              "local_read = name\n" +
                              "local_write = name\n" +
                              "attr_access = classname:fieldname\n" +
                              "attr_read = classname:fieldname\n" +
                              "attr_write = classname:fieldname\n" +
                              "attr_access = name\n" +
                              "attr_read = name\n" +
                              "attr_write = name\n" +
                              "method_invoke = classname:methodname\n" +
                              "method = name\n" +
                              "class = name\n" +
                              "object_created = classname\n" +
                              "object_released = classname\n" +
                              "exception_thrown = classname\n" +
                              "expression operator expression\n" +
                              "( hitcondition )\n" +
                              "hitcondition and hitcondition\n" +
                              "hitcondition or hitcondition\n" +
                              "not hitcondition\n" +
                              "custom_hit_condition ( arguments )");
  }

  private static void printCommands(PrintStream outStream) {
    LinkedHashMap<String, List<CommandHelpInfo>> categories = new LinkedHashMap<>();
    // Linked hash map, because we want to preserve our order of categories

    addBuiltInCommands(categories);

    addAliases(categories);

    addCustomCommands(categories);


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

  private static void addCustomCommands(LinkedHashMap<String, List<CommandHelpInfo>> categories) {
    ArrayList<CommandHelpInfo> customCommands = new ArrayList<>();
    for (Map.Entry<String, CustomCommand> entry : InspectorConfiguration.getInstance().getCustomCommands()) {
      customCommands.add(new CommandHelpInfo(entry.getKey(), null, entry.getValue().getHelpText()));
    }
    categories.put("Custom commands", customCommands);
  }

  private static void addAliases(LinkedHashMap<String, List<CommandHelpInfo>> categories) {
    ArrayList<CommandHelpInfo> aliases = new ArrayList<>();
    for(CommandAlias alias : InspectorConfiguration.getInstance().getAliases()) {
      String aliasLeftPart = alias.getKey().trim();
      String aliasRightPart = alias.getValue().trim();
      for (int i = 0; i < alias.getNumberOfRequiredParameters(); i++) {
        aliasLeftPart += " [arg" + i + "]";
      }
      for (int i = 0; i < alias.getNumberOfRequiredParameters(); i++) {
        aliasRightPart = aliasRightPart.replace("{" + i + "}", "[arg" + i + "]");
      }
      if (alias.getNumberOfRequiredParameters() == 0 && alias.usesFullArgumentLine()) {
        aliasLeftPart += " [argument]";
        aliasRightPart = aliasRightPart.replace(CommandAlias.FULL_ARGUMENT_PATTERN, "[argument]");
      }
      aliases.add(new CommandHelpInfo(aliasLeftPart, null, aliasRightPart));
    }
    categories.put("Aliases", aliases);
  }

  private static void addBuiltInCommands(LinkedHashMap<String, List<CommandHelpInfo>> categories) {
    ArrayList<CommandHelpInfo> informationalCommands = new ArrayList<>();
    informationalCommands.add(new CommandHelpInfo("hello", null, "Prints a simple hello message."));
    informationalCommands.add(new CommandHelpInfo("help", null, "Prints this message."));
    informationalCommands.add(new CommandHelpInfo("thread [id]", "ti", "Prints info about a thread.."));
    informationalCommands.add(new CommandHelpInfo("thread_pc [id]", "thpc", "Prints current instruction of a thread."));
    informationalCommands.add(new CommandHelpInfo("print [expression]", null, "Evaluates an expression."));
    informationalCommands.add(new CommandHelpInfo("print_attr [expression]", null, "Evaluates an expression, but prefers to print attributes, not values."));
    categories.put("Informational commands", informationalCommands);

    ArrayList<CommandHelpInfo> execution = new ArrayList<>();
    execution.add(new CommandHelpInfo("quit", "exit", "Closes the shell and terminates the process."));
    execution.add(new CommandHelpInfo("continue", "run, cont", "Starts or resumes execution."));
    execution.add(new CommandHelpInfo("break", null, "Suspends execution."));
    execution.add(new CommandHelpInfo("wait", null, "Blocks until JPF becomes stopped."));
    execution.add(new CommandHelpInfo("finish", null, "Blocks until JPF terminates."));
    execution.add(new CommandHelpInfo("terminate", null, "Terminates JPF but keeps the shell open."));
    execution.add(new CommandHelpInfo("set [target] = [value]", null, "Modifies a variable's value."));
    execution.add(new CommandHelpInfo("set_attr [target] = [value]", "seta", "Modifies a variable's attribute."));
    categories.put("Execution commands", execution);

    ArrayList<CommandHelpInfo> breakpoints = new ArrayList<>();
    breakpoints.add(new CommandHelpInfo("create breakpoint [properties...] [hit condition]", "cr bp", "Creates a new breakpoint."));
    breakpoints.add(new CommandHelpInfo("change breakpoint [id] state = [state]", "change bp", "Enables or disables a breakpoint."));
    breakpoints.add(new CommandHelpInfo("disable breakpoint [id]", "dis bp", "Disables the breakpoint."));
    breakpoints.add(new CommandHelpInfo("enable breakpoint [id]", "en bp", "Re-enables the breakpoint."));
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
    stepping.add(new CommandHelpInfo("back_field_access [class]:[field]", "bfa", "Undoes until the field access is reached."));
    stepping.add(new CommandHelpInfo("back_breakpoint_hit", "bbhit", "Undoes until the last breakpoint hit is reached."));
    categories.put("Stepping-related commands", stepping);

    ArrayList<CommandHelpInfo> choiceGenerators = new ArrayList<>();
    choiceGenerators.add(new CommandHelpInfo("[enable/disable] [data/sched] [ask/print] cg", null, "Sets what notifications about choice generators should be given to the user."));
    choiceGenerators.add(new CommandHelpInfo("show choice_generators", "show cg", "Prints notification status of choice generators."));
    choiceGenerators.add(new CommandHelpInfo("cg select [index]", null, "Selects a choice for the current choice generator."));
    choiceGenerators.add(new CommandHelpInfo("disable thread [index]", "dis t", "Suppresses a thread from being scheduled."));
    choiceGenerators.add(new CommandHelpInfo("enable thread [index]", "en t", "Permits a thread to be scheduled."));
    choiceGenerators.add(new CommandHelpInfo("used choice_generators", "used cg", "Prints all choice generators in the transition path."));
    categories.put("Choice generators", choiceGenerators);
  }

  private static String pad(String syntax, int length) {
    int spaces = length - syntax.length();
    String spaceString = "";
    for (int i = 0; i < spaces; i++) {
      spaceString += " ";
    }
    return syntax + spaceString;
  }

  @Override
  public String getNormalizedCommand() {
    return "help";
  }

  /**
   * Contains help information about a specific command (built-in or custom).
   */
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
