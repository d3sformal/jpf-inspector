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

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNoVMConnected;
import gov.nasa.jpf.inspector.server.expression.ExpressionStateRootNode;
import gov.nasa.jpf.inspector.server.expression.ExpressionStateUnaryOperator;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.programstate.StateNodeInterface;
import gov.nasa.jpf.inspector.server.programstate.StateThreadInfo;
import gov.nasa.jpf.vm.VM;

public class ExpressionStateThread extends ExpressionStateUnaryOperator<ExpressionStateStackFrame> implements
    ExpressionStateRootNode<ExpressionStateStackFrame> {

  private static final String TOKEN_HASH_THREAD = "#thread";

  private final Integer threadNum;

  /**
   * 
   * @param threadNum For which thread we should be uses in the expression
   * @param child Follower in the expression
   */
  public ExpressionStateThread (Integer threadNum, ExpressionStateStackFrame child) {
    super(child);

    this.threadNum = threadNum;
  }

  @Override
  public StateNodeInterface getResultExpression (JPFInspector inspector, InspectorState state) throws JPFInspectorException {
    assert state != null;

    VM vm = state.getJVM();
    JPFInspectorNoVMConnected.checkVM(vm);

    StateThreadInfo sti = new StateThreadInfo(inspector, vm, threadNum);

    ExpressionStateStackFrame child = getChild();
    if (child == null) {
      return sti;
    } else {
      return child.getResultExpression(sti);
    }
  }


  /* @see gov.nasa.jpf.inspector.server.expression.ExpressionNodeInterface#getNormalizedExpression() */
  @Override
  public String getNormalizedExpression () {
    String childExp = "";
    if (child != null) {
      childExp = child.getNormalizedExpression();
    }

    // TOKEN_HASH_THREAD : '#thread' ;
    if (threadNum == null) {
      return TOKEN_HASH_THREAD + childExp;
    }
    return TOKEN_HASH_THREAD + '[' + threadNum + ']' + childExp;
  }
}
