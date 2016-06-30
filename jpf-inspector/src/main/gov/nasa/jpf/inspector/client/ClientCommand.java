//
// Copyright (C) 2011 United States Government as represented by the
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
 * Represents a single instance of a command sent by the user.
 *
 * Whenever the user executes a command by typing in the Inspector console and pressing Enter, an instance of ClientCommand is created, and then executed using its {@link ClientCommand#execute(JPFInspectorClient, JPFInspectorBackEndInterface, PrintStream)} method.
 *
 * Each command (such as "help" or "print") has its own subclass.
 */
public abstract class ClientCommand implements ClientCommandInterface {

  /**
   * The last recorder used to record the command
   */
  protected CommandRecorder recorder;

  @Override
  public void recordCommand (CommandRecorder rec) {
    this.recorder = rec;
    rec.recordCommand(this);
  }

  @Override
  public boolean isHiddenCommand () {
    return false;
  }

  @Override
  public boolean isSafeToExecuteWhenNotPaused() {
    return true;
  }
}
