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
package gov.nasa.jpf.inspector.server.callbacks.commands;

import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGTypes;
import gov.nasa.jpf.inspector.interfaces.InspectorCallbacks;
import gov.nasa.jpf.inspector.server.callbacks.CallbackCommand;

/**
 * Element of the callback-sender queue that executes the client's notifyChoiceGeneratorNewChoice action.
 */
public class CallbackCommandChoiceGeneratorNewChoice implements CallbackCommand {

  private final CGTypes cgType;
  private final String cgName;
  private final int cgId;
  private final String[] choices;
  private final int nextChoice;
  private final int defaultChoice;

  public CallbackCommandChoiceGeneratorNewChoice (CGTypes cgType, String cgName, int cgId, String[] choices, int nextChoice, int defaultChoice) {
    this.cgType = cgType;
    this.cgName = cgName;
    this.cgId = cgId;
    this.choices = choices;
    this.nextChoice = nextChoice;
    this.defaultChoice = defaultChoice;
  }

  @Override
  public void sendCallback (InspectorCallbacks clientCallbacks) {
    clientCallbacks.notifyChoiceGeneratorNewChoice(cgType, cgName, cgId, choices, nextChoice, defaultChoice);
  }

  @Override
  public boolean waitJPF2stop () {
    return false;
  }

}
