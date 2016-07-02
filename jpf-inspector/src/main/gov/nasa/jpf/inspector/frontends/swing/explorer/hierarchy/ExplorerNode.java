//
// Copyright (C) 2016 Petr Hudeček
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import gov.nasa.jpf.inspector.frontends.swing.Icons;
import gov.nasa.jpf.inspector.frontends.swing.explorer.Attachment;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ProgramStateTreeModel;

import javax.swing.*;
import javax.swing.tree.TreeNode;

/**
 * Represents a node in the graphical Explorer's tree view.
 */
public abstract class ExplorerNode implements TreeNode {
  /**
   * The parent node of this node.
   */
  protected final ExplorerNode parent;
  /**
   * The way in which this node is connected to its parent.
   */
  protected Attachment attachment;
  /**
   * A node is "wrongly expanded" if the user attempted to expand it but JPF was not running or was not paused
   * at the time the expansion occurred, therefore the list of children could not be retrieved.
   *
   * A node that is wrongly expanded displays a warning message to the user.
   */
  protected boolean wronglyExpanded;
  /**
   * The tree model that this node belongs to.
   */
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

  /**
   * This method is called by the event dispatch thread after the client informs it, via {@link SwingUtilities#invokeLater(Runnable)}, that program state may have changed and that it's possible that JPF
   * is stopped now. What is guaranteed is that this method is only called in response to a command completion
   * or a callback. In addition, it is guaranteed that when this method runs, JPF is stopped.
   *
   * @param newVersion The way this node should look like according to the new JPF state
   */
  protected abstract void updateFromJpf(ExplorerNode newVersion);

  /**
   * Returns true if this node is just a newer version of the given node. For example, if this node represents
   * a Java array and an element of the array changes, the new array should still be recognizable as the old one.
   *
   * An object that changes its contents should remain recognizable. An object normally can't switch the class it's
   * represented by in the Explorer - so the instanceof checks are mostly just to be on the safe side. An object
   * that changes its attachment is no longer recognizable.
   *
   * @param oldNode An older version of this node.
   */
  protected abstract boolean isRecognizableAs(ExplorerNode oldNode);

  /**
   * Tells the treeview component that this node's display row has changed (i.e., its text changed).
   */
  protected void fireChanged() {
    if (this.model != null) {
      this.model.nodesChanged(this.parent, new int[]{this.parent.getIndex(this)});
    }
  }

  /**
   * Gets the icon that represents the relationship of this node to its parent.
   * @param expanded Whether this node is currently expanded.
   */
  public Icon getIcon(boolean expanded) {
    switch (attachment.getKind()) {
      case STACK_FRAME:
      case TOPMOST_STACK_FRAME:
        return Icons.stackFrame;
      case STACK_SLOT:
        return Icons.stackSlot;
      case ARRAY_ELEMENT:
        return Icons.instanceField;
      case INSTANCE_FIELD:
        return Icons.instanceField;
      case STATIC_FIELD:
        return Icons.staticField;
      case HEAP_ENTRY:
      case STATIC_AREA_ENTRY:
      case PARENTLESS:
      case UNSPECIFIED:
        if (expanded) {
          return Icons.folderOpen;
        } else {
          return Icons.folderClosed;
        }
    }
    throw new RuntimeException("This node has no icon.");
  }
}
