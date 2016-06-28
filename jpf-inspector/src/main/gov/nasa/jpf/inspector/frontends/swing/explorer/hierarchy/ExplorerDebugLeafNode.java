package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import gov.nasa.jpf.inspector.frontends.swing.explorer.ProgramStateTreeModel;

public class ExplorerDebugLeafNode extends ExplorerLeafNode {
  private final String name;

  public ExplorerDebugLeafNode(String name, ProgramStateTreeModel model, ExplorerNode parent) {
    super(null, model, parent);

    this.name = name;
  }
  @Override
  public String toString() {
    return name;
  }

  @Override
  public void updateFromJpf(ExplorerNode newVersion) {

  }

  @Override
  public boolean isRecognizableAs(ExplorerNode oldNode) {
    return false;
  }
}
