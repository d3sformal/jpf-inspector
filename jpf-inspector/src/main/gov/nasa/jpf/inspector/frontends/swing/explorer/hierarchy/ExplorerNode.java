package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import javax.swing.tree.TreeNode;
import java.util.List;

public abstract class ExplorerNode implements TreeNode {
  protected ExplorerNode parent;
  protected ExplorerNode(ExplorerNode parent) {
    this.parent = parent;
  }

  @Override
  public abstract String toString();

  @Override
  public final TreeNode getParent() {
    return parent;
  }

  public abstract void updateFromJpf();
}
