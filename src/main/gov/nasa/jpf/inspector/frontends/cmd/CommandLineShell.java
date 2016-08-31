//
// Copyright (C) 2016 Petr Hudeček
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

package gov.nasa.jpf.inspector.frontends.cmd;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.inspector.JPFInspectorFacade;
import gov.nasa.jpf.inspector.client.ExecutionContext;
import gov.nasa.jpf.inspector.common.Constants;
import gov.nasa.jpf.inspector.utils.InspectorConfiguration;
import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.commands.VerifyCommand;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * This shell is used when launching the Inspector from the command-line. If you want the Inspector in a GUI,
 * use .shell.basicshell.BasicShell instead. Only set this as your shell if you want the Inspector in the default
 * system command line.
 *
 * Represents a shell that is wholly separate from the Swing-based shell in jpf-shell.
 * Because that abstract Shell must be based on JPanel and because the ShellManager handles only Shells and not
 * any implementations of JPFShell, we must redo most of the Shell's work from scratch here.
 */
@SuppressWarnings({"NonSerializableFieldInSerializableClass", "serial"}) // This class is only serializable because it inherits from JFrame.
public final class CommandLineShell extends AbstractCommandLineBasedShell {

  private InputStream inputStream;
  private PrintStream outputStream;

  @Override
  public void mainLoop() {
    boolean batchMode = InspectorConfiguration.getInstance().isBatchModeActive();
    boolean batchModeEchoInput =  InspectorConfiguration.getInstance().shouldEchoInput();
    Config config = ShellManager.getManager().getConfig();

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

      if (!command.isEmpty()) {
        if (batchModeEchoInput) {
          outputStream.println(Constants.PROMPT + command);
        }
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

}
