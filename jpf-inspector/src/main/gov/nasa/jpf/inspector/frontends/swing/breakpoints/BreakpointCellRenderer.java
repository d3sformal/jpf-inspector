package gov.nasa.jpf.inspector.frontends.swing.breakpoints;

import gov.nasa.jpf.inspector.client.commands.CmdBreakpointShow;
import gov.nasa.jpf.inspector.interfaces.BreakpointStatus;

import javax.swing.*;
import java.awt.*;


/**
 * Renders a breakpoint inside the JList.
 */
public class BreakpointCellRenderer extends DefaultListCellRenderer {
  @Override
  public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    setText(CmdBreakpointShow.breakpointToString((BreakpointStatus) value));
    return this;
  }
}
