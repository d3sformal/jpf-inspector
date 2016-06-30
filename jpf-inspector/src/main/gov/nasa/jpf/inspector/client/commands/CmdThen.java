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
import gov.nasa.jpf.inspector.client.ClientCommandInterface;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.inspector.utils.InspectorConfiguration;

import java.io.PrintStream;

/**
 * Represents the "&&" connective command that executes two commands in a row.
 * Even if the first command fails, the second command is still executed. This is because commands do not have
 * a unified way of reporting whether they succeeded or failed.
 */
public class CmdThen extends ClientCommand {
  private ClientCommandInterface firstCommand;
  private ClientCommandInterface secondCommand;
  public CmdThen(ClientCommandInterface firstCommand, ClientCommandInterface secondCommand) {
    this.firstCommand = firstCommand;
    this.secondCommand = secondCommand;
  }
  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    firstCommand.execute(client, inspector, outStream);

    if (InspectorConfiguration.getInstance().isSafeModeActive()) {
      boolean paused = inspector.isPaused();
      if (!paused && !secondCommand.isSafeToExecuteWhenNotPaused()) {
        outStream.println("ERR: The command '" + secondCommand.getNormalizedCommand() + "' may only be used when JPF has started and is paused. The previous command was executed, though.");
        client.recordComment("The command '" + secondCommand.getNormalizedCommand() + "' failed because safe mode disabled it.");
        return;
      }
    }
    secondCommand.execute(client, inspector, outStream);
  }

  @Override
  public String getNormalizedCommand() {
    return firstCommand.getNormalizedCommand() + " && " + secondCommand.getNormalizedCommand();
  }

  @Override
  public boolean isSafeToExecuteWhenNotPaused() {
    return firstCommand.isSafeToExecuteWhenNotPaused();
  }
}
