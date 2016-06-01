package gov.nasa.jpf.inspector.frontends.cmd;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPFShell;
import gov.nasa.jpf.inspector.JPFInspectorFacade;
import gov.nasa.jpf.inspector.client.ExecutionContext;
import gov.nasa.jpf.inspector.client.JPFInspectorClientInterface;
import gov.nasa.jpf.inspector.common.Constants;
import gov.nasa.jpf.shell.Shell;
import gov.nasa.jpf.shell.ShellCommand;
import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.ShellPanel;

import java.io.IOException;
import java.util.Scanner;

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
public final class CommandLineShell extends Shell {
  @Override
  public void start(String[] args) {
    ShellManager.getManager().setStartingArgs(args);
    Config config = ShellManager.getManager().getConfig();
    Scanner scanner = new Scanner(System.in);
    JPFInspectorClientInterface inspector = JPFInspectorFacade.getInspectorClient(config.getTarget(), System.out);

    //noinspection InfiniteLoopStatement
    while (true) {
      System.out.print(Constants.PROMPT);
      String s = scanner.nextLine();

      String sTrimmed = s.trim();
      if (sTrimmed.length() > 0) {
        inspector.executeCommand(s, ExecutionContext.FROM_COMMAND_LINE_TERMINAL);
      }
    } // end of while
  }

  /**
   * The constructor called on by JPF when instantiating a shell. This is
   * equivalent to calling {@link #Shell(gov.nasa.jpf.Config, java.lang.String)}
   * with the arguments: config, TITLE
   * @param config the {@link gov.nasa.jpf.Config} object to be used for the
   * {@link gov.nasa.jpf.shell.ShellManager}.
   *
   */
  public CommandLineShell(Config config) {
    ShellManager.createShellManager(config);
    if (!ShellManager.getManager().hasShell(this)) {
      ShellManager.getManager().addShell(this);
      // We will handle when to exit the VM in ShellManager
    }
  }

  @Override
  public void installCommand(ShellCommand command) {

  }

  @Override
  public void uninstallCommand(ShellCommand command) {

  }

  @Override
  public void updateShellCommand(ShellCommand command) {

  }

  @Override
  public void updateShellPanel(ShellPanel panel) {

  }

  @Override
  public void requestFocus(ShellPanel panel) {

  }

  @Override
  public Shell createChildShell() {
    return null;
  }
}
