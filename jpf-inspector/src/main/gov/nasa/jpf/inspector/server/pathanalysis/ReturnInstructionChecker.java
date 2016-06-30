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
 * Checks whether an instruction is a return instruction (i.e. it removes frames from the call stack).
 */
class ReturnInstructionChecker extends JVMInstructionVisitorAdapter {
  private boolean isReturnInstruction = false;

  /**
   * Gets a value that indicates whether a step is a return step (i.e. it removes frames from the call stack).
   * @param step The step to check.
   */
  public boolean isReturnStep(Step step) {
    assert(step != null);
    return isReturnInstruction(step.getInstruction());
  }

  private boolean isReturnInstruction(Instruction inst) {
    assert(inst != null);
    isReturnInstruction = false;
    assert (inst instanceof JVMInstruction);
    ((JVMInstruction)inst).accept(this);
    return isReturnInstruction;
  }
  
  @Override
  public void visit(JVMReturnInstruction ins) {
    isReturnInstruction = true;
  }

  @Override
  public void visit(ARETURN ins) {
    isReturnInstruction = true;
  }

  @Override
  public void visit(DIRECTCALLRETURN ins) {
    isReturnInstruction = true;
  } 
    
  @Override
  public void visit(DRETURN ins) {
    isReturnInstruction = true;
  }

  @Override
  public void visit(FRETURN ins) {
    isReturnInstruction = true;
  }
  
  @Override
  public void visit(IRETURN ins) {
    isReturnInstruction = true;
  }
  
  @Override
  public void visit(LRETURN ins) {
    isReturnInstruction = true;
  }
  
  @Override
  public void visit(NATIVERETURN ins) {
    isReturnInstruction = true;
  }

  @Override
  public void visit(RETURN ins) {
    isReturnInstruction = true;
  }

  //DIRECTCALLRETURN
  // Synthetic instruction - there is no related Call instruction - user typically manually creates suitable (Direct)CallStackFrame so that postpone execution of current instruction  

  //RET - paired with JPR (used only for (finally) -> is not considered to be a method call
  
}