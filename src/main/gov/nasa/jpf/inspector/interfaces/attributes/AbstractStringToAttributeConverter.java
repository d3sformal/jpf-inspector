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

package gov.nasa.jpf.inspector.interfaces.attributes;

import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.StackFrame;

/**
 * This is an abstract base classes that users may extend in order to implement their own {@link StringToAttributeConverter}.
 * The interface, {@link StringToAttributeConverter}, contains multiple methods that provide additional information to
 * the converter, but most converters will probably find this information meaningless and will only be interested in
 * the actual string expression.
 *
 * For this reason, it is simpler to implement this class that provides only a single method,
 * {@link #stringToAttribute(String)}, for this purpose.
 */
public abstract class AbstractStringToAttributeConverter implements StringToAttributeConverter {
  protected abstract AttributeConversionResult stringToAttribute(String newAttributeValue);

  @Override
  public AttributeConversionResult convertForField(ElementInfo object, FieldInfo field, String newAttributeValue) {
    return stringToAttribute(newAttributeValue);
  }

  @Override
  public AttributeConversionResult convertForStackSlot(StackFrame stackFrame, int index, String newAttributeValue) {
    return stringToAttribute(newAttributeValue);
  }

  @Override
  public AttributeConversionResult convertForArrayElement(ElementInfo array, int index, String newAttributeValue) {
    return stringToAttribute(newAttributeValue);
  }
}
