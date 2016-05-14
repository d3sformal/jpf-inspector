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

package gov.nasa.jpf.inspector.utils;

import gov.nasa.jpf.jvm.MethodInfo;

//Represents method name of a class in the expression
public class MethodName {

  private String methodName;
  private ClassName className;
  
  public MethodName(String methodName, ClassName className) {
    assert methodName != null;
    assert className != null;

    this.methodName = methodName;
    this.className = className;
  }
  
  final public String getMethodName() {
    return methodName;
  }
  
  final public String getClassName() {
    return className.getClassName();
  }
  
  final public ClassName getClassNameClass() {
    return className;
  }
  
  public boolean isSameMethod(MethodInfo mi) {
    assert mi != null;
    return methodName.equals(mi.getName()) && className.isSameClass(mi.getClassInfo());
  }
  
}
