//
// Copyright (C) 2010 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
// 
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
// 
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//  

package gov.nasa.jpf.inspector.server.expression;

import java.util.List;

/**
 * Represents a node in the expression abstract syntax tree.
 *
 * There are two such kinds of nodes:
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
   * Note: Long versions of all keywords should be used.
   * Note: The returned string has to be parsable by the expression parser.
   * 
   * @return Normalized parsable version of the represented expression.
   */
  String getNormalizedExpression();

}
