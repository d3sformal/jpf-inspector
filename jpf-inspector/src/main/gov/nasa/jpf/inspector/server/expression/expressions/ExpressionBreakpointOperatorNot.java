//
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
import gov.nasa.jpf.inspector.server.expression.ExpressionBoolean;
import gov.nasa.jpf.inspector.server.expression.InspectorState;

import java.util.Objects;

/**
 * Represents the breakpoint expression "not (hit condition 1)".
 */
public class ExpressionBreakpointOperatorNot extends ExpressionBoolean {

  private ExpressionBoolean innerHitCondition;

  public ExpressionBreakpointOperatorNot(ExpressionBoolean inner) {
     innerHitCondition = inner;
  }

  @Override
  public boolean evaluateExpression (InspectorState state) throws JPFInspectorException {
       return !innerHitCondition.evaluateExpression(state);
  }

  @Override
  public BreakPointModes getBPMode() {
    return BreakPointModes.BP_UNIMPORTANT;
  }

  @Override
  public String getDetails (InspectorState state) {
    try {
      if (evaluateExpression(state)) {
        String innerDetails = innerHitCondition.getDetails(state);
        if (innerDetails != null && !Objects.equals(innerDetails.trim(), "")) {
          return "(negated condition) " + innerDetails;
        }
        return "";
      } else {
        return "";
      }
    } catch (JPFInspectorException e) {
      return "";
    }
  }

  @Override
  public String getNormalizedExpression () {
    return "not " + innerHitCondition.getNormalizedExpression();
  }
}
