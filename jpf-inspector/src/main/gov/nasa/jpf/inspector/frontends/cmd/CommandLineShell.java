package gov.nasa.jpf.inspector.frontends.cmd;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.inspector.JPFInspectorFacade;
import gov.nasa.jpf.inspector.client.ExecutionContext;
import gov.nasa.jpf.inspector.client.JPFInspectorClientInterface;
import gov.nasa.jpf.inspector.common.Constants;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.utils.InspectorConfiguration;
import gov.nasa.jpf.shell.Shell;
import gov.nasa.jpf.shell.ShellCommand;
import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.ShellPanel;
import gov.nasa.jpf.shell.commands.VerifyCommand;
import gov.nasa.jpf.shell.listeners.VerifyCommandListener;

import java.io.InputStream;
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
@SuppressWarnings({"NonSerializableFieldInSerializableClass", "serial"}) // This class is only serializable because it inherits from JFrame.
public final class CommandLineShell extends Shell implements VerifyCommandListener {
  private InputStream inputStream;
  private PrintStream outputStream;
  private JPFInspectorClientInterface inspector;
  @Override
  public void start(String[] args) {
    ShellManager.getManager().setStartingArgs(args);
    Config config = ShellManager.getManager().getConfig();

    boolean batchMode = InspectorConfiguration.getInstance().isBatchModeActive();
    boolean batchModeEchoInput =  InspectorConfiguration.getInstance().shouldEchoInput();

    Scanner scanner = new Scanner(inputStream);
    inspector = JPFInspectorFacade.getInspectorClient(config.getTarget(), outputStream);
    ShellManager.getManager().addCommandListener(VerifyCommand.class, this);

    if (!batchMode) {
      outputStream.println("This is the JPF Inspector console for debugging the target \"" + config.getTarget() + "\".");
      outputStream.println("Type \"hello\" to test if the Inspector is working or \"help\" to get a list of commands.");
      outputStream.print(Constants.PROMPT);
    }
    while (scanner.hasNextLine()) {
      String command = scanner.nextLine().trim();
      if (batchModeEchoInput) {
        outputStream.println(Constants.PROMPT + command);
      }

      if (!command.isEmpty()) {
        inspector.executeCommand(command, ExecutionContext.FROM_COMMAND_LINE_TERMINAL);
      }

      if (!batchMode) {
        outputStream.print(Constants.PROMPT);
      }
    }
    // No more commands will be given.
    // Let's terminate JPF then.
    if (inspector.isPaused()) {
      inspector.executeCommand("wait", ExecutionContext.FROM_COMMAND_LINE_TERMINAL);
      inspector.executeCommand("terminate", ExecutionContext.FROM_COMMAND_LINE_TERMINAL);
    }
  }


  /**
   * This is called by the JPF when executing via RunJPF
   */
  @SuppressWarnings("unused") // Called from JPF via Reflection
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
    assert !ShellManager.isShellManagerSet();

    Config config = new Config(new String[]{appProperties});
    ShellManager.createShellManager(config);
    if (!ShellManager.getManager().hasShell(this)) {
      ShellManager.getManager().addShell(this);
      // We will handle when to exit the VM in ShellManager
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

  @Override
  public void afterJPFInit(VerifyCommand command) {
    JPF jpf = command.getJPF();
    try {
      inspector.connect2JPF(jpf);
    } catch (JPFInspectorGenericErrorException ignored) {
      // Silently ignore - error is reported in connect2JPF method
    }
  }

  @Override
  public void exceptionDuringVerify(Exception ex) {
    ex.printStackTrace();
  }

  @Override
  public void preCommand(VerifyCommand command) {

  }

  @Override
  public void postCommand(VerifyCommand command) {

  }
}
