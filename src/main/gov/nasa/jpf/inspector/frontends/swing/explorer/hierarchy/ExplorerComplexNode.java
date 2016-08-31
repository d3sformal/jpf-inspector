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

import gov.nasa.jpf.inspector.frontends.swing.explorer.Attachment;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ProgramStateTreeModel;

import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

/**
 * Base class for nodes of the visual Explorer treeview that have children.
 */
public abstract class ExplorerComplexNode extends ExplorerNode {
  /**
   * Child nodes of this node. For threads, these are stack frames. For objects, these are fields etc.
   */
  protected ArrayList<ExplorerNode> children;

  protected ExplorerComplexNode(ProgramStateTreeModel model, Attachment attachment, ExplorerNode parent) {
    super(attachment, model, parent);
  }

  /**
   * A non-leaf Explorer node must override this method and then use the {@link #model} to discover what its children
   * should be, create them and return them. Only direct children should be created, not their descendants.
   *
   * It is guaranteed that when this method is called, JPF is stopped.
   */
  protected abstract ArrayList<ExplorerNode> populateChildren();

  private void ensureChildrenArePopulated() {
    if (children == null) {
      if (model.getServer().preventJpfFromResuming()) {
        try {
          children = populateChildren();
        } finally {
          model.getServer().permitJpfToResumeAgain();
        }
      } else {
        children = new ArrayList<>();
        this.wronglyExpanded = true;
        this.model.nodesChanged(parent, new int[]{parent.getIndex(this)});
      }
    }
  }

  @Override
  public final void updateFromJpf(ExplorerNode newVersion) {
    if (wronglyExpanded) {
      wronglyExpanded = false;
      children = null;
      model.nodeStructureChanged(this);
    }

    updateComplexNodeFromJpf(newVersion);

    if (children != null) {
      // Children exist, therefore they need to be updated, too.

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
            // Removal or change
            ExplorerNode oldChild = oldChildren.get(oldIndex);
            ExplorerNode newChild = newChildren.get(newIndex);
            if (newChild.isRecognizableAs(oldChild)) {
              oldChild.updateFromJpf(newChild);
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
  }

  /**
   * This is called from {@link #updateFromJpf(ExplorerNode)} only. A subclass must override this and modify itself
   * based on the value of its new version that was created based on the new state of JPF. This is guaranteed to run
   * only in response to a command or a callback, and only when JPF is stopped.
   *
   * @param newVersion New version of this node, based on an up-to-date JPF state.
   */
  protected abstract void updateComplexNodeFromJpf(ExplorerNode newVersion);

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
