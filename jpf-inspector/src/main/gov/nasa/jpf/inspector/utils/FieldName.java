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

import gov.nasa.jpf.jvm.FieldInfo;

//Represents field name of a class in the expression
public class FieldName {

  private final String fieldName;
  private final ClassName className;
  
  public FieldName(String fieldName, ClassName className) {
    assert fieldName != null;
    assert className != null;

    this.fieldName = fieldName;
    this.className = className;
  }
  
  final public String getFieldName() {
    return fieldName;
  }
  
  final public String getClassName() {
    return className.getClassName();
  }
  
  final public ClassName getClassNameClass() {
    return className;
  }
  
  public boolean isSameField(FieldInfo fi) {
    assert fi != null;
    return fieldName.equals(fi.getName()) && className.isSameClass(fi.getClassInfo());
  }
  
  @Override
  public String toString() {
    return className.getClassName() + ':' + fieldName;
  }
  
}
