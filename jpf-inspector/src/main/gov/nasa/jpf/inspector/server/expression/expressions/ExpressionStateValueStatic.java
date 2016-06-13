//
// Copyright (C) 2011 United States Government as represented by the
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
import gov.nasa.jpf.inspector.server.programstate.StateElementInfo;
import gov.nasa.jpf.inspector.server.programstate.StateNodeInterface;
import gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface;
import gov.nasa.jpf.inspector.server.programstate.StateStackFrame;
import gov.nasa.jpf.inspector.server.programstate.StateValueElementInfoField;

/**
 * Represents #static and #static[index]
 * 
 * @author Alf
 * 
 */
public class ExpressionStateValueStatic extends ExpressionStateValue {

  public static final String TOKEN_HASH_STATIC = "#static";

  private final Integer staticFieldIndex;

  public ExpressionStateValueStatic (ExpressionStateValue child, Integer staticFieldIndex) {
    super(child);

    this.staticFieldIndex = staticFieldIndex;
  }

  @Override
  public StateReadableValueInterface getResultExpression (StateReadableValueInterface parent) throws JPFInspectorException {

    StateReadableValueInterface result;
    if (staticFieldIndex != null) {
      result = StateValueElementInfoField.createStaticFieldFromIndex(parent, staticFieldIndex, 1);
    } else {
      result = StateElementInfo.createStaticClass(parent, parent.getClassInfo());
    }

    ExpressionStateValue child = getChild();
    if (child == null) {
      return result;
    } else {
      return child.getResultExpression(result);
    }
  }

  public StateNodeInterface getResultExpression (StateStackFrame stackFrame) throws JPFInspectorException {
    StateReadableValueInterface result;
    if (staticFieldIndex != null) {
      StateElementInfo staticEi = StateElementInfo.createStaticClass(stackFrame, stackFrame.getClassInfo());
      result = StateValueElementInfoField.createStaticFieldFromIndex(staticEi, staticFieldIndex, 1);
    } else {
      result = StateElementInfo.createStaticClass(stackFrame, stackFrame.getClassInfo());
    }

    ExpressionStateValue child = getChild();
    if (child == null) {
      return result;
    } else {
      return child.getResultExpression(result);
    }
  }

  @Override
  public String getNormalizedExpression () {
    // TOKEN_HASH_STATIC : '#static' ;
    // TOKEN_HASH_STATIC WS? '[' WS? intValue WS? ']' WS? a=cmdStateExpressionValue[$expFactory]?
    // TOKEN_HASH_STATIC WS? a=cmdStateExpressionClass[$expFactory]?
    if (staticFieldIndex != null) {
      return '.' + TOKEN_HASH_STATIC + '[' + staticFieldIndex + ']' + (child != null ? child.getNormalizedExpression() : "");
    }

    return '.' + TOKEN_HASH_STATIC + (child != null ? child.getNormalizedExpression() : "");
  }

}
