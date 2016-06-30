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
