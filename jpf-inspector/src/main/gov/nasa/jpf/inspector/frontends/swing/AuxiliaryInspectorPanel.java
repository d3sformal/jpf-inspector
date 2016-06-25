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
  protected JPFInspectorClientInterface inspectorClient;

  protected AuxiliaryInspectorPanel(String title, Icon icon, String tip) {
    super(title, icon, tip);

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
