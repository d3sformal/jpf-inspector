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

import gov.nasa.jpf.inspector.client.JPFInspectorClientInterface;
import gov.nasa.jpf.inspector.server.callbacks.InspectorServerCallbacks;
import gov.nasa.jpf.vm.VM;

/**
 * Interface provided by JPFInspector back-end ("server") part.
 *
 * The server itself contains more capabilities than those exposed by this interface (and the interfaces it extends).
 * However, these other capabilities don't usually need to be used from within the client.
 *
 * Note that the client and the server are still rather tightly coupled and in case of need, one can use the method
 * {@link JPFInspectorClientInterface#getServer()} to gain access to the full server, not just this partial interface.
 */
public interface JPFInspectorBackEndInterface extends
        CommandsInterface,
        BreakPointManagerInterface,
        ProgramStateInterface,
        ChoiceGeneratorsInterface,
        AttributeManagerInterface{


  /**
   * Gets the server-side serializer of callbacks. When this serializer's methods are called, new callbacks are sent
   * to the client.
   */
  InspectorServerCallbacks getServerCallbacks();
  /**
   * Blocks until JPF is connected with the Inspector. If JPF is already connected to the Inspector,
   * it returns immediately.
   */
  void waitUntilJpfBecomesConnected();

  /**
   * This may violate encapsulation, but, on the other hand, the Inspector's codebase is already too fragmented and
   * adding more interfaces and yet another hierarchy of data objects isn't worth it, but perhaps we'll think of an
   * alternative.
   *
   * It is the caller's responsibility to only use the VM when the VM is paused.
   */
  VM getVM();

  /**
   * If JPF is currently blocked, then it will be prevented from resuming until {@link #permitJpfToResumeAgain()} is
   * called. If JPF is currently running, this method will simply return false.
   *
   * If multiple calls to this method are made, then all of them must be undone using {@link #permitJpfToResumeAgain()}
   * before JPF can resume again.
   *
   * Synchronized on the access lock.
   *
   * @return True if JPF is currently stopped and will be prevented from resuming; false otherwise.
   */
  boolean preventJpfFromResuming();

  /**
   * If JPF is currently paused and prevented from resuming, it will be woken up and allowed to resume.
   *
   * If multiple {@link #preventJpfFromResuming()} calls were made, all of them must be undone using this mehod before
   * JPF resumes.
   *
   * Synchronized on the access lock.
   * @throws IllegalStateException When JPF was not being prevented from resuming.
   */
  void permitJpfToResumeAgain();
}
