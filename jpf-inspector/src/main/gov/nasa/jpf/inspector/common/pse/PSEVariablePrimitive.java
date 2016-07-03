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

package gov.nasa.jpf.inspector.common.pse;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;

import java.io.Serializable;

/**
 * Represents a Java primitive.
 */
public class PSEVariablePrimitive extends PSEVariable {

  private static final long serialVersionUID = 6680525792762309903L;

  public PSEVariablePrimitive(String varName,
                              String varTypeName, String varValue,
                              int index, Object wrappedValue) {
    super(varName, varTypeName, varValue, index);

    checkPrimitiveType(wrappedValue);

  }

  /**
   * Checks if value is Wrapped primitive type
   */
  private static void checkPrimitiveType (Object wrappedValue) {
    assert (wrappedValue != null);

    assert ((wrappedValue instanceof Boolean) || (wrappedValue instanceof Character) || (wrappedValue instanceof Byte) || (wrappedValue instanceof Short)
        || (wrappedValue instanceof Integer) || (wrappedValue instanceof Long) || (wrappedValue instanceof Float) || (wrappedValue instanceof Double));

    assert wrappedValue instanceof Serializable;

  }

  @Override
  public <T> T visit (PSEVisitor<T> visitor) throws JPFInspectorException {
    return visitor.visitPSEVariablePrimitive(this);
  }

}
