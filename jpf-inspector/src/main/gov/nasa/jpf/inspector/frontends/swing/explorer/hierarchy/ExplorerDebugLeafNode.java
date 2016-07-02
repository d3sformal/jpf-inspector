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

import gov.nasa.jpf.inspector.frontends.swing.explorer.ProgramStateTreeModel;

/**
 * This is just a small node used for testing purposes.
 */
public class ExplorerDebugLeafNode extends ExplorerLeafNode {
  private final String name;

  @SuppressWarnings("unused") // Used only for debugging.
  public ExplorerDebugLeafNode(String name, ProgramStateTreeModel model, ExplorerNode parent) {
    super(null, model, parent);

    this.name = name;
  }
  @Override
  public String toString() {
    return name;
  }

  @Override
  public void updateFromJpf(ExplorerNode newVersion) {

  }

  @Override
  public boolean isRecognizableAs(ExplorerNode oldNode) {
    return false;
  }
}
