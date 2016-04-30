/* Copyright (C) 2007 United States Government as represented by the
 * Administrator of the National Aeronautics and Space Administration
 * (NASA).  All Rights Reserved.
 *
 * This software is distributed under the NASA Open Source Agreement
 * (NOSA), version 1.3.  The NOSA has been approved by the Open Source
 * Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
 * directory tree for the complete NOSA document.
 *
 * THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
 * KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
 * LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
 * SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
 * A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
 * THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
 * DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
 */
package gov.nasa.jpf.shell.panels;

import javax.swing.*;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.shell.ShellCommandListener;
import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.commands.VerifyCommand;
import gov.nasa.jpf.shell.util.ConsolePanel;
import gov.nasa.jpf.shell.util.RunCommandPanel;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Display's JPF's STDOUT output after verification in a JavaOutputPane.
 * (A simple example of how to use ConsolePanel)
 */
public class VerifyConsolePanel extends ConsolePanel implements
					  ShellCommandListener<VerifyCommand>{

  private static final String TITLE = "Verify Output";
  private static final String TIP = "View JPF's output after verifying";

  private static final String PRECOMMAND_TEXT = "Verify the program to see its output.";

  private boolean captureStreams;
  private PrintStream sys_out,sys_err;
  private InputStream sys_in;

  public VerifyConsolePanel(){
    super(TITLE, null, TIP);
    
    // we mostly make this configurable to support debugging
    Config config = ShellManager.getManager().getConfig();
    captureStreams = config.getBoolean("shell.capture_streams", true);
  }

	@Override
	protected JComponent unhookedComponent(){
    return new RunCommandPanel(PRECOMMAND_TEXT, VerifyCommand.class);
	}

  @Override
  public void addedToShell(){
    ShellManager.getManager().addCommandListener(VerifyCommand.class, this);
  }

  @Override
  public void removedFromShell(){
    ShellManager.getManager().removeCommandListener(this);
  }

  public void preCommand(VerifyCommand command) {
    if (captureStreams){
      sys_out = System.out;
      sys_err = System.err;
      sys_in = System.in;

      try {
        PipedInputStream out = new PipedInputStream();
        PipedOutputStream pipe_out = new PipedOutputStream(out);
        System.setOut(new PrintStream(pipe_out, true));

        PipedInputStream err = new PipedInputStream();
        PipedOutputStream pipe_err = new PipedOutputStream(err);
        System.setErr(new PrintStream(pipe_err, true));

        PipedOutputStream in = new PipedOutputStream();
        PipedInputStream pipe_in = new PipedInputStream(in);
        System.setIn(pipe_in);

        hookSystemStreams(out, err, in);

      } catch (IOException ex) {
        Logger.getLogger(VerifyConsolePanel.class.getName()).log(Level.SEVERE, null, ex);
      }
    } else {
      Logger.getLogger(VerifyConsolePanel.class.getName()).log(Level.INFO, "system streams not captured");      
    }
  }

  public void postCommand(VerifyCommand command) {
    if (captureStreams){
      unhookSystemStreams();
      
      System.setOut(sys_out);
      System.setErr(sys_err);
      System.setIn(sys_in);
    }
  }
}
