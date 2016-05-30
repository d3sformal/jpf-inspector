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
import gov.nasa.jpf.inspector.server.expression.ExpressionBoolean;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanBinaryOperator;
import gov.nasa.jpf.inspector.server.expression.InspectorState;

public class ExpressionBreakpointOperatorAnd extends ExpressionBooleanBinaryOperator {

  public ExpressionBreakpointOperatorAnd (ExpressionBoolean left, ExpressionBoolean right) {
    super(left, right);
  }

  @Override
  public boolean evaluateExpression (InspectorState state) throws JPFInspectorException {
    assert children != null;
    assert children.size() == 2;
    assert children.get(0) != null && children.get(1) != null;

    if (!children.get(0).evaluateExpression(state)) {
      return false;
    }
    return children.get(1).evaluateExpression(state);
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
    StringBuffer sb = new StringBuffer();
    sb.append("( ");
    sb.append(children.get(0).getNormalizedExpression());
    sb.append(" ) and ( ");
    sb.append(children.get(1).getNormalizedExpression());
    sb.append(" )");

    return sb.toString();
  }

}
