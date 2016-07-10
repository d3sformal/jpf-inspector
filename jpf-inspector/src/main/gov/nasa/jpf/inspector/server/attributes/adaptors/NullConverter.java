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

package gov.nasa.jpf.inspector.server.attributes.adaptors;

import gov.nasa.jpf.inspector.client.JPFInspectorClientInterface;
import gov.nasa.jpf.inspector.interfaces.attributes.AbstractStringToAttributeConverter;
import gov.nasa.jpf.inspector.interfaces.attributes.AttributeConversionResult;

import java.util.Objects;

public class NullConverter extends AbstractStringToAttributeConverter {

  @Override
  public AttributeConversionResult stringToAttribute(String newAttributeValue) {
    if (Objects.equals(newAttributeValue, "null")) {
      return AttributeConversionResult.successful(null);
    } else {
      return AttributeConversionResult.failed("The new value was not 'null'.");
    }

  }

  @Override
  public void initialize(JPFInspectorClientInterface inspector) {

  }
}
