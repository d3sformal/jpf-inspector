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
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.Transition;

/**
 * Represents the "thread_scheduled = [in/out/both] : [index]" hit condition that hits when the specified thread is
 * scheduled in or out.
 */
public class ExpressionBreakpointThreadScheduled extends ExpressionBooleanLeaf {

  private BreakPointModes bpMode;
  /**
   * The index of the thread we are interested in, or "null" for any thread.
   */
  private final Integer threadNum;

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

    VM vm = state.getVM();
    if (bpMode == BreakPointModes.BP_MODE_THREAD_SCHEDULED_BOTH) {
      Transition tr = vm.getLastTransition();
      ThreadInfo prevTi = (tr != null ? tr.getThreadInfo() : null);

      return ((vm.getCurrentThread().getId() == threadNum) // Newly planned thread
      || ((prevTi != null) && (prevTi.getId() == threadNum))); // / Previous thread if exists

    } else if (bpMode == BreakPointModes.BP_MODE_THREAD_SCHEDULED_IN) {
      return vm.getCurrentThread().getId() == threadNum;

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

  @Override
  public BreakPointModes getBPMode () {
    return bpMode;
  }

  @Override
  public String getNormalizedExpression () {
    StringBuilder sb = new StringBuilder(64);
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
