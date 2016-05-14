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
import gov.nasa.jpf.inspector.utils.MethodName;
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.bytecode.InvokeInstruction;

public class ExpressionBreakpointMethodInvoke extends ExpressionBooleanLeaf {

  private BreakPointModes bpMode;
  private MethodName mn;

  public ExpressionBreakpointMethodInvoke(BreakPointModes bpMode, MethodName mn) {
    if (bpMode == BreakPointModes.BP_MODE_METHOD_INVOKE) {
      this.bpMode = bpMode;
    } else {
      throw new RuntimeException("Internal error - Unsupported bpMode(" + bpMode + ")");
    }
    if (mn != null) {
      this.mn = mn;
    } else {
      throw new RuntimeException("Internal error - Unexpected null value");
    }
  }

  public MethodName getMethodName() {
    return mn;
  }

  @Override
  public boolean evaluateExpression(InspectorState state) {
    assert state != null;

    if (state.getListenerMethod() != ListenerMethod.LM_INSTRUCTION_EXECUTED) {
      return false;
    }

    VM vm = state.getJVM();
    Instruction inst = vm.getLastInstruction();

    if (!(inst instanceof InvokeInstruction)) {
      return false;
    }

    InvokeInstruction iiInst = (InvokeInstruction) inst;
    if (bpMode == BreakPointModes.BP_MODE_METHOD_INVOKE) {
      return mn.isSameMethod(iiInst.getInvokedMethod());
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
    result.append("method_invoke");
    result.append('=');
    result.append(mn.getClassName());
    result.append(':');
    result.append(mn.getMethodName());
    return result.toString();
  }

}
