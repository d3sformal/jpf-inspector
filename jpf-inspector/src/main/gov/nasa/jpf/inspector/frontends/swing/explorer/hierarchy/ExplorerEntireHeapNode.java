package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import gov.nasa.jpf.inspector.frontends.swing.explorer.Attachment;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ProgramStateTreeModel;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.Heap;

import java.util.ArrayList;

public class ExplorerEntireHeapNode extends ExplorerComplexNode {
  ProgramStateTreeModel model;

  public ExplorerEntireHeapNode(ProgramStateTreeModel model, ExplorerNode parent) {
      super(model, Attachment.irrelevant(), parent);
      this.model = model;

  }

  @Override
  public String toString() {
    return "The Heap";
  }

  @Override
  public ArrayList<ExplorerNode> populateChildren() {
    ArrayList<ExplorerNode> heapList = new ArrayList<>(400);
    Heap heap = model.getVM().getHeap();
    for (ElementInfo elementInfo : heap) {
      if (elementInfo.getClassInfo().isArray()) {
        ExplorerArrayNode newObject = new ExplorerArrayNode(Attachment.heapEntry(elementInfo.getObjectRef()),
                                                                      elementInfo,
                                                                      model, this);

        heapList.add(newObject);
      } else {
        ExplorerJavaObjectNode newObject = new ExplorerJavaObjectNode(Attachment.heapEntry(elementInfo.getObjectRef()),
                                                                      elementInfo,
                                                                      model, this);

        heapList.add(newObject);
      }
    }
    return heapList;
  }


  @Override
  public void updateComplexNodeFromJpf(ExplorerNode newVersion) {
    // Updates self
  }

  @Override
  public boolean isRecognizableAs(ExplorerNode oldNode) {
    return oldNode instanceof ExplorerEntireHeapNode;
  }
}
