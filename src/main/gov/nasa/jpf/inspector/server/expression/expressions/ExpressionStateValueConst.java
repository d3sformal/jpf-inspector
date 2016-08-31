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
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.server.expression.ExpressionStateRootNode;
import gov.nasa.jpf.inspector.server.programstate.StateReadableValue;

/**
 * Base class for all constant expressions of the first hierarchy.
 */
public abstract class ExpressionStateValueConst extends ExpressionStateValue implements ExpressionStateRootNode {

  // Const values do not have any members.
  protected ExpressionStateValueConst () {
    super(null);
  }

  @Override
  public final StateReadableValue toHierarchy2(StateReadableValue parent) throws JPFInspectorException {
    assert false;
    throw new JPFInspectorGenericErrorException(
            "The toHierarchy2() method must not be called on constants because they have no parents.");
  }

}
