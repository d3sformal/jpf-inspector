package gov.nasa.jpf.inspector.frontends.swing;

import gov.nasa.jpf.shell.ShellPanel;

/**
 * The source code view panel allows the user to view the source code of the file where the
 * currently executed instruction resides.
 */
public class SourceCodeViewPanel extends ShellPanel {
  private static final long serialVersionUID = -332650333255593274L;

  public SourceCodeViewPanel() {
    super("Source", null, "View the source code of the currently executed instruction.");
  }
}
