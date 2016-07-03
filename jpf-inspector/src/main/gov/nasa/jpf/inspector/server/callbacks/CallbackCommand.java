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
package gov.nasa.jpf.inspector.server.callbacks;

import gov.nasa.jpf.inspector.interfaces.InspectorCallbacks;

/**
 * A callback command is a server-side class that sends a specific callback to the client. Each class corresponds
 * to a type of callback.
 *
 * These callbacks are put by the server into a queue from where the callback-sender thread executes them by sending
 * them to the client.
 */
public interface CallbackCommand {
  /**
   * Sends this callback to the client. Only the callback sending thread calls this method.
   * 
   * @param clientCallbacks Client-side class that displays callbacks to the user.
   */
  void sendCallback(InspectorCallbacks clientCallbacks);
  
  /**
   * Indicates whether the callback sending thread should wait for JPF to be paused.
   *
   *  If false, the callback should be sent as soon as all previous callbacks are processed.
   *  If true, the callbacks should only be sent once JPF is stopped. Until then, the callback sending thread
   *  will be blocked.
   */
  boolean waitJPF2stop();
}
