/* Copyright (C) 2007 United States Government as represented by the
 * Administrator of the National Aeronautics and Space Administration
 * (NASA).  All Rights Reserved.
 *
 * This software is distributed under the NASA Open Source Agreement
 * (NOSA), version 1.3.  The NOSA has been approved by the Open Source
 * Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
 * directory tree for the complete NOSA document.
 *
 * THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
 * KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
 * LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
 * SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
 * A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
 * THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
 * DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
 */

package gov.nasa.jpf.shell;

import javax.swing.Icon;

/**
 * The most basic class for creating a command that can be executed in jpf-shell
 * applications. <br>
 * ShellCommands should be added to the ShellManager through
 * {@link gov.nasa.jpf.shell.ShellManager#addCommand(gov.nasa.jpf.shell.ShellCommand)}.
 * The ShellManager will then notify all of the shells that a new command
 * has been added and all Shells will install the newly added command. All
 * new shells created afterwards will install the command. <br>
 *
 * To execute a ShellCommand that was added to a ShellManager
 * {@link gov.nasa.jpf.shell.ShellManager#fireCommand(gov.nasa.jpf.shell.ShellCommand)}
 * should be used. This will generate a ShellCommandEvent and fire all of the
 * listeners that are associated with the ShellCommand. <br>
 *
 * Listeners can be added to the ShellManager for any ShellCommand by using 
 * {@link gov.nasa.jpf.shell.ShellManager#addCommandListener(gov.nasa.jpf.shell.ShellCommand, gov.nasa.jpf.shell.ShellCommandListener)}
 * in order to receive notification of a command being executed.<br>
 *
 * ShellCommands have a name, icon and tooltip. It is up to
 * the shell implementations to decide how these fields are displayed to the
 * user.<br>
 *
 * Most importantly ShellCommands have {@link #prepare()} and {@link #execute()}
 * methods. execute() contains the actual work that the command performs.
 * prepare() can be used as a method to determine whether the ShellCommand is
 * in a state such that it can properly perform the {@link #execute()} method.
 * ie.) if all pre-requisites aren't met, the prepare command can return false
 * and display a message to the user about why the command did not execute.<br>
 *
 * They key difference between {@link #prepare()} and just checking in the
 * {@link #execute()} method is that  {@link #prepare()} occurs <br>before</br>
 * the listeners are notified that the command is going to execute and therefore
 * if prepare() is false then listeners for the command are none-the-wiser.<br>
 *
 * See {@link gov.nasa.jpf.shell.TestCommand} and  {@link gov.nasa.jpf.shell.VerifyCommand}
 * for examples of how to implement ShellCommands.
 */
public abstract class ShellCommand {

    /**
     * @return the user visible name of this command
     */
    public String getName(){
      return "";
    }

    /**
     * @return The icon to represent this command
     */
    public Icon getIcon(){
      return null;
    }

    /**
     *@return The tip info for this command
     */
    public String getToolTip(){
      return "";
    }

    /**
     * @return whether to continue with launching the command
     */
    public boolean prepare(){
      return true;
    }

    /**
     * The action to be performed when the user excute this command
     * This is run in its own thread, NOT IN THE SWING THREAD
     * The shell given is the shell from which the command was executed from.
     */
    public abstract void execute();


    /**
     * Requests that all shells updated their rendering of this command since
     * either the name, tooltip or icon changed.
     */
    public void requestShellUpdate(){
      ShellManager.getManager().updateCommand(this);
    }

  public void fireInNewThread() {
    new Thread(){
      @Override
      public void run(){
        ShellManager.getManager().fireCommand(ShellCommand.this);
      }
    }.start();
  }
}
