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

package gov.nasa.jpf.inspector.server.expression;

import gov.nasa.jpf.inspector.server.breakpoints.BreakpointHandler;
import gov.nasa.jpf.inspector.server.jpf.InspectorListener;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.VM;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a program state, i.e. a point in the program timeline.
 */
public class InspectorStateImpl implements InspectorState {

  /**
   * For each thread, this stores the instruction that was last executed. The instructions threads are currently
   * pointing to but have not yet executed are not in this.
   *
   * The key is thread id.
   */
  private final Map<Integer, Instruction> lastExecutedInstructions = new HashMap<>();

  private VM vm;
  /**
   * Search class used for the last executed step.
   */
  private Search search;

  /**
   * This field is using by breakpoint hit conditions to see if they should trigger. It contains the last method that
   * of the {@link InspectorListener} that caused the {@link BreakpointHandler#checkBreakpoints(InspectorState)} method
   * to be called.
   */
  private InspectorState.ListenerMethod listenerMethod = ListenerMethod.LM_NOT_IN_LIST;
  private ElementInfo lastCreatedOrReleasedElementInfo;

  public  void notifyListenerElementInfoMethodCall(VM vm,  ListenerMethod listenerMethod, ElementInfo elementInfo) {
    lastCreatedOrReleasedElementInfo = elementInfo;
    this.listenerMethod = listenerMethod;
    this.vm = vm;
  }
  @Override
  public ElementInfo getLastCreatedOrReleasedElementInfo() {
    return lastCreatedOrReleasedElementInfo;
  }

  @Override
  public VM getVM() {
    return vm;
  }


  @Override
  public Instruction getLastExecutedInstruction(int thread) {
    if (lastExecutedInstructions.containsKey(thread)) {
      return lastExecutedInstructions.get(thread);
    } else {
      return null;
    }
  }

  @Override
  public Search getSearch () {
    return search;
  }

  @Override
  public ListenerMethod getListenerMethod () {
    return listenerMethod;
  }

  // Has to be called after each executed instruction
  public void instructionExecuted(int threadId, Instruction executedInstruction, VM newJVM) {
    this.vm = newJVM;
    this.listenerMethod = ListenerMethod.LM_INSTRUCTION_EXECUTED;

    lastExecutedInstructions.put(threadId, executedInstruction);

  }

  public void stateChanged (Search search, ListenerMethod listenerMethod) {
    this.vm = search.getVM();
    this.search = search;
    this.listenerMethod = listenerMethod;
  }

  public void notifyListenerMethodCall (ListenerMethod listenerMethod, VM jvm) {
    this.listenerMethod = listenerMethod;
    this.vm = jvm;
  }

}
