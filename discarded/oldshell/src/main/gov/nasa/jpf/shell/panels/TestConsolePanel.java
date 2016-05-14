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

import gov.nasa.jpf.Config;
import gov.nasa.jpf.shell.ShellCommand;
import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.commands.TestCommand;
import gov.nasa.jpf.shell.listeners.TestCommandListener;
import gov.nasa.jpf.shell.util.ConsolePanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Displays the output and error streams of the SuT in a JavaOutputPanel.
 * It also allows for the user to send data into the SuT's Standard Input Stream.
 *
 */
public class TestConsolePanel extends ConsolePanel implements TestCommandListener{
  
  public TestConsolePanel(){
    super("Test Output",null,"View the output from testing the application");
  }

	@Override
	protected JComponent unhookedComponent(){
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new Box.Filler(new Dimension(100,100), new Dimension(100,100), new Dimension(100,100)));

		JLabel notHookedLabel = new JLabel("Test the program to see its output.", JLabel.CENTER);
		notHookedLabel.setOpaque(true);
		notHookedLabel.setBackground(Color.white);
		notHookedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		notHookedLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
		panel.add(notHookedLabel);

		JButton commandButton = new JButton("Test");
		commandButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				ShellManager.getManager().fireCommand(TestCommand.class);
			}
		});

		commandButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		commandButton.setAlignmentY(Component.CENTER_ALIGNMENT);
		panel.add(commandButton);
		return panel;
	}

  public void applicationStarted(TestCommand command) {
    Process p = command.getApplication();
    if (p != null)//This really can't be null
      hookSystemStreams(p.getInputStream(), p.getErrorStream(), p.getOutputStream());
    else
      throw new IllegalStateException("TestCommand.getApplication() is null"+
	      " in appliationStarted! There is probably a bug somewhere");
  }

  public void applicationEnded(TestCommand command) {
    unhookSystemStreams();
  }


  @Override
  public void addedToShell(){
    ShellManager.getManager().addCommandListener(TestCommand.class, this);
  }

  @Override
  public void removedFromShell(){
    ShellManager.getManager().removeCommandListener(this);
  }

  public void preCommand(ShellCommand command) {}
  public void postCommand(ShellCommand command) { }

}
