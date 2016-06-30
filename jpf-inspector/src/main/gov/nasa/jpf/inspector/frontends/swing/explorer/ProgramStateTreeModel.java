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

package gov.nasa.jpf.inspector.frontends.swing.explorer;

import gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy.ExplorerRoot;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.vm.VM;

import javax.swing.tree.DefaultTreeModel;

/**
 * The Explorer's tree model maintains the Explorer's view of the current program state.
 *
 * In the beginning, only the root nodes exist, but as the user expands nodes, new children are created. Nodes
 * are never removed, except when their parent ceases to exist (for example, because it was a field whose value was
 * overwritten. Collapsing a node does not delete its children.
 */
public class ProgramStateTreeModel extends DefaultTreeModel {
  private ExplorerRoot explorerRoot;
  private JPFInspectorBackEndInterface server;

  /**
   * Creates the model and initializes it.
   * @param server The Inspector server.
   */
  public ProgramStateTreeModel(JPFInspectorBackEndInterface server) {
    super(new ExplorerRoot(), true);
    // The explorer root also needs access to this object. However, we cannot pass this object in the root's
    // parameter, because Java forbids passing "this" to superclass constructors. This is why we need an initialize
    // method.
    this.server = server;
    this.explorerRoot = (ExplorerRoot)this.root;
    ((ExplorerRoot)this.root).initialize(this);
  }

  /**
   * Gets the virtual machine currently used by the Inspector server.
   */
  public VM getVM() {
    return server.getVM();
  }

  /**
   * This method is called whenever the entire tree must be rechecked for validity. This happens most of the time
   * after a callback is received or a command is executed. At this time, if the server is currently paused (it usually
   * is after a callback), any nodes that are no longer valid are removed or recalculated and redrawn, as appropriate.
   */
  public void update() {
    if (server.isPaused()) {
      this.explorerRoot.updateFromJpf(this.explorerRoot);
    }
  }

  /**
   * Gets the Inspector server.
   */
  public JPFInspectorBackEndInterface getServer() {
    return server;
  }

  boolean wasConnectedLastTime = false;
  public boolean isConnectedToVM() {
    boolean nowPaused = server.isPaused();
    if (nowPaused && !wasConnectedLastTime) {
      wasConnectedLastTime = true;
      update();
    }
    if (!nowPaused && wasConnectedLastTime) {
      wasConnectedLastTime = false;
      explorerRoot.fireChanged();
    }
    return server.isPaused();
  }
}
