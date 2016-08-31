package gov.nasa.jpf.inspector.tests.acceptance.architecture;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.inspector.JPFInspectorFacade;
import gov.nasa.jpf.inspector.client.ExecutionContext;
import gov.nasa.jpf.inspector.client.JPFInspectorClientInterface;
import gov.nasa.jpf.inspector.frontends.cmd.AbstractCommandLineBasedShell;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.commands.VerifyCommand;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class InteractableTestShell extends AbstractCommandLineBasedShell {
  private ByteArrayOutputStream outputStream;
  private PrintStream printOutputStream;
  private PrintStream callbackOutputStream;


  @Override
  protected void mainLoop() {
    Config config = ShellManager.getManager().getConfig();
    inspector = JPFInspectorFacade.getInspectorClient(config.getTarget(), printOutputStream, callbackOutputStream);
    ShellManager.getManager().addCommandListener(VerifyCommand.class, this);
  }


  public InteractableTestShell(String appProperties, ByteArrayOutputStream outputStream, PrintStream callbackOutputStream) {
    this.outputStream = outputStream;
    this.printOutputStream = new PrintStream(outputStream, true);
    this.callbackOutputStream = callbackOutputStream;
    Config config = new Config(new String[]{appProperties});
    ShellManager.createShellManager(config);
    if (!ShellManager.getManager().hasShell(this)) {
      ShellManager.getManager().addShell(this);
      // We will handle when to exit the VM in ShellManager
    }
  }
  public String executeCommand(String command) {
    outputStream.reset();
    inspector.executeCommand(command, ExecutionContext.FROM_COMMAND_LINE_TERMINAL);
    String printed = outputStream.toString().trim();
    outputStream.reset();
    return printed;
  }

  public JPFInspectorClientInterface getClient() {
    return inspector;
  }
  public JPFInspectorBackEndInterface getServer() {
    return inspector.getServer();
  }
}
