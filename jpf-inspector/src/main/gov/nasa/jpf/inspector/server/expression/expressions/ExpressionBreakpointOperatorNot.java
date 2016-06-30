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
