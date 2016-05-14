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
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.Transition;

public class ExpressionBreakpointThreadScheduled extends ExpressionBooleanLeaf {

  private BreakPointModes bpMode;
  private final Integer threadNum; // / Null means any thread!!!

  public ExpressionBreakpointThreadScheduled (BreakPointModes bpMode, Integer threadNum) {
    if ((bpMode == BreakPointModes.BP_MODE_THREAD_SCHEDULED_BOTH) || (bpMode == BreakPointModes.BP_MODE_THREAD_SCHEDULED_IN)
        || (bpMode == BreakPointModes.BP_MODE_THREAD_SCHEDULED_OUT)) {
      this.bpMode = bpMode;
    } else {
      throw new RuntimeException("Internal error - Unsupported bpMode(" + bpMode + ")");
    }
    this.threadNum = threadNum;
  }

  @Override
  public boolean evaluateExpression (InspectorState state) {
    // Notice We handle "threadScheduled"
    // in this case the getLastThread gets Newly scheduled thread and
    // lastTransition (if exists), holds previous thread
    assert state != null;
    if (state.getListenerMethod() != ListenerMethod.LM_THREAD_SCHEDULED) {
      return false;
    }
    if (threadNum == null) {
      return true;
    }

    VM vm = state.getJVM();
    if (bpMode == BreakPointModes.BP_MODE_THREAD_SCHEDULED_BOTH) {
      Transition tr = vm.getLastTransition();
      ThreadInfo prevTi = (tr != null ? tr.getThreadInfo() : null);

      return ((vm.getLastThreadInfo().getId() == threadNum) // Newly planned thread
      || ((prevTi != null) && (prevTi.getId() == threadNum))); // / Previous thread if exists

    } else if (bpMode == BreakPointModes.BP_MODE_THREAD_SCHEDULED_IN) {
      return vm.getLastThreadInfo().getId() == threadNum;

    } else if (bpMode == BreakPointModes.BP_MODE_THREAD_SCHEDULED_OUT) {
      Transition tr = vm.getLastTransition();
      if (tr == null) {
        return false;
      }
      ThreadInfo prevTi = tr.getThreadInfo();
      return prevTi.getId() == threadNum;
    }

    return false;
  }

  public Integer getThreadNum () {
    return threadNum;
  }

  @Override
  public BreakPointModes getBPMode () {
    return bpMode;
  }

  @Override
  public String getNormalizedExpression () {
    StringBuffer sb = new StringBuffer(64);
    sb.append("thread_scheduled");
    sb.append('=');

    if (bpMode == BreakPointModes.BP_MODE_THREAD_SCHEDULED_IN) {
      sb.append("in");
    } else if (bpMode == BreakPointModes.BP_MODE_THREAD_SCHEDULED_OUT) {
      sb.append("out");
    } else if (bpMode == BreakPointModes.BP_MODE_THREAD_SCHEDULED_BOTH) {
      sb.append("both");
    } else {
      throw new RuntimeException("Internal error - Unsupported bpMode(" + bpMode + ")");
    }
    if (threadNum != null) {
      sb.append(':');
      sb.append(threadNum);
    }

    return sb.toString();
  }
}
