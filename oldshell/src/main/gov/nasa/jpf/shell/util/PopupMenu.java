//
// Copyright (C) 2011 United States Government as represented by the
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

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

/**
 * little helper to make it less tedious to create popups with corresponding
 * KeyStroke actions (accelerators are not processed if the popup is not visible)
 */
public class PopupMenu extends JPopupMenu {
  
  JComponent target;
  
  public PopupMenu (JComponent target){
    this.target = target;
    target.setComponentPopupMenu(this);
  }
  
  public void add (String mItemName, Action action){
    JMenuItem mItem = new JMenuItem(mItemName);

    mItem.addActionListener( action);
    add(mItem);    
  }
  
  public void add (String mItemName, Action action, KeyStroke keyStroke){
    JMenuItem mItem = new JMenuItem(mItemName);

    mItem.addActionListener( action);
    mItem.setAccelerator(keyStroke);
    add(mItem);
    
    target.getInputMap().put(keyStroke, mItemName);
    target.getActionMap().put(mItemName, action);
  }

}
