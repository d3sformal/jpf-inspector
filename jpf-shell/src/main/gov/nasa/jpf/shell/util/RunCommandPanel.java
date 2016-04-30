//
// Copyright (C) 2006 United States Government as represented by the
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
package gov.nasa.jpf.shell.util;

import gov.nasa.jpf.shell.ShellCommand;
import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.commands.VerifyCommand;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This panel serves as an easy way to make shell panels that require commands
 * to be fired to be displayed. It will show a message about the command (And
 * preferably the panel also) along with a button to fire the command.
 * @author Sandro Badame
 */
public class RunCommandPanel extends JPanel {

  private static final Dimension SPACE_FROM_TOP = new Dimension(100, 100);

  public RunCommandPanel(String txt, final ShellCommand cmd){
    setBackground(Color.WHITE);
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    add(new Box.Filler(SPACE_FROM_TOP, SPACE_FROM_TOP, SPACE_FROM_TOP));

    JLabel messageLabel = new JLabel(txt);
    messageLabel.setOpaque(false);
		messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		messageLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
    add(messageLabel);

    JButton commandButton = new JButton(cmd.getName(), cmd.getIcon());
    commandButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        new Thread(){
          @Override
          public void run(){
            ShellManager.getManager().fireCommand(cmd);
          }
        }.start();
      }
    });
    add(commandButton);
  }

  public RunCommandPanel(String txt, final Class<? extends ShellCommand> cmd) {
    this(txt, ShellManager.getManager().getCommand(cmd));
  }

}
