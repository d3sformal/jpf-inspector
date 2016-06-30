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
