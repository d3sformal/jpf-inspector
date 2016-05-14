//
// Copyright (C) 2010 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
// 
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
// 
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//  

package gov.nasa.jpf.inspector.server.expression.expressions;

import gov.nasa.jpf.inspector.server.breakpoints.BreakPointModes;
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
  public BreakPointModes getBPMode () {
    return BreakPointModes.BP_MODE_STATE_ADVANCED;
  }

  @Override
  public String getDetails (InspectorState state) {
    if (state == null) {
      return "";
    }
    Instruction instr = state.getJVM().getCurrentTransition().getLastStep().getInstruction();
    return "New transition started (vm.transition=" + state.getJVM().getCurrentTransition() + ", vm.stateID=" + state.getSearch().getStateId()
        + ", vm.currentThread= " + state.getJVM().getCurrentThread().getId() + " lastInstr=" + instr + ", position=" + instr.getFilePos() + " )";
  }

  @Override
  public String getNormalizedExpression () {
    return "state_advanced";
  }
}
