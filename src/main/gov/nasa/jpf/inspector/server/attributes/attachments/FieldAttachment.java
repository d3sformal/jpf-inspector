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
import gov.nasa.jpf.util.ObjectList;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;

/**
 * Accesses attributes associated with a field of an object.
 */
public class FieldAttachment implements AttributeAttachment {

  private final ElementInfo objectInfo;
  private final FieldInfo field;

  public FieldAttachment(ElementInfo objectInfo, FieldInfo field) {

    this.objectInfo = objectInfo;
    this.field = field;
  }

  @Override
  public Iterable<Object> getAttributes() {
    return ObjectList.iterator(objectInfo.getFieldAttr(field));
  }

  @Override
  public String convertToStringUsing(Object attribute, AttributeToStringConverter converter) {
    return converter.fieldToString(objectInfo, field.getFieldIndex(), attribute);
  }

  @Override
  public AttributeConversionResult convertToAttributeUsing(String expression, StringToAttributeConverter converter) {
    return converter.convertForField(objectInfo, field, expression);
  }

  @Override
  public void setAttribute(Object createdAttribute) {
    objectInfo.getModifiableInstance().setFieldAttr(field, createdAttribute);
  }
}
