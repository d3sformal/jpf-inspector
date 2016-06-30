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
import gov.nasa.jpf.inspector.interfaces.BreakpointState;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;

import java.io.PrintStream;

/**
 * Represents the "change breakpoint", "enable breakpoint" and "disable breakpoint" commands which change
 * a breakpoint's state.
 */
public final class CmdBreakpointChange extends ClientCommand {
  private String breakpointId;
  private BreakpointState newState;
  private String normalizedCommand;

  private CmdBreakpointChange(String breakpointId, BreakpointState newState, String normalizedCommand) {
    this.breakpointId = breakpointId;
    this.newState = newState;
    this.normalizedCommand = normalizedCommand;
  }

  public static CmdBreakpointChange createEnableBreakpointCommand(String id) {
    return new CmdBreakpointChange(id, BreakpointState.ENABLED, "enable breakpoint " + id);
  }
  public static CmdBreakpointChange createDisableBreakpointCommand(String id) {
    return new CmdBreakpointChange(id, BreakpointState.DISABLED, "disable breakpoint " + id);
  }
  public static CmdBreakpointChange createChangeBreakpointCommand(String id, BreakpointState newState) {
    String stateWord = null;
    switch (newState) {
      case DISABLED: stateWord = "disable"; break;
      case ENABLED: stateWord = "enable"; break;
      case LOGGING: stateWord = "log"; break;
    }
    return new CmdBreakpointChange(id, newState, "change breakpoint " + id + " state = " + stateWord);
  }


  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    try {
      int breakpointIdInteger = Integer.parseInt(breakpointId);
      final boolean rc = inspector.changeBreakpointState(breakpointIdInteger, newState);
      if (!rc) {
        outStream.println("Breakpoint with ID " + breakpointIdInteger + " does not exist.");
      } else {
        String newStatus = "enabled";
        if (newState == BreakpointState.LOGGING) {
          newStatus = "a logging breakpoint";
        }
        if (newState == BreakpointState.DISABLED) {
          newStatus = "disabled";
        }
        outStream.println("Breakpoint with ID " + breakpointIdInteger + " is now " + newStatus + ".");
      }
    } catch (NumberFormatException e) {
      outStream.println("Malformed breakpoint identifier - \"" + breakpointId + "\" is not an integer (maybe it is out of bounds).");
    }
  }

  @Override
  public String getNormalizedCommand() {
    return normalizedCommand;
  }
}
