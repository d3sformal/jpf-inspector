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

/**
 * Represents a node in the expression abstract syntax tree.
 *
 * There are two such kinds of nodes:
 *
 * - Program state expressions
 * - Hit conditions
 *
 * There is a third kind, the assignment expression for the "set" command but that does not need to implement
 * this interface.
 */
public interface ExpressionNodeInterface {
  /**
   * Gets the string which represents the normalized version of the represented expression.
   *
   * This is the expression text that is displayed in the output of the "show breakpoints" command, for example.
   *
   * - Note: Long versions of all keywords should be used.
   * - Note: The returned string has to be parsable by the expression parser.
   * 
   * @return Normalized parsable version of the represented expression.
   */
  String getNormalizedExpression();

}
