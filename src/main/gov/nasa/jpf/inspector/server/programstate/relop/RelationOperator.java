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

package gov.nasa.jpf.inspector.server.programstate.relop;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorIncompatibleTypesException;
import gov.nasa.jpf.inspector.server.programstate.StateReadableValue;

/**
 * An operator, such as "==" or "!=".
 *
 * This is the public interface in this package. To get it, use the {@link RelationOperatorFactory} class.
 */
public interface RelationOperator {

  /**
   * Compares the two operands and returns the result.
   *
   * There was this note in the documentation which I do not understand:
   *  "Does not comparing "<=" on references or reference with double."
   * 
   * @param left The left operand.
   * @param right The right operand.
   * @return True if relation holds, false otherwise.
   * @throws JPFInspectorIncompatibleTypesException If the two operands cannot be compared.
   */
  boolean compare(
          StateReadableValue left,
          StateReadableValue right)
          throws JPFInspectorIncompatibleTypesException;

  /**
   * @return Gets representation of the operator in the input expression.
   */
  String getNormalizedText();
}
