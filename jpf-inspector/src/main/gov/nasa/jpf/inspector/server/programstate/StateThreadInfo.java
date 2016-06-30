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
