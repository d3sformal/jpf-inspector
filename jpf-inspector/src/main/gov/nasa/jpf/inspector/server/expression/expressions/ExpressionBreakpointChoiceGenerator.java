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
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.ThreadInfo;

public class ExpressionBreakpointChoiceGenerator extends ExpressionBooleanLeaf {

  private BreakPointModes bpMode;

  public ExpressionBreakpointChoiceGenerator(BreakPointModes bpMode) {
    assert bpMode != null;

    if ((bpMode == BreakPointModes.BP_MODE_CHOICE_BOTH) ||
        (bpMode == BreakPointModes.BP_MODE_CHOICE_DATA) ||
        (bpMode == BreakPointModes.BP_MODE_CHOICE_SCHEDULING)) {
      this.bpMode = bpMode;
    } else
      throw new RuntimeException("Internal error - Unsupported bpMode(" + bpMode + ")");
  }

  @Override
  public boolean evaluateExpression(InspectorState state) {
    assert state != null;
    if (state.getListenerMethod() != ListenerMethod.LM_CHOICE_GENERATOR_ADVANCED) {
      return false;
    }

    ChoiceGenerator<?> cg = state.getVM().getChoiceGenerator();
    Class<?> choiceType = cg.getChoiceType();

    if (bpMode == BreakPointModes.BP_MODE_CHOICE_BOTH) {
      return true;
    } else if (bpMode == BreakPointModes.BP_MODE_CHOICE_SCHEDULING) {
      return ThreadInfo.class.isAssignableFrom(choiceType);
    } else if (bpMode == BreakPointModes.BP_MODE_CHOICE_DATA) {
      return !ThreadInfo.class.isAssignableFrom(choiceType);
    } else {
      throw new RuntimeException("Unsupported " + BreakPointModes.class.getSimpleName() + "enum entry " + bpMode);
    }
  }

  @Override
  public BreakPointModes getBPMode() {
    return bpMode;
  }

  @Override
  public String getNormalizedExpression() {
    StringBuffer sb = new StringBuffer(20);
    sb.append("choice_generator=");
    if (bpMode == BreakPointModes.BP_MODE_CHOICE_DATA) {
      sb.append("data");
    } else if (bpMode == BreakPointModes.BP_MODE_CHOICE_SCHEDULING) {
      sb.append("scheduling");
    } else if (bpMode == BreakPointModes.BP_MODE_CHOICE_BOTH) {
      sb.append("both");
    } else {
      throw new RuntimeException("Internal error - Unsupported bpMode(" + bpMode + ")");
    }
    return sb.toString();
  }

}
