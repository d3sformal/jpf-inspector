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
import gov.nasa.jpf.inspector.server.programstate.relop.RelationOperator;
import gov.nasa.jpf.vm.ClassInfo;

/**
 * @author Alf
 * 
 */
public class JPFInspectorIncompatibleTypesException extends JPFInspectorException {

  private static final long serialVersionUID = -6306195283207523411L;

  // Incompatible types when assigning
  public JPFInspectorIncompatibleTypesException (ClassInfo ci, ClassInfo newValCI) {
    super("Cannot assign new value. A value of type \"" + StateWritableValue.demangleTypeName(newValCI.getSignature()) + "\" cannot be assigned to a variable of type \""
        + StateWritableValue.demangleTypeName(ci.getSignature()) + "\".");
  }

  // Incompatible type when comparing
  public JPFInspectorIncompatibleTypesException (RelationOperator relOper, ClassInfo ci, ClassInfo newValCI) {
    super("Cannot compare values of given types - incompatible types. \"" + StateWritableValue.demangleTypeName(ci.getSignature()) + "\" " + relOper.getNormalizedText()
        + " \"" + StateWritableValue.demangleTypeName(newValCI.getSignature()) + "\"");
  }

}
