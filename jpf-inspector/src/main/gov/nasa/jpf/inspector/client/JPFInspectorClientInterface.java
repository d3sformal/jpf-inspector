package gov.nasa.jpf.inspector.client;

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;

/**
 * The client part of the JPF Inspector.
 * This part takes input from the user and passes it, after parsing, to the server.
 */
public interface JPFInspectorClientInterface {
  /**
   * Parse and execute a command given by the user.
   * 
   * @param cmd The command to execute.
   */

  public void executeCommand (String cmd);

  /**
   * Connect client to specific instance of Java PathFinder.
   * 
   * Note: Should be called before JPF.run method is called.
   * Note: Modifies JPF Configuration.
   * 
   * @param jpf JPF to connect to.
   * @throws JPFInspectorGenericErrorException
   */
  public void connect2JPF (JPF jpf) throws JPFInspectorGenericErrorException;

}
