package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import gov.nasa.jpf.inspector.frontends.swing.explorer.ProgramStateTreeModel;

import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

public abstract class ExplorerComplexNode extends ExplorerNode {
  protected final ProgramStateTreeModel model;
  protected ArrayList<ExplorerNode> children;

  protected ExplorerComplexNode(ProgramStateTreeModel model, ExplorerNode parent) {
    super(parent);
    this.model = model;
  }

  protected abstract ArrayList<ExplorerNode> populateChildren();

  private void ensureChildrenArePopulated() {
    if (children == null) {
      if (!model.getServer().isPaused()) {
        children = new ArrayList<>();
        this.wronglyExpanded = true;
        this.model.nodesChanged(parent, new int[]{parent.getIndex(this)});
        return;
      }
      if (model.getVM() == null) {
        children = new ArrayList<>();
        this.wronglyExpanded = true;
        this.model.nodesChanged(parent, new int[]{parent.getIndex(this)});
        return;
      }
      children = populateChildren();
    }
  }

  @Override
  public final void updateFromJpf() {
    if (wronglyExpanded) {
      wronglyExpanded = false;
      children = null;
      model.nodeStructureChanged(this);
    }

    updateComplexNodeFromJpf();

    if (children != null) {
      // First, update what children are shown.
      childrenNeedToBeUpdated();

      // Then, update the children themselves.
      for(ExplorerNode child : children) {
        child.updateFromJpf();
      }
    }
  }

  private void childrenNeedToBeUpdated() {
    ArrayList<ExplorerNode> newChildren = populateChildren();
    ArrayList<ExplorerNode> oldChildren = children;
    int oldIndex = 0;
    for (int newIndex = 0; newIndex < newChildren.size(); newIndex++) {
        if (oldIndex >= oldChildren.size()) {
          // This is a new child, inserted at the end.
          oldChildren.add(newChildren.get(newIndex));
          model.nodesWereInserted(this, new int[]{oldChildren.size() - 1});
          oldIndex++;
        } else {
          // Who knows what this is??
          ExplorerNode oldChild = oldChildren.get(oldIndex);
          ExplorerNode newChild = newChildren.get(newIndex);
          if (newChild.isRecognizableAs(oldChild)) {
            oldIndex++;
          } else {
            oldChildren.remove(oldIndex);
            model.nodesWereRemoved(this, new int[] { oldIndex }, new Object[] { oldChild});
            //noinspection AssignmentToForLoopParameter
            newIndex--;
          }
        }
    }
    for (int remainingItemsIndex = oldIndex; remainingItemsIndex < children.size(); remainingItemsIndex++) {
      ExplorerNode removedNode = children.get(oldIndex);
      children.remove(oldIndex);
      model.nodesWereRemoved(this, new int[]{oldIndex}, new Object[]{removedNode});
    }
  }

  public abstract void updateComplexNodeFromJpf();

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
    if (node == null) {
      return -1;
    }
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
  public final Enumeration<ExplorerNode> children() {
    ensureChildrenArePopulated();
    return Collections.enumeration(this.children);
  }


}
