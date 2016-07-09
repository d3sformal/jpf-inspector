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

import gov.nasa.jpf.inspector.server.expression.AccessMode;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.expression.InspectorState.ListenerMethod;
import gov.nasa.jpf.inspector.utils.expressions.FieldName;
import gov.nasa.jpf.jvm.bytecode.GETFIELD;
import gov.nasa.jpf.jvm.bytecode.GETSTATIC;
import gov.nasa.jpf.jvm.bytecode.PUTFIELD;
import gov.nasa.jpf.jvm.bytecode.PUTSTATIC;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.bytecode.FieldInstruction;

/**
 * Represent the family of field access hit conditions that hit when the field is about to be referenced.
 * This family contains the hit conditions "field_access", "field_read" and "field_write".
 */
public class ExpressionBreakpointFieldAccess extends ExpressionBooleanLeaf {

  private AccessMode accessMode;
  private FieldName fn;

  public ExpressionBreakpointFieldAccess(AccessMode accessMode, FieldName fn) {
    assert fn != null;
    this.fn = fn;
    this.accessMode = accessMode;
  }

  @Override
  public boolean evaluateExpression(InspectorState state) {
    assert state != null;

    if (state.getListenerMethod() != ListenerMethod.LM_EXECUTE_INSTRUCTION) {
      return false;
    }

    VM vm = state.getVM();
    Instruction inst = vm.getInstruction();

    if (!(inst instanceof FieldInstruction)) {
      return false;
    }

    FieldInstruction fiInst = (FieldInstruction) inst;
    if (accessMode == AccessMode.ANY_ACCESS) {
      return fn.isSameField(fiInst.getFieldInfo());
    } else if (accessMode == AccessMode.READ) {
      return (fiInst instanceof GETFIELD) || (fiInst instanceof GETSTATIC) && fn.isSameField(fiInst.getFieldInfo());
    } else if (accessMode == AccessMode.WRITE) {
      return (fiInst instanceof PUTFIELD) || (fiInst instanceof PUTSTATIC) && fn.isSameField(fiInst.getFieldInfo());
    }

    return false;
  }

  @Override
  public String getNormalizedExpression() {
    StringBuilder result = new StringBuilder();
    if (accessMode == AccessMode.ANY_ACCESS) {
      result.append("field_access");
    } else if (accessMode == AccessMode.READ) {
      result.append("field_read");
    } else if (accessMode == AccessMode.WRITE) {
      result.append("field_write");
    }

    result.append('=');
    result.append(fn);

    return result.toString();
  }

}
