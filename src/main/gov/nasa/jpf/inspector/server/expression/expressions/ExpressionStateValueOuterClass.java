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
import gov.nasa.jpf.inspector.server.programstate.StateReadableValue;
import gov.nasa.jpf.inspector.server.programstate.StateValueElementInfoField;

/**
 * @author Alf
 * 
 */
public class ExpressionStateValueOuterClass extends ExpressionStateValue {

  private static final String TOKEN_HASH_OUTER_CLASS = "#outerClass";

  public ExpressionStateValueOuterClass (ExpressionStateValue child) {
    super(child);
  }

  @Override
  public StateReadableValue toHierarchy2(StateReadableValue parent) throws JPFInspectorException {

    StateReadableValue srvi = StateValueElementInfoField.createOuterClass(parent);

    ExpressionStateValue child = getChild();
    if (child == null) {
      return srvi;
    } else {
      return child.toHierarchy2(srvi);
    }
  }

  @Override
  public String getNormalizedExpression () {
    return '.' + TOKEN_HASH_OUTER_CLASS + (getChild() != null ? getChild().getNormalizedExpression() : "");
  }

}
