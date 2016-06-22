package gov.nasa.jpf.inspector.server.pathanalysis;

import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.Step;
import gov.nasa.jpf.jvm.bytecode.ATHROW;
import gov.nasa.jpf.jvm.bytecode.JVMInstruction;
import gov.nasa.jpf.jvm.bytecode.JVMInstructionVisitorAdapter;

/**
 * Checker whether an instruction is a throw instruction (that we often can't handle).
 */
public class ThrowInstructionChecker extends JVMInstructionVisitorAdapter {
  private boolean isThrowInstruction = false;

  /**
   * Returns a value that indicates whether the step's instruction is a throw insturction.
   * @param step The step to check.
   */
  public boolean isThrowStep(Step step) {
    assert(step != null);
    return isThrowInstruction(step.getInstruction());
  }

  private boolean isThrowInstruction(Instruction inst) {
    assert(inst != null);
    isThrowInstruction = false;
    assert (inst instanceof JVMInstruction);
    ((JVMInstruction)inst).accept(this);
    return isThrowInstruction;
  }

  @Override
  public void visit(ATHROW ins) {
    isThrowInstruction = true;
  }
}

