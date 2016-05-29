package gov.nasa.jpf.inspector.frontends.cmd;

import gov.nasa.jpf.JPFShell;

/**
 *
 * This shell is used when launching the Inspector from the command-line. If you want the Inspector in a GUI,
 * use .shell.basicshell.BasicShell instead. Only set this as your shell if you want the Inspector in the default
 * system command line.
 *
 * Represents a shell that is wholly separate from the Swing-based shell in jpf-shell.
 * Because that abstract Shell must be based on JPanel and because the ShellManager handles only Shells and not
 * any implementations of JPFShell, we must redo most of the Shell's work from scratch here.
 *
 */
public class CommandLineShell implements JPFShell {
  @Override
  public void start(String[] args) {

  }
}
