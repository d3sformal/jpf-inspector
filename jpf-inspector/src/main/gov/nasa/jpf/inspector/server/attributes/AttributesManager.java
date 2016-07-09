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

package gov.nasa.jpf.inspector.server.attributes;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.interfaces.AttributeManagerInterface;
import gov.nasa.jpf.inspector.interfaces.attributes.AttributeConversionResult;
import gov.nasa.jpf.inspector.interfaces.attributes.AttributeToStringConverter;
import gov.nasa.jpf.inspector.interfaces.attributes.StringToAttributeConverter;
import gov.nasa.jpf.inspector.server.attributes.attachments.ArrayElementAttachment;
import gov.nasa.jpf.inspector.server.attributes.attachments.AttributeAttachment;
import gov.nasa.jpf.inspector.server.attributes.attachments.FieldAttachment;
import gov.nasa.jpf.inspector.server.attributes.attachments.StackSlotAttachment;
import gov.nasa.jpf.inspector.server.expression.ExpressionParser;
import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionStateAttributeAssignment;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.programstate.StateWritableValue;
import gov.nasa.jpf.inspector.utils.InspectorConfiguration;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.StackFrame;

import java.util.ArrayList;

public class AttributesManager implements AttributeManagerInterface {
  private JPFInspector inspector;
  private ArrayList<AttributeToStringConverter> attributeToStringConverters = new ArrayList<>();
  private ArrayList<StringToAttributeConverter> stringToAttributeConverters = new ArrayList<>();
  private final ExpressionParser parser;

  // Initialization
  public AttributesManager(JPFInspector inspector) {
    this.inspector = inspector;
    this.attributeToStringConverters = InspectorConfiguration.getInstance().getLoadedAttributeToStringConverters();
    this.stringToAttributeConverters = InspectorConfiguration.getInstance().getLoadedStringToAttributeConverters();
    parser = new ExpressionParser(inspector);
  }

  // String to attribute
  @Override
  public void setAttributeValue(String expression) throws JPFInspectorException {
    if (stringToAttributeConverters.isEmpty()) {
      throw new JPFInspectorGenericErrorException("No attribute-to-string converters are loaded.");
    }
    inspector.waitUntilStopped();
    ExpressionStateAttributeAssignment parsedExpr = parser.getAttributeAssignment(expression);
    StateWritableValue targetObject = parsedExpr.getTargetObject(inspector,
                                                                 inspector.getStopHolder().getInspectorState());
    AttributeAttachment attachment = targetObject.getAttributeAttachment();
    String errorMessages = "";
    for (StringToAttributeConverter converter : stringToAttributeConverters) {
      AttributeConversionResult acr = attachment.convertToAttributeUsing(expression, converter);
      if (acr.isSuccess()) {
        attachment.setAttribute(acr.getCreatedAttribute());
        return;
      } else {
        if (acr.getReasonForRefusal() != null) {
          errorMessages += converter.getClass().getSimpleName() + ": " + acr.getReasonForRefusal() + "\n";
        }
      }
    }

    // We failed.
    throw new JPFInspectorGenericErrorException("No converter was able to parse the new attribute value." +
                                                        (errorMessages.isEmpty() ? "" : "\n" + errorMessages));
  }



  // Attribute to string
  private String convertAttributeToString(AttributeAttachment attributeAttachment, Object attribute) {
    for (AttributeToStringConverter converter : attributeToStringConverters) {
      String converted = attributeAttachment.convertToStringUsing(attribute, converter);
      if (converted != null) {
        return converted;
      }
    }
    return attribute.toString();
  }

  private String getAttachmentAttributes(AttributeAttachment attributeAttachment) {
    ArrayList<String> attributeStrings = new ArrayList<>();
    for (Object attribute : attributeAttachment.getAttributes()) {
      attributeStrings.add(convertAttributeToString(attributeAttachment, attribute));
    }
    if (attributeStrings.isEmpty()) {
      return null;
    }
    return String.join(",", attributeStrings);
  }

  public String getAttachmentAttributes(StackFrame sf, int index) {
    AttributeAttachment attributeAttachment = new StackSlotAttachment(sf, index);
    return getAttachmentAttributes(attributeAttachment);
  }

  public String getAttachmentAttributes(ElementInfo ei, FieldInfo fieldInfo) {
    return getAttachmentAttributes(new FieldAttachment(ei, fieldInfo));
  }

  public String getAttachmentAttributes(ElementInfo ei, int index) {
    return getAttachmentAttributes(new ArrayElementAttachment(ei, index));
  }

  // Detection
}
