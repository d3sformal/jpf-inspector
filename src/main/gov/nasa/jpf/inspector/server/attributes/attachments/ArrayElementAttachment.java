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

/**
 * Accesses attributes associated with an element of an array.
 */
public class ArrayElementAttachment implements AttributeAttachment {
  private final ElementInfo arrayInfo;
  private final int index;

  public ArrayElementAttachment(ElementInfo arrayInfo, int index) {

    this.arrayInfo = arrayInfo;
    this.index = index;
  }

  @Override
  public Iterable<Object> getAttributes() {
    return ObjectList.iterator(arrayInfo.getElementAttr(index));
  }

  @Override
  public String convertToStringUsing(Object attribute, AttributeToStringConverter converter) {
    return converter.fieldToString(arrayInfo, index, attribute);
  }

  @Override
  public AttributeConversionResult convertToAttributeUsing(String expression, StringToAttributeConverter converter) {
    return converter.convertForArrayElement(arrayInfo, index, expression);
  }

  @Override
  public void setAttribute(Object createdAttribute) {
    arrayInfo.getModifiableInstance().setElementAttr(index, createdAttribute);
  }
}
