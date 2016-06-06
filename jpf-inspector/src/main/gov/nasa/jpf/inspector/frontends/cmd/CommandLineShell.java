package gov.nasa.jpf.inspector.frontends.cmd;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.inspector.JPFInspectorFacade;
import gov.nasa.jpf.inspector.client.ExecutionContext;
import gov.nasa.jpf.inspector.client.JPFInspectorClientInterface;
import gov.nasa.jpf.inspector.common.Constants;
import gov.nasa.jpf.shell.Shell;
import gov.nasa.jpf.shell.ShellCommand;
import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.ShellPanel;

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

    boolean batchMode = config.getBoolean("jpf-inspector.batch-mode", false);
    boolean safeMode = config.getBoolean("jpf-inspector.safe-mode", true);
    boolean batchModeEchoInput =  batchMode && config.getBoolean("jpf-inspector.batch-mode.echo-input", false);

    Scanner scanner = new Scanner(System.in);
    JPFInspectorClientInterface inspector = JPFInspectorFacade.getInspectorClient(config.getTarget(), System.out);

    if (!batchMode) {
      System.out.println("This is the JPF Inspector console for debugging the target \"" + config.getTarget() + "\".");
      System.out.println("Type \"hello\" to test if the Inspector is working or \"help\" to get a list of commands.");
      System.out.print(Constants.PROMPT);
    }
    while (scanner.hasNextLine()) {
      String s = scanner.nextLine();
      String sTrimmed = s.trim();
      if (batchModeEchoInput) {
        System.out.println(Constants.PROMPT + sTrimmed);
      }
      if (sTrimmed.length() > 0) {
        inspector.executeCommand(s, ExecutionContext.FROM_COMMAND_LINE_TERMINAL);
      }
      if (!batchMode) {
        System.out.print(Constants.PROMPT);
      }
    }
  }


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
