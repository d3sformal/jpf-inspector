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
import gov.nasa.jpf.symbc.string.StringConstant;

/**
 * SPF-related converter that converts a string (e.g. "hello") into a symbolic string constant ({@link StringConstant}).
 */
public class StringConverter extends AbstractStringToAttributeConverter {
  @Override
  public AttributeConversionResult stringToAttribute(String newAttributeValue) {

    if (newAttributeValue.length() < 2 || newAttributeValue.charAt(0) != '"' || !newAttributeValue.endsWith("\"")) {
      return AttributeConversionResult.failed("The value is not in double quotes.");
    }
    String stringContents = newAttributeValue.substring(1, newAttributeValue.length()-1);
    return AttributeConversionResult.successful(new gov.nasa.jpf.symbc.string.StringConstant(stringContents));
    
  }

  @Override
  public void initialize(JPFInspectorClientInterface inspector) {

  }
}
