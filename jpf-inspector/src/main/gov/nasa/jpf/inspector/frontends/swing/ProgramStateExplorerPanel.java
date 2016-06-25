package gov.nasa.jpf.inspector.frontends.swing;

import gov.nasa.jpf.shell.ShellPanel;

/**
 * The Program State Explorer panels allows the user to view values of all variables on the heap,
 * in the threads and in the static area, visually.
 */
public class ProgramStateExplorerPanel extends ShellPanel {
  private static final long serialVersionUID = -332650333255593274L;

  public ProgramStateExplorerPanel() {
    super("Explorer", null, "View all Java objects in the current program state.");
  }
}
