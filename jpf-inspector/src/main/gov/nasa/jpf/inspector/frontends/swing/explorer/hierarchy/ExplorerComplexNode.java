package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public abstract class ExplorerComplexNode extends ExplorerNode {
  protected List<ExplorerNode> children;

  protected ExplorerComplexNode(ExplorerNode parent) {
    super(parent);
  }

  protected abstract void ensureChildrenArePopulated();

  @Override
  public final TreeNode getChildAt(int childIndex) {
    ensureChildrenArePopulated();
    return children.get(childIndex);
  }

  @Override
  public final int getChildCount() {
    ensureChildrenArePopulated();
    return children.size();
  }

  @Override
  public final int getIndex(TreeNode node) {
    if (node == null) return -1;
    assert node instanceof ExplorerNode;
    ensureChildrenArePopulated();
    return children.indexOf(node);
  }

  @Override
  public final boolean getAllowsChildren() {
    return true;
  }

  @Override
  public final boolean isLeaf() {
    return false;
  }

  @Override
  public final Enumeration children() {
    ensureChildrenArePopulated();
    return Collections.enumeration(this.children);
  }


}
