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
import gov.nasa.jpf.inspector.server.programstate.StateWritableValue;
import gov.nasa.jpf.vm.ClassLoaderInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.Statics;

import java.util.ArrayList;

/**
 * Represents the single "Static Area" node that contains static representations of all classes loaded by the
 * current class loader.
 */
public class ExplorerStaticAreaNode extends ExplorerComplexNode {
  /**
   * Initializes the single "Static Area" node.
   * @param model The tree model.
   * @param explorerRoot The parent of this node.
   */
  public ExplorerStaticAreaNode(ProgramStateTreeModel model, ExplorerRoot explorerRoot) {
    super(model, Attachment.irrelevant(), explorerRoot);
  }

  @Override
  protected ArrayList<ExplorerNode> populateChildren() {
    ArrayList<ExplorerNode> newChildren = new ArrayList<>();
    Statics statics = ClassLoaderInfo.getCurrentClassLoader().getStatics();
    for (ElementInfo elementInfo : statics) {
      String typeName = StateWritableValue.demangleTypeName(elementInfo.getType());
      newChildren.add(new ExplorerJavaObjectNode(Attachment.staticAreaEntry(typeName),
                                              elementInfo, model, this));
    }
    return newChildren;
  }

  @Override
  public void updateComplexNodeFromJpf(ExplorerNode newVersion) {
    // Nothing is needed.
  }

  @Override
  public String toString() {
    return "Statics";
  }

  @Override
  public boolean isRecognizableAs(ExplorerNode oldNode) {
    return oldNode instanceof ExplorerStaticAreaNode;
  }
}
