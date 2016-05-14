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
package gov.nasa.jpf.shell.basicshell;

import gov.nasa.jpf.shell.listeners.TestCommandListener;
import gov.nasa.jpf.shell.commands.VerifyCommand;
import gov.nasa.jpf.shell.commands.TestCommand;
import gov.nasa.jpf.shell.*;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import static gov.nasa.jpf.shell.basicshell.images.JPFShell_Images.*;

/**
 * A JPanel that lays along the bottom of the BasicShell displaying icons that
 * indicate the status of various ShellCommands (Right now just {@link gov.nasa.jpf.shell.commands.TestCommand}
 * and {@link gov.nasa.jpf.shell.commands.VerifyCommand}).  
 */
public class StatusPanel extends JPanel implements ShellCommandListener, TestCommandListener{

  //Verify
  private Icon verifyStatusOn = getStatusIcon(StatusIcon.VerifyOn, "Currently Verifying...");
  private Icon verifyStatusOff = getStatusIcon(StatusIcon.VerifyOff, "");
  private JLabel verifyStatusLabel = new JLabel(verifyStatusOff);

  //Test
  private Icon testStatusOn = getStatusIcon(StatusIcon.TestOn, "Currently Running the System Under Test...");
  private Icon testStatusOff = getStatusIcon(StatusIcon.TestOff, "");
  private JLabel testStatusLabel = new JLabel(testStatusOff);

  public StatusPanel(){
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    setBorder(BorderFactory.createLoweredBevelBorder());
  }

  /**
   * Listens for this VerifyCommand and updates this panel's states as it
   * detects changes in the state of the command.
   * @param command the verify command to listen to
   */
  public void addCommand(VerifyCommand command){
    add(verifyStatusLabel);
    ShellManager.getManager().addCommandListener(command, this);
  }

  /**
   * Listens for this TestComman and updates this panel's states as it
   * detects changes in the state of the command.
   * @param command the test command to listen to
   */
  void addCommand(TestCommand command){
    add(testStatusLabel);
    ShellManager.getManager().addCommandListener(command, (TestCommandListener)this);
  }

  /*----------------------- These are for the VerifyCommand ------------------*/
  public void preCommand(ShellCommand command) {
    verifyStatusLabel.setIcon(verifyStatusOn);
  }

  public void postCommand(ShellCommand command) {
    verifyStatusLabel.setIcon(verifyStatusOff);
  }

  /* ------------- These are for the TestCommand -----------------------------*/
  public void applicationStarted(TestCommand command) {
     testStatusLabel.setIcon(testStatusOn);
  }

  public void applicationEnded(TestCommand command) {
     testStatusLabel.setIcon(testStatusOff);
  }
}
