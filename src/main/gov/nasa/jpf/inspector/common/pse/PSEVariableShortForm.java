//
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
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;

/**
 * Represents an array or an object without loaded elements or fields. Such an array or object is only intended
 * to be displayed to the user in its short form.
 */
public class PSEVariableShortForm extends PSEVariable {
  private static final long serialVersionUID = -2393412289253782599L;

  public PSEVariableShortForm(String varName, String varTypeName,
                              String varValue, int index, String attributes) {
    super(varName, varTypeName, varValue, index, attributes);
  }

  @Override
  public <T> T visit(PSEVisitor<T> visitor) throws JPFInspectorException {
    throw new JPFInspectorGenericErrorException("Short-form variables may only be printed as part of a larger entry and not alone.");
  }
}
