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
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.StackFrame;

import java.util.Iterator;

public abstract class AttributeAttachment {
  public abstract Iterable<Object> getAttributes();

  public abstract String convertToStringUsing(Object attribute, AttributeToStringConverter converter);

  public abstract AttributeConversionResult convertToAttributeUsing(String expression, StringToAttributeConverter converter);

  public abstract void setAttribute(Object createdAttribute);
}
