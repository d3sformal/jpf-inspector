package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import gov.nasa.jpf.inspector.frontends.swing.explorer.Attachment;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ProgramStateTreeModel;

import java.util.ArrayList;

public class ExplorerRoot extends ExplorerComplexNode {

  public ExplorerRoot() {
    super(null, Attachment.irrelevant(), null);
  }


  public void initialize(ProgramStateTreeModel model) {
    children = new ArrayList<>();
    children.add(new ExplorerEntireHeapNode(model, this));
    children.add(new ExplorerAllThreadsNode(model, this));
    children.add(new ExplorerStaticAreaNode(model, this));
  }

  @Override
  public String toString() {
    return "Program State Explorer Root Node";
  }

  @Override
  protected ArrayList<ExplorerNode> populateChildren() {
    return children; // We will never change.
  }

  @Override
  public void updateComplexNodeFromJpf(ExplorerNode newVersion) {
    // Nothing.
  }

  @Override
  public boolean isRecognizableAs(ExplorerNode oldNode) {
    return oldNode instanceof ExplorerRoot;
  }
}
