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

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.inspector.JPFInspectorFacade;
import gov.nasa.jpf.inspector.client.JPFInspectorClientInterface;
import gov.nasa.jpf.inspector.frontends.jpfshell.commands.completors.CmdRunCompletor;
import gov.nasa.jpf.inspector.frontends.jpfshell.gui.SwingTerminal;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.ShellPanel;
import gov.nasa.jpf.shell.commands.VerifyCommand;
import gov.nasa.jpf.shell.listeners.VerifyCommandListener;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.io.PrintStream;

import jline.ConsoleReader;

/**
 * JPF Shell panel. <br>
 * Contains main command loop,
 * 
 */
public class JPFShellInspectorPanel extends ShellPanel implements VerifyCommandListener {
  // predefined commands       
  private static final String[] INITIAL_COMMANDS = {};

  private int initialCmdProcessed = 0;

  private static final boolean DEBUG = false;

  private static final long serialVersionUID = 20110715L;

  private final ConsoleReader console; // / Console that handles user input (jLine from jline library)
  private final SwingTerminal terminal; // / Terminal that shows input and output
  private final PrintStream consolePrintStream; // Stream where print output to user

  private final JPFInspectorClientInterface inspector; // / Server part of the inspector

  /**
   * Creates the JPFInspector - front end
   */
  public JPFShellInspectorPanel () {
    super("Inspector", null, "JPF Inspector textual console");

    terminal = SwingTerminal.getSwingTerminalWhite();
    // terminal = SwingTerminal.getSwingTerminalBlack();

    addComponentListener(new PanelComponentListener());

    consolePrintStream = terminal.getUserTextPrintStream();

    String target = ShellManager.getManager().getConfig().getTarget();
    // Initialize server part
    inspector = JPFInspectorFacade.getInspectorClient(target, consolePrintStream);

    // Registering JPFShellInspectorPanel as Verify command listener
    ShellManager.getManager().addCommandListener(VerifyCommand.class, this);
    this.setLayout(new BorderLayout());

    add(terminal.getScrollPanel());

    consolePrintStream.println("The Inspector console: " + target);
    consolePrintStream.println();

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
              s = console.readLine("?:>");
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
