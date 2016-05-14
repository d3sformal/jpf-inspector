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
package gov.nasa.jpf.shell.commands;

import java.util.logging.Level;
import gov.nasa.jpf.shell.listeners.VerifyCommandListener;
import gov.nasa.jpf.shell.*;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.util.LogManager;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import static gov.nasa.jpf.shell.basicshell.images.JPFShell_Images.*;

/**
 * The command responsible for starting and running JPF and also for canceling
 * it.
 */
public class VerifyCommand extends ShellCommand{

  public static final String NAME = "Verify";

  private static boolean running = false;
	private boolean error_occured = false;

  private Icon verifyIcon = getCommandIcon(CommandIcon.Verify,"Verify this application.");
  private Icon workingIcon = getStatusIcon(StatusIcon.Working, "Currently Verifying this application");

  private JPF jpf;

  @Override
  public String getName(){
    return NAME;
  }
  
  @Override
  public String getToolTip(){
    return "Verify the loaded application";
  }

  @Override
  public Icon getIcon(){
    if (running)
      return workingIcon;
    else
      return verifyIcon;
  }

  /**
   * If jpf is running, kills it.
   */
  private void cancelVerify(){
    if (running){
      if (jpf != null)
        jpf.getSearch().terminate();
    }
  }

  @Override
  public boolean prepare(){
    if (running){
      if (JOptionPane.showConfirmDialog(ShellManager.getManager().getShell(),
	  "Are you sure that you want to cancel JPF?")==JOptionPane.YES_OPTION)
        cancelVerify();
      return false;
    }
    return true;
  }

  /**
   * Responsible for starting JPF. <b>DO NOT CALL THIS DIRECTLY</b> instead use
   * one of the <code>ShellManager.getManager().fireCommand()</code> methods.
   *
   * The VerifyCommand has an extra step between pre and post command, some
   * listeners need the jpf instance after jpf's init() but before the run
   * (Specifically to add publishers). Therefore, before jpf.run() can be called
   * the {@link gov.nasa.jpf.shell.VerifyCommandListener#afterJPFInit(gov.nasa.jpf.JPF) }
   * method needs to be called on all of the VerifyCommandListener that are
   * registered with the ShellManager. That is all handled here. This method
   * will get all of the VerifyCommandListeners and execute the afterJPFInit
   * after the jpf init takes place.
   */
  public void execute() {
		error_occured = false;
		jpf = new JPF(ShellManager.getManager().getConfig());

		List<VerifyCommandListener> listeners = ShellManager.getManager().getCommandListeners(getClass(), VerifyCommandListener.class);
		for (VerifyCommandListener vcl :listeners) { vcl.afterJPFInit(this); }

		running = true;
		requestShellUpdate();
		try{
			jpf.run();
		}catch(Exception ex){
      LogManager.getLogger("gov.nasa.jpf").log(Level.SEVERE, "Error during jpf.run()", ex);
			error_occured = true;
			//We need to notify any listeners that an exception occured during
			//the verify
			List<VerifyCommandListener> vcl = ShellManager.getManager().getCommandListeners(getClass(), VerifyCommandListener.class);
			for (VerifyCommandListener listener : vcl) {
				listener.exceptionDuringVerify(ex);
			}
      ShellManager.getManager().getLogger().log(Level.SEVERE, "JPF Error", ex);
		}
		running = false;
		requestShellUpdate();
  }

  /**
   * @return true if jpf is running, otherwise false.
   */
  public boolean isVerifying(){
    return running;
  }

  /**
   * only works after {@link #prepare()} is called.
   * @return the instance of JPF that is being used to run the verify. 
   *	     Mostly meant to be used by listeners.
   */
  public JPF getJPF(){
    if (jpf == null)
      throw new IllegalStateException("Cannot reference JPF before prepare()");
    return jpf;
  }

	public boolean errorOccured() {
		return error_occured;
	}

}