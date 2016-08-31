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

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNotLValueException;
import gov.nasa.jpf.inspector.server.expression.ExpressionStateRootNode;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.programstate.StateNodeInterface;
import gov.nasa.jpf.inspector.server.programstate.StateWritableValue;

/**
 * Represents the "lvalue = rvalue" expression that is used as the argument to the command "set".
 * Maintains the <i>lvalue</i> and the <i>rvalue</i> as hierarchy-1 expressions.
 */
public class ExpressionStateAttributeAssignment {

  private final ExpressionStateRootNode assignToHere;
  private final String newAttributeValue;

  public ExpressionStateAttributeAssignment(ExpressionStateRootNode assignToHere, String newAttributeValue) {
    this.assignToHere = assignToHere;
    this.newAttributeValue = newAttributeValue;
  }

  public StateWritableValue getTargetObject(JPFInspector inspector, InspectorState state) throws JPFInspectorException {
      StateNodeInterface sni = assignToHere.getResultExpression(inspector, state);
      assert (sni != null);
      if (!(sni instanceof StateWritableValue)) {
        throw new JPFInspectorNotLValueException(sni.getStateExpr());
      }
      return (StateWritableValue) sni;

  }

  public String getNewAttributeValue() {
    return newAttributeValue;
  }
}
