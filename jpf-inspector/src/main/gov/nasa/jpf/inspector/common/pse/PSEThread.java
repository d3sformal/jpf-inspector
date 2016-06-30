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

package gov.nasa.jpf.inspector.common.pse;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionStateThread;
import gov.nasa.jpf.inspector.server.programstate.StateThreadInfo;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.ThreadInfo.State;

/**
 * Represents a thread.
 *
 * Linked classes:
 *
 * - {@link StateThreadInfo}
 * - {@link ExpressionStateThread}
 */
public final class PSEThread extends ProgramStateEntry {

  private static final long serialVersionUID = 6527461109192602939L;

  public static final String EXPRESSION_THREAD_KEY_WORD = "#thread";
  /**
   * Index of the represented thread
   */
  private final int threadNum;
  /**
   * State of represented thread - Blocked, Running, ....
   */
  private final ThreadInfo.State state;
  /**
   * Name of represented thread, taken directly from {@link ThreadInfo#getName()}
   */
  private final String threadName;
  /**
   * Name of the class of the represented thread
   */
  private final String threadTypeName;

  // As used in standard Java
  private final int priority;
  private final boolean isDaemon;

  private PSEMethod[] refCallStack; // Stack with called method in the represented thread

  public PSEThread(int threadNum, State state, String threadName,
                   String threadTypeName, int priority, boolean isDaemon, PSEMethod[] refCallStack) {

    this.threadNum = threadNum;
    this.state = state;
    this.threadName = threadName;
    this.threadTypeName = threadTypeName;
    this.priority = priority;
    this.isDaemon = isDaemon;
    this.refCallStack = refCallStack;
  }

  public PSEMethod[] getCallStack () {
    return refCallStack;
  }

  public int getThreadNum () {
    return threadNum;
  }

  public ThreadInfo.State getState () {
    return state;
  }

  public String getThreadName () {
    return threadName;
  }

  public String getThreadTypeName () {
    return threadTypeName;
  }

  public int getPriority () {
    return priority;
  }

  public boolean isDaemon () {
    return isDaemon;
  }

  @Override
  public <T> T visit (PSEVisitor<T> visitor) throws JPFInspectorException {
    return visitor.visitPSEThread(this);  }

}
