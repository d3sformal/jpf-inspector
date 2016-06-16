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
public class PSEThread extends ProgramStateEntry {

  private static final long serialVersionUID = 6527461109192602939L;

  public static final String EXPRESSION_THREAD_KEY_WORD = "#thread"; // Representation of the thread in program state expression

  private final int threadNum; // Number of the represented thread
  private final ThreadInfo.State state; // State of represented thread - Blocked, Running, ....
  private final String threadName; // Name of represented thread

  private final String threadTypeName; // Type of represented thread

  // As used in standard Java
  private final int priority;
  private final boolean isDaemon;

  private PSEMethod[] refCallStack; // Stack with called method in the represented thread

  private boolean referenceCreated;

  public PSEThread(StateNode sn, int threadNum, State state, String threadName,
                   String threadTypeName, int priority, boolean isDaemon, PSEMethod[] refCallStack) {
    super(sn);

    referenceCreated = refCallStack != null;

    this.threadNum = threadNum;
    this.state = state;
    this.threadName = threadName;
    this.threadTypeName = threadTypeName;
    this.priority = priority;
    this.isDaemon = isDaemon;
    this.refCallStack = refCallStack;
  }

  /**
   * @return Gets list of called methods
   */
  public PSEMethod[] getCallStack () throws JPFInspectorException {
    loadReferences();
    return refCallStack;
  }

  public int getThreadNum () {
    return threadNum;
  }

  public final ThreadInfo.State getState () {
    return state;
  }

  public final String getThreadName () {
    return threadName;
  }

  public final String getThreadTypeName () {
    return threadTypeName;
  }

  public final int getPriority () {
    return priority;
  }

  public final boolean isDaemon () {
    return isDaemon;
  }

  /**
   * Lazy load of references
   */
  private void loadReferences() throws JPFInspectorException {
    if (!referenceCreated) {
      if (DEBUG) {
        System.out.println(this.getClass().getSimpleName() + ".loadReferences() - lazy reference load");
      }
      // Create a copy of this PSE with filled references
      ProgramStateEntry pse = getInspector().evaluateStateExpression(getStateExpr());
      assert (pse instanceof PSEThread);
      PSEThread myCopy = (PSEThread) pse;

      refCallStack = myCopy.getCallStack();

      referenceCreated = true;
    }
  }

  @Override
  public <T> T visit (PSEVisitor<T> visitor) throws JPFInspectorException {
    return visitor.visitPSEThread(this);
  }

}
