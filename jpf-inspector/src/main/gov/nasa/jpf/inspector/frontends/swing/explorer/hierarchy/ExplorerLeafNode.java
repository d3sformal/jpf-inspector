package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import javax.swing.tree.TreeNode;
import java.util.Enumeration;

public abstract class ExplorerLeafNode extends ExplorerNode {
  protected ExplorerLeafNode(ExplorerNode parent) {
    super(parent);
  }

  @Override
  public final TreeNode getChildAt(int childIndex) {
    return null;
  }

  @Override
  public final int getChildCount() {
    return 0;
  }

  @Override
  public final int getIndex(TreeNode node) {
    return -1;
  }

  @Override
  public final boolean getAllowsChildren() {
    return false;
  }

  @Override
  public final boolean isLeaf() {
    return true;
  }

  @Override
  public final Enumeration children() {
    return null;
  }

  @Override
  public final boolean isRecognizableAs(ExplorerNode oldNode) {
    return false;
  }
}
