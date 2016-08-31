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
