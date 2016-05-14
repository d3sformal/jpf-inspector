package gov.nasa.jpf.inspector.server.expression.expressions;

import gov.nasa.jpf.inspector.interfaces.JPFInspectorException;
import gov.nasa.jpf.inspector.server.expression.ExpressionBoolean;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanBinaryOperator;
import gov.nasa.jpf.inspector.server.expression.InspectorState;

public class ExpressionBreakpointOperatorOr extends ExpressionBooleanBinaryOperator {

  public ExpressionBreakpointOperatorOr (ExpressionBoolean left, ExpressionBoolean right) {
    super(left, right);
  }

  @Override
  public boolean evaluateExpression (InspectorState state) throws JPFInspectorException {
    assert children != null;
    assert children.size() == 2;
    assert children.get(0) != null && children.get(1) != null;
    try {
      if (children.get(0).evaluateExpression(state)) {
        return true;
      }
    } catch (JPFInspectorException e_left) {
      try {
        // Try if right parameter can be fullfilled
        return children.get(1).evaluateExpression(state);
      } catch (JPFInspectorException e_right) {
        throw e_left; // Throw first exception
      }
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
      }
      return "";
    } catch (JPFInspectorException e) {
      return "";
    } // if BP Holds
  }

  @Override
  public String getNormalizedExpression () {
    StringBuffer sb = new StringBuffer();
    sb.append("( ");
    sb.append(children.get(0).getNormalizedExpression());
    sb.append(" ) or ( ");
    sb.append(children.get(1).getNormalizedExpression());
    sb.append(" )");

    return sb.toString();
  }

}
