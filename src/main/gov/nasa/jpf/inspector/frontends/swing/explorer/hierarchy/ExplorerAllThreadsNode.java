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
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.ThreadList;

import java.util.ArrayList;

/**
 * Represents the single "All Threads" Explorer node.
 */
public class ExplorerAllThreadsNode extends ExplorerComplexNode {
  protected ExplorerAllThreadsNode(ProgramStateTreeModel model, ExplorerNode parent) {
    super(model, Attachment.irrelevant(), parent);
  }

  @Override
  protected ArrayList<ExplorerNode> populateChildren() {
    ArrayList<ExplorerNode> newChildren = new ArrayList<>();
    ThreadList threadList = model.getVM().getThreadList();
    for (ThreadInfo threadInfo : threadList) {
      newChildren.add(new ExplorerThreadInfoNode(threadInfo, model, this));
    }
    return newChildren;
  }

  @Override
  public void updateComplexNodeFromJpf(ExplorerNode newVersion) {
  }

  @Override
  public String toString() {
    return "Threads";
  }

  @Override
  public boolean isRecognizableAs(ExplorerNode oldNode) {
    return oldNode instanceof ExplorerAllThreadsNode;
  }
}
