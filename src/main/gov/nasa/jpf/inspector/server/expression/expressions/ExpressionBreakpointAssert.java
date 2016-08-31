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
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanBinaryOperator;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanInterface;
import gov.nasa.jpf.inspector.server.expression.InspectorState;

/**
 * Represents the pseudo-breakpoint that is created when the user uses the "assert" command.
 */
public class ExpressionBreakpointAssert extends ExpressionBooleanBinaryOperator {

  private static final String TOKEN_ASSERT = "assert";

  public ExpressionBreakpointAssert (ExpressionBooleanInterface position, ExpressionBooleanInterface condition) {
    super(position, condition);
  }

  @Override
  public boolean evaluateExpression (InspectorState state) {
    assert children != null;
    assert children.size() == 2;
    assert children.get(0) != null && children.get(1) != null;

    try {
      // Position
      if (!children.get(0).evaluateExpression(state)) {
        return false;
      }

      // Condition not hold
      return !children.get(1).evaluateExpression(state);

    } catch (JPFInspectorException e) {
      return false;
    }

  }

  @Override
  public String getDetails (InspectorState state) {
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
  }

  public String getNormalizedPosition () {
    String pos = children.get(0).getNormalizedExpression();

    if (pos.startsWith("position=")) {
      pos = pos.substring(9);
    }

    return pos;
  }

  public String getNormalizedCondition () {
    return children.get(1).getNormalizedExpression();
  }

  @Override
  public String getNormalizedExpression () {
    String sb = TOKEN_ASSERT +
            " ( " +
            getNormalizedPosition() +
            " ) ( " +
            getNormalizedCondition() +
            " )";

    return sb;
  }
}
