package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import gov.nasa.jpf.inspector.frontends.swing.explorer.Attachment;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ProgramStateTreeModel;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.ThreadList;

import java.util.ArrayList;

/**
 * Represents the single "All Threads" Explorer node.
 */
public class ExplorerAllThreadsNode extends ExplorerComplexNode {
  protected ExplorerAllThreadsNode(ProgramStateTreeModel model, ExplorerNode parent) {
    super(model, Attachment.irrelevant(), parent);
  }

  @Override
  protected ArrayList<ExplorerNode> populateChildren() {
    ArrayList<ExplorerNode> newChildren = new ArrayList<>();
    ThreadList threadList = model.getVM().getThreadList();
    for (ThreadInfo threadInfo : threadList) {
      newChildren.add(new ExplorerThreadInfoNode(threadInfo, model, this));
    }
    return newChildren;
  }

  @Override
  public void updateComplexNodeFromJpf(ExplorerNode newVersion) {
  }

  @Override
  public String toString() {
    return "Threads";
  }

  @Override
  public boolean isRecognizableAs(ExplorerNode oldNode) {
    return oldNode instanceof ExplorerAllThreadsNode;
  }
}
