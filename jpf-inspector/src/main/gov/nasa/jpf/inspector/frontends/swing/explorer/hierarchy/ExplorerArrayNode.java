package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import gov.nasa.jpf.inspector.frontends.swing.explorer.Attachment;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ExplorerNodeFactory;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ProgramStateTreeModel;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.Heap;

import java.util.ArrayList;

/**
 * Represents a Java array in the Explorer.
 *
 * Note that it inherits from {@link ExplorerJavaObjectNode}.
 */
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
      Object value = getValue(arrayElementClass, elementInfo, i);
      elements.add(ExplorerNodeFactory.createFromArrayElement(i, arrayElementClass, value, model, parent));
    }
    return elements;
  }

  /**
   * Gets the value of an element of this array.
   * @param componentClassInfo The type of elements of this array.
   * @param arrayElementInfo The ElementInfo of the entire array.
   * @param index The index of the element we want to retrieve.
   * @return Value of an element of this array at the given index. The return value is ElementInfo for reference objects and an appropriate boxed primitive type for primitive types.
   */
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
