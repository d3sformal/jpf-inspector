//
// Copyright (C) 2010 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
// 
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
// 
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//  

package gov.nasa.jpf.inspector.client.commands;

import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.commands.VerifyCommand;

import java.io.PrintStream;
import java.util.List;

/**
 * Represents the commands "run", "continue" and "break".
 * "run" is merely an alias of "continue".
 */
public class CmdRun extends ClientCommand {

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
      assert vcList.size() > 0; // Verify command exists

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
        }).start();
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
  }

  @Override
  public String getNormalizedCommand () {
    return commandName;
  }

}
