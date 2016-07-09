///
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
///

package gov.nasa.jpf.inspector.server.attributes;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.interfaces.AttributeManagerInterface;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;

public class AttributesManager implements AttributeManagerInterface {
  private JPFInspector inspector;

  public AttributesManager(JPFInspector inspector) {

    this.inspector = inspector;
  }

  @Override
  public void setAttributeValue(String expression) throws JPFInspectorException {
    throw new JPFInspectorGenericErrorException("Not yet implemented.");
  }
}
