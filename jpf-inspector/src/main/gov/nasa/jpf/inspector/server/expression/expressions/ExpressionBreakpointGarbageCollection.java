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
    } else
      throw new RuntimeException("Internal error - Unsupported bpMode(" + bpMode + ")");
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
  public BreakPointModes getBPMode() {
    return bpMode;
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
