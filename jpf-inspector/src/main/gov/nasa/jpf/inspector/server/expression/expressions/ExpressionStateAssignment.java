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
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNotLValueException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNotValueException;
import gov.nasa.jpf.inspector.server.expression.ExpressionStateRootNode;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.programstate.StateNodeInterface;
import gov.nasa.jpf.inspector.server.programstate.StateReadableValue;
import gov.nasa.jpf.inspector.server.programstate.StateWritableValue;

/**
 * Represents the "lvalue = rvalue" expression that is used as the argument to the command "set".
 * Maintains the <i>lvalue</i> and the <i>rvalue</i> as hierarchy-1 expressions.
 */
public class ExpressionStateAssignment {

  private final ExpressionStateRootNode lVal;
  private final ExpressionStateRootNode rVal;

  public ExpressionStateAssignment (ExpressionStateRootNode lVal, ExpressionStateRootNode rVal) {
    this.lVal = lVal;
    this.rVal = rVal;
  }

  /**
   * @return Gets runtime representation of the new value.
   */
  public StateReadableValue getRValue (JPFInspector inspector, InspectorState state)
          throws JPFInspectorException {

    StateNodeInterface sni = rVal.getResultExpression(inspector, state);
    assert (sni != null);

    if (!(sni instanceof StateReadableValue)) {
      throw new JPFInspectorNotValueException(sni.getStateExpr());
    }
    return (StateReadableValue) sni;
  }

  /**
   * @return Gets runtime representation of the position where the value should be assigned.
   * @throws JPFInspectorException If the expression is not an l-value.
   */
  public StateWritableValue getLValue (JPFInspector inspector, InspectorState state) throws JPFInspectorException {
    StateNodeInterface sni = lVal.getResultExpression(inspector, state);
    assert (sni != null);

    if (!(sni instanceof StateWritableValue)) {
      throw new JPFInspectorNotLValueException(sni.getStateExpr());
    }
    return (StateWritableValue) sni;
  }
}
