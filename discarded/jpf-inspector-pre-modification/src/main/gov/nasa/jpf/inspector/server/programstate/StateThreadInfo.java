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

import gov.nasa.jpf.inspector.interfaces.JPFInspectorException;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorNoThread;
import gov.nasa.jpf.inspector.server.expression.ExpressionParserInterface;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.programstate.client.PSEMethod;
import gov.nasa.jpf.inspector.server.programstate.client.PSEThread;
import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.ThreadInfo.State;
import gov.nasa.jpf.jvm.ThreadList;

/**
 * @author Alf
 * 
 */
public class StateThreadInfo extends StateNode {

  protected final ThreadInfo ti;

  public StateThreadInfo (JPFInspector inspector, JVM vm, Integer threadNum) throws JPFInspectorException {
    this(inspector, vm, threadNum, 2);
  }

  public StateThreadInfo (JPFInspector inspector, JVM vm, Integer threadNum, int referenceDepth) throws JPFInspectorException {
    super(inspector, referenceDepth);
    ThreadInfo ti = null;
    assert (vm != null);

    if (threadNum == null) {
      ti = vm.getCurrentThread();
      if (ti == null) {
        throw new JPFInspectorNoThread();
      }

    } else {
      ThreadList tl = vm.getKernelState().getThreadList();
      ti = tl.getThreadInfoForId(threadNum);

      if (ti == null) {
        throw new JPFInspectorNoThread(threadNum);
      }
    }

    assert ti != null; // Error should be reported before

    this.ti = ti;
    setStateExpr(StateThreadInfo.createStateExpression(ti));

  }

  @Override
  public PSEThread getResultExpression (String name, int clientID) throws JPFInspectorException {

    int threadNum = ti.getId();
    State state = ti.getState();
    String threadName = ti.getName();
    String threadTypeName = ti.getClassInfo().getName();

    boolean isDaemon = ti.isDaemon();
    int priority = ti.getPriority();

    PSEMethod[] refCallStack = null;
    if (referenceDepth > 0) {
      refCallStack = new PSEMethod[ti.getStackDepth()];
      for (int i = 0; i < ti.getStackDepth(); i++) {
        StateStackFrame ssf = new StateStackFrame(this, i, referenceDepth - 1);
        refCallStack[i] = ssf.getResultExpression(name, clientID);
      }
    }

    return new PSEThread(name, clientID, this, threadNum, state, threadName, threadTypeName, priority, isDaemon, refCallStack);
  }

  public ThreadInfo getThreadInfo () {
    return ti;
  }

  /**
   * Creates state expression which can be used to get selected {@link ThreadInfo}
   * 
   * @param ti ThreadInfo
   * @return State expression which can be parsed by {@link ExpressionParserInterface#getExpressionStateInterface(String)}.
   */
  public static String createStateExpression (ThreadInfo ti) {
    assert (ti != null);
    return PSEThread.EXPRESSION_THREAD_KEY_WORD + '[' + ti.getId() + ']';
  }

}
