package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import gov.nasa.jpf.inspector.frontends.swing.explorer.Attachment;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ExplorerNodeFactory;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ProgramStateTreeModel;
import gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy.ExplorerComplexNode;
import gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy.ExplorerNode;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.utils.expressions.ClassName;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.Heap;

import java.util.ArrayList;

public class ExplorerArrayNode extends ExplorerJavaObjectNode {
  public ExplorerArrayNode(Attachment attachment, ElementInfo elementInfo, ProgramStateTreeModel model, ExplorerNode parent) {
    super(attachment, elementInfo, model, parent);
  }

  @Override
  protected ArrayList<ExplorerNode> populateChildren() {
    ClassInfo arrayClass = elementInfo.getClassInfo();
    ClassInfo arrayElementClass = arrayClass.getComponentClassInfo();

    ArrayList<ExplorerNode> elements = new ArrayList<>();
    int length = elementInfo.arrayLength();
    for (int i =0 ;i < length; i++) {
      ExplorerNode arrayElement = null;
      Object value = getValue(arrayElementClass, elementInfo, i);
      elements.add(ExplorerNodeFactory.createFromArrayElement(i, arrayElementClass, value, model, parent));
    }
    return elements;
  }

  public Object getValue (ClassInfo componentClassInfo, ElementInfo arrayElementInfo, int index) {
    String sig = componentClassInfo.getSignature();

    switch (sig.charAt(0)) {
      case 'Z':
        return arrayElementInfo.getBooleanElement(index);
      case 'B':
        return arrayElementInfo.getByteElement(index);
      case 'C':
        return arrayElementInfo.getCharElement(index);
      case 'S':
        return arrayElementInfo.getShortElement(index);
      case 'I':
        return arrayElementInfo.getIntElement(index);
      case 'J':
        return arrayElementInfo.getLongElement(index);
      case 'F':
        return arrayElementInfo.getFloatElement(index);
      case 'D':
        return arrayElementInfo.getDoubleElement(index);
      default: // reference Array of Object
        int ref = arrayElementInfo.getReferenceElement(index);
        Heap heap = model.getVM().getHeap();
        return heap.get(ref);
    }
  }
}
