package gov.nasa.jpf.inspector.server.pathanalysis;

import gov.nasa.jpf.jvm.Step;
import gov.nasa.jpf.jvm.bytecode.ATHROW;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.jvm.bytecode.InstructionVisitorAdapter;

public class CheckThrowInstruction extends InstructionVisitorAdapter {
  private boolean isThrowInstr = false;

  public boolean isThrowStep(Step step) {
    assert(step != null);
    return isThrowInstruction(step.getInstruction());
  }

  public boolean isThrowInstruction(Instruction inst) {
    assert(inst != null);
    isThrowInstr = false;
    inst.accept(this);
    return isThrowInstr;
  }

  @Override
  public void visit(ATHROW ins) {
    isThrowInstr = true;
  }
}

