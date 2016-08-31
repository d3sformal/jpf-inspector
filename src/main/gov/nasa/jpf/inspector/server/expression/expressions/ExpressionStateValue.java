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
import gov.nasa.jpf.inspector.server.expression.ExpressionStateUnaryOperator;
import gov.nasa.jpf.inspector.server.programstate.StateReadableValue;

/**
 * In the expression tree hierarchy, this is a node that has a value, such as an array element or a field,
 * but not a thread or a stack frame.
 */
public abstract class ExpressionStateValue extends ExpressionStateUnaryOperator<ExpressionStateValue> {

  /**
   * Initializes a new instance of this class.
   *
   * @param child A member access expression attached to this expression. May be null to signify that this is the final expression in the acces chain.
   */
  protected ExpressionStateValue (ExpressionStateValue child) {
    super(child);
  }

  /**
   * Returns the hierarchy-2 expression equivalent of this expression.
   *
   * @param parent The expression that this expression is a member of.  Must not be null.
   * @return The hierarchy-2 expression.
   */
  public abstract StateReadableValue toHierarchy2(StateReadableValue parent)
          throws JPFInspectorException;
}
