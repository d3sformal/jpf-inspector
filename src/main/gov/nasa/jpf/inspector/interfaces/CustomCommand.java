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

package gov.nasa.jpf.inspector.interfaces;


import gov.nasa.jpf.inspector.client.JPFInspectorClient;

import java.io.PrintStream;

/**
 * Implement this interface to create a custom command that you can use within the JPF Inspector console.
 * See the wiki page "Custom Commands" for documentation on how to use this interface.
 *
 * Remember that a class that implements {@link CustomCommand} must also have a parameterless constructor.
 */
public interface CustomCommand {

  /**
   * Returns the keywords that may be used as the command name to execute this command.
   * For example, if this method returns a string array with the only element "hi",
   * then the command "hi arg1 arg2" would cause an instance of this class to be created.
   *
   * A custom command SHOULD NOT return a keyword of the Inspector in this method because that wouldn't work anyway.
   *
   * Each element of the array is one way to name the command. For example, if the built-in command
   * "continue" were implemented as a custom command, then this method would return an array consisting
   * of "continue" and "cont".
   *
   * This method MUST always returns the same constant value.
   */
  String[] getNames();

  /**
   * Returns a short description of the use of the command. This description is displayed in the output of the
   * command "help". All custom commands are listed under the header "Custom Commands" in the help output.
   *
   * This method MUST always returns the same constant value.
   */
  String getHelpText();

  /**
   * Returns true if it is safe to execute this command while JPF is running and not paused, or a time when
   * JPF has not yet been started or has already terminated. For example, the "hello" command would return "true",
   * the "break" command would return "true", but the command "print" would return false.
   */
  boolean isSafeToExecuteWhenNotPaused();

  /**
   * Executes the command and prints the result into a stream.
   *
   * @param arguments The rest of the line is passed to the command as arguments.
   * @param client JPF Inspector client
   * @param server JPF inspector server
   * @param outStream Stream where results (output and errors) of the executed command are printed.
   */
  void execute(String arguments, JPFInspectorClient client, JPFInspectorBackEndInterface server, PrintStream outStream);

}
