package gov.nasa.jpf.inspector.server.pathanalysis;

import gov.nasa.jpf.jvm.Step;
import gov.nasa.jpf.jvm.bytecode.ARETURN;
import gov.nasa.jpf.jvm.bytecode.DIRECTCALLRETURN;
import gov.nasa.jpf.jvm.bytecode.DRETURN;
import gov.nasa.jpf.jvm.bytecode.FRETURN;
import gov.nasa.jpf.jvm.bytecode.IRETURN;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.jvm.bytecode.InstructionVisitorAdapter;
import gov.nasa.jpf.jvm.bytecode.LRETURN;
import gov.nasa.jpf.jvm.bytecode.NATIVERETURN;
import gov.nasa.jpf.jvm.bytecode.RETURN;
import gov.nasa.jpf.jvm.bytecode.ReturnInstruction;

class CheckReturnInstruction extends InstructionVisitorAdapter {
  private boolean isReturnInstr = false;
  
  public boolean isReturnStep(Step step) {
    assert(step != null);
    return isReturnInstruction(step.getInstruction());
  }

  public boolean isReturnInstruction(Instruction inst) {
    assert(inst != null);
    isReturnInstr = false;
    inst.accept(this);
    return isReturnInstr;
  }
  
  @Override
  public void visit(ReturnInstruction ins) {
    isReturnInstr = true;
  }

  @Override
  public void visit(ARETURN ins) {
    isReturnInstr = true;
  }

  @Override
  public void visit(DIRECTCALLRETURN ins) {
    isReturnInstr = true;
  } 
    
  @Override
  public void visit(DRETURN ins) {
    isReturnInstr = true;
  }

  @Override
  public void visit(FRETURN ins) {
    isReturnInstr = true;
  }
  
  @Override
  public void visit(IRETURN ins) {
    isReturnInstr = true;
  }
  
  @Override
  public void visit(LRETURN ins) {
    isReturnInstr = true;
  }
  
  @Override
  public void visit(NATIVERETURN ins) {
    isReturnInstr = true;
  }

  @Override
  public void visit(RETURN ins) {
    isReturnInstr = true;
  }

  //DIRECTCALLRETURN
  // Synthetic instruction - there is no related Call instruction - user typically manually creates suitable (Direct)CallStackFrame so that postpone execution of current instruction  

  //RET - paired with JPR (used only for (finally) -> is not considered to be a method call
  
}