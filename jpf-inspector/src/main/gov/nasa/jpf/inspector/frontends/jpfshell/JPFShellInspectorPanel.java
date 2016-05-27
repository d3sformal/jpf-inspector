//
// Copyright (C) 2010 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
// 
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
// 
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//  

package gov.nasa.jpf.inspector.frontends.jpfshell;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.inspector.JPFInspectorFacade;
import gov.nasa.jpf.inspector.client.JPFInspectorClientInterface;
import gov.nasa.jpf.inspector.frontends.jpfshell.commands.completors.CmdRunCompletor;
import gov.nasa.jpf.inspector.frontends.jpfshell.gui.SwingTerminal;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.utils.Debugging;
import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.ShellPanel;
import gov.nasa.jpf.shell.commands.VerifyCommand;
import gov.nasa.jpf.shell.listeners.VerifyCommandListener;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import jline.ConsoleReader;

/**
 * JPF Shell panel. <br>
 * Contains main command loop,
 * 
 */
public class JPFShellInspectorPanel extends ShellPanel implements VerifyCommandListener {
  static Logger log = Debugging.getLogger(ShellManager.getManager().getConfig());
  // predefined commands       
  private static final String[] INITIAL_COMMANDS = {};

  private int initialCmdProcessed = 0;

  private static final boolean DEBUG = true;

  private static final long serialVersionUID = 20110715L;

  private final ConsoleReader console; // / Console that handles user input (jLine from jline library)
  private final SwingTerminal terminal; // / Terminal that shows input and output
  private final PrintStream consolePrintStream; // Stream where print output to user

  private final JPFInspectorClientInterface inspector; // / Server part of the inspector

  /**
   * Creates the JPFInspector - front end
   */
  public JPFShellInspectorPanel () {
    super("JPF Inspector", null, "JPF Inspector command console");
    Config config = ShellManager.getManager().getConfig();

    // Create terminal with specified theme
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

    addComponentListener(new PanelComponentListener());

    consolePrintStream = terminal.getUserTextPrintStream();
    PrintStream startingInfoStream = terminal.getSimplePrintStream();

    String target = config.getTarget();

    // Initialize server part
    inspector = JPFInspectorFacade.getInspectorClient(target, consolePrintStream);

    // Registering JPFShellInspectorPanel as Verify command listener
    ShellManager.getManager().addCommandListener(VerifyCommand.class, this);
    this.setLayout(new BorderLayout());

    add(terminal.getScrollPanel());

    // TODO document that multiple calls to consolePrintStream.println will fail because of how text is appended/inserted in TextComponentFeeder::getPositionBeforePrompt.
    startingInfoStream.println("This is the JPF Inspector console for debugging the target \"" + target + "\".");
    startingInfoStream.println("Type \"hello\" to test if the Inspector is working or \"help\" to get a list of commands.");
    startingInfoStream.println();

    console = terminal.getConsoleReader();

    Thread t = new CommandProcessingThread();
    t.start();

    // Prepare all completors
    console.addCompletor(CmdRunCompletor.getInstance());
  }

  public SwingTerminal getTerminal () {
    return terminal;
  }

  /**
   * Main command processing thread with main command loop.
   * 
   * Parses and executes user commands.
   * 
   */
  class CommandProcessingThread extends Thread {
    public CommandProcessingThread () {
      super(CommandProcessingThread.class.getSimpleName());
    }

    @Override
    public void run () {
      try {
        while (true) {
          try {
            String s;
            if (initialCmdProcessed < INITIAL_COMMANDS.length) {
              // if (initialCmdProcessed > 1 && INITIAL_COMMANDS[initialCmdProcessed-1].equals("run")) sleep(500);
              // useful for debugging (if some commands are predefined)
              sleep(1500);
              s = INITIAL_COMMANDS[initialCmdProcessed++];
            } else {
              s = console.readLine("cmd>");
            }
            String sTrimmed = s.trim();
            if (sTrimmed.length() == 0) {
              consolePrintStream.println();
            }
            if (s.trim().length() > 0) {
              consolePrintStream.println();
              inspector.executeCommand(s);
            }
          } catch (IOException e) {
            consolePrintStream.println("ERR: Error while parsing command");
          }

        } // end of while
      } catch (InterruptedException e) {
        // Terminate the command loop
      }
    };
  }

  @Override
  public void afterJPFInit (VerifyCommand command) {
    if (DEBUG) {
      System.out.println(JPFShellInspectorPanel.class.getSimpleName() + ".afterJPFInit(command=" + command + " )");
    }
    JPF jpf = command.getJPF();
    try {
      inspector.connect2JPF(jpf);
    } catch (JPFInspectorGenericErrorException e) {
      // Silently ignore - error is reported in connect2JPF method
    }
  }

  @Override
  public void postCommand (VerifyCommand command) {
    // Nothing to do

  }

  @Override
  public void preCommand (VerifyCommand command) {
    // Nothing to do
  }

  @Override
  public void exceptionDuringVerify (Exception ex) {
    System.out.println(ex);
    ex.printStackTrace();
  };

  /**
   * If JPFInspector panel selected then sets focus directly into terminal.
   */
  class PanelComponentListener implements ComponentListener {

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
