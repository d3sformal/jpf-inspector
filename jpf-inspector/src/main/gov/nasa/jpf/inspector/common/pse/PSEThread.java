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

package gov.nasa.jpf.inspector.common.pse;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionStateThread;
import gov.nasa.jpf.inspector.server.programstate.StateNode;
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

  public PSEThread(StateNode sn, int threadNum, State state, String threadName,
                   String threadTypeName, int priority, boolean isDaemon, PSEMethod[] refCallStack) {
    super(sn);

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
