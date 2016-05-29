package gov.nasa.jpf.inspector.client;

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;

public interface JPFInspectorClientInterface {
  /**
   * Parse and execute given command in JPF Shell.
   * 
   * @param cmd Text with command to execute.
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
