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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a boolean expression with two operands. Used as a hit condition.
 */
public abstract class ExpressionBooleanBinaryOperator extends ExpressionBoolean {

  protected List<ExpressionBooleanInterface> children = new ArrayList<>(2);

  public ExpressionBooleanBinaryOperator (ExpressionBooleanInterface left, ExpressionBooleanInterface right) {
    assert left != null;
    assert right != null;

    children.add(left);
    children.add(right);
  }

  @Override
  public abstract boolean evaluateExpression (InspectorState state) throws JPFInspectorException;

  @Override
  public BreakPointModes getBPMode () {
    return BreakPointModes.BP_MODE_USER_CONDITION;
  }

}
