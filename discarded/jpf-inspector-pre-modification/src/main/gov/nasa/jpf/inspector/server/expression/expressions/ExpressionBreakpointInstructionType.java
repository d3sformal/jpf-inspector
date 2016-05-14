//
// Copyright (C) 2010 United States Government as represented by the
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

package gov.nasa.jpf.inspector.server.expression.expressions;

import gov.nasa.jpf.inspector.interfaces.InstructionTypes;
import gov.nasa.jpf.inspector.server.breakpoints.BreakPointModes;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.expression.InspectorState.ListenerMethod;
import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.bytecode.ArrayInstruction;
import gov.nasa.jpf.jvm.bytecode.FieldInstruction;
import gov.nasa.jpf.jvm.bytecode.GETFIELD;
import gov.nasa.jpf.jvm.bytecode.GETSTATIC;
import gov.nasa.jpf.jvm.bytecode.IfInstruction;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.jvm.bytecode.InvokeInstruction;
import gov.nasa.jpf.jvm.bytecode.LockInstruction;
import gov.nasa.jpf.jvm.bytecode.PUTFIELD;
import gov.nasa.jpf.jvm.bytecode.PUTSTATIC;
import gov.nasa.jpf.jvm.bytecode.ReturnInstruction;

public class ExpressionBreakpointInstructionType extends ExpressionBooleanLeaf {

  final public InstructionTypes instType;

  public ExpressionBreakpointInstructionType (InstructionTypes instType) {
    assert instType != null;
    this.instType = instType;
  }

  @Override
  public boolean evaluateExpression (InspectorState state) {
    assert state != null;
    if (state.getListenerMethod() != ListenerMethod.LM_INSTRUCTION_EXECUTED) {
      return false;
    }
    JVM vm = state.getJVM();
    Instruction inst = vm.getLastInstruction();

    if (inst == null) {
      return false;
    } else if (instType == InstructionTypes.IT_ANY) {
      return true;
    } else if (instType == InstructionTypes.IT_ARRAY) {
      return (inst instanceof ArrayInstruction);
    } else if (instType == InstructionTypes.IT_FIELD_ACCESS) {
      return (inst instanceof FieldInstruction);
    } else if (instType == InstructionTypes.IT_FIELD_READ) {
      return (inst instanceof GETFIELD) || (inst instanceof GETSTATIC);
    } else if (instType == InstructionTypes.IT_FIELD_WRITE) {
      return (inst instanceof PUTFIELD) || (inst instanceof PUTSTATIC);
    } else if (instType == InstructionTypes.IT_IFCOND) {
      return (inst instanceof IfInstruction);
    } else if (instType == InstructionTypes.IT_INVOKE) {
      return (inst instanceof InvokeInstruction);
    } else if (instType == InstructionTypes.IT_LOCK) {
      return (inst instanceof LockInstruction);
    } else if (instType == InstructionTypes.IT_RETURN) {
      return (inst instanceof ReturnInstruction);
    } else {
      throw new RuntimeException("Internal error - Unknown enum " + InstructionTypes.class.getSimpleName() + " entry " + instType);
    }
  }

  @Override
  public BreakPointModes getBPMode () {
    return BreakPointModes.BP_MODE_SPECIFIC_INSTRUCTION_TYPE;
  }

  public InstructionTypes getInstructionType () {
    return instType;
  }

  @Override
  public String getDetails (InspectorState state) {
    if (state != null && evaluateExpression(state)) {
      Instruction instr = state.getJVM().getLastInstruction();
      return "SuT (Thread=" + state.getJVM().getCurrentThread().getId() + ") executes the " + instr.getMethodInfo().getSourceFileName() + ":"
          + instr.getLineNumber() + " - " + instr.toString();
    }
    return "";
  }

  @Override
  public String getNormalizedExpression () {
    StringBuffer sb = new StringBuffer(30);
    sb.append("instruction_type=");
    if (instType == InstructionTypes.IT_ANY) {
      sb.append("any");
    } else if (instType == InstructionTypes.IT_NONE) {
      sb.append("none");
    } else if (instType == InstructionTypes.IT_INVOKE) {
      sb.append("invoke");
    } else if (instType == InstructionTypes.IT_RETURN) {
      sb.append("return");
    } else if (instType == InstructionTypes.IT_FIELD_ACCESS) {
      sb.append("field_access");
    } else if (instType == InstructionTypes.IT_FIELD_READ) {
      sb.append("field_read");
    } else if (instType == InstructionTypes.IT_FIELD_WRITE) {
      sb.append("field_write");
    } else if (instType == InstructionTypes.IT_IFCOND) {
      sb.append("condition");
    } else if (instType == InstructionTypes.IT_LOCK) {
      sb.append("lock");
    } else if (instType == InstructionTypes.IT_ARRAY) {
      sb.append("array");
    } else {
      throw new RuntimeException("Internal error - Unsupported " + instType.getClass().getSimpleName() + "(" + instType + ")");
    }
    return sb.toString();
  }

}
