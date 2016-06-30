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

package gov.nasa.jpf.inspector.server.choicegenerators;

import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.VMListener;

/**
 * This interface is only implemented by {@link ChoiceGeneratorsManager}. It is used by the JPF thread (via the JPF
 * Inspector listener) to allow the Inspector to intercept choice genrators and print or make choices.
 */
public interface ChoiceGeneratorNotifications {
  /**
   * Called only from {@link VMListener#choiceGeneratorAdvanced(VM, ChoiceGenerator)} by the JPF thread.
   *
   * @param cg Choice generator that should be advanced.
   * @param vm The VM.
   * @param inspState Representation of the JPF inspector and its state when CG.advance() occurs.
   */
  void notifyChoiceGeneratorAdvance(ChoiceGenerator<?> cg, VM vm, InspectorState inspState);
  
}
