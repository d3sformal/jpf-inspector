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
