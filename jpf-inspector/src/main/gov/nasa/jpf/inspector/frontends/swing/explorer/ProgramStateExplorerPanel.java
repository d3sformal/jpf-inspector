package gov.nasa.jpf.inspector.frontends.swing.explorer;

import gov.nasa.jpf.inspector.frontends.swing.AuxiliaryInspectorPanel;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import java.awt.*;

/**
 * The Program State Explorer panels allows the user to view values of all variables on the heap,
 * in the threads and in the static area, visually.
 */
public class ProgramStateExplorerPanel extends AuxiliaryInspectorPanel {

  private ProgramStateTreeModel model;
  private final JTree jTree;

  public ProgramStateExplorerPanel() {
    super("Explorer", null, "View all Java objects in the current program state.");

    setLayout(new BorderLayout());
    jTree = new JTree();
    jTree.setRootVisible(false);
    jTree.setShowsRootHandles(true);
    jTree.setCellRenderer(new ProgramStateCellRenderer());
    jTree.addTreeExpansionListener(new TreeExpansionListener() {
      @Override
      public void treeExpanded(TreeExpansionEvent event) {

      }

      @Override
      public void treeCollapsed(TreeExpansionEvent event) {

      }
    });
    JScrollPane treeView = new JScrollPane(jTree);
    add(treeView, BorderLayout.CENTER);
  }

  private boolean firstTimeAddedToShell = true;
  @Override
  protected void addedToShell() {
    super.addedToShell();
    if (firstTimeAddedToShell) {
      model = new ProgramStateTreeModel(inspectorClient.getServer());
      jTree.setModel(model);
    }
    // Here, we could automatically expand some path if that would be useful for users.
    firstTimeAddedToShell = false;
  }



  @Override
  protected void commandExecutedOrCallbackReceived() {

    model.update();
  }
}
