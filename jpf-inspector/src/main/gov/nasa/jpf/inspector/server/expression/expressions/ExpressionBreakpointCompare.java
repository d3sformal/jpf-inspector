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
import gov.nasa.jpf.inspector.server.breakpoints.BreakPointModes;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.ExpressionStateRootNode;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.programstate.StateNodeInterface;
import gov.nasa.jpf.inspector.server.programstate.StateReadableValue;
import gov.nasa.jpf.inspector.server.programstate.relop.RelationOperator;

/**
 * @author Alf
 * 
 */
public class ExpressionBreakpointCompare extends ExpressionBooleanLeaf {

  private final ExpressionStateRootNode leftOp;
  private final ExpressionStateRootNode rightOp;

  private final JPFInspector inspector;
  private final RelationOperator relOper;

  private String detail = "";

  public ExpressionBreakpointCompare (ExpressionStateRootNode leftOp, ExpressionStateRootNode rightOp, RelationOperator relOper, JPFInspector inspector) {
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

      if (!(sniLeft instanceof StateReadableValue)) {
        detail = "left operand does not represent a value";
        return false;
      }

      if (!(sniRight instanceof StateReadableValue)) {
        detail = "right operand does not represent a value";
        return false;
      }

      return relOper.compare((StateReadableValue) sniLeft, (StateReadableValue) sniRight);
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

  @Override
  public String getDetails (InspectorState state) {
    return detail;
  }

}
