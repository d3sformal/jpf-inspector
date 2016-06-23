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
