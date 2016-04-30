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

import gov.nasa.jpf.JPFShell;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.shell.panels.ConfigPanel;
import gov.nasa.jpf.shell.panels.PropertiesPanel;
import gov.nasa.jpf.shell.panels.ReportPanel;
import gov.nasa.jpf.shell.panels.SitePanel;
import gov.nasa.jpf.shell.panels.TestConsolePanel;
import gov.nasa.jpf.shell.panels.VerifyConsolePanel;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * The <code>Shell</code> class represents a JPF Shell.<br>
 * Shells contain a collection {@link ShellPanel} and {@link ShellCommand}. It is
 * up to subclasses of Shell to determine how they are represented. 
 * <br> By default once all Shells are disposed of, then a
 * System.exit(0) is called. To disable this behavior set {@link gov.nasa.jpf.shell.ShellManager#SYSTEM_EXIT_ON_SHELL_CLOSE}
 * to false.<br>
 *
 * Although this class extends JFrame there is no swing code in here, that is
 * upto the implementor of this class. (See {@link BasicShell} for an example on
 * what this entails. <br>
 * 
 * There is only 1 single {@link gov.nasa.jpf.Config} that exists for all Shell instances,
 * see {@link gov.nasa.jpf.shell.ShellManager} for more information on how
 * shells interact with resources like the {@link gov.nasa.jpf.Config} instance and error handling.
 *
 * @author Sandro Badame 
 */
public abstract class Shell extends JFrame implements JPFShell, ShellFrame{

  /**
   * The default TITLE for a Shell
   */
  public static final String TITLE = "JPF Shell";

  public static final Image DEFAULT_ICON
          = new ImageIcon(Shell.class.getResource("spiral-of-death-small.png"))
                          .getImage();

  protected String titlePrefix;
  
  private static String getTitleSuffix(Config c){
    String suffix = c.getProperty("jpf.app");
    if (suffix != null) {
      suffix = new File(suffix).getName();
    }else{
      suffix = c.getTarget();
    }
    return " - " + suffix;
  }

  private ArrayList<ShellPanel> panels = new ArrayList<ShellPanel>();

  /**
   * Creates a {@link gov.nasa.jpf.shell.ShellManager} with the specified
   * {@link gov.nasa.jpf.Config} object and a Shell with a custom title.
   * @param config the config used in the ShellManager
   * @param title  the title of this frame
   */
  protected Shell(Config config, String title){
    super(title + getTitleSuffix(config));
    titlePrefix = title;

    ShellManager.createShellManager(config);
  }

 /**
    * Create a shell with a title
    * @param title - the title of this shell
    */
  public Shell(String title){
    super(title);
    titlePrefix = title;
  }

  /**
   * The constructor called on by JPF when instantiating a shell. This is
   * equivalent to calling {@link #Shell(gov.nasa.jpf.Config, java.lang.String)}
   * with the arguments: config, TITLE
   * @param config the {@link gov.nasa.jpf.Config} object to be used for the 
   * {@link gov.nasa.jpf.shell.ShellManager}.
   *
   */
  protected Shell(Config config){
    this(config, TITLE);
  }

  /**
   * Creates a Shell with the default title.
   */
  public Shell(){
    this(TITLE);
  }

  
  /**
   * Does the dirty work of adding its self to the ShellManager and various
   * Swing related details.
   */
  public final void registerShell(){
    if (!ShellManager.getManager().hasShell(this)) {
      ShellManager.getManager().addShell(this);
      //We will handle when to exit the VM in ShellManager
      setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      //Responsible to report shell closing to the ShellManager
      addWindowListener(new ShellWindowListener());

      setIconImage(DEFAULT_ICON);
    }
  }

  public void configChanged(Config config){
    setTitle( titlePrefix + getTitleSuffix(config));
    
    // <2do> need to add/remove panels accordingly
    
    for (ShellPanel panel : panels){
      panel.configChanged(config);
    }
  }
  
  /**
   * Installs a command that was registered with the ShellManager onto this
   * shell.
   * @param command the command to be installed.
   */
  public abstract void installCommand(ShellCommand command);
  

  /**
   * Removes a command from this shell and then un-installs it.
   * @param the command to be removed
   */
  public abstract void uninstallCommand(ShellCommand command);

  /**
   * Sends a request to the shell that this command be updated
   * @param command  the command to be updated
   */
  public abstract void updateShellCommand(ShellCommand command);

  /**
   * Sends a request to the shell that this panel be updated because the
   * title, tooltip or icon changed.
   * @param panel the panel to be updated 
   */
  public abstract void updateShellPanel(ShellPanel panel);


   /**
    * Sends a request to the shell that this panel receive focus, it is upto the
    * the shell implementation to decide what that means. Usually it is the
    * panel requesting to be visible to the user.
    */
   public abstract void requestFocus(ShellPanel panel);

   /**
    *
    */
   protected void setVisibleInShell(ShellPanel p, boolean visible){
     p.currentlyVisible = visible;
   }

  /**
   * adds a panel to this shell
   * @param panel the panel to be registered
   */
  public void addShellPanel(ShellPanel panel){
    panels.add(panel);
    panel.setShell(this);
    panel.addedToShell();
  }

  /**
   * Define how to display exceptions in this shell
   */
  public void error(Exception e) {
    error("The following error was caught:", e);
  }

  /**
   * Displays an error message to the user
   */
  public void error(String err) {
    JOptionPane.showMessageDialog(this, err, "Error", JOptionPane.ERROR_MESSAGE);
  }

  /**
   *  Displays a dialog that contains a message and exception stack trace.
   *  @param msg
   *  @param ex
   */
  public void error(String msg, Exception ex) {
    StringWriter writer = new StringWriter();
    ex.printStackTrace(new PrintWriter(writer));
    JScrollPane scroll = new JScrollPane(new JTextArea(writer.toString()));
    Box box = Box.createVerticalBox();
    box.add(new JLabel(msg));
    box.add(scroll);
    JOptionPane.showMessageDialog(this, box, "Error: " + ex.getClass().getName(), JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Searches this shell for the first instance of a panel that is a subclass
   * of the shellPanelClass
   * @param shellPanelClass The class to search for
   * @return The first panel that this shell holds that is an instance of
   * shellPanelClass, null if a panel is not found.
   */
  public <T extends ShellPanel> T getShellPanel(Class<T> shellPanelClass) {
    for (ShellPanel panel : panels) {
      if (panel.getClass().isAssignableFrom(shellPanelClass)) {
        return (T) panel;
      }
    }
    return null;
  }

  /**
   * @return a copy of the list of panels in this Shell.
   */
  public  List<ShellPanel> getPanels(){
    return new ArrayList(panels);
  }

  /**
   * Finds a ShellPanel via {@link #getShellPanel(java.lang.Class)} and then removes
   * it using {@link #removeShellPanel(gov.nasa.jpf.shell.ShellPanel) }
   * @param shellPanelClass
   */
  public void removeShellPanel(Class<? extends ShellPanel> shellPanelClass) {
    ShellPanel t = getShellPanel(shellPanelClass);
    if (t != null) {
      removeShellPanel(t);
    }
  }

  /**
   * Removes this panel from this shell. The panel is not being moved, it is being
   * disposed of. If this shell will contain no panels after this removal, then
   * the shell is disposed of.
   * @param panel
   */
  public void removeShellPanel(ShellPanel panel) {
    panels.remove(panel);
    panelRemoval(panel);
  }

  @Override
  public void dispose(){
    for (ShellPanel panel : panels) {
      panelRemoval(panel);
    }
    panels.clear();
    ShellManager.getManager().removeShell(Shell.this);
    super.dispose();
  }

  private void panelRemoval(ShellPanel panel){
    if (panels.isEmpty()) {
      dispose();
    }
    panel.removedFromShell();
  }


  /**
   *
   */
  protected void receivedFocus(ShellPanel shellPanel) {
    shellPanel.receivedFocus();
  }


  
	/**
	 * This method looks into the "shell.panel" property and creates instances of
	 * of the panels then adds them to this shell.
	 */
	protected void loadPanelsFromConfig(){
    Config config = ShellManager.getManager().getConfig();
    
    ShellPanel[] panels = config.getGroupInstances("shell.panels", null, ShellPanel.class,
            ".shell.panels.PropertiesPanel",
            ".shell.panels.ConfigPanel",
            ".shell.panels.SitePanel",
            ".shell.panels.ReportPanel",
            ".shell.panels.VerifyConsolePanel",
            ".shell.panels.TestConsolePanel");
    
    for (ShellPanel panel : panels){
      addShellPanel(panel);
    }
	}

  /**
   * Creates another shell for the case of panel tearing. ie) the Shell that will
   * hold the panel that is being isolated.
   * @return a new shell to hold the panel that is being dragged away.
   */
  public abstract Shell createChildShell();

  /**
   * This class serves as the listener for when the Shells are being closed.
   */
  private class ShellWindowListener extends WindowAdapter {

    @Override
    public void windowClosing(WindowEvent e) {
      for (ShellPanel panel : panels) {
        //Check if all of the panels are OK with closing
        if (panel.closing() == false) {
          return;
        }
      }
      setVisible(false);
      dispose();
    }

    @Override
    public void windowClosed(WindowEvent e) {
      for (ShellPanel panel : panels) {
        panel.closed();
      }
    }
  }
}
