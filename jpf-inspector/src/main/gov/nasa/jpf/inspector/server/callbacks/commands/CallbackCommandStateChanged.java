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

import gov.nasa.jpf.inspector.interfaces.InspectorStatusChange;
import gov.nasa.jpf.inspector.interfaces.InspectorCallbacks;
import gov.nasa.jpf.inspector.server.callbacks.CallbackCommand;

/**
 * Element of the callback-sender queue that executes the client's notifyStateChange action.
 */
public class CallbackCommandStateChanged implements CallbackCommand {
  private final String details;
  private final InspectorStatusChange state;
  
   
  public CallbackCommandStateChanged(InspectorStatusChange state, String details) {
    this.state = state;
    this.details = details;
  }

  @Override
  public void sendCallback(InspectorCallbacks clientCallbacks) {
    clientCallbacks.notifyStateChange(state, details);
  }

  @Override
  public boolean waitJPF2stop() {
    return state == InspectorStatusChange.JPF_STOPPED;
  }

}
