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

import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorParsingErrorException;
import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionStateAssignment;
import gov.nasa.jpf.inspector.utils.expressions.FieldName;

/**
 * A class implementing this interface encapsulates the ANTLR parser for expressions.
 */
public interface ExpressionParserInterface {
  /**
   * Parses the given expression as a program state expression.
   * @param expr Expression to parse.
   * @return Tree representation of the given expression.
   */
  ExpressionStateRootNode getExpressionStateInterface(String expr) throws
          JPFInspectorParsingErrorException,
          JPFInspectorGenericErrorException;

  FieldName getFieldName(String expr) throws JPFInspectorParsingErrorException, JPFInspectorGenericErrorException;

  /**
   * Parses the given expression as a breakpoint hit condition.
   * 
   * @param expr Expression to parse.
   * @return Tree representation of the given expression.
   */
  ExpressionBooleanInterface getBreakpointExpression(String expr) throws
          JPFInspectorParsingErrorException,
          JPFInspectorGenericErrorException;

  /**
   * Parses the given expression as an assignment for the "set" command.
   *
   * @param expr Expression to parse.
   * @return Tree representation of the given expression.
   */
  ExpressionStateAssignment getExpressionAssignment(String expr) throws
          JPFInspectorParsingErrorException,
          JPFInspectorGenericErrorException;

  /**
   * Gets an expression factory used for parsing.
   */
  ExpressionFactory getExpressionFactory();

}
