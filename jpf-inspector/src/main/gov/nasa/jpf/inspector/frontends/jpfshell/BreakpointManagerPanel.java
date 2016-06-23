package gov.nasa.jpf.inspector.frontends.jpfshell;

import gov.nasa.jpf.shell.ShellPanel;

/**
 * The breakpoint manager panel allows the user to view and delete breakpoints.
 */
public class BreakpointManagerPanel extends ShellPanel {
  private static final long serialVersionUID = -5461391946280194091L;

  public BreakpointManagerPanel() {
    super("Breakpoints", null, "View and delete Inspector breakpoints.");
  }
}
