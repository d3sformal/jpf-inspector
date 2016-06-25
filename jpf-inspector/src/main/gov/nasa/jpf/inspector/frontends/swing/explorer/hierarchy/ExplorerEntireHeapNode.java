package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import gov.nasa.jpf.inspector.frontends.swing.explorer.CustomTreeModelDeprecated;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ProgramStateTreeModel;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.Heap;

import java.util.ArrayList;

public class ExplorerEntireHeapNode extends ExplorerComplexNode {
  ProgramStateTreeModel model;

  public ExplorerEntireHeapNode(ProgramStateTreeModel model, ExplorerNode parent) {
      super(parent);
      this.model = model;

  }

  @Override
  public String toString() {
    return "The Heap";
  }

  @Override
  public void updateFromJpf() {
  }

  @Override
  protected void ensureChildrenArePopulated() {
    if (children == null) {
      children = new ArrayList<>();
      Heap heap = model.getVM().getHeap();
      for (ElementInfo elementInfo : heap) {
        children.add(new ExplorerDebugLeafNode(elementInfo.getClassInfo().getName(), this));
        model.nodesWereInserted(this, new int[]{children.size() - 1});
      }
    }
  }
}
