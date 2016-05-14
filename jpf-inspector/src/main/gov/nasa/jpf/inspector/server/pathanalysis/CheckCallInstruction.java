package gov.nasa.jpf.inspector.server.pathanalysis;

import gov.nasa.jpf.vm.Step;
import gov.nasa.jpf.jvm.bytecode.INVOKECLINIT;
import gov.nasa.jpf.jvm.bytecode.INVOKEINTERFACE;
import gov.nasa.jpf.jvm.bytecode.INVOKESPECIAL;
import gov.nasa.jpf.jvm.bytecode.INVOKESTATIC;
import gov.nasa.jpf.jvm.bytecode.INVOKEVIRTUAL;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.jvm.bytecode.InstructionVisitorAdapter;
import gov.nasa.jpf.jvm.bytecode.InvokeInstruction;

public class CheckCallInstruction extends InstructionVisitorAdapter {
  private boolean isCallInstr = false;
  
  public boolean isCallStep(Step step) {
    assert(step != null);
    return isCallInstruction(step.getInstruction());
  }

  public boolean isCallInstruction(Instruction inst) {
    assert(inst != null);
    isCallInstr = false;
    inst.accept(this);
    
    return isCallInstr;
  }
  
  @Override
  public void visit(INVOKECLINIT ins) {
    isCallInstr = true;
  }
  
  @Override
  public void visit(InvokeInstruction ins) {
    isCallInstr = true;
  }
  
  @Override
  public void visit(INVOKEINTERFACE ins) {
    isCallInstr = true;
  }
  
  @Override
  public void visit(INVOKESPECIAL ins) {
    isCallInstr = true;
  }
  
  @Override
  public void visit(INVOKESTATIC ins) {
    isCallInstr = true;
  }
  
  @Override
  public void visit(INVOKEVIRTUAL ins) {
    isCallInstr = true;

    //TODO 
    //  There is no guaranty that it is really the method that has been called
    //  we silently suppose that in case of MJI methods no overloading take place (so that the instruction consistently calls the same method)
    //MethodInfo invokedMethod = ins.getInvokedMethod();
    //assert(invokedMethod != null);
    //isCallInstr = !invokedMethod.isMJI(); 
    // MJI methods does not have paired return instruction (and any inner instruction) -> so that consider such calls as ordinary not call instruction  
  }

  // Not considered as call -> the native_return is paired with normal invoke into the native method (which does not have paired return instruction)
  //@Override
  //public void visit(EXECUTENATIVE ins) {}

  //JSR, JSR_W -> not a call used to jump into "finally" into try-catch-finally
  // See JVM Spec., Chapter 7.12, 7.13, http://java.sun.com/docs/books/jvms/second_edition/html/Compiling.doc.html#9934
}
