package gov.nasa.jpf.inspector.server.expression.expressions;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.server.breakpoints.BreakPointModes;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.jvm.bytecode.JVMLocalVariableInstruction;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.bytecode.StoreInstruction;

/**
 * Represent the family of local variable access hit conditions that hit when the local variable is about to be referenced.
 * This family contains the hit conditions "local_access", "local_read" and "local_write".
 */
public class ExpressionBreakpointLocalAccess extends ExpressionBooleanLeaf {
  private final LocalAccessMode accessMode;
  private final String localName;

  public ExpressionBreakpointLocalAccess(LocalAccessMode accessMode, String localName) {
    assert accessMode != null;
    assert localName != null;
    this.accessMode = accessMode;
    this.localName = localName;
  }

  @Override
  public boolean evaluateExpression(InspectorState state) throws JPFInspectorException {
    assert state != null;

    if (state.getListenerMethod() != InspectorState.ListenerMethod.LM_EXECUTE_INSTRUCTION) {
      return false;
    }

    VM vm = state.getVM();
    Instruction inst = vm.getInstruction();

    if (!(inst instanceof JVMLocalVariableInstruction)) {
      return false;
    }
    JVMLocalVariableInstruction jvmLocalVariableInstruction = (JVMLocalVariableInstruction)inst;
    String variableName = jvmLocalVariableInstruction.getLocalVariableName();
    if (!java.util.Objects.equals(variableName, localName)) {
      return false;
    }
    switch (accessMode) {
      case ANY_ACCESS:
        return true;
      case READ:
        return !(inst instanceof StoreInstruction);
      case WRITE:
        return (inst instanceof StoreInstruction);
    }
    throw new RuntimeException("Internal error - Unsupported access mode (" + accessMode + ")");
  }

  @Override
  public BreakPointModes getBPMode() {
    return BreakPointModes.BP_UNIMPORTANT;
  }

  @Override
  public String getNormalizedExpression() {
    StringBuilder result = new StringBuilder();
    if (accessMode == LocalAccessMode.ANY_ACCESS) {
      result.append("local_access");
    } else if (accessMode == LocalAccessMode.READ) {
      result.append("local_read");
    } else if (accessMode == LocalAccessMode.WRITE) {
      result.append("local_write");
    } else {
      throw new RuntimeException("Internal error - Unsupported access mode (" + accessMode + ")");
    }
    result.append('=');
    result.append(localName);

    return result.toString();
  }

  /**
   * Indicates what kind of instructions we are interested in.
   */
  public enum LocalAccessMode {
    /**
     * Any local variable instruction will do.
     */
    ANY_ACCESS,
    /**
     * Only in variable read instructions.
     */
    READ,
    /**
     * Only in variable write instructions.
     */
    WRITE
  }
}
