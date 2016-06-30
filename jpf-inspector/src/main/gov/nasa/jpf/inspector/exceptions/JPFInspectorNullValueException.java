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

package gov.nasa.jpf.inspector.exceptions;

import gov.nasa.jpf.inspector.server.programstate.StateWritableValue;
import gov.nasa.jpf.vm.ClassInfo;

/**
 * @author Alf
 * 
 */
public class JPFInspectorNullValueException extends JPFInspectorException {

  private static final long serialVersionUID = -2461536983888931586L;

  public JPFInspectorNullValueException (String varName, ClassInfo ci) {
    super("Dereferencing null value: Trying to access the field \"" + varName + "\" of class \"" + StateWritableValue.getSimpleName(ci) + "\" but the instance is null.");
  }

  // Dereferencing null array
  public JPFInspectorNullValueException (int arrayElementIndex, ClassInfo ci) {
    super("Dereferencing null value: Trying to access an array of type " + StateWritableValue.getSimpleName(ci) + " (at index " + arrayElementIndex + ") but the array is null.");
  }

  // While assigning value - Dereferencing null during unboxing of the value.
  public JPFInspectorNullValueException (ClassInfo ci) {
    super("Dereferencing null value: The new value is null, but the null value cannot be assigned to a primitive type. Unboxing failed.");
    // super("Dereferencing null pointer when unboxin value of the " + StateWritableValue.getSimpleName(ci) + " type.");
  }

}
