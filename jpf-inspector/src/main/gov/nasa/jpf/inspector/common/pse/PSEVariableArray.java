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
 * @author Alf
 * 
 */
public class PSEVariableArray extends PSEVariable {

  private static final long serialVersionUID = -7445210497451473309L;

  /**
   * Length of the represented array
   */
  private final int length;
  /**
   * Content of the array
   */
  private PSEVariable[] refArrayItems;

  /**
   * Creates full representation of the array with references to array entries
   */
  public PSEVariableArray(String varName, String varTypeName,
                          String varValue, boolean isStatic,
                          String definedIn, int index, int length, PSEVariable[] refArrayItems) {
    super(varName, varTypeName, varValue, isStatic, definedIn, index);

    this.length = length;
    this.refArrayItems = refArrayItems;
  }

  public int getLength() {
    return length;
  }

  public PSEVariable[] getArrayItems () throws JPFInspectorException {
    return refArrayItems;
  }

  @Override
  public <T> T visit (PSEVisitor<T> visitor) throws JPFInspectorException {
    return visitor.visitPSEVariableArray(this);
  }

}
