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
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.JPFConfigException;
import gov.nasa.jpf.shell.util.LinkDestination;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

/**
 * A singleton object that manages commands, shells and resources.<br>
 * The <code>ShellManager</code> has the following responsibilities:
 * <ul>
 *   <li>Begin the execution of
 *       {@link gov.nasa.jpf.shell.ShellCommand#execute()}</li>
 *   <li>Notify all Shells when a command is either added, removed or requests
 *       to be updated</li>
 *   <li>Manage {@link gov.nasa.jpf.ShellCommandListener} instances</li> 
 *   <li>Exit the System once all {@link gov.nasa.jpf.shell.Shell} instances are
 *       disposed of</li>
 *   <li>Hold a reference to the single {@link gov.nasa.jpf.Config} object</li> 
 *   <li>Serve as a logging facility for Shell implementations</li> 
 *   <li>Serve as a conduit to the outside world. (Right now this means opening
 *       files in IDE's, IO streams and shell/socket management)</li>
 * </ul>
 *
 * Clients have the following responsibilities to the <code>ShellManager</code>:
 * <ul>
 *   <li>No Shell is created before a ShellManager exists (check Shell
 *       constructors to see which will create ShellManager before hand)</li>
 *   <li>Add all created shells to the ShellManager using 
 *       {@link #addShell(gov.nasa.jpf.shell.Shell)} (If your Shell class is a
 *        subclass of Shell you should be covered) </li>
 *   <li>All disposed Shells are removed from the Manager (Once again, if your
 *       Shell class is a subclass of Shell you should be covered)</li>
 *   <li>All commands are executed through the ShellManager</li>
 * </ul>
 */
public class ShellManager {

  /**
   * If true then when all shells are disposed of System.exit(0) will be called.
   * <br> Default is <b>true</b>.
   */
  public static boolean SYSTEM_EXIT_ON_SHELL_CLOSE = true;

  //Holds the singleton instance of ShellManager
  private static ShellManager manager;

  /**
   * Creates a new ShellManager with the given Config,
   * @throws IllegalStateException if a ShellManager already exists
   * @param c the Config to base this ShellManager around
   * @return the new singleton instance of the ShellManager
   */
  public static ShellManager createShellManager(Config c){
    setManager(new ShellManager(c));
    return getManager();
  }

  /**
   * @throws IllegalStateException if no ShellManager has been created
   * @return the single <code>ShellMonitor</code> instance
   */
  public static ShellManager getManager(){
    if (manager == null){
      Thread.dumpStack();
      throw new IllegalStateException("No ShellManager exists");
    }
    return manager;
  }

  /**
   * Sets the ShellManager to the one given 
   * @throws IllegalStateException if a manager is already set 
   * @param m the manager to be used. 
   */
  public static void setManager(ShellManager m){
    if (manager == null){
      manager = m;
    } else {
      throw new IllegalStateException("ShellManager already set!");
    }
  }

  //Holds this config for this set of shells.
  private Config config;
	private String[] startingArgs;

  //Logging
  private static final Logger shellLog = Logger.getLogger("gov.nasa.jpf.shell");
  static {
    //We don't want the error being reported twice in the ErrorPanel
    shellLog.setUseParentHandlers(false);
    shellLog.setLevel(Level.INFO);
  }

  public static Logger getLogger(){
    return shellLog;
  }
  
  //Holds all of the known shells
  private ArrayList<WeakReference<Shell>> shells =
	  new ArrayList<WeakReference<Shell>>();

  //Holds a mapping of ShellCommands to their sclisteners.
//  private HashMap<ShellCommand, List<ShellCommandListener>> listeners =
//	  new HashMap<ShellCommand, List<ShellCommandListener>>();

  //Holds a mapping of ShellCommandClasses to their sclisteners.
  private HashMap<Class<? extends ShellCommand>, List<ShellCommandListener>> 
	  classlisteners = new HashMap<Class<? extends ShellCommand>,
	                               List<ShellCommandListener>>();
  
  //Stream to outside program
  private PrintWriter ideOut = null;

  //Holds a list of all commands that are currently registered
  private ArrayList<ShellCommand> commands = new ArrayList<ShellCommand>();

  /**
   * Creates a new ShellManager with the given Config. If the shell.port
   * property is set in the config, then a connection is made to the port to 
   * communicate with.
   * @param c
   */
  private ShellManager(Config c){
    this.config = c;

		shellLog.addHandler(new StreamHandler(System.out,new SimpleFormatter()));

    //Setup IDE port
    final int p = c.getInt("shell.port", -1);
    if (p >= 0){
      new Thread(){
        @Override
        public void run(){
          try {
            ServerSocket s = new ServerSocket(p);
            Socket c = s.accept();
            ideOut = new PrintWriter(c.getOutputStream(), true);
          } catch (IOException ex) {
            shellLog.log(Level.SEVERE, "failed to open shell port", ex);
          }
        }
      }.start();
    }

    try {
      ShellCommand[] commands = c.getGroupInstances("shell.commands", null, ShellCommand.class,
              ".shell.commands.TestCommand",
              ".shell.commands.VerifyCommand");
      for (ShellCommand cmd : commands) {
        addCommand(cmd);
      }
    } catch (JPFConfigException cex){
      shellLog.log(Level.SEVERE, "failed to initialize commands", cex); 
    }    
  }

  /**
   * Adds a Shell to this ShellManager. Once a Shell is added all commands that
   * have been added to the manager are installed onto the shell.
   * @param shell the shell to be added, does nothing if the shell has already
   *	    been added to the manager
   */
  public void addShell(Shell shell){
    //Check if this shell is already added
    for (WeakReference<Shell> r : shells) {
      if(shell.equals(r.get())){
        return;
      }
    }

    shells.add(new WeakReference(shell));

    for (ShellCommand command : commands) {
      if (command != null) {
        shell.installCommand(command);
      }
    }
  }

  /**
   * Removes the shell from this manager. If this manager has no other shells
   * and {@link #SYSTEM_EXIT_ON_SHELL_CLOSE} is <code>TRUE</code> then
   * <code>System.exit(0)</code>
   * @param shell the shell to be removed
   */
  public void removeShell(Shell shell){
    WeakReference w = null;
    for (WeakReference<Shell> r : shells) {
      if (shell.equals(r.get())){
        shells.remove(r);
        break;
      }
    }
    
    if (SYSTEM_EXIT_ON_SHELL_CLOSE && shells.isEmpty()){
      System.exit(0);
    }
  }

  /**
   * @return this manager's configuration
   */
  public Config getConfig(){
    return config;
  }


  /**
   * Returns all of the known Shell instances
   * @return an array containing all of the known shells.
   */
  public Shell[] getShells(){
    return shells.toArray(new Shell[shells.size()]);
  }

  /**
   * Searches all shells for the ShellPanel of the given class.
   * @param panelClass the ShellPanel class being searched for. The first one
   *		     found is returned.
   * @return the ShellPanel found or null if none was found
   */
  public <T extends ShellPanel> T findPanel(Class<T> panelClass) {
    for (WeakReference<Shell> r : shells) {
      T panel = r.get().getShellPanel(panelClass);
      if (panel != null)
        return panel;
    }
    return null;
  }

  /**
   * Adds a listener for the command
   * @param comman the command to listen for
   * @param listener the listener to register for the command
   */
  public void addCommandListener(ShellCommand command,
                                 ShellCommandListener listener){
    addCommandListener(command.getClass(), listener);
  }

  /**
   * Adds the listener to all commands that are a subclass of the <code>commandClass</code>
   * @param commandClass - the ShellCommand class to listen for
   * @param listener - The listener to be executed when the ShellCommand executes
   */
  public void addCommandListener(Class<? extends ShellCommand> commandClass,
                                 ShellCommandListener listener ){
    if (!classlisteners.containsKey(commandClass)) {
      classlisteners.put(commandClass, new ArrayList<ShellCommandListener>());
    }
    classlisteners.get(commandClass).add(listener);
  }

  /**
   * Removes the given ShellCommandListener from any commands that it is registered to
   * @param listener - the ShellCommandListener to be removed
   */
  public void removeCommandListener(ShellCommandListener listener){
    for (List<ShellCommandListener> list : classlisteners.values()) {
      if (list.contains(listener)) {
        list.remove(listener);
      }
    }
  }
  
  /**
   * Used to add a command to this manager. This is the method that should be
   * used to add new commands. <br>
   * The command is added and then all shells install the command.
   * @param command the command to be added
   */
  public void addCommand(ShellCommand command){
    commands.add(command);

    for (WeakReference<Shell> shellref : shells) {
      Shell s;
      if ((s = shellref.get()) != null){
        s.installCommand(command);
      }
    }
  }

  public <T extends ShellCommand> List<T> getCommands(Class<T> c){
    ArrayList<T> r = new ArrayList<T>();
    for (ShellCommand s : commands){
      if (s.getClass().isAssignableFrom(c))
        r.add((T) s);
    }
    return r;
  }

  /**
   * Removes the command from this ShellManager and un-installs it from all 
   * Shells.
   * @param command
   */
  public void removeCommand(ShellCommand command){
    commandRemoval(command);
    commands.remove(command);
  }

  private void commandRemoval(ShellCommand command){
    for (WeakReference<Shell> r : shells) {
      r.get().uninstallCommand(command);
    }
  }

  /**
   * Removes any command that is a subclass of the the given class.
   * (Helps to override commands with new subclasses see UIShell in jpf-awt-shell)
   */
  public void removeCommand(Class<? extends ShellCommand> c){
    ArrayList<ShellCommand> r = new ArrayList<ShellCommand>();
    for (ShellCommand command : commands) {
      if (command.getClass().isAssignableFrom(c)){
				commandRemoval(command);
				r.add(command);
      }
    }
    commands.removeAll(r);
  }

  /**
   * Gives access to the first shell that was added to this manager. Only really
   * used when access is needed to a JFrame so that a dialog can appear.
   * @return the first Shell added to this manager
   */
  public Shell getShell(){
    for(WeakReference<Shell> r : shells)
      if (r.get() != null){
	return r.get();
      }
    return null;
  }

  /**
   * This method that should be called to launch/fire/execute a command. It first
   * calls {@link gov.nasa.jpf.r.ShellCommand#prepare()} to first determine
   * whether to continue and notify the sclisteners. If prepare() is true then
   * {@link gov.nasa.jpf.r.ShellCommandListener#preCommand(gov.nasa.jpf.r.ShellCommand)}
   * is executed for all sclisteners to this command. The command is then executed via
   * {@link gov.nasa.jpf.r.ShellCommand#execute()}. All the sclisteners are then
   * once again notified about the completion of the command through
   * {@link gov.nasa.jpf.r.ShellCommandListener#postCommand(gov.nasa.jpf.r.ShellCommand)}
   *
   * @param command the command who's sclisteners and execute method will be fired.
   */
  public void fireCommand(ShellCommand command){
		if (command.prepare()){
      List<ShellCommandListener> sclisteners = getCommandListeners(command);
      for (ShellCommandListener scl : sclisteners) { 
        try{
          scl.preCommand(command);
        }catch(Exception e){
          getLogger().log(Level.SEVERE, "Error in preCommand", e);
        }
      }
			command.execute();
      for (ShellCommandListener scl : sclisteners) {
        try{
          scl.postCommand(command);
        }catch(Exception e){
          getLogger().log(Level.SEVERE, "Error in postCommand", e);
        }
      }
		}
  }

  public List<ShellCommandListener> getCommandListeners(ShellCommand c){
    List<ShellCommandListener> list = new ArrayList<ShellCommandListener>();
    for (Class ls : classlisteners.keySet()) {
      if (ls.isAssignableFrom(c.getClass())) {
        list.addAll(classlisteners.get(ls));
      }
    }
    return list;
  }

  /**
   * Fires a ShellCommand to be executed.<br>
   * The following occurs when a ShellCommand is fired:
   * <ol>
   *  <li>All of the ShellCommand'manager sclisteners preCommands are executed in the
   * order that the sclisteners were added</li>
   *  <li>ShellCommand.execute() is called</li>
   *  <li>All of the ShellCommand'manager sclisteners postCommands are executed in the
   * order that the sclisteners were added</li>
   * </ol>
   * @param commandClass - the class of the command being executed.
   */
  public void fireCommand(Class<? extends ShellCommand> commandClass){
    for (ShellCommand command : commands) {
      if (commandClass.isAssignableFrom(command.getClass())){
        fireCommand(command);
      }
    }
  }

  public <C extends ShellCommand> C getCommand(Class<C> cls){
    for (ShellCommand shellCommand : commands) {
      if (cls.isAssignableFrom(shellCommand.getClass())) {
        return (C) shellCommand;
      }
    }
    return null;
  }

	/**
	 * @return all of the command sclisteners of a certain type
	 */
	public <E extends ShellCommandListener> List<E> getCommandListeners(Class<? extends ShellCommand> commandType, Class<E> listenerType){
		ArrayList<E> arrayList = new ArrayList<E>();

    for (Class<? extends ShellCommand> class1 : classlisteners.keySet()) {
      if (class1.isAssignableFrom(commandType)) {
        for (ShellCommandListener shellCommandListener : classlisteners.get(class1)) {
          if (listenerType.isAssignableFrom(shellCommandListener.getClass())) {
            arrayList.add((E) shellCommandListener);
          }
        }
      }
    }

		return arrayList;
	}

  /**
   * Notifies all shells that a command wishes to be updated. This should be
   * used to notify Shells to update how they display commands because the
   * icon, tool-tip or name of the command changed.
   * @param command the command that needs to be updated
   */
  public void updateCommand(ShellCommand command){
    for (WeakReference<Shell> r : shells) {
      Shell shell = r.get();
      if (shell != null){
        shell.updateShellCommand(command);
      }
    }
  }

	public void updateShellPanel(ShellPanel panel){
		for (WeakReference<Shell> r : shells){
      Shell shell = r.get();
      if (shell != null){
        shell.updateShellPanel(panel);
      }
		}
	}

  /**
   * Attempts to open the given file in an editor.<br>
   * If there is already a connection made to the editor through the use of the
   * "shell.port" property then the following string is printed to it:<br>
   * <code>[LINK] <i>path</i>:<i>line</i></code><br>
   * Where path is the absolute path to the file and line is the line number of
   * of the file starting with 1.<br>
   * If there is no connection made to an editor then if "shell.editor" is
   * defined its value is executed as a command with "#{file}" replaced by 
   * the absolute path of the file and "#{linenumber}" is replaced by the line
   * number in the file starting with 1
   * @param d the destination of the link
   */
  public void printLinkCommand(LinkDestination d){
    if (ideOut != null){
      ideOut.println("[LINK]" + d );
    }else{
      String e = getConfig().getProperty("shell.editor", "");
      if (e.isEmpty() == false){
        e = e.replace("#{file}", d.path);
        e = e.replace("#{linenumber}", String.valueOf(d.line));
        try {
          Runtime.getRuntime().exec(e);
        } catch (IOException ex) {
          getLogger().severe("Command: \"" + e +"\" could not be executed "+ ex);
        }
      }
    }
  }

  /**
   * Prints the given command to the connection made to "shell.port"
   * @param command
   */
  public void printCommand(String command){
    if (ideOut != null){
      //A socket is connected, just give them the comannds
      ideOut.print(command);    }
  }

	public void setStartingArgs(String[] args){
		this.startingArgs = args;
	}

	public void reloadAppProperties(String appProperties){
    String[] newArgs = replaceAppProperties(startingArgs, appProperties);
    if (newArgs != startingArgs){
      startingArgs = newArgs;
      
      config = JPF.createConfig(startingArgs);

      for (WeakReference<Shell> wref : shells) {
        Shell shell = wref.get();
        if (shell != null) {
          shell.configChanged(config);
        }
      }
    }
	}

  public void reloadSiteProperties (String siteProperties){
    String[] newArgs = replaceSiteProperties(startingArgs, siteProperties);
    if (newArgs != startingArgs){
      startingArgs = newArgs;
      config = JPF.createConfig(startingArgs);

      for (WeakReference<Shell> wref : shells) {
        Shell shell = wref.get();
        if (shell != null) {
          shell.configChanged(config);
        }
      }
    }    
  }

  String[] replaceSiteProperties (String[] args, String siteProperties){
    String[] newArgs;

    if (args == null || args.length == 0){
      if (siteProperties == null){
        return args;
      } else {
        newArgs = new String[1];
        newArgs[0] = "+site=" + siteProperties;
      }
      
    } else {
      int i;
      for (i=0; i<args.length; i++){
        if (args[i].startsWith("+site=")){
          break;
        }
      }
      if (i < args.length){
        if (siteProperties == null){ // remove the previous one
          newArgs = new String[args.length -1];
          if (i > 0){
            System.arraycopy(args,0, newArgs,0, i-1);
          }
          if (i < args.length-1){
            System.arraycopy(args,i+1, newArgs, i, newArgs.length-i);
          }
        } else { // replace the previous one
          newArgs = args.clone();
          newArgs[i] = "+site=" + siteProperties;
        }
        
      } else { // add the new one
        newArgs = new String[args.length+1];
        newArgs[0] = "+site=" + siteProperties;
        System.arraycopy(args, 0, newArgs, 1, args.length);
      }
    }
    
    return newArgs;
  }
  
  String[] replaceAppProperties (String[] args, String appProperties){
    String[] newArgs;

    if (args == null || args.length == 0){
      if (appProperties != null){
        newArgs = new String[1];
        newArgs[0] = appProperties; 
      } else {
        newArgs = new String[0];
      }
      
    } else {
      int i;
      for (i=args.length-1; i>=0; i--){
        if (args[i].endsWith(".jpf")){
          break;
        }
      }
      
      if (i>= 0){ // we have to replace previous appProperties
        
        if (appProperties != null){ // replace and dump trailing app arguments
          newArgs = new String[i + 1];
          if (i>0){
            System.arraycopy(args, 0, newArgs, 0, i-1);
          }
          newArgs[i] = appProperties;
          
        } else { // dump previous appProperties and trailing app args
          newArgs = new String[i];
          if (i>0){
            System.arraycopy(args, 0, newArgs, 0, i-1);
          }
        }
        
      } else { // no previous appProperties
        if (appProperties != null){ // append
          newArgs = new String[args.length + 1];
          System.arraycopy(args, 0, newArgs, 0, args.length);
          newArgs[args.length] = appProperties;
          
        } else {
          newArgs = args; // no change
        }
      }
    }
      
    return newArgs;
  }
  
  public boolean hasShell(Shell aThis) {
    for (WeakReference<Shell> shell : shells) {
      if (shell.get() == aThis) {
        return true;
      }
    }
    return false;
  }

}
