package gov.nasa.jpf.inspector.server.expression.expressions;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.server.breakpoints.BreakPointModes;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.utils.expressions.MethodName;

public class ExpressionBreakpointMethodName extends ExpressionBooleanLeaf {
  public ExpressionBreakpointMethodName(MethodName methodName) {

  }

  @Override
  public boolean evaluateExpression(InspectorState state) throws JPFInspectorException {
    return false;
  }

  @Override
  public BreakPointModes getBPMode() {
    return null;
  }

  @Override
  public String getNormalizedExpression() {
    return null;
  }
}
