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

import gov.nasa.jpf.inspector.server.breakpoints.BreakPointModes;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.expression.InspectorState.ListenerMethod;
import gov.nasa.jpf.inspector.utils.expressions.MethodName;
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.bytecode.InvokeInstruction;

/**
 * Represents the "method_invoke = [methodname]" hit condition that hits when a method is about to be invoked.
 */
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

  @Override
  public boolean evaluateExpression(InspectorState state) {
    assert state != null;

    if (state.getListenerMethod() != ListenerMethod.LM_EXECUTE_INSTRUCTION) {
      return false;
    }

    VM vm = state.getVM();
    Instruction inst = vm.getInstruction();

    if (!(inst instanceof InvokeInstruction)) {
      return false;
    }

    InvokeInstruction iiInst = (InvokeInstruction) inst;
    return mn.isSameMethod(iiInst.getInvokedMethod());

  }

  @Override
  public BreakPointModes getBPMode() {
    return bpMode;
  }

  @Override
  public String getNormalizedExpression() {
    String result = "method_invoke" +
            '=' +
            mn.getClassName() +
            ':' +
            mn.getMethodName();
    return result;
  }

}
