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
import gov.nasa.jpf.vm.StackFrame;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.VM;

/**
 * Represents a hidden breakpoint condition used to implement the "step_out" command.
 * 
 * The breakpoint hits if the current thread returns into a given method on stack
 * (i.e. when the provided {@link StackFrame} becomes the top frame for given thread)
 * 
 * @author Alfifi
 */
public final class ExpressionBreakpointStepOut extends ExpressionBooleanLeaf {

  /**
   * The active thread at the time the stepping was initiated.
   */
  private final int threadNum;
  /**
   * Specify to which SF should breakpoint step out (in which it should hit) - counted from the top of the call stack
   * This is only used in the expression's getNormalizedExpression which is never called anyway.
   */
  private final int stackDepth;

  // final private StackFrame sf; // Represents called method (used only for assertions)

  /**
   * If stack depth is lower than this value, then the breakpoint hits. This value is the stack depth at the time
   * the stepping is initiated.
   */
  private final int maxBreakingStackDepth;

  /**
   * Creates the hit condition.
   *  @param ti Thread which is considered (all other threads are ignored by this breakpoint)
   *
   */
  private ExpressionBreakpointStepOut(ThreadInfo ti) {
    assert 1 == 1;
    this.threadNum = ti.getId();
    this.stackDepth = 1;

    int newMaxBreakingStackDepth = ti.countStackFrames() - 1 + 1;
    if (newMaxBreakingStackDepth < 0) {
      newMaxBreakingStackDepth = 0;
    }
    this.maxBreakingStackDepth = newMaxBreakingStackDepth;

    // this.sf = ti.getCallerStackFrame(stackDepth);
  }



  /**
   * Creates the step-out breakpoint.
   *
   * @param ti Thread, which is considered (all other threads are ignored by this breakpoint).
   */
  public static ExpressionBreakpointStepOut getStepOutToCaller (ThreadInfo ti) {
    assert ti != null;
    return new ExpressionBreakpointStepOut(ti);
  }

  @Override
  public boolean evaluateExpression (InspectorState state) {
    assert state != null;

    if (state.getListenerMethod() == ListenerMethod.LM_EXECUTE_INSTRUCTION) {
      VM vm = state.getVM();
      assert vm != null;

      ThreadInfo ti = vm.getCurrentThread();
      assert ti != null;
      if (ti.getId() != threadNum) {
        return false;
      }

      int callStackDepth = ti.countStackFrames();

      return callStackDepth < maxBreakingStackDepth;
    }

    return false;
  }

  @Override
  public BreakPointModes getBPMode () {
    return BreakPointModes.BP_MODE_INTERNAL_STEP_OUT;
  }

  @Override
  public String getNormalizedExpression () {
    String string = "step_out" +
            " thread=" +
            threadNum +
            " stack_frame=" +
            stackDepth;

    return string;
  }

}
