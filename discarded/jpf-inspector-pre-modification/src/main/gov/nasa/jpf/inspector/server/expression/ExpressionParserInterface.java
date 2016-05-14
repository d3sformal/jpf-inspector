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

import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorParsingErrorException;
import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionStateAssignment;

/**
 */
public interface ExpressionParserInterface {
  /**
   * @param expr Expression to parse
   */
  public ExpressionStateRootNode<?> getExpressionStateInterface (String expr) throws JPFInspectorParsingErrorException, JPFInspectorGenericErrorException;

  /**
   * Gets boolean expression which evaluates whether the breakpoint hit or not
   * 
   * @param expr Expression to parse
   * @return Tree representation of the given expression of null, if the expr contains (syntax) error.
   */
  public ExpressionBooleanInterface getBreakpointExpression (String expr) throws JPFInspectorParsingErrorException, JPFInspectorGenericErrorException;

  /**
   * 
   * @param expr Expression to parse
   * @return Gets parsed expression of the value assignment
   */
  public ExpressionStateAssignment getExpressionAssignment (String expr) throws JPFInspectorParsingErrorException, JPFInspectorGenericErrorException;

  /**
   * @return Gets expression factory used by parser
   */
  public ExpressionFactory getExpressionFactory ();

}
