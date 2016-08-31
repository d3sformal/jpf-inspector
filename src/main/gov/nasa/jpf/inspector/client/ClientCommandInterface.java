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
  void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream);

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
