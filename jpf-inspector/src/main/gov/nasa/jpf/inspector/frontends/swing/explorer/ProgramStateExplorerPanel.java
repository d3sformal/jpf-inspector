package gov.nasa.jpf.inspector.frontends.swing.explorer;

import gov.nasa.jpf.inspector.frontends.swing.AuxiliaryInspectorPanel;
import gov.nasa.jpf.shell.ShellPanel;

import javax.swing.*;
import java.awt.*;

/**
 * The Program State Explorer panels allows the user to view values of all variables on the heap,
 * in the threads and in the static area, visually.
 */
public class ProgramStateExplorerPanel extends AuxiliaryInspectorPanel {
  public ProgramStateExplorerPanel() {
    super("Explorer", null, "View all Java objects in the current program state.");

    setLayout(new BorderLayout());
    ProgramStateTreeModel model = new ProgramStateTreeModel();
    JTree jTree = new JTree(model);
    JScrollPane treeView = new JScrollPane(jTree);
    add(treeView, BorderLayout.CENTER);
  }

  @Override
  protected void commandExecutedOrCallbackReceived() {

  }
}
