package gov.nasa.jpf.inspector.frontends.swing.explorer;

import gov.nasa.jpf.inspector.common.Constants;
import gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy.ExplorerNode;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * Renders elements of the Explorer tree view. This is responsible for adding icons and additional text, and fonts.
 */
public class ProgramStateCellRenderer extends DefaultTreeCellRenderer {
  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                boolean leaf, int row, boolean hasFocus) {
    String stringToDisplay = value.toString();
    if (value instanceof ExplorerNode) {
      ExplorerNode node = (ExplorerNode) value;
      if (node.isWronglyExpanded()) {
        stringToDisplay = "[Access denied - JPF not paused] " + stringToDisplay;
      }
    }

    DefaultTreeCellRenderer item =
            (DefaultTreeCellRenderer) super.getTreeCellRendererComponent(tree, stringToDisplay, sel, expanded, leaf, row, hasFocus);
    item.setFont(Constants.fontMonospaced);
    item.setToolTipText(stringToDisplay);
    return item;
  }
}
