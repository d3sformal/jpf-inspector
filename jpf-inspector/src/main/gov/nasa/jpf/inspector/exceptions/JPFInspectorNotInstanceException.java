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

import gov.nasa.jpf.inspector.server.programstate.StateWritableValue;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.MethodInfo;

/**
 * @author Alf
 * 
 */
public class JPFInspectorNotInstanceException extends JPFInspectorException {

  private static final long serialVersionUID = -8178100800086257124L;

  public JPFInspectorNotInstanceException (MethodInfo mi) {
    super("The keywords '#this', '#outerClass', '#field' and '#super' cannot be used here because " + StateWritableValue
            .getSimpleMethodName(mi) + " is a static method. Try '.#static.#field' instead.");
  }

  public JPFInspectorNotInstanceException (ClassInfo ci) {
    super("The keywords '#this', '#outerClass', '#field' and '#super' cannot be used here because " + StateWritableValue
            .getSimpleName(ci) + " is a primitive type, an array or a static class.");
  }
}
