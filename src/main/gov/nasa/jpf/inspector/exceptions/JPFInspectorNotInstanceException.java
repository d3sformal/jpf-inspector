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
