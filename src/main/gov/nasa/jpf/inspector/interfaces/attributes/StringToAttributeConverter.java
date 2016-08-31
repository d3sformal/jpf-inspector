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
 * Attribute adaptor that parses a user-given string into an attribute object.
 */
@SuppressWarnings("UnusedParameters") // We don't use the converters' full capabilities, but other modules might.
public interface StringToAttributeConverter extends AttributeAdaptorBase {
  /**
   * Parses a user-given string to create an attribute object to be associated with an element of an array.
   *
   * @param array The array where the attribute will be stored.
   * @param index Array index of the element with which the attribute will be associated.
   * @param newAttributeValue String to be parsed and converted into an attribute object.
   * @return Result of the conversion. Use the static factory method to create it.
   */
  AttributeConversionResult convertForArrayElement(ElementInfo array, int index, String newAttributeValue);

  /**
   * Parses a user-given string to create an attribute object to be associated with a field of an object.
   *
   * @param object The Java object where the attribute will be stored (i.e. the owner of the field)
   * @param field Field with which the attribute will be associated.
   * @param newAttributeValue String to be parsed and converted into an attribute object.
   * @return Result of the conversion. Use the static factory method to create it.
   */
  AttributeConversionResult convertForField(ElementInfo object, FieldInfo field, String newAttributeValue);

  /**
   * Parses a user-given string to create an attribute object to be associated with a slot of a stack frame.
   *
   * @param stackFrame The stack frame where the attribute will be stored.
   * @param index Index of the stack slot with which the attribute will be associated.
   * @param newAttributeValue String to be parsed and converted into an attribute object.
   * @return Result of the conversion. Use the static factory method to create it.
   */
  AttributeConversionResult convertForStackSlot(StackFrame stackFrame, int index, String newAttributeValue);
}
