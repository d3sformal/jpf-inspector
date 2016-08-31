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
