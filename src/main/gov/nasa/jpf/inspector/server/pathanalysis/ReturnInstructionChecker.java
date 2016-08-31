//
// Copyright (C) 2010-2011 Pavel Jančík
// Copyright (C) 2016 Petr Hudeček
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
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