package gov.nasa.jpf.inspector.frontends.cmd;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.inspector.JPFInspectorFacade;
import gov.nasa.jpf.inspector.client.ExecutionContext;
import gov.nasa.jpf.inspector.client.JPFInspectorClientInterface;
import gov.nasa.jpf.inspector.common.Constants;
import gov.nasa.jpf.inspector.utils.InspectorConfiguration;
import gov.nasa.jpf.shell.Shell;
import gov.nasa.jpf.shell.ShellCommand;
import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.ShellPanel;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
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
  private InputStream inputStream;
  private PrintStream outputStream;
  @Override
  public void start(String[] args) {
    ShellManager.getManager().setStartingArgs(args);
    Config config = ShellManager.getManager().getConfig();

    boolean batchMode = InspectorConfiguration.getInstance().isBatchModeActive();
    boolean batchModeEchoInput =  InspectorConfiguration.getInstance().shouldEchoInput();

    Scanner scanner = new Scanner(inputStream);
    JPFInspectorClientInterface inspector = JPFInspectorFacade.getInspectorClient(config.getTarget(), outputStream);

    if (!batchMode) {
      outputStream.println("This is the JPF Inspector console for debugging the target \"" + config.getTarget() + "\".");
      outputStream.println("Type \"hello\" to test if the Inspector is working or \"help\" to get a list of commands.");
      outputStream.print(Constants.PROMPT);
    }
    while (scanner.hasNextLine()) {
      String s = scanner.nextLine();
      String sTrimmed = s.trim();
      if (batchModeEchoInput) {
        outputStream.println(Constants.PROMPT + sTrimmed);
      }
      if (sTrimmed.length() > 0) {
        inspector.executeCommand(s, ExecutionContext.FROM_COMMAND_LINE_TERMINAL);
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      if (!batchMode) {
        System.out.print(Constants.PROMPT);
      }
    }
  }


  /**
   * This is called by the JPF when executing via RunJPF
   */
  public CommandLineShell(Config config) {
    ShellManager.createShellManager(config);
    if (!ShellManager.getManager().hasShell(this)) {
      ShellManager.getManager().addShell(this);
      // We will handle when to exit the VM in ShellManager
    }
    this.inputStream = System.in;
    this.outputStream = System.out;
  }

  /**
   * This is called by the tests.
   *
   */
  public CommandLineShell(String appProperties, InputStream inputStream, PrintStream outputStream) {
    if (ShellManager.isShellManagerSet()) {
      ShellManager.getManager().reloadAppProperties(appProperties);
    } else {
      Config config = new Config(new String[] { appProperties });
      ShellManager.createShellManager(config);
      if (!ShellManager.getManager().hasShell(this)) {
        ShellManager.getManager().addShell(this);
        // We will handle when to exit the VM in ShellManager
      }
    }

    this.inputStream = inputStream;
    this.outputStream = outputStream;
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
