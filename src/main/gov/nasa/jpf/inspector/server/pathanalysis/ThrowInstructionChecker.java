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

