package gov.nasa.jpf.inspector.frontends.cmd;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.inspector.client.ExecutionContext;
import gov.nasa.jpf.inspector.common.Constants;
import gov.nasa.jpf.shell.ShellManager;
import jline.ConsoleReader;

import java.io.IOException;
import java.io.PrintStream;

/**
 * This was a half-hearted attempt at supporting a better command-line console using JLine.
 * However, JLine0 is old, unmaintained, barely documented and we are using a custom version of it. It is in this customized
 * portion of the library that this new class fails.
 *
 * Implementing JLine is probably more trouble than it's worth and I recommend you use {@link CommandLineShell} instead
 * which uses standard input and output streams.
 *
 * I'll leave this class in here for now if at some point somebody wishes to finish this.
 */
public class JLineCommandLineShell extends AbstractCommandLineBasedShell {
  private static final long serialVersionUID = 4399206883405403106L;

  /**
   * This is called by the JPF when executing via RunJPF
   */
  @SuppressWarnings("unused") // Called from JPF via Reflection
  public JLineCommandLineShell(Config config) {
    ShellManager.createShellManager(config);
    if (!ShellManager.getManager().hasShell(this)) {
      ShellManager.getManager().addShell(this);
      // We will handle when to exit the VM in ShellManager
    }
  }

  @Override
  void mainLoop() {
    try {

      Config config = ShellManager.getManager().getConfig();
      String target = config.getTarget();
      ConsoleReader consoleReader;
      PrintStream outStream = System.out;
      consoleReader = new ConsoleReader();

      consoleReader.setBellEnabled(false);
      consoleReader.setUseHistory(true);

      consoleReader.printString("This is the JPF Inspector console for debugging the target \"" + target + "\".");
    /*
    outStream.println("Type \"hello\" to test if the Inspector is working or \"help\" to get a list of commands.");
    outStream.println();*/

      //noinspection InfiniteLoopStatement
      while (true) {
        String input;
        try {
          input = consoleReader.readLine(Constants.PROMPT);
          if (input.trim().isEmpty()) {
            outStream.println("EMPTY");
          } else {
            inspector.executeCommand(input.trim(), ExecutionContext.FROM_COMMAND_LINE_TERMINAL);
          }
        } catch (IOException e) {
          outStream.println("ERR: Error while reading a command.");
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("There was an unknown JLine error.");
    }
  }
}
