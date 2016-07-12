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
import gov.nasa.jpf.inspector.interfaces.attributes.AttributeAccessDetector;
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
import gov.nasa.jpf.inspector.utils.expressions.FieldName;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.StackFrame;

import java.util.ArrayList;

/**
 * The attribute manager has multiple functions, all related to processing attributes:
 *
 * - Its {@link #setAttributeValue(String)} modifies attributes in response to client commands
 * - Its {@link #convertAttributeToString(AttributeAttachment, Object)} and its public accessors return attributes as a human-readable string.
 * - Its _detectRead_ and _detectWrite_ methods determine whether the _attr_access_ family of hit conditions should hit.
 */
public class AttributesManager implements AttributeManagerInterface {
  private final JPFInspector inspector;
  private ArrayList<AttributeToStringConverter> attributeToStringConverters = new ArrayList<>();
  private ArrayList<StringToAttributeConverter> stringToAttributeConverters = new ArrayList<>();
  private ArrayList<AttributeAccessDetector> detectors = new ArrayList<>();
  private final ExpressionParser parser;

  // Initialization
  public AttributesManager(JPFInspector inspector) {
    this.inspector = inspector;
    this.attributeToStringConverters = InspectorConfiguration.getInstance().getLoadedAttributeToStringConverters();
    this.stringToAttributeConverters = InspectorConfiguration.getInstance().getLoadedStringToAttributeConverters();
    this.detectors = InspectorConfiguration.getInstance().getLoadedAttributeAccessDetectors();
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
      AttributeConversionResult acr = attachment.convertToAttributeUsing(parsedExpr.getNewAttributeValue().trim(),
                                                                         converter);
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

  @Override
  public String getAttachmentAttributes(StackFrame sf, int index) {
    AttributeAttachment attributeAttachment = new StackSlotAttachment(sf, index);
    return getAttachmentAttributes(attributeAttachment);
  }

  @Override
  public String getAttachmentAttributes(ElementInfo ei, FieldInfo fieldInfo) {
    return getAttachmentAttributes(new FieldAttachment(ei, fieldInfo));
  }

  @Override
  public String getAttachmentAttributes(ElementInfo ei, int index) {
    return getAttachmentAttributes(new ArrayElementAttachment(ei, index));
  }

  // Detection
  public boolean detectRead(Instruction impendingInstruction, FieldName fieldName) {
    for (AttributeAccessDetector detector : detectors) {
      if (detector.detectRead(impendingInstruction, fieldName)) {
        return true;
      }
    }
    return false;
  }

  public boolean detectRead(Instruction impendingInstruction, String localVariable) {
    for (AttributeAccessDetector detector : detectors) {
      if (detector.detectRead(impendingInstruction, localVariable)) {
        return true;
      }
    }
    return false;
  }

  public boolean detectWrite(Instruction impendingInstruction, FieldName fieldName) {
    for (AttributeAccessDetector detector : detectors) {
      if (detector.detectWrite(impendingInstruction, fieldName)) {
        return true;
      }
    }
    return false;
  }

  public boolean detectWrite(Instruction impendingInstruction, String localVariable) {
    for (AttributeAccessDetector detector : detectors) {
      if (detector.detectWrite(impendingInstruction, localVariable)) {
        return true;
      }
    }
    return false;
  }

}
