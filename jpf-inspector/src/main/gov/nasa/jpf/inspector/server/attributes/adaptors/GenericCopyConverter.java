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

package gov.nasa.jpf.inspector.server.attributes.adaptors;

import gov.nasa.jpf.inspector.client.JPFInspectorClientInterface;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.interfaces.attributes.AbstractStringToAttributeConverter;
import gov.nasa.jpf.inspector.interfaces.attributes.AttributeConversionResult;
import gov.nasa.jpf.inspector.server.attributes.attachments.AttributeAttachment;
import gov.nasa.jpf.inspector.server.expression.ExpressionParser;
import gov.nasa.jpf.inspector.server.expression.ExpressionStateRootNode;
import gov.nasa.jpf.inspector.server.programstate.StateNodeInterface;
import gov.nasa.jpf.inspector.server.programstate.StateWritableValue;

/**
 * This converter evaluates its string as an Inspector expression (e.g. "#thread[2].#stackSlot[3]") and copies the attribute
 * of that expression (in this case, a stack slot) into the target object. If the argument object has no attributes,
 * then the target object's attributes are cleared.
 */
public class GenericCopyConverter extends AbstractStringToAttributeConverter {
  private JPFInspectorClientInterface inspector;

  @Override
  public AttributeConversionResult stringToAttribute(String newAttributeValue) {
    ExpressionParser expressionParser = new ExpressionParser(inspector.getServer());
    try {
      ExpressionStateRootNode root = expressionParser.getExpressionStateInterface(newAttributeValue);
      StateNodeInterface resultExpression = root.getResultExpression(inspector.getServer(), inspector.getServer()
              .getStopHolder()
              .getInspectorState());
      if (!(resultExpression instanceof StateWritableValue)) {
        throw new JPFInspectorGenericErrorException("You referenced an object that cannot have attributes attached.");
      }
      StateWritableValue value = (StateWritableValue)resultExpression;
      AttributeAttachment attributes = value.getAttributeAttachment();
      Iterable<Object> iterable = attributes.getAttributes();
      Object firstItem = null;
      for (Object iterableItem : iterable) {
        if (firstItem != null) {
          throw new JPFInspectorGenericErrorException("The referenced object has multiple attributes. In JPF Inspector, a converter can only create a single attribute.");
        }
        firstItem = iterableItem;
      }
      return AttributeConversionResult.successful(firstItem);

    } catch (Exception exception) {
      return AttributeConversionResult.failed("Attribute could not be copied: " + exception.getMessage());
    }
  }

  @Override
  public void initialize(JPFInspectorClientInterface inspector) {
    this.inspector = inspector;
  }
}
