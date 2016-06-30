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

public abstract class ExplorerComplexNode extends ExplorerNode {
  protected ArrayList<ExplorerNode> children;

  protected ExplorerComplexNode(ProgramStateTreeModel model, Attachment attachment, ExplorerNode parent) {
    super(attachment, model, parent);
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
  public final void updateFromJpf(ExplorerNode newVersion) {
    if (wronglyExpanded) {
      wronglyExpanded = false;
      children = null;
      model.nodeStructureChanged(this);
    }

    updateComplexNodeFromJpf(newVersion);

    if (children != null) {
      childrenNeedToBeUpdated();
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
          // Removal or change
          ExplorerNode oldChild = oldChildren.get(oldIndex);
          ExplorerNode newChild = newChildren.get(newIndex);
          if (newChild.isRecognizableAs(oldChild)) {
            oldChild.updateFromJpf(newChild);
            oldIndex++;
          } else {
            oldChildren.remove(oldIndex);
            model.nodesWereRemoved(this, new int[] { oldIndex }, new Object[] { oldChild});
            System.out.println("NODE REMOVED.");
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

  public abstract void updateComplexNodeFromJpf(ExplorerNode newVersion);

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

  @Override
  public void fireChanged() {
    super.fireChanged();
    if (children != null) {
      for (ExplorerNode child : children) {
        child.fireChanged();
      }
    }
  }
}
