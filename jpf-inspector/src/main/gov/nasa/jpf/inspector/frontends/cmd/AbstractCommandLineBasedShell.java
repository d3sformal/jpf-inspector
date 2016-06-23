package gov.nasa.jpf.inspector.frontends.cmd;

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.inspector.client.JPFInspectorClientInterface;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.shell.Shell;
import gov.nasa.jpf.shell.ShellCommand;
import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.ShellPanel;
import gov.nasa.jpf.shell.commands.VerifyCommand;
import gov.nasa.jpf.shell.listeners.VerifyCommandListener;

/**
 * This is an abstract base class for two command-line-based shells:
 *
 * - One of them uses standard input and output;
 * - The other uses the JLine library which permits it some additonal features such as command history
 *
 * However, I never succeeded in completing the JLine shell.
 */
public abstract class AbstractCommandLineBasedShell  extends Shell implements VerifyCommandListener {
  private static final long serialVersionUID = -579285459191711234L;
  protected JPFInspectorClientInterface inspector;

  abstract void mainLoop();


  @Override
  public final void start(String[] args) {
    ShellManager.getManager().setStartingArgs(args);

    mainLoop();


  }




  @Override
  public final void installCommand(ShellCommand command) {
  }

  @Override
  public final  void uninstallCommand(ShellCommand command) {
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
