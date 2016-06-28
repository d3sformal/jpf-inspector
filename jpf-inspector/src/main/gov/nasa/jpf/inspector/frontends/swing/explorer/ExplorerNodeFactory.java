package gov.nasa.jpf.inspector.frontends.swing.explorer;

import gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy.*;
import gov.nasa.jpf.util.json.Value;
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
    return createNode(Attachment.instanceField(name), fieldInfo.getValueObject(fields), fieldInfo.getTypeClassInfo(), model, parent);

  }
  public static ExplorerNode createFromStackSlot(String name,
                                                 StackFrame stackFrame,
                                                 LocalVarInfo localVarInfo,
                                                 ProgramStateTreeModel model,
                                                 ExplorerStackFrameNode parent) {
    String stackSlotType = localVarInfo.getType();
    ClassInfo stackSlotClass = ClassLoaderInfo.getCurrentResolvedClassInfo(stackSlotType);
    return createNode(Attachment.stackSlot(name), stackFrame.getLocalValueObject(localVarInfo), stackSlotClass, model, parent);
  }
  public static ExplorerNode createFromArrayElement(int index,
                                                    ClassInfo componentClass,
                                                    Object componentValue,
                                                    ProgramStateTreeModel model,  ExplorerNode parent) {
    return createNode(Attachment.arrayElement(index), componentValue, componentClass, model, parent);
  }
}
