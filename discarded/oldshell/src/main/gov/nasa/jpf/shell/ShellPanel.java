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
package gov.nasa.jpf.shell;

import gov.nasa.jpf.Config;
import java.awt.Font;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * The most basic class for creating a swing UI using the jpf-shell mechanism.
 * <br>
 * ShellPanels are added directly to the shell that it meant to contain them.
 * Shells contain a title/icon/tip, if it changes a request can be sent to the
 * shell using getShell().updateShellPanel(this) to notify the Shell of a change.
 * <br>
 *
 * This class is used to implement everything from text-editors to jpf reports.
 * @see {@link gov.nasa.jpf.shell.panels} for examples on how this is done.<br>
 *
 */
public abstract class ShellPanel extends JPanel{

    private String title;
    protected Shell shell;

    protected Icon icon;
    protected String tip;

    boolean currentlyVisible = false;

    public ShellPanel(String title, Icon icon, String tip){
      this.title = title;
      this.icon = icon;
      this.tip = tip;
    }

    public Shell getShell(){
      return shell;
    }

    /**
     * DO NOT USE THIS UNLESS YOU ARE IMPLEMENTING A SHELL. To move a ShellPanel
     * from one panel to another instead call removeFromShell() on this panel
     * and then use the new shell's {@link gov.nasa.jpf.shell.Shell#addShellPanel(gov.nasa.jpf.shell.ShellPanel)}
     * to add this panel.
     * @param shell changes the return value of getShell()
     */
    public void setShell(Shell s){
      shell = s;
    }

    public String getTitle(){
      return title;
    }

    public void setTitle(String title){
      this.title = title;
    }

    public Icon getIcon(){
        return icon;
    }

    public String getTip(){
        return tip;
    }

    /**
     * Called by the Shell when the user wishes to close to the Shell.
     * The panel can cancel the closing by returning false. Its common courtesy
     * to give the user a valid reason why the panel canceled the Shell's closing.
     * @return true - to continue the closing of the shell
     *         false - to cancel the Shell's closing process.
     */
    public boolean closing(){
      return true;
    }

    /**
     * Called by the Shell when it is closing. At this point there is no canceling
     * the close process. 
     */
    public void closed(){/* Do Nothing */}

    /**
     * This method is executed whenever the Panel is removed from a shell 
     * whether the shell is being disposed of, or the panel is being moved.
     */
    protected void removedFromShell(){/* Do Nothing */}

    /**
     * This method is executed once the panel is in the new Shell.
     * This can contain heavy swing code if needed and can be used instead of
     * the constructor to create the UI, just be warned that this method
     * can be executed multiple times if a panel is being moved from one Shell
     * to another (ie: make sure not to keep on recreating your UI).<br>
     * {@link #getShell() refers to the new shell that this Panel was added to}
     */
    protected void addedToShell(){/* Do Nothing */}

    /**
     * Sends a request to the parent shell that this panel receive focus.
     */
    public void requestShellFocus(){
      getShell().requestFocus(this);
    }

    protected void receivedFocus() {}

    protected boolean isVisibleInShell() {
      return currentlyVisible;
    }
    
    protected void warning(String msg){
      JOptionPane.showMessageDialog(shell, msg, "Operation Failed", 
              JOptionPane.NO_OPTION | JOptionPane.WARNING_MESSAGE);
    }

    protected void error(String msg){
      JOptionPane.showMessageDialog(shell, msg, "Operation Failed",
              JOptionPane.NO_OPTION | JOptionPane.ERROR_MESSAGE);
    }
    
    public void configChanged(Config config){
      // to be overridden by subclasses
    }
    
    protected Font getFont( String keyPrefix, String defFamily, int defSize, int defStyle){      
      Config config = ShellManager.getManager().getConfig();
            
      String name = config.getString( keyPrefix + ".name", defFamily);
      int size = config.getInt(keyPrefix + ".size", defSize);
      int style = config.getInt(keyPrefix + ".style", defStyle);      
      
      return new Font( name, style, size);
    } 
}
