package gov.nasa.jpf.inspector.server.expression.expressions;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.server.breakpoints.BreakPointModes;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.utils.expressions.ClassName;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.MethodInfo;

import java.util.Objects;

/**
 * Represents the "class = [classname]" hit condition that hits before each instruction in the specified class.
 */
public class ExpressionBreakpointClassName extends ExpressionBooleanLeaf {
  private ClassName className;

  public ExpressionBreakpointClassName(ClassName className) {
    this.className = className;
  }

  @Override
  public String getDetails(InspectorState state) {
    return "In class \"" + state.getVM().getInstruction().getMethodInfo().getClassName() + "\".";
  }

  @Override
  public boolean evaluateExpression(InspectorState state) throws JPFInspectorException {
    if (state.getListenerMethod() != InspectorState.ListenerMethod.LM_EXECUTE_INSTRUCTION) {
      return false;
    }
    Instruction instruction = state.getVM().getInstruction();
    MethodInfo methodInfo = instruction.getMethodInfo();
    if (methodInfo == null) {
      return false;
    }
    ClassInfo actualClassInfo = methodInfo.getClassInfo();
    if (actualClassInfo == null) {
      return false;
    }
    return className.isSameClass(actualClassInfo);
  }

  @Override
  public BreakPointModes getBPMode() {
    return BreakPointModes.BP_UNIMPORTANT;
  }

  @Override
  public String getNormalizedExpression() {
    return "class = " + className.getClassName();
  }
}
