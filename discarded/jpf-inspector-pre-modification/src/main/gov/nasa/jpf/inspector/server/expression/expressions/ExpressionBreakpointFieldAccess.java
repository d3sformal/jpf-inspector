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

import gov.nasa.jpf.inspector.server.breakpoints.BreakPointModes;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.expression.InspectorState.ListenerMethod;
import gov.nasa.jpf.inspector.utils.FieldName;
import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.bytecode.FieldInstruction;
import gov.nasa.jpf.jvm.bytecode.GETFIELD;
import gov.nasa.jpf.jvm.bytecode.GETSTATIC;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.jvm.bytecode.PUTFIELD;
import gov.nasa.jpf.jvm.bytecode.PUTSTATIC;

public class ExpressionBreakpointFieldAccess extends ExpressionBooleanLeaf {

  private BreakPointModes bpMode;
  private FieldName fn;

  public ExpressionBreakpointFieldAccess(BreakPointModes bpMode, FieldName fn) {
    if ((bpMode == BreakPointModes.BP_MODE_FIELD_ACCESS) ||
        (bpMode == BreakPointModes.BP_MODE_FIELD_ACCESS_READ) ||
        (bpMode == BreakPointModes.BP_MODE_FIELD_ACCESS_WRITE)) {
      this.bpMode = bpMode;
    } else {
      throw new RuntimeException("Internal error - Unsupported bpMode(" + bpMode + ")");
    }
    if (fn != null) {
      this.fn = fn;
    } else {
      throw new RuntimeException("Internal error - Unexpected null value");
    }
  }

  public FieldName getFieldName() {
    return fn;
  }

  @Override
  public boolean evaluateExpression(InspectorState state) {
    assert state != null;

    if (state.getListenerMethod() != ListenerMethod.LM_INSTRUCTION_EXECUTED) {
      return false;
    }

    JVM vm = state.getJVM();
    Instruction inst = vm.getLastInstruction();

    if (!(inst instanceof FieldInstruction)) {
      return false;
    }

    FieldInstruction fiInst = (FieldInstruction) inst;
    if (bpMode == BreakPointModes.BP_MODE_FIELD_ACCESS) {
      return fn.isSameField(fiInst.getFieldInfo());
    } else if (bpMode == BreakPointModes.BP_MODE_FIELD_ACCESS_READ) {
      return (fiInst instanceof GETFIELD) || (fiInst instanceof GETSTATIC) && fn.isSameField(fiInst.getFieldInfo());
    } else if (bpMode == BreakPointModes.BP_MODE_FIELD_ACCESS_WRITE) {
      return (fiInst instanceof PUTFIELD) || (fiInst instanceof PUTSTATIC) && fn.isSameField(fiInst.getFieldInfo());
    }

    return false;
  }

  @Override
  public BreakPointModes getBPMode() {
    return bpMode;
  }

  @Override
  public String getNormalizedExpression() {
    StringBuffer result = new StringBuffer();
    if (bpMode == BreakPointModes.BP_MODE_FIELD_ACCESS) {
      result.append("field_access");
    } else if (bpMode == BreakPointModes.BP_MODE_FIELD_ACCESS_READ) {
      result.append("field_read");
    } else if (bpMode == BreakPointModes.BP_MODE_FIELD_ACCESS_WRITE) {
      result.append("field_write");
    } else {
      throw new RuntimeException("Internal error - Unsupported bpMode(" + bpMode + ")");
    }
    result.append('=');
    result.append(fn);

    return result.toString();
  }

}
