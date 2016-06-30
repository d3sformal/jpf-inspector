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

package gov.nasa.jpf.inspector.frontends.swing;

import gov.nasa.jpf.inspector.client.ExecutionContext;
import gov.nasa.jpf.inspector.client.JPFInspectorClientInterface;
import gov.nasa.jpf.shell.ShellCommand;

import javax.swing.*;
import java.io.PrintStream;
import java.util.Objects;

/**
 * Represents a command (button) on the Inspector toolbar. These commands permit the user to perform the most common
 * tasks related to the Inspector without using console commands.
 */
public class InspectorToolbarCommand extends ShellCommand {

  private String name;
  private String tooltip;
  private Icon icon;
  private String command;
  private final JPFInspectorClientInterface client;
  private final PrintStream consolePrintStream;

  public InspectorToolbarCommand(String name, Icon icon, String tooltip, String command,
                                 JPFInspectorClientInterface client, PrintStream consolePrintStream) {
    this.name = name;
    this.icon = icon;
    this.tooltip = tooltip;
    this.command = command;
    this.client = client;
    this.consolePrintStream = consolePrintStream;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getToolTip() {
    return  tooltip + (!Objects.equals(tooltip, "") ? " " : "") + "[" + command + "]";
  }

  @Override
  public Icon getIcon() {
    return icon;
  }



  @Override
  public void execute() {
    consolePrintStream.println();
    client.executeCommand(command, ExecutionContext.FROM_SWING_TERMINAL);
  }
}
