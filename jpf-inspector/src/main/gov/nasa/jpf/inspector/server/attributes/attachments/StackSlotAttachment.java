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
import gov.nasa.jpf.vm.StackFrame;

public class StackSlotAttachment implements AttributeAttachment {
  private final StackFrame stackFrame;
  private final int index;

  public StackSlotAttachment(StackFrame stackFrame, int index) {

    this.stackFrame = stackFrame;
    this.index = index;
  }

  @Override
  public Iterable<Object> getAttributes() {
    return ObjectList.iterator(stackFrame.getSlotAttr(index));
  }

  @Override
  public String convertToStringUsing(Object attribute, AttributeToStringConverter converter) {
    return converter.stackSlotToString(stackFrame, index, attribute);
  }

  @Override
  public AttributeConversionResult convertToAttributeUsing(String expression, StringToAttributeConverter converter) {
    return converter.convertForStackSlot(stackFrame, index, expression);
  }

  @Override
  public void setAttribute(Object createdAttribute) {
    stackFrame.setSlotAttr(index, createdAttribute);
  }
}
