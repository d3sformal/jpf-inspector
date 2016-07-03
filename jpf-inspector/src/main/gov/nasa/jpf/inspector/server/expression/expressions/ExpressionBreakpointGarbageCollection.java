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

/**
 * Represents the "garbage_collection = [begin/end/both]" hit condition that hits when garbage collection starts
 * or ends.
 */
public class ExpressionBreakpointGarbageCollection extends ExpressionBooleanLeaf {

  private BreakPointModes bpMode;

  public ExpressionBreakpointGarbageCollection(BreakPointModes bpMode) {
    assert (bpMode != null);

    if ((bpMode == BreakPointModes.BP_MODE_GC_BEGIN) ||
        (bpMode == BreakPointModes.BP_MODE_GC_END) ||
        (bpMode == BreakPointModes.BP_MODE_GC_BOTH)) {
      this.bpMode = bpMode;
    } else {
      throw new RuntimeException("Internal error - Unsupported bpMode(" + bpMode + ")");
    }
  }

  @Override
  public boolean evaluateExpression(InspectorState state) {
    assert state != null;
    if (state.getListenerMethod() == ListenerMethod.LM_GC_BEGIN) {
      return (bpMode == BreakPointModes.BP_MODE_GC_BEGIN) || (bpMode == BreakPointModes.BP_MODE_GC_BOTH);
    }
    if (state.getListenerMethod() == ListenerMethod.LM_GC_END) {
      return (bpMode == BreakPointModes.BP_MODE_GC_END) || (bpMode == BreakPointModes.BP_MODE_GC_BOTH);
    }
    return false;
  }

  @Override
  public String getNormalizedExpression() {
    StringBuilder sb = new StringBuilder(20);
    sb.append("garbage_collection=");
    if (bpMode == BreakPointModes.BP_MODE_GC_BEGIN) {
      sb.append("begin");
    } else if (bpMode == BreakPointModes.BP_MODE_GC_END) {
      sb.append("end");
    } else if (bpMode == BreakPointModes.BP_MODE_GC_BOTH) {
      sb.append("both");
    } else {
      throw new RuntimeException("Internal error - Unsupported bpMode(" + bpMode + ")");
    }
    return sb.toString();
  }
}
