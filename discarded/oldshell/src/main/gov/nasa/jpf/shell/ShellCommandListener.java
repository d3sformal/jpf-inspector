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

/**
 * This interface is used to listen for when shell commands are executed
 * If the {@link gov.nasa.jpf.shell.ShellCommand#prepare()} method is false
 * then preCommand and postCommand are never executed. Otherwise the these
 * methods wrap around the execution of the command. In order to gain more
 * insight about what stage the command is at at each point, or how have code
 * that needs to be executed at more "intricate" and ShellCommand implementation
 * specific points, check to see if there is a subclass of ShellCommandListener
 * that serves your needs.<br>
 *
 * For details on how to create more intricate ShellCommandListeners for 
 * complicated commands see {@link gov.nasa.jpf.shell.commands.TestCommandListener} or
 * {@link gov.nasa.jpf.shell.commands.VerifyCommandListener}.
 */
public interface ShellCommandListener<E extends ShellCommand> {


  /**
   * Executed directly before a command the command's 
   * {@link gov.nasa.jpf.shell.ShellCommand#execute() } method is executed.
   * @param command the command to be executed
   */
  public void preCommand(E command);

  /**
   * Executed directly after command's
   * {@link gov.nasa.jpf.shell.ShellCommand#execute() } method is executed.
   * @param command the command being executed.
   */
  public void postCommand(E command);
}
