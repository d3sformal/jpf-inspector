package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import javax.swing.tree.TreeNode;
import java.util.List;

public abstract class ExplorerNode implements TreeNode {
  protected ExplorerNode parent;
  protected boolean wronglyExpanded;

  public boolean isWronglyExpanded() {
    return wronglyExpanded;
  }

  protected ExplorerNode(ExplorerNode parent) {
    this.parent = parent;
  }

  @Override
  public abstract String toString();

  @Override
  public final TreeNode getParent() {
    return parent;
  }

  public abstract void updateFromJpf(ExplorerNode newVersion);

  /**
   * Returns true if this node is just a newer version of the given node. For example, if this node represents
   * a Java array and an element of the array changes, the new array should still be recognizable as the old one.
   * @param oldNode An older version of this node.
   */
  public abstract boolean isRecognizableAs(ExplorerNode oldNode);
}
