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
import gov.nasa.jpf.inspector.server.programstate.StateValueArrayElement;

/**
 * @author Alf
 * 
 */
public class ExpressionStateValueArrayIndex extends ExpressionStateValue {

  private final int elementIndex;

  public ExpressionStateValueArrayIndex (ExpressionStateValue child, int elementIndex) {
    super(child);

    this.elementIndex = elementIndex;
  }

  @Override
  public StateReadableValue toHierarchy2(StateReadableValue parent) throws JPFInspectorException {

    StateValueArrayElement svae = StateValueArrayElement.createArrayElement(parent, elementIndex);

    ExpressionStateValue child = getChild();
    if (child == null) {
      return svae;
    } else {
      return child.toHierarchy2(svae);
    }
  }

  @Override
  public String getNormalizedExpression () {
    return '[' + elementIndex + ']' + (getChild() != null ? getChild().getNormalizedExpression() : "");
  }

}
