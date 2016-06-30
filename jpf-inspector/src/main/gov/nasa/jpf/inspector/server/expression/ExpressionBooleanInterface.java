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

package gov.nasa.jpf.inspector.server.expression;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.server.breakpoints.BreakPointModes;

/**
 * Represents a node in the expressions syntax tree.
 * Boolean expressions are used as breakpoint hit conditions.
 */
public interface ExpressionBooleanInterface extends ExpressionNodeInterface {

  /**
   * Evaluates this expression.
   * 
   * Successors - logical operators, compare (any type)
   */
  boolean evaluateExpression(InspectorState state) throws JPFInspectorException;

  /**
   * Returns the breakpoint mode of the expression, if any.
   * Breakpoint modes are used by some kinds of hit conditions internally.
   */
  BreakPointModes getBPMode();

  /**
   * Gets details related to the expression.
   * Note: This information is printed to user if breakpoint hits.
   * This method can provide supplementary information for users.
   * 
   * @return Details related to evaluation of the expresison or null to print no extra information.
   */
  String getDetails(InspectorState state);

}
