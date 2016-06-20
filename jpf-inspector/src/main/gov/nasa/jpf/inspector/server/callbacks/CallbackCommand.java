package gov.nasa.jpf.inspector.server.callbacks;

import gov.nasa.jpf.inspector.interfaces.InspectorCallbacks;

/**
 * A callback command is a server-side class that sends a specific callback to the client. Each class corresponds
 * to a type of callback.
 */
public interface CallbackCommand {
  /**
   * Sends this callback to the client. Only the callback sending thread calls this method.
   * 
   * @param clientCallbacks Client-side class that displays callbacks to the user.
   */
  void sendCallback(InspectorCallbacks clientCallbacks);
  
  /**
   * Indicates whether the callback sending thread should wait for JPF to be paused.
   *
   *  If false, the callback should be sent as soon as all previous callbacks are processed.
   *  If true, the callbacks should only be sent once JPF is stopped. Until then, the callback sending thread
   *  will be blocked.
   */
  boolean waitJPF2stop();
}
