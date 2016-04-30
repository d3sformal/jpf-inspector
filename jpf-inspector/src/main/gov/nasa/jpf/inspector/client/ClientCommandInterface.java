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

import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;

import java.io.PrintStream;

/**
 * Interface that implements all user commands from console
 */

public interface ClientCommandInterface {

  /**
   * Sent command to the server part and parses the result.
   * 
   * @param inspector JPF inspector server part that serves commands.
   * @param outStream Stream where results (output and errors) of the executed command are printed.
   */
  public void executeCommands (JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream);

  /**
   * Converts command to string. The string has to represent the command with long version of options. Note: Given string has to be parsable by the command
   * parse.
   * 
   * @return Textual parsable representation of the command.
   */
  public String getNormalizedCommand ();

  /**
   * Method used to record executed command
   * 
   * @param rec Recorder where the command should be recorded.
   */
  public void recordCommand (CommandRecorder rec);

  /**
   * @return Gets true if command execution of the command should not be reported to user.
   */
  public boolean isHiddenCommand ();
}
