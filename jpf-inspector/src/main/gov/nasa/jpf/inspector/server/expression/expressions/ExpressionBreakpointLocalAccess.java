package gov.nasa.jpf.inspector.server.expression.expressions;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.server.breakpoints.BreakPointModes;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.utils.expressions.FieldName;

import javax.print.DocFlavor;

public class ExpressionBreakpointLocalAccess extends ExpressionBooleanLeaf {
  public ExpressionBreakpointLocalAccess(LocalAccessMode accessMode, String localName) {

  }

  @Override
  public boolean evaluateExpression(InspectorState state) throws JPFInspectorException {
    return false;
  }

  @Override
  public BreakPointModes getBPMode() {
    return BreakPointModes.BP_UNIMPORTANT;
  }

  @Override
  public String getNormalizedExpression() {
    return ""; // TODO
  }

  public static enum LocalAccessMode {
    ANY_ACCESS,
    READ,
    WRITE
  }
}
