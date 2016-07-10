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

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.frontends.swing.AuxiliaryInspectorPanel;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;

/**
 * The client part of the JPF Inspector.
 * This part takes input from the user and passes it, after parsing, to the server.
 */
public interface JPFInspectorClientInterface {
  /**
   * Parses and executes a command given by the user.
   * 
   * @param cmd The command to execute.
   */
  void executeCommand(String cmd, ExecutionContext context);

  /**
   * Returns true if the JPF thread has started but is currently paused.
   */
  boolean isPaused();

  /**
   * Connects the client to a specific instance of Java PathFinder.
   * 
   * Note: Should be called before JPF.run method is called.
   * Note: Modifies JPF Configuration.
   * 
   * @param jpf JPF to connect to.
   * @throws JPFInspectorGenericErrorException
   */
  void connect2JPF(JPF jpf) throws JPFInspectorGenericErrorException;

  /**
   * Adds a new panel as a listener to events of the client.
   *
   * These events are:
   *
   * * A command is executed.
   * * A callback his received.
   *
   * @param auxiliaryInspectorPanel A new listener.
   */
  void addInspectorListener(AuxiliaryInspectorPanel auxiliaryInspectorPanel);

  /**
   * Gets the Inspector server.
   */
  JPFInspector getServer();
}
