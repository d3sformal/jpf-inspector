//
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

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.utils.expressions.MethodName;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.MethodInfo;

import java.util.Objects;

/**
 * Represents the "method = (methodname) and "method = (classname):(methodname)" hit conditions that hit before
 * each instruction in the given method.
 */
public class ExpressionBreakpointMethodName extends ExpressionBooleanLeaf {
  private final MethodName methodName;

  public ExpressionBreakpointMethodName(MethodName methodName) {
    this.methodName = methodName;
  }

  @Override
  public String getDetails(InspectorState state) {
    return "In method \"" + state.getVM().getInstruction().getMethodInfo().getName() + "\".";
  }

  @Override
  public boolean evaluateExpression(InspectorState state) throws JPFInspectorException {
    if (state.getListenerMethod() != InspectorState.ListenerMethod.LM_EXECUTE_INSTRUCTION) {
      return false;
    }
    Instruction instruction = state.getVM().getInstruction();
    MethodInfo methodInfo = instruction.getMethodInfo();
    if (methodInfo == null) {
      return false;
    }
    return methodName.isSameMethod(methodInfo);
  }

  @Override
  public String getNormalizedExpression() {
    return "method = " + (!Objects.equals(methodName.getClassName(), "*") ? methodName.getClassName() + ":" : "") + methodName.getMethodName();
  }
}
