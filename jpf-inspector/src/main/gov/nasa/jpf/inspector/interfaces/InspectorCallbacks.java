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

package gov.nasa.jpf.inspector.interfaces;

import gov.nasa.jpf.vm.ChoiceGenerator;

/**
 * This handles callbacks from the server to the client. Used to notify the client about interesting events.
 *
 * Client has to implement this interface and provide these methods to the server.
 * The server has a class that implements this interface and uses it to send these callbacks.
 *
 * Thus, the methods of this interface have a double meaning:
 *
 * 1. In the client, their implementation is what happens when the callback is received.
 * 2. In the server, their implementation means "send this callback now".
 * 
 * Note: Only the JPF thread is permitted to sent callbacks (except {@link #genericInfo(String)} which can be called by any thread).
 */
public interface InspectorCallbacks {

  /**
   * The server calls this when the Inspector's running status (running/stopped) changes.
   * @param newState What status the Inspector switched to
   * @param details Information that should be printed to the console
   */
  void notifyStateChange(InspectorStatusChange newState, String details);

  /**
   * The server calls this whenever an error should be reported to the console.
   * @param msg Message to print.
   */
  void genericError(String msg);

  /**
   * The server calls this whenever a non-error message should be printed to the user.
   * @param msg Message to print.
   */
  void genericInfo(String msg);
  /**
   * The server calls this whenever a breakpoint is hit.
   * @param bp Information about the hit breakpoint.
   */
  void notifyBreakpointHit(BreakpointStatus bp);

  /**
   * Notification from the server that ChoiceGenerator is asked for new value. {@link ChoiceGenerator#advance()} is called.
   * 
   * @param cgType Specifies type of the ChoiceGenerator
   * @param cgName Name of the {@link ChoiceGenerator}
   * @param cgId Identification of the {@link ChoiceGenerator} - the hashCode
   * @param choices List with possible choices of {@link ChoiceGenerator}
   * @param nextChoice Index of selected choice. (Choice which will be selected after the {@link ChoiceGenerator#advance()} call
   * @param defaultChoice Value of default choice, if any. ({@link ChoiceGeneratorsInterface#NO_DEFAULT_CHOICE} is used if no default choice exists)
   */
  void notifyChoiceGeneratorNewChoice(ChoiceGeneratorsInterface.CGTypes cgType,
                                      String cgName, int cgId, String[] choices, int nextChoice,
                                      int defaultChoice);

  /**
   * Callback used to print prompt(notification) to user that execution is stopped and you should specify which choice to use.
   * 
   * <br>Note: This method is called after
   * {@link #notifyChoiceGeneratorNewChoice(ChoiceGeneratorsInterface.CGTypes, String, int, String[], int, int)} which shows
   * possible choices
   * 
   * @param maxChoiceIndex - specifies maximum allowed index of the choice
   */
  void specifyChoiceToUse(int maxChoiceIndex);

  /**
   * Notification from the server with used choice.
   * 
   * @param cgType Specifies type of the ChoiceGenerator
   * @param cgName Name of the {@link ChoiceGenerator}
   * @param cgId Identification of the {@link ChoiceGenerator} - the hashCode
   * @param usedChoiceIndex Index of used choice in {@link ChoiceGenerator}
   * @param usedChoice Used choice - text representation of used choice
   */
  void notifyUsedChoice(ChoiceGeneratorsInterface.CGTypes cgType, String cgName, int cgId, int usedChoiceIndex, String usedChoice);

}
