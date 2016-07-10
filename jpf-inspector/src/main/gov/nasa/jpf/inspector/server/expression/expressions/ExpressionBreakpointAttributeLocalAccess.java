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
import gov.nasa.jpf.inspector.server.attributes.AttributesManager;
import gov.nasa.jpf.inspector.server.expression.AccessMode;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.vm.Instruction;

/**
 * Represent the family of local variable access hit conditions that hit when the local variable is about to be referenced.
 * This family contains the hit conditions "local_access", "local_read" and "local_write".
 */
public class ExpressionBreakpointAttributeLocalAccess extends ExpressionBooleanLeaf {
  private final AccessMode accessMode;
  private final String localName;
  private JPFInspector inspector;

  public ExpressionBreakpointAttributeLocalAccess(JPFInspector inspector, AccessMode accessMode, String localName) {
    this.inspector = inspector;
    assert accessMode != null;
    assert localName != null;
    this.accessMode = accessMode;
    this.localName = localName;
  }

  @Override
  public boolean evaluateExpression(InspectorState state) throws JPFInspectorException {
    assert state != null;

    if (state.getListenerMethod() != InspectorState.ListenerMethod.LM_EXECUTE_INSTRUCTION) {
      return false;
    }

    AttributesManager manager = inspector.getAttributesManager();
    Instruction impendingInstruction = state.getVM().getInstruction();

    switch (accessMode) {
      case ANY_ACCESS:
        return manager.detectRead(impendingInstruction, localName) ||
               manager.detectWrite(impendingInstruction, localName);
      case READ:
        return manager.detectRead(impendingInstruction, localName);
      case WRITE:
        return manager.detectWrite(impendingInstruction, localName);
    }
    throw new RuntimeException("Internal error - Unsupported access mode (" + accessMode + ").");
  }

  @Override
  public String getNormalizedExpression() {
    StringBuilder result = new StringBuilder();
    if (accessMode == AccessMode.ANY_ACCESS) {
      result.append("attr_access");
    } else if (accessMode == AccessMode.READ) {
      result.append("attr_read");
    } else if (accessMode == AccessMode.WRITE) {
      result.append("attr_write");
    } else {
      throw new RuntimeException("Internal error - Unsupported access mode (" + accessMode + ")");
    }
    result.append('=');
    result.append(localName);

    return result.toString();
  }

}
