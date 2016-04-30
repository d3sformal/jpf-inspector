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

package gov.nasa.jpf.inspector.interfaces.exceptions;

import gov.nasa.jpf.inspector.interfaces.JPFInspectorException;
import gov.nasa.jpf.inspector.server.programstate.StateValue;
import gov.nasa.jpf.inspector.server.programstate.relop.RelationOperator;
import gov.nasa.jpf.jvm.ClassInfo;

/**
 * @author Alf
 * 
 */
public class JPFInspectorIncompatibleTypesException extends JPFInspectorException {

  private static final long serialVersionUID = -6306195283207523411L;

  // Incompatible types when assigning
  public JPFInspectorIncompatibleTypesException (ClassInfo ci, ClassInfo newValCI) {
    super("Cannot assign new value. The value of the \"" + StateValue.demangleTypeName(ci.getSignature()) + "\" type cannot be assigned to the value of the \""
        + StateValue.demangleTypeName(newValCI.getSignature()) + "\" type.");
  }

  // Incompatible type when comparing
  public JPFInspectorIncompatibleTypesException (RelationOperator relOper, ClassInfo ci, ClassInfo newValCI) {
    super("Cannot compare value of given types - incompatible type. \"" + StateValue.demangleTypeName(ci.getSignature()) + "\" " + relOper.getNormalizedText()
        + " \"" + StateValue.demangleTypeName(newValCI.getSignature()) + "\"");
  }

}
