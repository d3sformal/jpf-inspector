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
import gov.nasa.jpf.vm.StackFrame;

/**
 * Attribute adaptor that converts an attribute object to a human-readable string.
 */
@SuppressWarnings("UnusedParameters") // We don't use the converters' full capabilities, but other modules might.
public interface AttributeToStringConverter extends AttributeAdaptorBase {
  /**
   * Converts an attribute associated with an object's field to a human-readable string, or returns null if this
   * converter is incapable of such a conversion.
   *
   * @param owner The object that contains the field; or the array that contains the element.
   * @param index Index of the field; or index of the array element.
   * @param attribute An attribute of the field or element that should be converted.
   * @return Human-readable representation of the attribute; or null.
   */
  String fieldToString(ElementInfo owner, int index, Object attribute);

  /**
   * Converts an attribute associated with a stack slot to a human-readable string, or returns null if the converter
   * is incapable of such a conversion.
   *
   * @param stackFrame The stack frame that owns the slot.
   * @param index Index of the stack slot in the stack frame.
   * @param attribute An attribute of the slot that should be converted.
   * @return Human-readable representation of teh attribute; or null.
   */
  String stackSlotToString(StackFrame stackFrame, int index, Object attribute);
}
