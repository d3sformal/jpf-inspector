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
 * This is an abstract base classes that users may extend in order to implement their own {@link AttributeToStringConverter}.
 * The interface, {@link AttributeToStringConverter}, contains multiple methods that provide additional information to
 * the converter, but most converters will probably find this information meaningless and will only be interested in
 * the actual attribute values.
 *
 * For this reason, it is simpler to implement this class that provides only a single method,
 * {@link #attributeToString(Object)}, for this purpose.
 */
public abstract class AbstractAttributeToStringConverter implements AttributeToStringConverter {
  /**
   * Converts an attribute object into a human-readable string, or returns null if it fails.
   * @param attribute An attribute of a field, stack slot, array element or object. It is guaranteed not to be null.
   * @return Human-readable description of the attribute, or null if this converter cannot handle this attribute.
   */
  protected abstract String attributeToString(Object attribute);

  @Override
  public String objectToString(ElementInfo object, Object attribute) {
    return attributeToString(attribute);
  }

  @Override
  public String fieldToString(ElementInfo owner, int index, Object attribute) {
    return attributeToString(attribute);
  }

  @Override
  public String stackSlotToString(StackFrame stackFrame, int index, Object attribute) {
    return attributeToString(attribute);
  }
}
