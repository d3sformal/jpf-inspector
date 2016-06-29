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

package gov.nasa.jpf.inspector.server.programstate;

import gov.nasa.jpf.inspector.common.Constants;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNoThread;
import gov.nasa.jpf.inspector.server.expression.ExpressionParserInterface;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.common.pse.PSEMethod;
import gov.nasa.jpf.inspector.common.pse.PSEThread;
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.ThreadInfo.State;
import gov.nasa.jpf.vm.ThreadList;

/**
 * Represents a thread. Information about it is kept as a {@link ThreadInfo} object.
 */
public final class StateThreadInfo extends StateNode {

  private final ThreadInfo ti;

  /**
   * Initializes a new hierarchy-2 representation of the thread with the specified index.
   *
   * @param inspector The Inspector server.
   * @param vm The virtual machine.
   * @param threadNum Index of the thread.
   */
  public StateThreadInfo (JPFInspector inspector, VM vm, Integer threadNum) throws JPFInspectorException {
    super(inspector);
    ThreadInfo threadInfo;
    assert (vm != null);

    if (threadNum == null) {
      threadInfo = vm.getCurrentThread();
      if (threadInfo == null) {
        throw new JPFInspectorNoThread();
      }
    } else {
      ThreadList tl = vm.getKernelState().getThreadList();
      threadInfo = tl.getThreadInfoForId(threadNum);

      if (threadInfo == null) {
        throw new JPFInspectorNoThread(threadNum);
      }
    }

    assert threadInfo != null; // Error should be reported before

    this.ti = threadInfo;
    setStateExpr(StateThreadInfo.createStateExpression(threadInfo));
  }

  @Override
  public PSEThread toHierarchy3() throws JPFInspectorException {

    int threadNum = ti.getId();
    State state = ti.getState();
    String threadName = ti.getName();
    String threadTypeName = Constants.UNKNOWN_THREAD_TYPE_NAME;
    if (ti.getClassInfo() != null) {
      threadTypeName = ti.getClassInfo().getName();
    }

    boolean isDaemon = ti.isDaemon();
    int priority = ti.getPriority();

    PSEMethod[] refCallStack;

    refCallStack = new PSEMethod[ti.getStackDepth()];
    for (int i = 0; i < ti.getStackDepth(); i++) {
      StateStackFrame ssf = new StateStackFrame(this, i);
      refCallStack[i] = ssf.toHierarchy3();
    }

    return new PSEThread(threadNum, state, threadName, threadTypeName, priority, isDaemon, refCallStack);
  }

  /**
   * Gets the JPF representation of the the thread.
   */
  public ThreadInfo getThreadInfo () {
    return ti;
  }

  /**
   * Creates state expression which can be used to get selected {@link ThreadInfo}
   * 
   * @param ti ThreadInfo
   * @return State expression which can be parsed by {@link ExpressionParserInterface#getExpressionStateInterface(String)}.
   */
  private static String createStateExpression(ThreadInfo ti) {
    assert (ti != null);
    return PSEThread.EXPRESSION_THREAD_KEY_WORD + '[' + ti.getId() + ']';
  }

}
