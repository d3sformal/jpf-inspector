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
 * @author Alf
 * 
 */
public interface RelationOperator {

  /**
   * Gets true if given left and right values are of the suitable(comparable type) type. Does not comparing "<=" on references or reference with double.
   * 
   * @param left left operand
   * @param right right operand
   * @return true if relation holds, false if types are incompatible of relation does not hold.
   */
  public boolean compare (StateReadableValueInterface left, StateReadableValueInterface right) throws JPFInspectorIncompatibleTypesException;

  /**
   * @return Gets representation of the operator in the input expression.
   */
  public String getNormalizedText ();
}
