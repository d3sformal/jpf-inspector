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

import gov.nasa.jpf.shell.listeners.TestCommandListener;
import gov.nasa.jpf.shell.*;
import gov.nasa.jpf.Config;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import javax.swing.Icon;

import static gov.nasa.jpf.shell.basicshell.images.JPFShell_Images.*;

/**
 * Launches the System Under Test in its own process. <b>Note:</b> This requires
 * that the configuration defines a <i>classpath</i> property. The command
 * launches a new process if the SuT is not already running or will kill it if
 * it already is.
 * This command not only allows for listeners to be aware of when the command is
 * executed but also when the application was started or terminated.
 * @see gov.nasa.jpf.shell.listeners.TestCommandListener
 */
public class TestCommand extends ShellCommand {

  public static final String NAME = "Test";

  private Icon working = getStatusIcon(StatusIcon.Working, "Application is running.");
  private Icon test = getCommandIcon(CommandIcon.Test, "Test this application.");

  private String classpath;
  private String target;
  private String[] args;

  protected Process sut = null;

  //If this is not null, then an application is running and this thread will 
  //terminate when it does.
  protected Thread appWatcherThread = null;

  //Added to this VM's shutdown hooks when ever the SuT is run, just incase this
  //VM closes, the appKiller will kill the SuT too.
  private AppKiller appKiller = null;

  @Override
  public String getName(){
    return NAME;
  }

  @Override
  public Icon getIcon(){
    return isApplicationRunning() ? working : test;
  }

  @Override
  public String getToolTip(){
    return isApplicationRunning() ? "terminate application test run" :
            "start application test run";
  }

  /**
   * If the SuT is running, terminate it, if not then try to start it
   */
  public void execute() {
    if ( isApplicationRunning() ){
      killApp();
    }else{
      startApplication();
    }
    requestShellUpdate();
  }

  /**
   * Checks if it is possible to launch the SuT with the given properties
   * available.
   * @return true if both the target and class properties are defined in the
   *              Config. False if otherwise.
   */
  @Override
  public boolean prepare(){
    //If the application isn't running yet, that means that we're checking if
    //we CAN run it.
    if ( isApplicationRunning() )//App is already runing, no need to pre to kill
      return true;
    Config c = ShellManager.getManager().getConfig();
    target = c.getTarget();
    classpath = c.getProperty("classpath");
    args = c.getTargetArgs();

    if (target == null){
      ShellManager.getManager().getLogger().log(Level.SEVERE, "The properties loaded don't define a \"target\".");
      return false;
    }

    if (classpath == null){
      ShellManager.getManager().getLogger().log(Level.SEVERE, "The properties loaded don't define a \"classpath\".");
      return false;
    }
    
    return true;
  }

  /**
   * Returns the OS command to be executed to launch the SuT as a String.
   * @return string as follows: <code>java -cp ${classpath} ${target} ${args}
   * </code> where the classpaths are delimited by {@link java.io.File#pathSeparatorChar}
   * and ${classpath}, ${target} and ${args} are all defined in the Config.
   */
  protected String getCommand(){
    StringBuilder command = new StringBuilder("java -cp ");
    command.append(classpath.replace(',', File.pathSeparatorChar));
    command.append(" ").append(target);
    if (args != null){
      for (String arg : args)
        command.append(" ").append(arg);
    }
    return command.toString();
  }

  /**
   * Starts SuT in its own process. Adds a Shutdown hook to the VM that will
   * kill the process if the VM exits. Then notifies the
   * TestCommandListeners that the SuT has started.
   */
  protected void startApplication(){
    String command = getCommand();
    try {
      appKiller = new AppKiller();
      Runtime.getRuntime().addShutdownHook( appKiller );
      sut = Runtime.getRuntime().exec(command);
    } catch (IOException ex) {
      ShellManager.getManager().getLogger().log(Level.SEVERE, "IO Error with command", ex);
    }
    ShellManager.getManager().updateCommand(this);

    //Waits for the sut to terminate, this will happen when the process ends
    //for whatever reason.
    appWatcherThread = new Thread(){
      @Override
      public void run(){
				try{
					sut.waitFor();
				}catch(InterruptedException ex){
					//Probably us trying to terminate this process
				}
				terminateApp();
      }
    };
    appWatcherThread.start();
    //Notify the listeners
    List<TestCommandListener> commandListeners = ShellManager.getManager().getCommandListeners(getClass(), TestCommandListener.class);
    for (TestCommandListener listener: commandListeners) {
			listener.applicationStarted(this);
    }
  }


  /**
   * Kills and cleans up the SuT. If the SuT is already did, then it just does
   * the cleaning. Cleaning includes: Notifying the TestCommandListeners that
   * the application has ended, removing the shutdown hook, notifying the 
   * shells that our state has changed back to not having a an app running.
   */
  protected void terminateApp(){
    //Should already be dead, but we can't be too sure of things now can we?
    sut.destroy();

    //Notify listeners that the application has ended
    for (TestCommandListener listener: ShellManager.getManager().getCommandListeners(getClass(), TestCommandListener.class)) {
			listener.applicationEnded(this);
    }

    sut = null;
    appWatcherThread = null;

    requestShellUpdate();
    try{
      Runtime.getRuntime().removeShutdownHook(appKiller);
    }catch(IllegalStateException ex){
      //Probably just a shutdown in progress exception
    }
  }

  /**
   * @return true if the sut is running, false if not.
   */
  public boolean isApplicationRunning(){
    return sut != null;
  }

  /**
   * @return the process of the SuT running, null if the SuT is not running.
   */
  public Process getApplication(){
    return sut;
  }

  /**
   * If the application is running, it triggers the appWatcherThread to kill it.
   */
  private void killApp(){
    if (appWatcherThread != null)
      appWatcherThread.interrupt(); //Kills the sut by ending the wait and
                                   //destructing
  }

  class AppKiller extends Thread{
    @Override
    public void run(){
      killApp();
    }
  }

}
