package gov.nasa.jpf.inspector.frontends.swing.explorer;

import gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy.ExplorerNode;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class ProgramStateCellRenderer extends DefaultTreeCellRenderer {
  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                boolean leaf, int row, boolean hasFocus) {
    DefaultTreeCellRenderer item =
            (DefaultTreeCellRenderer) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
    if (value instanceof ExplorerNode) {
      ExplorerNode  node = (ExplorerNode)value;
      if (node.isWronglyExpanded()) {
        item.setText("[Access denied - JPF not paused] " + item.getText());
      }
      return item;
    } else {
      return item;
    }
  }
}
