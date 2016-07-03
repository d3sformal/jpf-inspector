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
import gov.nasa.jpf.inspector.utils.expressions.ClassName;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.MethodInfo;

/**
 * Represents the "class = [classname]" hit condition that hits before each instruction in the specified class.
 */
public class ExpressionBreakpointClassName extends ExpressionBooleanLeaf {
  private final ClassName className;

  public ExpressionBreakpointClassName(ClassName className) {
    this.className = className;
  }

  @Override
  public String getDetails(InspectorState state) {
    return "In class \"" + state.getVM().getInstruction().getMethodInfo().getClassName() + "\".";
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
    ClassInfo actualClassInfo = methodInfo.getClassInfo();
    if (actualClassInfo == null) {
      return false;
    }
    return className.isSameClass(actualClassInfo);
  }

  @Override
  public String getNormalizedExpression() {
    return "class = " + className.getClassName();
  }
}
