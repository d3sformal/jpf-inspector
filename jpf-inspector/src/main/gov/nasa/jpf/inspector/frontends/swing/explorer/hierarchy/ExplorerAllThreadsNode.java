package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import gov.nasa.jpf.inspector.frontends.swing.explorer.ProgramStateTreeModel;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.ThreadList;

import java.util.ArrayList;

public class ExplorerAllThreadsNode extends ExplorerComplexNode {
  protected ExplorerAllThreadsNode(ProgramStateTreeModel model, ExplorerNode parent) {
    super(model, parent);
  }

  @Override
  protected ArrayList<ExplorerNode> populateChildren() {
    ArrayList<ExplorerNode> children = new ArrayList<>();
    ThreadList threadList = model.getVM().getThreadList();
    for (ThreadInfo threadInfo : threadList) {
      children.add(new ExplorerThreadInfoNode(threadInfo, model, this));
    }
    return children;
  }

  @Override
  public void updateComplexNodeFromJpf() {

  }

  @Override
  public String toString() {
    return "All Threads";
  }

  @Override
  public boolean isRecognizableAs(ExplorerNode oldNode) {
    return oldNode instanceof ExplorerAllThreadsNode;
  }
}
