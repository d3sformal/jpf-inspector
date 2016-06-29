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
   *
   * @param ti Thread which is considered (all other threads are ignored by this breakpoint)
   * @param stackDepth How many stack frames (from the current call stack) should be exited before the breakpoint hit (on the first such instruction).
   *                   This is always 1.
   */
  private ExpressionBreakpointStepOut(ThreadInfo ti, int stackDepth) {
    assert stackDepth == 1;
    this.threadNum = ti.getId();
    this.stackDepth = stackDepth;

    int newMaxBreakingStackDepth = ti.countStackFrames() - stackDepth + 1;
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
    return new ExpressionBreakpointStepOut(ti, 1);
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
