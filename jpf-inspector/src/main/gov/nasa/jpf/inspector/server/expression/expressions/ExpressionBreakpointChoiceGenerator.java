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

import gov.nasa.jpf.inspector.server.breakpoints.BreakPointModes;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.expression.InspectorState.ListenerMethod;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.ThreadInfo;

/**
 * Represents the "cg = [choice generator type]" hit condition that hits when a choice is requested from
 * a choice generator of the given type.
 */
public class ExpressionBreakpointChoiceGenerator extends ExpressionBooleanLeaf {

  private BreakPointModes bpMode;

  public ExpressionBreakpointChoiceGenerator(BreakPointModes bpMode) {
    assert bpMode != null;

    if ((bpMode == BreakPointModes.BP_MODE_CHOICE_BOTH) ||
        (bpMode == BreakPointModes.BP_MODE_CHOICE_DATA) ||
        (bpMode == BreakPointModes.BP_MODE_CHOICE_SCHEDULING)) {
      this.bpMode = bpMode;
    } else {
      throw new RuntimeException("Internal error - Unsupported bpMode(" + bpMode + ")");
    }
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
    StringBuilder sb = new StringBuilder(20);
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
