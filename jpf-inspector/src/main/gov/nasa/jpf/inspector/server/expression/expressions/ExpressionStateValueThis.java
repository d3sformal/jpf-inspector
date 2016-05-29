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
import gov.nasa.jpf.inspector.server.programstate.StateNodeInterface;
import gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface;
import gov.nasa.jpf.inspector.server.programstate.StateStackFrame;
import gov.nasa.jpf.inspector.server.programstate.StateValueStackSlot;

/**
 * @author Alf
 * 
 */
public class ExpressionStateValueThis extends ExpressionStateValue {

  private static final String TOKEN_HASH_THIS = "#this";

  public ExpressionStateValueThis (ExpressionStateValue child) {
    super(child);
  }

  /*
   * @see
   * gov.nasa.jpf.inspector.server.expression.expressions.ExpressionStateValue#getResultExpression(gov.nasa.jpf.inspector.server.programstate.
   * StateReadableValueInterface
   * )
   */
  @Override
  public StateReadableValueInterface getResultExpression (StateReadableValueInterface srv) throws JPFInspectorException {
    assert (srv != null);

    StateReadableValueInterface srvi = srv.createThisValue();

    ExpressionStateValue child = getChild();
    if (child == null) {
      return srvi;
    } else {
      return child.getResultExpression(srvi);
    }
  }

  public StateNodeInterface getResultExpression (StateStackFrame ssf) throws JPFInspectorException {

    // "This" is stored in slot 0, however it is guaranteed only when method is called (not during method execution)
    // There is not a better solution if "this" should be an assignable value :-(
    // otherwise StateElementInfo from the sf.getThis() can be created
    StateValueStackSlot svss = StateValueStackSlot.createThisSlotValue(ssf);

    ExpressionStateValue child = getChild();
    if (child == null) {
      return svss;
    } else {
      return child.getResultExpression(svss);
    }
  }

  /* @see gov.nasa.jpf.inspector.server.expression.ExpressionNodeInterface#getNormalizedExpression() */
  @Override
  public String getNormalizedExpression () {
    // TOKEN_HASH_THIS : '#this' ;
    // TOKEN_HASH_THIS WS? a=cmdStateExpressionValue[$expFactory]? { $expr = $expFactory.getStateValueThis($a.expr); }

    return '.' + TOKEN_HASH_THIS + (child != null ? child.getNormalizedExpression() : "");
  }
}
