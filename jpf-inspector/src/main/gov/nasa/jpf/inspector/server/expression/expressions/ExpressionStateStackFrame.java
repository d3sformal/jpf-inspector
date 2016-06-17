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
import gov.nasa.jpf.inspector.server.expression.ExpressionStateUnaryOperator;
import gov.nasa.jpf.inspector.server.programstate.StateNodeInterface;
import gov.nasa.jpf.inspector.server.programstate.StateStackFrame;
import gov.nasa.jpf.inspector.server.programstate.StateThreadInfo;

/**
 * Represents the ".#stackFrame[integer]" expression.
 */
public final class ExpressionStateStackFrame extends ExpressionStateUnaryOperator<ExpressionStateValue> {

  private static final String TOKEN_HASH_STACK_FRAME = "#stackFrame";

  private final Integer stackFrameNum;

  // Only 3 child types are supported
  public ExpressionStateStackFrame (ExpressionStateValueName name, Integer stackFrame) {
    this((ExpressionStateValue) name, stackFrame);
  }

  public ExpressionStateStackFrame (ExpressionStateStackFrameSlot sfs, Integer stackFrame) {
    this((ExpressionStateValue) sfs, stackFrame);
  }

  public ExpressionStateStackFrame (ExpressionStateValueThis child, Integer stackFrame) {
    this((ExpressionStateValue) child, stackFrame);
  }

  public ExpressionStateStackFrame (ExpressionStateValueStatic child, Integer stackFrame) {
    this((ExpressionStateValue) child, stackFrame);
  }

  private ExpressionStateStackFrame (ExpressionStateValue child, Integer stackFrame) {
    super(child);

    this.stackFrameNum = stackFrame;
  }

  public StateNodeInterface getResultExpression (StateThreadInfo thread) throws JPFInspectorException {
    assert thread != null;

    StateStackFrame thisStackFrame = new StateStackFrame(thread, stackFrameNum);

    ExpressionStateValue child = getChild();
    if (child == null) {
      return thisStackFrame;
    } else {
      // #stackSlot
      if (child instanceof ExpressionStateStackFrameSlot) {
        ExpressionStateStackFrameSlot childSfs = (ExpressionStateStackFrameSlot) child;
        return childSfs.getExpressionFromStackFrame(thisStackFrame);
      // #this (also comes from #super, #outerClass etc...)
      } else if (child instanceof ExpressionStateValueThis) {
        ExpressionStateValueThis childThis = (ExpressionStateValueThis) child;
        return childThis.getExpressionFromStackFrame(thisStackFrame);
      // a name
      } else if (child instanceof ExpressionStateValueName) {
        ExpressionStateValueName childName = (ExpressionStateValueName) child;
        return childName.getExpressionFromStackFrame(thisStackFrame);
      // #static
      } else if (child instanceof ExpressionStateValueStatic) {
        ExpressionStateValueStatic childStatic = (ExpressionStateValueStatic) child;
        return childStatic.getExpressionFromStackFrame(thisStackFrame);
      } else {
        throw new RuntimeException("Invalid child type.");
      }
    }
  }

  @Override
  public String getNormalizedExpression () {

    String childExp = "";
    if (getChild() != null) {
      childExp = getChild().getNormalizedExpression();
    }

    if (stackFrameNum == null) {
      return '.' + TOKEN_HASH_STACK_FRAME + childExp;
    } else {
      return '.' + TOKEN_HASH_STACK_FRAME + '[' + stackFrameNum + ']' + childExp;
    }
  }

}
