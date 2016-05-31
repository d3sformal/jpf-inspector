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

package gov.nasa.jpf.inspector.client;

import gov.nasa.jpf.inspector.client.commands.CmdCallback;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;

import java.io.PrintStream;

/**
 * A command typed by the user in the JPF Inspector console.
 */
public interface ClientCommandInterface {

  /**
   * Sends the command to the server and prints the result into a stream.
   *
   * @param client JPF Inspector client
   * @param inspector JPF inspector server
   * @param outStream Stream where results (output and errors) of the executed command are printed.
   */
  void executeCommands(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream);

  /**
   * Converts the command to a string. The string must represent the command using non-abbreviated versions of all keywords. The returned string must be a legal command that, if parsed, would result in the same ClientCommandInterface object.
   * 
   * @return Text parseable representation of the command.
   */
  String getNormalizedCommand();

  /**
   * Records the command into the record log.
   * 
   * @param rec Recorder where the command should be recorded.
   */
  void recordCommand(CommandRecorder rec);

  /**
   * Returns true if command execution of the command should not be reported to user.
   * Only {@link CmdCallback} is a hidden command.
   */
  boolean isHiddenCommand();

  /**
   * Returns true if it is safe to execute this command while JPF is running and not paused.
   */
  boolean isSafeToExecuteWhenNotPaused();
}
