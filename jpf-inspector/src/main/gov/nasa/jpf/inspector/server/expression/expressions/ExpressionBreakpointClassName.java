package gov.nasa.jpf.inspector.server.expression.expressions;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.server.breakpoints.BreakPointModes;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.utils.expressions.ClassName;

public class ExpressionBreakpointClassName extends ExpressionBooleanLeaf {
  public ExpressionBreakpointClassName(ClassName className) {
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
