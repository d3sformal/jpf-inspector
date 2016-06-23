
package gov.nasa.jpf.inspector.server.expression.expressions;

import gov.nasa.jpf.inspector.common.pse.ProgramStateEntry;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.interfaces.CustomHitCondition;
import gov.nasa.jpf.inspector.server.breakpoints.BreakPointModes;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.ExpressionStateRootNode;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.programstate.StateNodeInterface;
import gov.nasa.jpf.inspector.utils.InspectorConfiguration;
import gov.nasa.jpf.inspector.utils.expressions.Expressions;
import gov.nasa.jpf.inspector.utils.parser.GenericErrorRuntimeException;

/**
 * Represents a custom hit condition call, such as "custom1('a')".
 */
public class ExpressionBreakpointCustomHitCondition extends ExpressionBooleanLeaf {
  private static final boolean DEBUG = true;
  private final JPFInspector inspector;
  private final String name;
  private final Expressions parameters;
  private final CustomHitCondition customHitCondition;


  public ExpressionBreakpointCustomHitCondition(JPFInspector inspector, String name, CustomHitCondition customHitCondition, Expressions expressions) {
    assert name != null;
    assert inspector != null;
    assert expressions != null;
    this.inspector = inspector;
    this.parameters = expressions;
    this.name = name;
    this.customHitCondition = customHitCondition;
    try {
      this.customHitCondition.initialize(expressions.toStringArray());
    } catch (Throwable e) {
      throw new GenericErrorRuntimeException(new JPFInspectorGenericErrorException(e.getMessage()));
    }
  }

  @Override
  public boolean evaluateExpression (InspectorState state) throws JPFInspectorException {
    assert state != null;

    try {
      return customHitCondition.isHit(inspector, state);
    } catch (Throwable e) {
      return InspectorConfiguration.getInstance().doesCustomHitConditionExceptionBreak();
    }
  }

  @Override
  public BreakPointModes getBPMode () {
    return BreakPointModes.BP_MODE_USER_CONDITION;
  }

  @Override
  public String getDetails (InspectorState state) {

        try {
          return customHitCondition.getDetails(inspector, state);
        } catch (Throwable e) {
          return "";
        }
  }

  @Override
  public String getNormalizedExpression () {
    String string = name + "(" + parameters.toString() + ")";

    return string;
  }

}
