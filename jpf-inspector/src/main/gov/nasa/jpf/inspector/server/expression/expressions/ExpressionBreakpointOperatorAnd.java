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
import gov.nasa.jpf.inspector.server.expression.ExpressionBoolean;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanBinaryOperator;
import gov.nasa.jpf.inspector.server.expression.InspectorState;

/**
 * Represents the breakpoint expression "(hit condition 1) and (hit condition 2)".
 */
public class ExpressionBreakpointOperatorAnd extends ExpressionBooleanBinaryOperator {

  public ExpressionBreakpointOperatorAnd (ExpressionBoolean left, ExpressionBoolean right) {
    super(left, right);
  }

  @Override
  public boolean evaluateExpression (InspectorState state) throws JPFInspectorException {
    assert children != null;
    assert children.size() == 2;
    assert children.get(0) != null && children.get(1) != null;

    return children.get(0).evaluateExpression(state) && children.get(1).evaluateExpression(state);
  }

  @Override
  public String getDetails (InspectorState state) {
    try {
      if (evaluateExpression(state)) {
        String leftDetails = children.get(0).getDetails(state);
        String rightDetails = children.get(1).getDetails(state);

        if (leftDetails != null && !leftDetails.trim().isEmpty()) {
          if (rightDetails != null && !rightDetails.trim().isEmpty()) {
            return leftDetails + "\n\t" + rightDetails;
          } else {
            return leftDetails;
          }
        } else {
          if (rightDetails != null && !rightDetails.trim().isEmpty()) {
            return rightDetails;
          } else {
            return "";
          }
        }
      } // if BP Holds
      return "";
    } catch (JPFInspectorException e) {
      return "";
    }
  }

  @Override
  public String getNormalizedExpression () {
    String sb = "( " +
            children.get(0).getNormalizedExpression() +
            " ) and ( " +
            children.get(1).getNormalizedExpression() +
            " )";

    return sb;
  }

}
