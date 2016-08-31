//
// Copyright (C) 2010-2011 Pavel Jančík
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

package gov.nasa.jpf.inspector.frontends.swing;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.inspector.JPFInspectorFacade;
import gov.nasa.jpf.inspector.client.ExecutionContext;
import gov.nasa.jpf.inspector.client.JPFInspectorClientInterface;
import gov.nasa.jpf.inspector.common.Constants;
import gov.nasa.jpf.inspector.frontends.swing.terminal.SwingTerminal;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.utils.Debugging;
import gov.nasa.jpf.inspector.utils.InspectorConfiguration;
import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.ShellPanel;
import gov.nasa.jpf.shell.commands.VerifyCommand;
import gov.nasa.jpf.shell.listeners.VerifyCommandListener;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.logging.Logger;

import jline.ConsoleReader;

import javax.swing.*;

/**
 * This a panel for JPF Shell that contains the Inspector console.
 * This is the main entry point for JPF Inspector, if it is launched by means of a graphical shell.
 * 
 */
public class InspectorPrimaryConsolePanel extends ShellPanel implements VerifyCommandListener {
  private static final Logger log = Debugging.getLogger();
  private static final boolean DEBUG = false;
  private static final long serialVersionUID = 20110715L;

  /**
   * Console that handles user input (jLine from jline library)
   */
  private final ConsoleReader console;
  /**
   * Terminal that shows input and output
   */
  private final SwingTerminal terminal;
  /**
   * Stream where print output to user
   */
  private final PrintStream consolePrintStream;
  /**
   * The Inspector client.
   */
  private final JPFInspectorClientInterface inspectorClient;

  /**
   * Creates the JPFInspector Swing frontend main panel.
   */
  public InspectorPrimaryConsolePanel() {
    super("JPF Inspector", null, "JPF Inspector command console");
    Config config = ShellManager.getManager().getConfig();
    String target = config.getTarget();

    // Create a terminal with specified theme
    if (config.get("jpf-inspector.theme").equals("white_on_black")) {
      terminal = SwingTerminal.getSwingTerminalBlack();
      log.info("JPF Inspector theme: white-on-black.");
    } else if (config.get("jpf-inspector.theme").equals("black_on_white")) {
      terminal = SwingTerminal.getSwingTerminalWhite();
      log.info("JPF Inspector theme: black-on-white.");
    }
    else {
      terminal = SwingTerminal.getSwingTerminalWhite();
      log.warning("Theme '" + config.get("jpf-inspector.theme") + "' not recognized. Defaulting to black-on-white.");
    }

    // Make sure the terminal is selected whenever we are on this panel
    addComponentListener(new PanelComponentListener());

    // Prepare communication streams
    consolePrintStream = terminal.getUserTextPrintStream();
    PrintStream startingInfoStream = terminal.getSimplePrintStream();
    console = terminal.getConsoleReader();

    // Initialize server part
    inspectorClient = JPFInspectorFacade.getInspectorClient(target, consolePrintStream);

    // Register InspectorPrimaryConsolePanel as a Verify command listener
    ShellManager.getManager().addCommandListener(VerifyCommand.class, this);

    // Set up the layout
    this.setLayout(new BorderLayout());
    add(terminal.getScrollPanel());

    // Say hello to the user
    startingInfoStream.println("This is the JPF Inspector console for debugging the target \"" + target + "\".");
    startingInfoStream.println("Type \"hello\" to test if the Inspector is working or \"help\" to get a list of commands.");
    startingInfoStream.println();

    // Set up quick commands
    setUpQuickCommands();

    // Start up the command execution thread
    Thread t = new CommandProcessingThread();
    t.start();
  }

  private void setUpQuickCommands() {
    @SuppressWarnings("ConstantConditions")
    InspectorToolbarCommand[] commands = {
            new InspectorToolbarCommand("Backstep Out", getQuickCommandIcon("BackStepOut"),
                                        "Undoes everything in this methods and backsteps out to the caller.", "back_step_out",
                                        getInspectorClient(), consolePrintStream),
            new InspectorToolbarCommand("Backstep Over",getQuickCommandIcon("BackStepOver"),
                                        "Undoes the previous line of code.", "back_step_over",
                                        getInspectorClient(), consolePrintStream),
            new InspectorToolbarCommand("Backstep In", getQuickCommandIcon("BackStepIn"),
                                        "Steps back into the previous method call.", "back_step_in",
                                        getInspectorClient(), consolePrintStream),
            new InspectorToolbarCommand("Backstep Instruction", getQuickCommandIcon("BackStep"),
                                        "Undoes the last bytecode instruction.", "back_step_instruction",
                                        getInspectorClient(), consolePrintStream),
            new InspectorToolbarCommand("Continue", getQuickCommandIcon("Continue"),
                                        "Resumes execution of JPF.", "continue",
                                        getInspectorClient(), consolePrintStream),
            new InspectorToolbarCommand("Step Instruction", getQuickCommandIcon("Step"),
                                        "Steps one bytecode instruction forward.", "step_instruction",
                                        getInspectorClient(), consolePrintStream),
            new InspectorToolbarCommand("Step In", getQuickCommandIcon("StepIn"),
                                        "Steps into the next method call.", "step_in",
                                        getInspectorClient(), consolePrintStream),
            new InspectorToolbarCommand("Step Over", getQuickCommandIcon("StepOver"),
                                        "Steps over this line of code.", "step_over",
                                        getInspectorClient(), consolePrintStream),
            new InspectorToolbarCommand("Step Out", getQuickCommandIcon("StepOut"),
                                        "Steps out of the current method.", "step_out",
                                        getInspectorClient(), consolePrintStream)
    };
    for (InspectorToolbarCommand command : commands) {
      ShellManager.getManager().addCommand(command);
    }
    for (InspectorToolbarCommand command : InspectorConfiguration.getInstance().getCustomToolBarCommands(getInspectorClient(), consolePrintStream)) {
      ShellManager.getManager().addCommand(command);
    }
  }

  private static Icon getQuickCommandIcon(String filename) {
    URL url = InspectorPrimaryConsolePanel.class.getResource("icons/" + filename + ".png");
    ImageIcon imageIcon = new ImageIcon(url);
    return imageIcon;
  }

  public void addAuxiliaryPanel(AuxiliaryInspectorPanel auxiliaryInspectorPanel) {
    getInspectorClient().addInspectorListener(auxiliaryInspectorPanel);
  }

  /**
   * The Inspector client.
   */
  public JPFInspectorClientInterface getInspectorClient() {
    return inspectorClient;
  }

  /**
   * Main command processing thread with main command loop.
   * 
   * Parses and executes user commands.
   * 
   */
  private class CommandProcessingThread extends Thread {
    public CommandProcessingThread () {
      super(CommandProcessingThread.class.getSimpleName());
    }

    @Override
    public void run () {
      //noinspection InfiniteLoopStatement
      while (true) {
        try {
          String s = console.readLine(Constants.PROMPT);
          if (s.trim().isEmpty()) {
            consolePrintStream.println("\n" + Constants.PROMPT);
          } else {
            consolePrintStream.println();
            getInspectorClient().executeCommand(s.trim(), ExecutionContext.FROM_SWING_TERMINAL);
          }
        } catch (IOException e) {
          consolePrintStream.println("ERR: Error while reading a command.");
        }
      } // end of while
    }
  }

  @Override
  public void afterJPFInit (VerifyCommand command) {
    if (DEBUG) {
      System.out.println(InspectorPrimaryConsolePanel.class.getSimpleName() + ".afterJPFInit(command=" + command + " )");
    }
    JPF jpf = command.getJPF();
    try {
      getInspectorClient().connect2JPF(jpf);
    } catch (JPFInspectorGenericErrorException ignored) {
      // Silently ignore - error is reported in connect2JPF method
    }
  }

  @Override
  public void postCommand (VerifyCommand command) {
    // Nothing to do
  }
  @Override
  public void preCommand (VerifyCommand command) {
    if (InspectorConfiguration.getInstance().shouldRequestFocusOnVerify()) {
      requestShellFocus();
    }
  }
  @Override
  public void exceptionDuringVerify (Exception ex) {
    ex.printStackTrace();
  }

  /**
   * When JPFInspector panel is selected, this sets focus directly on terminal.
   */
  private class PanelComponentListener implements ComponentListener {

    @Override
    public void componentShown (ComponentEvent e) {
      // Automatically set focus to the terminal
      terminal.getPane().requestFocusInWindow();
    }

    @Override
    public void componentHidden (ComponentEvent e) {
    }

    @Override
    public void componentMoved (ComponentEvent e) {
    }

    @Override
    public void componentResized (ComponentEvent e) {
    }
  }

}
