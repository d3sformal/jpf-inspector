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
import java.util.Enumeration;

/**
 * Base class for those nodes of the Program State Explorer that have no children.
 * These nodes are: {@link ExplorerPrimitiveNode} and {@link ExplorerDebugLeafNode}.
 */
public abstract class ExplorerLeafNode extends ExplorerNode {
  protected ExplorerLeafNode(Attachment attachment, ProgramStateTreeModel model, ExplorerNode parent) {
    super(attachment, model, parent);
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
  public final Enumeration<ExplorerNode> children() {
    return null;
  }
}
