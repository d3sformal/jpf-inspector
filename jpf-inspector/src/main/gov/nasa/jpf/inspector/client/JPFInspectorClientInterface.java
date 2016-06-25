package gov.nasa.jpf.inspector.client;

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.frontends.swing.AuxiliaryInspectorPanel;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;

/**
 * The client part of the JPF Inspector.
 * This part takes input from the user and passes it, after parsing, to the server.
 */
public interface JPFInspectorClientInterface {
  /**
   * Parses and executes a command given by the user.
   * 
   * @param cmd The command to execute.
   */
  void executeCommand(String cmd, ExecutionContext context);

  /**
   * Returns true if the JPF thread has started but is currently paused.
   */
  boolean isPaused();

  /**
   * Connects the client to a specific instance of Java PathFinder.
   * 
   * Note: Should be called before JPF.run method is called.
   * Note: Modifies JPF Configuration.
   * 
   * @param jpf JPF to connect to.
   * @throws JPFInspectorGenericErrorException
   */
  void connect2JPF(JPF jpf) throws JPFInspectorGenericErrorException;

  /**
   * Adds a new panel as a listener to events of the client.
   *
   * These events are:
   *
   * * A command is executed.
   * * A callback his received.
   *
   * @param auxiliaryInspectorPanel A new listener.
   */
  void addInspectorListener(AuxiliaryInspectorPanel auxiliaryInspectorPanel);

  /**
   * Gets the Inspector server.
   */
  JPFInspectorBackEndInterface getServer();
}
