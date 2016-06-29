package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import gov.nasa.jpf.inspector.frontends.swing.explorer.Attachment;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ProgramStateTreeModel;

import javax.swing.tree.TreeNode;

public abstract class ExplorerNode implements TreeNode {
  protected ExplorerNode parent;
  protected Attachment attachment;
  protected boolean wronglyExpanded;
  protected final ProgramStateTreeModel model;

  public boolean isWronglyExpanded() {
    return wronglyExpanded;
  }

  protected ExplorerNode(Attachment attachment, ProgramStateTreeModel model, ExplorerNode parent) {
    this.attachment = attachment;
    this.model = model;
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

  public void fireChanged() {
    if (this.model != null) {
      this.model.nodesChanged(this.parent, new int[]{this.parent.getIndex(this)});
    }
  }
}
