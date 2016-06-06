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

package gov.nasa.jpf.inspector.exceptions;

import gov.nasa.jpf.inspector.server.programstate.StateValue;
import gov.nasa.jpf.vm.ClassInfo;

/**
 * @author Alf
 * 
 */
public class JPFInspectorNullValueException extends JPFInspectorException {

  private static final long serialVersionUID = -2461536983888931586L;

  public JPFInspectorNullValueException (String varName, ClassInfo ci) {
    super("Dereferencing null value: Trying to access the field \"" + varName + "\" of class \"" + StateValue.getSimpleName(ci) + "\" but the instance is null.");
  }

  // Dereferencing null array
  public JPFInspectorNullValueException (int arrayElementIndex, ClassInfo ci) {
    super("Dereferencing null value: Trying to access an array of type " + StateValue.getSimpleName(ci) + " (at index " + arrayElementIndex + ") but the array is null.");
  }

  // While assigning value - Dereferencing null during unboxing of the value.
  public JPFInspectorNullValueException (ClassInfo ci) {
    super("Dereferencing null value: The new value is null, but the null value cannot be assigned to a primitive type. Unboxing failed.");
    // super("Dereferencing null pointer when unboxin value of the " + StateValue.getSimpleName(ci) + " type.");
  }

}
