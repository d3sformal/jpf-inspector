//
// Copyright (C) 2011 United States Government as represented by the
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

package gov.nasa.jpf.inspector.server.programstate.relop;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorIncompatibleTypesException;
import gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface;

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
          StateReadableValueInterface left,
          StateReadableValueInterface right)
          throws JPFInspectorIncompatibleTypesException;

  /**
   * @return Gets representation of the operator in the input expression.
   */
  String getNormalizedText();
}
