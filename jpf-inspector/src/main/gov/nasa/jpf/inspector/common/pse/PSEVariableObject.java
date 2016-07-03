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

/**
 * Represents a non-array, non-primitive Java object.
 */
public class PSEVariableObject extends PSEVariable {

  private static final long serialVersionUID = 5368791941110343439L;

  /**
   * Sorted by field index (ensured by creator)
   *
   * Note: Previously, this comment said "sorted by field/slot index" but I don't understand how "slots" could come
   * in play here so I removed it.
   */
  private final PSEVariable[] refFields;
  /**
   * Sorted by field index (static element fields)
   */
  private final PSEVariable[] refStaticFields;

  public PSEVariableObject(String varName,
                           String varTypeName, String varValue,
                           int index, PSEVariable[] refFields, PSEVariable[] refStaticFields) {
    super(varName, varTypeName, varValue, index);

    this.refFields = refFields;
    this.refStaticFields = refStaticFields;
  }

  public PSEVariable[] getFields () {
    return refFields;
  }

  public PSEVariable[] getStaticFields () {
    return refStaticFields;
  }

  @Override
  public <T> T visit (PSEVisitor<T> visitor) throws JPFInspectorException {
    return visitor.visitPSEVariableObject(this);
  }
}
