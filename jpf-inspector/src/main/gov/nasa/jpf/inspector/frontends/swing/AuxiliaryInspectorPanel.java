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

package gov.nasa.jpf.inspector.frontends.swing;

import gov.nasa.jpf.inspector.client.JPFInspectorClientInterface;
import gov.nasa.jpf.shell.ShellPanel;

import javax.swing.*;
import java.util.List;

/**
 * Base class for JPF Inspector panels other than the {@link InspectorPrimaryConsolePanel}. These panels
 * MUST be loaded only AFTER the primary console panel, because they are linked to the primary panel
 * when they are being added to shell.
 */
public abstract class AuxiliaryInspectorPanel extends ShellPanel {

  private boolean firstTimeAddedToShell = true;
  /**
   * This panel must register itself as a listener in the Inspector client to catch callback-received and command-executed
   * events.
   */
  protected JPFInspectorClientInterface inspectorClient;

  protected AuxiliaryInspectorPanel(String title, String tip) {
    super(title, null, tip);

  }
  @Override
  protected void addedToShell() {
    if (firstTimeAddedToShell) {
      if (!(linkWithPrimaryConsolePanel())) {
        throw new RuntimeException(
                "The primary console panel must be loaded before this panel ('" + this.getClass().getName() + "').");
      }
      firstTimeAddedToShell = false;
    }
  }

  private boolean linkWithPrimaryConsolePanel() {
    List<ShellPanel> panels = this.getShell().getPanels();
    for(ShellPanel panel : panels) {
      if (panel instanceof InspectorPrimaryConsolePanel) {
        InspectorPrimaryConsolePanel primaryConsolePanel = (InspectorPrimaryConsolePanel) panel;
        primaryConsolePanel.addAuxiliaryPanel(this);
        this.inspectorClient = primaryConsolePanel.getInspectorClient();
        return true;
      }
    }
    return false;
  }

  protected abstract void commandExecutedOrCallbackReceived();

  public void fireCommandExecutedOrCallbackReceived() {
    SwingUtilities.invokeLater(this::commandExecutedOrCallbackReceived);
  }
}
