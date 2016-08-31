//
// Copyright (C) 2010-2011 Pavel Jančík
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
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.utils.InspectorConfiguration;
import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.commands.VerifyCommand;

import java.io.PrintStream;
import java.util.List;

/**
 * Represents the commands "run", "continue" and "break".
 * "run" is merely an alias of "continue".
 */
public class CmdRun extends ClientCommand {

  /**
   * Indicates whether execution should be resumed or stopped.
   */
  public enum CmdRunTypes {
    /**
     * Execution should be started or resumed.
     */
    RUN,
    /**
     * Execution should be paused.
     */
    STOP
  }

  private final CmdRunTypes type;
  /**
   * This will be "run", "break" or "continue", but not "cont".
   */
  private final String commandName;

  public CmdRun (CmdRunTypes type, String commandName) {
    this.commandName = commandName;
    this.type = type;
  }

  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, final PrintStream outStream) {
    if (type == CmdRunTypes.RUN) {
      final ShellManager shellMgr = ShellManager.getManager();
      // Check whether JPF is running or not (if not we start them)
      final List<VerifyCommand> vcList = shellMgr.getCommands(VerifyCommand.class);
      assert vcList != null;
      assert !vcList.isEmpty(); // Verify command exists

      final VerifyCommand vc = vcList.get(0);
      if (!vc.isVerifying()) {
        // Executes the JPF in the separate thread
        new Thread(() -> {
          try {
            ShellManager.getManager().fireCommand(vc);
          } catch (Throwable t) {
            outStream.println("Exception while starting/resuming JPF.");
            outStream.println(t.getMessage());
            t.printStackTrace(outStream);
          }
        }, "JPF Thread from CmdRun").start();
        inspector.waitUntilJpfBecomesConnected();
      } else {
        // JPF is running
        try {
          inspector.start();
        } catch (JPFInspectorException e) {
          outStream.println(e.getMessage());
          client.recordComment(e.getMessage());
        }
      }
    } else if (type == CmdRunTypes.STOP) {
      try {
        inspector.stop();
      } catch (JPFInspectorException e) {
        outStream.println(e.getMessage());
        client.recordComment(e.getMessage());
      }
    } else {
      throw new RuntimeException("Unknown enum " + type.getClass().getName() + " entry " + type);
    }
    if (InspectorConfiguration.getInstance().shouldWaitAfterRun()) {
      inspector.waitUntilStopped();
    }
  }

  @Override
  public String getNormalizedCommand () {
    return commandName;
  }

}
