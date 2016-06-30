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

package gov.nasa.jpf.inspector.utils.expressions;

import gov.nasa.jpf.vm.FieldInfo;

/**
 * Represents the expression syntax expression for a qualified field name.
 */
public class FieldName {

  private final String fieldName;
  private final ClassName className;
  
  public FieldName(String fieldName, ClassName className) {
    assert fieldName != null;
    assert className != null;

    this.fieldName = fieldName;
    this.className = className;
  }
  
  public final String getFieldName() {
    return fieldName;
  }
  
  public final String getClassName() {
    return className.getClassName();
  }
  
  public final ClassName getClassNameClass() {
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
