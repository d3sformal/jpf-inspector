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

package gov.nasa.jpf.inspector.utils.expressions;

import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.MethodInfo;

/**
 * Represents a qualified method name extracted from user input by the server expression parser.
 */
public class MethodName {

  private String methodName;
  private ClassName className;
  
  public MethodName(String methodName, ClassName className) {
    assert methodName != null;
    assert className != null;

    this.methodName = methodName;
    this.className = className;
  }
  
  public final String getMethodName() {
    return methodName;
  }
  
  public final String getClassName() {
    return className.getClassName();
  }
  
  public final ClassName getClassNameClass() {
    return className;
  }

  /**
   * Returns true if this expression represents the specified method. This is done by comparing method simple names
   * (as strings) and comparing containing classes (see {@link ClassName#isSameClass(ClassInfo)}).
   *
   * @param mi A method.
   */
  public boolean isSameMethod(MethodInfo mi) {
    assert mi != null;
    return methodName.equals(mi.getName()) && className.isSameClass(mi.getClassInfo());
  }
  
}
