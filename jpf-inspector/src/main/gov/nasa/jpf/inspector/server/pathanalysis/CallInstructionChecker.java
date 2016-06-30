//
// Copyright (C) 2011 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
//
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
//
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//
package gov.nasa.jpf.inspector.server.pathanalysis;

import gov.nasa.jpf.jvm.bytecode.*;
import gov.nasa.jpf.vm.Step;
import gov.nasa.jpf.vm.Instruction;

/**
 * Checks whether the instruction is a method call (i.e. it puts stuff on the call stack).
 */
public class CallInstructionChecker extends JVMInstructionVisitorAdapter {
  private boolean isCallInstr = false;

  /**
   * Returns a value that indicates whether the step's instruction is a method call.
   * @param step The step to check.
   */
  public boolean isCallStep(Step step) {
    assert(step != null);
    return isCallInstruction(step.getInstruction());
  }

  private boolean isCallInstruction(Instruction inst) {
    assert(inst != null);
    isCallInstr = false;
    assert(inst instanceof JVMInstruction);
    ((JVMInstruction)inst).accept(this);
    
    return isCallInstr;
  }
  
  @Override
  public void visit(INVOKECLINIT ins) {
    isCallInstr = true;
  }
  
  @Override
  public void visit(JVMInvokeInstruction ins) {
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
