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

import gov.nasa.jpf.shell.basicshell.ShellTabbedPane.ShellPanelTabComponent;
import gov.nasa.jpf.shell.commands.VerifyCommand;
import gov.nasa.jpf.shell.commands.TestCommand;
import gov.nasa.jpf.shell.*;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.shell.panels.*;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.shell.util.GraphicsUtil;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.util.HashMap;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The most basic swing implementation of a Shell in the jpf-shell library. Serves
 * most of the purposes that any shell need to serve. When developing your own
 * shells this should be  a starting point since the BasicShell class is subclass-able.
 *
 * <h4>Commands: {@link #getDesiredCommands()} </h4>
 * <ul>
 *	<li> {@link gov.nasa.jpf.shell.TestCommand}</li>
 *	<li> {@link gov.nasa.jpf.shell.VerifyCommand}</li>
 * </ul>
 *
 * <h4>Tabs: {@link #getDesiredPanels()} </h4>
 * <ul>
 *	<li> {@link gov.nasa.jpf.shell.panels.PropertiesPanel} </li>
 *	<li> {@link gov.nasa.jpf.shell.panels.ReportPanel} </li>
 *	<li> {@link gov.nasa.jpf.shell.panels.TestConsolePanel} </li>
 *	<li> {@link gov.nasa.jpf.shell.panels.VerifyConsolePanel} </li>
 * </ul>
 *
 * <h4>Listeners: {@link #addShellListeners()} </h4>
 * <ul>
 *	<li> NONE </li>
 * </ul>
 */
public class BasicShell extends Shell {

  private static final Dimension STARTING_SIZE = new Dimension(1000, 600);

  final static Color baseColor = UIManager.getColor("Tree.selectionBackground");

  /**
   * The pane that holds all of the shell panels for this BasicShell
   */
  protected ShellTabbedPane tabbedPane;

  private HashMap<ShellCommand, CommandButton> commandMap = new HashMap<ShellCommand, CommandButton>();
  private Container commandArea;
  private StatusPanel statusBar;

  private JMenuBar panelMenuBar;
  private JMenu panelMenu;
  private int closedTabcount = 0;

  /**
   * Creates a BasicShell using the ShellManager returned by 
   * {@link gov.nasa.jpf.shell.ShellManager#getManager()} and the TITLE defined
   * by {@link #TITLE}<br>
   * <b>Note:</b> this requires that the ShellManager already be initialized
   *  
   */
  public BasicShell() {
    super();
    createShell();
  }


  /**
   * This constructor exists because it's what JPF wants when creating a shell.
   * This really shouldn't be used for any other purpose. This constructor
   * calls the {@link gov.nasa.jpf.shell.ShellManager#createShellManager(gov.nasa.jpf.Config)} 
   * method to create a ShellManager then calls the {@link #BasicShell} constructor
   * to actually create the shell.
   * @param c The configuration that is used to create the ShellManager
   */
  public BasicShell(Config c){
    this(c, TITLE);
  }

  /**
   * This constructor is what subclasses will probably use. Allowing for a 
   * ShellManager to be created and the title to be specified.
   */
  protected BasicShell(Config c, String title){
    super(c, title);
    createShell();
  }


  /**
   * Creates a BasicShell using the ShellManager returned by 
   * {@link gov.nasa.jpf.shell.ShellManager#getManager()} and a custom title.
   * <br>
   * <b>Note:</b> this requires that the ShellManager already be initialized
   */
  public BasicShell(String title){
    super(title);
    createShell();
  }

  /**
   * Sets up the frame to have commands and panels added to it. <br>
   * This is where the swing code comes in to lay everything out.
   *
   * After that, the VerifyCommand and TestCommand is added to the manager
   * who then adds those commands back onto us. (yes, this sounds crazy but this
   * is what works)
   */
  private void createShell(){

    setLayout(new BorderLayout());

    add(createToolBar(), BorderLayout.NORTH);
    add(createTabbedArea(), BorderLayout.CENTER);
    add(createStatusBar(), BorderLayout.SOUTH);

    pack();
    setSize(STARTING_SIZE);
    setLocationRelativeTo(null);
    registerShell();
  }

  protected JPanel createToolBar() {
    Component commandPanel = createCommandArea();
    panelMenuBar = createPanelMenu();

    JPanel toolbar = new JPanel() {
      @Override
      public void paintComponent(Graphics g) {
        GraphicsUtil.drawGradientBackground(g, getBackground().brighter(),
                                              getBackground().darker());
      }
    };
    toolbar.add(createCommandArea());
    toolbar.add(panelMenuBar);
    toolbar.setBorder(BorderFactory.createLineBorder(Color.GRAY));

    SpringLayout layout = new SpringLayout();
    //Command area
    layout.putConstraint(SpringLayout.NORTH, toolbar, 1, SpringLayout.NORTH, commandPanel);
    layout.putConstraint(SpringLayout.SOUTH, toolbar, 1, SpringLayout.SOUTH, commandPanel);
    layout.putConstraint(SpringLayout.WEST, toolbar, 1, SpringLayout.WEST, commandPanel);

    //Panel menu
    layout.putConstraint(SpringLayout.NORTH, panelMenuBar, 1, SpringLayout.NORTH, toolbar);
    layout.putConstraint(SpringLayout.SOUTH, panelMenuBar, 1, SpringLayout.SOUTH, toolbar);
    layout.putConstraint(SpringLayout.EAST, panelMenuBar, 1, SpringLayout.EAST, toolbar);
    toolbar.setLayout(layout);

    return toolbar;
  }

  /**
   * Adds a command to this shell. <br>
   * This involves creating a new button for the command on the command toolbar.
   * If there is an Icon only the icon is displayed. Borders are created for the
   * mouse over/press events.
   * @param command the command to be added to this shell.
   */
  @Override
  public void installCommand(final ShellCommand command) {
    final CommandButton button = new CommandButton(command);
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        new Thread(){
          @Override
          public void run(){
            ShellManager.getManager().fireCommand(command);
          }
        }.start();
      }
    });


    commandMap.put(command, button);
    updateShellCommand(command);
    commandArea.add(button);
    commandArea.validate();

    //Take care of the status bar
    if (command instanceof VerifyCommand || command instanceof TestCommand){
      if (getStatusBar() == null)
        createStatusBar();
      if (command instanceof VerifyCommand)
        getStatusBar().addCommand((VerifyCommand)command);
      if (command instanceof TestCommand)
        getStatusBar().addCommand((TestCommand)command);
    }
  }

	/*
   * Called when a toolbar is branching off into its own shell.
   * The child shell of the BasicShell is a BasicShell with no commands
   * at the top. See {@link gov.nasa.jpf.shell.BasicShell} for more information.
   * @return the new child shell
   */
  public Shell createChildShell() {
    return new BasicChildShell();
  }

  /**
   * Creates a status bar for the shell that is put along the bottom of the 
   * shell frame. The status bar serves the purpose of updating the user on
   * what the shell is doing.
   * @return The JPanel that holds the status bar.
   */
  protected StatusPanel createStatusBar(){
    statusBar = new StatusPanel();
    return getStatusBar();
  }

  /**
   * @returns the toolbar that represents the status bar see {@link #createStatusBar()}
   * for more information.
   */
  public StatusPanel getStatusBar(){
    return statusBar;
  }

  /**
   * Constructs the Container that holds all of the command buttons. <br>
   * Override this to implement a custom Command Area toolbar.
   */
  protected Container createCommandArea() {
    if (commandArea == null) {
      JPanel panel= new JPanel();
      panel.setBackground(new Color(0, 0, 0, 0));
      panel.setOpaque(false);
      commandArea = panel;
    }
    return commandArea;
  }


  protected JMenuBar createPanelMenu() {
    JMenuBar tabsBar = new JMenuBar();
    tabsBar.setVisible(false);
    tabsBar.setOpaque(false);
    panelMenu = new JMenu("Closed Tabs"){
      @Override
      public void paintComponent(Graphics g){
        super.paintComponent(g);
        GraphicsUtil.drawButtonBackground(g, this);
      }
    };
    panelMenu.setOpaque(false);
    panelMenu.setRolloverEnabled(true);
    tabsBar.add(panelMenu);
    return tabsBar;
  }

  /**
   * Constructs the {@link gov.nasa.jpf.shell.ShellTabbedPane} that will hold
   * all of the panels. <br />
   * Override to implement a custom ShellPanel area, or a replacement for
   * ShellTabbedPane
   */
  protected Container createTabbedArea() {
    tabbedPane = new ShellTabbedPane(this);
    ToolTipManager.sharedInstance().registerComponent(tabbedPane);

    //Add listener for the popup menu
    tabbedPane.addMouseListener(new MouseAdapter() {

      @Override
      public void mousePressed(MouseEvent e) {
        showTabPopup(e);
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        showTabPopup(e);
      }
    });

    tabbedPane.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent ce) {
        Component c = tabbedPane.getSelectedComponent();
        if (c != null && c instanceof ShellPanel) {
          for (ShellPanel panel : getPanels()) {
            setVisibleInShell(panel, false);
          }
          setVisibleInShell((ShellPanel)c, true);
          receivedFocus((ShellPanel)c);
        }
      }

    });

    return tabbedPane;
  }

  /**
   * This is the container that holds all of the panels in this shell. <br>
   * Override this method if a custom ShellTabbedPane class is implemented.
   * @return the ShellTabbedPane that holds the panels
   */
  public ShellTabbedPane getTabPane(){
    return tabbedPane;
  }

  /**
   * This function is responsible for the menu that appears when a toolbar header
   * right clicked.
   * @param e the mouse event generated from the toolbar header click
   */
  private void showTabPopup(MouseEvent e) {
    if (!e.isPopupTrigger()) {
      return;
    }

    Point p = e.getPoint();
    int tabIndex = tabbedPane.indexAtLocation(p.x, p.y);
    if (tabIndex == -1) {
      return;
    }
    final ShellPanel panel = (ShellPanel) tabbedPane.getComponent(tabIndex);

    JPopupMenu popup = new JPopupMenu();
    JMenuItem close = new JMenuItem("Close");
    close.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        if (panel.closing()) {
          panel.closed();
        }
        removeShellPanel(panel);
      }
    });
    popup.add(close);
    popup.show(tabbedPane, p.x, p.y);
  }

  /**
   * Removes the command from this shell. This will remove any mention of the
   * the command from the UI and call the {@link gov.nasa.jpf.shell.ShellCommand#uninstall() }
   * method.<br>
   * Override this command if a custom command implementation is being used.
   * @param command
   */
  @Override
  public void uninstallCommand(ShellCommand command){
    commandArea.remove(commandMap.get(command));
    commandMap.remove(command);
  }

  /**
   * Call this method whenever the UI for a command needs to be updated.
   * @param command the command that needs to be updated. Does nothing if the
   * command is not in this shell.
   */
  @Override
  public void updateShellCommand(final ShellCommand command){
    final CommandButton button = commandMap.get(command);
    if (button == null) return;

    if (SwingUtilities.isEventDispatchThread()) {
      button.setToolTipText(command.getToolTip());
      button.setIcon(command.getIcon());
      button.validate();
    }else{
      try {
        SwingUtilities.invokeAndWait(new Runnable() {
          public void run() {
            updateShellCommand(command);
          }
        });
      } catch (InterruptedException ex) {
        Logger.getLogger(BasicShell.class.getName()).log(Level.SEVERE, null, ex);
      } catch (InvocationTargetException ex) {
        Logger.getLogger(BasicShell.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

  }

  /**
   * Call this method whenever the UI for a toolbar needs to be updated.
   */
  @Override
  public void updateShellPanel(final ShellPanel panel){
    //Gaurentee that we only run this in the EDT
    if (SwingUtilities.isEventDispatchThread()) {
      int index = tabbedPane.indexOfComponent(panel);
      if (index != -1) {
        final ShellTabbedPane.ShellPanelTabComponent tabcomponent = (ShellPanelTabComponent) tabbedPane.getTabComponentAt(index);
        JLabel label = tabcomponent.getLabel();
        String s;
        Icon i;
        if ((s = panel.getTitle()) != null) {label.setText(s);}
        if ((i = panel.getIcon()) != null) {label.setIcon(i);}
        if ((s = panel.getTip()) != null) { label.setToolTipText(s); }
      }
    } else {
        try {
          SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
              updateShellPanel(panel);
            }
          });
        } catch (InterruptedException ex) {
          Logger.getLogger(BasicShell.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
          Logger.getLogger(BasicShell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  }

  /**
   * @param toolbar makes this toolbar visible to the user, by selecting it.
   */
  @Override
  public void requestFocus(ShellPanel panel) {
    tabbedPane.setSelectedComponent(panel);
  }

  /**
   * This is is a convenience method that is equivalent to:
   * <pre> addShellPanel(toolbar, getTabPane().getTabCount()) </pre>.
   * @param toolbar adds this toolbar to the end of the pane.
   */
  @Override
  public void addShellPanel(final ShellPanel panel) {
    addShellPanel(panel, tabbedPane.getTabCount());
  }

  /**
   * Inserts a toolbar into the specified position.
   * @param toolbar the shellPanel that need be inserted.
   * @param index the position to add the toolbar into.
   */
  public void addShellPanel(final ShellPanel panel, int index) {
    getTabPane().add(panel, index);
    tabbedPane.insertShellPanel(panel, index);
    super.addShellPanel(panel);
    setSize(STARTING_SIZE);
  }

  /**
   * Removes the toolbar from the shell
   * @param toolbar the toolbar that will be removed. Does nothing if the specified toolbar
   * is not found on this shell. If this is the last toolbar on the shell, the shell
   * is closed.
   */
  @Override
  public void removeShellPanel(final ShellPanel panel) {
    int index = tabbedPane.indexOfComponent(panel);
    if (index > -1) {
      tabbedPane.removeTabAt(index);
    }
    super.removeShellPanel(panel);

    //Handle the tab manu
    if ( closedTabcount == 0 ) {
      panelMenuBar.setVisible(true);
    }
    closedTabcount++;
    final JMenuItem mi = new JMenuItem(panel.getTitle());
    mi.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae) {
        panelMenu.remove(mi);
        closedTabcount--;
        addShellPanel(panel);
        if (closedTabcount == 0) {
          panelMenuBar.setVisible(false);
        }
      }
    });
    panelMenu.add(mi);
  }

  /**
   * This is what gets called by JPF.
   */
  public void start(final String[] args){
    // since we validate during pack(), this has to happen from the EventDispatchThread
    // or we risk deadlocks due to async (SwingWorker) pane content initialization
    // (which in turn might validate)
    SwingUtilities.invokeLater( new Runnable(){
      public void run(){
        ShellManager.getManager().setStartingArgs(args);
        loadPanelsFromConfig();
        pack();
        setSize(STARTING_SIZE);
        setLocationRelativeTo(null);
        setVisible(true);        
      }
    });
  }

  /**
   * Assume that JPF isn't calling this and that a config hasn't been made
   */
  public static void main(String[] args){
    ShellManager.createShellManager(new Config(args));
		ShellManager.getManager().setStartingArgs(args);
    new BasicShell().start(args);
  }

  static class CommandButton extends JButton{

    public CommandButton(ShellCommand c){
      super(c.getIcon());
      setOpaque(false);
      setContentAreaFilled(false);
      setRolloverEnabled(true);
      setFocusPainted(false);
      setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    
    @Override
    protected void paintComponent(Graphics g){
      GraphicsUtil.drawButtonBackground(g, this);
      super.paintComponent(g);
    }

  }

}