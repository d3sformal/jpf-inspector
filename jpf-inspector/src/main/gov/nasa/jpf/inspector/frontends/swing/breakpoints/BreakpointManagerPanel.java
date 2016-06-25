package gov.nasa.jpf.inspector.frontends.swing.breakpoints;

import gov.nasa.jpf.inspector.client.ExecutionContext;
import gov.nasa.jpf.inspector.frontends.swing.AuxiliaryInspectorPanel;
import gov.nasa.jpf.inspector.interfaces.BreakpointState;
import gov.nasa.jpf.inspector.interfaces.BreakpointStatus;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * The breakpoint manager panel allows the user to view and delete breakpoints.
 */
public class BreakpointManagerPanel extends AuxiliaryInspectorPanel {
  private static final long serialVersionUID = -5461391946280194091L;
  private final JButton buttonEnable;
  private final JButton buttonLogging;
  private final JButton buttonDisable;
  private final JButton buttonDelete;
  private final BreakpointsListModel breakpointsListModel;
  private final JList<BreakpointStatus> breakpointStatusJList;

  public BreakpointManagerPanel() {
    super("Breakpoints", null, "View and delete Inspector breakpoints.");

    // Create layout
    breakpointsListModel = new BreakpointsListModel();
    breakpointStatusJList = new JList<>(breakpointsListModel);
    breakpointStatusJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    breakpointStatusJList.setCellRenderer(new BreakpointCellRenderer());
    breakpointStatusJList.addListSelectionListener(this::listSelectionChanged);


    JPanel buttons = new JPanel(new FlowLayout());
    buttonEnable = new JButton("Enable");
    buttonLogging = new JButton("Logging");
    buttonDisable = new JButton("Disable");
    buttonDelete = new JButton("Delete");
    buttonEnable.setEnabled(false);
    buttonEnable.addActionListener((actionEvent) -> this.changeBreakpointStatus(BreakpointState.ENABLED));
    buttonLogging.setEnabled(false);
    buttonLogging.addActionListener((actionEvent) -> this.changeBreakpointStatus(BreakpointState.LOGGING));
    buttonDisable.setEnabled(false);
    buttonDisable.addActionListener((actionEvent) -> this.changeBreakpointStatus(BreakpointState.DISABLED));
    buttonDelete.setEnabled(false);
    buttonDelete.addActionListener(this::deleteBreakpoint);

    buttons.add(buttonEnable);
    buttons.add(buttonLogging);
    buttons.add(buttonDisable);
    buttons.add(buttonDelete);
    setLayout(new BorderLayout());
    add(breakpointStatusJList, BorderLayout.CENTER);
    add(buttons, BorderLayout.SOUTH);
  }

  private void changeBreakpointStatus(BreakpointState newState) {
    java.util.List<BreakpointStatus> statuses = breakpointStatusJList.getSelectedValuesList();
    for (BreakpointStatus bpStatus : statuses) {
      inspectorClient.getServer().changeBreakpointState(bpStatus.getBPID(), newState);
    }
    breakpointsListModel.update(inspectorClient.getServer().getBreakpoints());
    listSelectionChanged(null);
  }

  private void listSelectionChanged(ListSelectionEvent listSelectionEvent) {
    java.util.List<BreakpointStatus> statuses = breakpointStatusJList.getSelectedValuesList();
    boolean canEnable = false;
    boolean canLog = false;
    boolean canDisable = false;
    boolean canDelete = false;
    for (BreakpointStatus status : statuses) {
      canDelete = true;
      switch (status.getState()) {
        case DISABLED: canEnable = true; canLog = true; break;
        case ENABLED: canDisable = true; canLog = true; break;
        case LOGGING: canEnable = true; canDisable = true; break;
      }
    }
    buttonEnable.setEnabled(canEnable);
    buttonDelete.setEnabled(canDelete);
    buttonDisable.setEnabled(canDisable);
    buttonLogging.setEnabled(canLog);
  }

  private void deleteBreakpoint(ActionEvent actionEvent) {
    java.util.List<BreakpointStatus> statuses = breakpointStatusJList.getSelectedValuesList();
    for (BreakpointStatus bpStatus : statuses) {
      inspectorClient.executeCommand("delete breakpoint " + bpStatus.getBPID(), ExecutionContext.FROM_SWING_TERMINAL);
    }
  }

  @Override
  protected void commandExecutedOrCallbackReceived() {
    breakpointsListModel.update(this.inspectorClient.getServer().getBreakpoints());
    listSelectionChanged(null);
  }
}
