//
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
//

package gov.nasa.jpf.inspector.frontends.swing.explorer;

import gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy.*;
import gov.nasa.jpf.vm.*;

public class ExplorerNodeFactory {
  /*
    The general procedure for creating a node of unspecified type is;
    1. Determine its class.
    2. If the class is a primitive, the value is the boxed primitive.
    3. If the class is an array, the value is an ElementInfo (I think) that can be used to extract elements.
    4. Otherwise, it's a Java object.


   */

  private static ExplorerNode createNode(Attachment attachment,
                                         Object value,
                                         ClassInfo classInfo,
                                         ProgramStateTreeModel model,
                                         ExplorerNode parent) {
    if (classInfo.isPrimitive()) {
      return new ExplorerPrimitiveNode(value, attachment, model, parent);
    } else if (classInfo.isArray()) {
      return new ExplorerArrayNode(attachment, (ElementInfo)value, model, parent);
    } else {
      return new ExplorerJavaObjectNode(attachment, (ElementInfo) value, model, parent);
    }
  }

  public static ExplorerNode createFromField(String name, FieldInfo fieldInfo, Fields fields,
                                             ProgramStateTreeModel model,  ExplorerJavaObjectNode parent) {
    return createNode(Attachment.instanceField(name), fieldInfo.getValueObject(fields), fieldInfo.getTypeClassInfo(),
                      model, parent);

  }
  public static ExplorerNode createFromStackSlot(String name,
                                                 StackFrame stackFrame,
                                                 LocalVarInfo localVarInfo,
                                                 ProgramStateTreeModel model,
                                                 ExplorerStackFrameNode parent) {
    String stackSlotType = localVarInfo.getType();
    ClassInfo stackSlotClass = ClassLoaderInfo.getCurrentResolvedClassInfo(stackSlotType);
    return createNode(Attachment.stackSlot(name), stackFrame.getLocalValueObject(localVarInfo), stackSlotClass,
                      model, parent);
  }
  public static ExplorerNode createFromArrayElement(int index,
                                                    ClassInfo componentClass,
                                                    Object componentValue,
                                                    ProgramStateTreeModel model,  ExplorerNode parent) {
    return createNode(Attachment.arrayElement(index), componentValue, componentClass, model, parent);
  }

  public static ExplorerNode createFromStaticField(String name, FieldInfo fieldInfo, Fields fields,
                                                   ProgramStateTreeModel model, ExplorerJavaObjectNode parent) {
    return createNode(Attachment.staticField(name), fieldInfo.getValueObject(fields), fieldInfo.getTypeClassInfo(),
                      model, parent);

  }
}
