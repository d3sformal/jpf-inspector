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

package gov.nasa.jpf.inspector.symbolic;

import gov.nasa.jpf.inspector.client.JPFInspectorClientInterface;
import gov.nasa.jpf.inspector.interfaces.attributes.AbstractStringToAttributeConverter;
import gov.nasa.jpf.inspector.interfaces.attributes.AttributeConversionResult;

public class StringConverter extends AbstractStringToAttributeConverter {
  @Override
  public AttributeConversionResult stringToAttribute(String newAttributeValue) {
    return AttributeConversionResult.failed("Converter not yet implemented.");
  }

  @Override
  public void initialize(JPFInspectorClientInterface inspector) {

  }
}
