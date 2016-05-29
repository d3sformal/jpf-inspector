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
import gov.nasa.jpf.inspector.server.breakpoints.BreakPointModes;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanInterface;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.ExpressionStateRootNode;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.programstate.StateNodeInterface;
import gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface;
import gov.nasa.jpf.inspector.server.programstate.relop.RelationOperator;

import java.util.List;

/**
 * @author Alf
 * 
 */
public class ExpressionBreakpointCompare extends ExpressionBooleanLeaf {

  private final ExpressionStateRootNode<?> leftOp;
  private final ExpressionStateRootNode<?> rightOp;

  private final JPFInspector inspector;
  private final RelationOperator relOper;

  private String detail = "";

  public ExpressionBreakpointCompare (ExpressionStateRootNode<?> leftOp, ExpressionStateRootNode<?> rightOp, RelationOperator relOper, JPFInspector inspector) {
    super();
    this.leftOp = leftOp;
    this.rightOp = rightOp;
    this.relOper = relOper;
    this.inspector = inspector;
  }

  /* @see gov.nasa.jpf.inspector.server.expression.ExpressionBooleanInterface#evaluateExpression(gov.nasa.jpf.inspector.server.expression.InspectorState) */
  @Override
  public boolean evaluateExpression (InspectorState state) throws JPFInspectorException {
    try {
      detail = "";

      StateNodeInterface sniLeft = leftOp.getResultExpression(inspector, state);
      StateNodeInterface sniRight = rightOp.getResultExpression(inspector, state);

      if (!(sniLeft instanceof StateReadableValueInterface)) {
        detail = "left operator does not represent a value";
        return false;
      }

      if (!(sniRight instanceof StateReadableValueInterface)) {
        detail = "right operator does not represent a value";
        return false;
      }

      return relOper.compare((StateReadableValueInterface) sniLeft, (StateReadableValueInterface) sniRight);
      // return expectedResult ^ (sniLeft.equals(sniRight));

    } catch (JPFInspectorException e) {
      detail = e.getMessage();
      throw e;
    }
  }

  /* @see gov.nasa.jpf.inspector.server.expression.ExpressionBooleanInterface#getBPMode() */
  @Override
  public BreakPointModes getBPMode () {
    return BreakPointModes.BP_MODE_USER_CONDITION;
  }

  /* @see gov.nasa.jpf.inspector.server.expression.ExpressionBooleanInterface#getNormalizedExpression() */
  @Override
  public String getNormalizedExpression () {
    return '(' + leftOp.getNormalizedExpression() + ' ' + relOper.getNormalizedText() + ' ' + rightOp.getNormalizedExpression() + ')';
  }

  /* @see gov.nasa.jpf.inspector.server.expression.ExpressionNodeInterface#getChilds() */
  @Override
  public List<? extends ExpressionBooleanInterface> getChilds () {
    return null;
  }

  @Override
  public String getDetails (InspectorState state) {
    return detail;
  }

}
