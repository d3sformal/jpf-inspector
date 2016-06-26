package gov.nasa.jpf.inspector.frontends.swing.explorer;

import gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy.ExplorerDebugLeafNode;
import gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy.ExplorerJavaObject;
import gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy.ExplorerNode;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.Fields;

public class ExplorerNodeFactory {
  public static ExplorerNode createFromField(String name, FieldInfo fieldInfo, Fields fields,
                                             ProgramStateTreeModel model,  ExplorerNode parent) {
    if (fieldInfo.getTypeClassInfo().isPrimitive()) {
      return new ExplorerDebugLeafNode(name + ": A primitive value.", parent);
    }
    return new ExplorerJavaObject(name, (ElementInfo)fieldInfo.getValueObject(fields), model, parent);
  }
}
