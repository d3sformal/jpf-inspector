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

import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.migration.MigrationUtilities;
import gov.nasa.jpf.inspector.server.breakpoints.BreakPointModes;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.expression.InspectorState.ListenerMethod;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.jpf.StopHolder;
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.KernelState;
import gov.nasa.jpf.vm.StackFrame;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.ThreadList;

import java.util.Iterator;

/**
 * Represents a hidden breakpoint condition used to implement the "step_out" command.
 * 
 * The breakpoint hits if the current thread returns into a given method on stack
 * (i.e. when the provided {@link StackFrame} becomes the top frame for given thread)
 * 
 * @author Alfifi
 */
public class ExpressionBreakpointStepOut extends ExpressionBooleanLeaf {

  /**
   * The active thread at the time the stepping was initiated.
   */
  final private int threadNum;
  /**
   * Specify to which SF should breakpoint step out (in which it should hit) - counted from the top of the call stack
   * This is only used in the expression's getNormalizedExpression which is never called anyway.
   */
  final private int stackDepth;

  // final private StackFrame sf; // Represents called method (used only for assertions)

  /**
   * If stack depth is lower than this value, then the breakpoint hits. This value is the stack depth at the time
   * the stepping is initiated.
   */
  final private int maxBreakingStackDepth;

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

    int maxBreakingStackDepth = ti.countStackFrames() - stackDepth + 1;
    if (maxBreakingStackDepth < 0) {
      maxBreakingStackDepth = 0;
    }
    this.maxBreakingStackDepth = maxBreakingStackDepth;

    // this.sf = ti.getCallerStackFrame(stackDepth);
  }

  /**
   * This is apparently only used from the grammar and using grammar for stepping is completely undocumented,
   * so why even have this here? Maybe for testing? or something?
   */
  public static ExpressionBreakpointStepOut getStepOutToCaller (JPFInspector inspector, int threadId, int stackDepth) throws JPFInspectorGenericErrorException {
    assert inspector != null;
    StopHolder sh = inspector.getStopHolder();
    assert sh != null;
    InspectorState inspState = sh.getInspectorState();
    if (inspState == null) {
      throw new JPFInspectorGenericErrorException(
          "cannot create given breakpoint, JPF has to be connected and stopped - program state is required to create given breakpoint.");
    }

    VM vm = inspState.getVM();
    assert (vm != null);

    // Find thread with given ThreadID
    KernelState ks = vm.getKernelState();
    ThreadList threadList = ks.getThreadList();

    ThreadInfo ti = null;

    for (ThreadInfo itTi : threadList) {
      assert (itTi != null);

      if (itTi.getId() == threadId) {
        ti = itTi;
        break;
      }
    }

    if (ti == null) {
      throw new JPFInspectorGenericErrorException("Thread with index " + threadId + " not exists");
    }

    return new ExpressionBreakpointStepOut(ti, stackDepth);
  }

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
