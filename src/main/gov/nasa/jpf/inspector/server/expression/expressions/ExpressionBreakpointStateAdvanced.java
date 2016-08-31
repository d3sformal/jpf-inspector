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

package gov.nasa.jpf.inspector.server.expression.expressions;

import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.expression.InspectorState.ListenerMethod;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.search.SearchListener;

/**
 * Hits when new new transition is started.
 * ({@link SearchListener#stateAdvanced(gov.nasa.jpf.search.Search)} is calles)
 * 
 */
public class ExpressionBreakpointStateAdvanced extends ExpressionBooleanLeaf {

  @Override
  public boolean evaluateExpression (InspectorState state) {
    assert state != null;
    return (state.getListenerMethod() == ListenerMethod.LM_STATE_ADVANCED);
  }

  @Override
  public String getDetails (InspectorState state) {
    if (state == null) {
      return "";
    }
    Instruction instr = state.getVM().getCurrentTransition().getLastStep().getInstruction();
    return "New transition started (vm.transition=" + state.getVM().getCurrentTransition() + ", vm.stateID=" + state.getSearch().getStateId()
        + ", vm.currentThread= " + state.getVM().getCurrentThread().getId() + " lastInstr=" + instr + ", position=" + instr.getFilePos() + ")";
  }

  @Override
  public String getNormalizedExpression () {
    return "state_advanced";
  }
}
