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

package gov.nasa.jpf.inspector.server.attributes.attachments;

import gov.nasa.jpf.inspector.interfaces.attributes.AttributeConversionResult;
import gov.nasa.jpf.inspector.interfaces.attributes.AttributeToStringConverter;
import gov.nasa.jpf.inspector.interfaces.attributes.StringToAttributeConverter;

/**
 * Represents attributes attached to a target object in relation to its parent. One could say that the attribute belongs
 * to a _relationship_. For example, a field, a stack slot or an array element, but not an ElementInfo object itself.
 */
public interface AttributeAttachment {
  Iterable<Object> getAttributes();
  String convertToStringUsing(Object attribute, AttributeToStringConverter converter);
  AttributeConversionResult convertToAttributeUsing(String expression, StringToAttributeConverter converter);
  void setAttribute(Object createdAttribute);
}
