
package gov.nasa.jpf.inspector.server.expression.expressions;

import gov.nasa.jpf.inspector.interfaces.CustomHitCondition;
import gov.nasa.jpf.inspector.interfaces.InstructionPosition;
import gov.nasa.jpf.inspector.server.breakpoints.BreakPointModes;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.expression.InspectorState.ListenerMethod;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.utils.expressions.Expressions;
import gov.nasa.jpf.jvm.bytecode.JSR;
import gov.nasa.jpf.jvm.bytecode.JSR_W;
import gov.nasa.jpf.jvm.bytecode.RET;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.Path;
import gov.nasa.jpf.vm.Transition;
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.bytecode.ReturnInstruction;

public class ExpressionBreakpointCustomHitCondition extends ExpressionBooleanLeaf {
  private static final boolean DEBUG = true;
  private final JPFInspector inspector;
  private final String name;
  private final Expressions parameters;
  private final CustomHitCondition customHitCondition;


  public ExpressionBreakpointCustomHitCondition(JPFInspector inspector, String name, Expressions expressions) {
    assert name != null;
    assert inspector != null;
    assert expressions != null;
    this.inspector = inspector;
    this.parameters = expressions;
    this.name = name;
    this.customHitCondition = null;
    // TODO instantiate
  }

  @Override
  public boolean evaluateExpression (InspectorState state) {
    assert state != null;
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".evaluateExpression(...)");
    }
    return false;
  }

  @Override
  public BreakPointModes getBPMode () {
    return BreakPointModes.BP_MODE_USER_CONDITION;
  }

  @Override
  public String getDetails (InspectorState state) {
    if (state != null && evaluateExpression(state)) {

    }
    return "TODO details";
  }

  @Override
  public String getNormalizedExpression () {
    String string = name + "(" + "TODO parameters" + ")";
    return string;
  }

}
