package gov.nasa.jpf.inspector.server.callbacks;

import gov.nasa.jpf.inspector.interfaces.InspectorCallbacks;

public interface CallbackCommand {
  /**
   * Called to send represented callback.
   * 
   * @param callbacks Interface where sent the callback.
   */
  public void sendCallback(InspectorCallbacks callbacks);
  
  /**
   *  if false callback should be sent as soon as all previous callbacks are processed 
   *  if true callbacks should be sent if JPF is stopped.
   *  
   *  @return Specifies callback behavior.
   */
  public boolean waitJPF2stop();
}
