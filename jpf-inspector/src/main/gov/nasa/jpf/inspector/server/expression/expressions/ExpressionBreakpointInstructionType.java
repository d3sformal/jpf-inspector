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

package gov.nasa.jpf.inspector.server.expression.expressions;

import gov.nasa.jpf.inspector.interfaces.InstructionType;
import gov.nasa.jpf.inspector.server.breakpoints.BreakPointModes;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.expression.InspectorState.ListenerMethod;
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.bytecode.*;
import gov.nasa.jpf.jvm.bytecode.GETFIELD;
import gov.nasa.jpf.jvm.bytecode.GETSTATIC;
import gov.nasa.jpf.jvm.bytecode.IfInstruction;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.jvm.bytecode.LockInstruction;
import gov.nasa.jpf.jvm.bytecode.PUTFIELD;
import gov.nasa.jpf.jvm.bytecode.PUTSTATIC;

/**
 * Represents the "instruction_type" hit condition. This hit condition is also used internally for creating breakpoints
 * for single-stepping.
 */
public class ExpressionBreakpointInstructionType extends ExpressionBooleanLeaf {

  private final InstructionType instType;

  public ExpressionBreakpointInstructionType (InstructionType instType) {
    assert instType != null;
    this.instType = instType;
  }

  @Override
  public boolean evaluateExpression (InspectorState state) {
    assert state != null;
    if (state.getListenerMethod() != ListenerMethod.LM_EXECUTE_INSTRUCTION) {
      return false;
    }
    VM vm = state.getVM();
    Instruction inst = vm.getInstruction();

    if (inst == null) {
      return false;
    } else if (instType == InstructionType.ANY) {
      return true;
    } else if (instType == InstructionType.ARRAY) {
      return (inst instanceof ArrayElementInstruction);
    } else if (instType == InstructionType.FIELD_ACCESS) {
      return (inst instanceof FieldInstruction);
    } else if (instType == InstructionType.FIELD_READ) {
      return (inst instanceof GETFIELD) || (inst instanceof GETSTATIC);
    } else if (instType == InstructionType.FIELD_WRITE) {
      return (inst instanceof PUTFIELD) || (inst instanceof PUTSTATIC);
    } else if (instType == InstructionType.IFCOND) {
      return (inst instanceof IfInstruction);
    } else if (instType == InstructionType.INVOKE) {
      return (inst instanceof InvokeInstruction);
    } else if (instType == InstructionType.LOCK) {
      return (inst instanceof LockInstruction);
    } else if (instType == InstructionType.RETURN) {
      return (inst instanceof ReturnInstruction);
    } else if (instType == InstructionType.LOCAL_ACCESS) {
      return (inst instanceof gov.nasa.jpf.jvm.bytecode.JVMLocalVariableInstruction);
    } else if (instType == InstructionType.LOCAL_READ) {
      return (inst instanceof gov.nasa.jpf.jvm.bytecode.JVMLocalVariableInstruction) &&
              !(inst instanceof StoreInstruction);
    } else if (instType == InstructionType.LOCAL_WRITE) {
      return (inst instanceof gov.nasa.jpf.jvm.bytecode.JVMLocalVariableInstruction) &&
              (inst instanceof StoreInstruction);
    } else {
      throw new RuntimeException("Internal error - Unknown enum " + InstructionType.class.getSimpleName() + " entry " + instType);
    }
  }

  @Override
  public BreakPointModes getBPMode () {
    return BreakPointModes.BP_MODE_SPECIFIC_INSTRUCTION_TYPE;
  }

  @Override
  public String getDetails (InspectorState state) {
    if (state != null && evaluateExpression(state)) {
      Instruction instr = state.getVM().getInstruction();
      return "SuT (Thread=" + state.getVM().getCurrentThread().getId() + ") executes the " + instr.getMethodInfo().getSourceFileName() + ":"
          + instr.getLineNumber() + " - " + instr.toString();
    }
    return "";
  }

  @Override
  public String getNormalizedExpression () {
    StringBuilder sb = new StringBuilder(30);
    sb.append("instruction_type=");
    if (instType == InstructionType.ANY) {
      sb.append("any");
    } else if (instType == InstructionType.NONE) {
      sb.append("none");
    } else if (instType == InstructionType.INVOKE) {
      sb.append("invoke");
    } else if (instType == InstructionType.RETURN) {
      sb.append("return");
    } else if (instType == InstructionType.FIELD_ACCESS) {
      sb.append("field_access");
    } else if (instType == InstructionType.FIELD_READ) {
      sb.append("field_read");
    } else if (instType == InstructionType.FIELD_WRITE) {
      sb.append("field_write");
    } else if (instType == InstructionType.IFCOND) {
      sb.append("condition");
    } else if (instType == InstructionType.LOCK) {
      sb.append("lock");
    } else if (instType == InstructionType.ARRAY) {
      sb.append("array");
    } else if (instType == InstructionType.LOCAL_ACCESS) {
      sb.append("local_access");
    } else if (instType == InstructionType.LOCAL_WRITE) {
      sb.append("local_write");
    }  else if (instType == InstructionType.LOCAL_READ) {
      sb.append("local_read");
    }  else {
      throw new RuntimeException("Internal error - Unsupported " + instType.getClass().getSimpleName() + "(" + instType + ")");
    }
    return sb.toString();
  }

}
