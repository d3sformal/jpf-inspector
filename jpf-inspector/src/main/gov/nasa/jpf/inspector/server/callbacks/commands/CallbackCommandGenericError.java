package gov.nasa.jpf.inspector.server.callbacks.commands;

import gov.nasa.jpf.inspector.interfaces.InspectorCallbacks;
import gov.nasa.jpf.inspector.server.callbacks.CallbackCommand;

/**
 * Sends the {@link InspectorCallbacks#genericError(String)} callbacks.
 */
public class CallbackCommandGenericError implements CallbackCommand {

  private final String msg;

  public CallbackCommandGenericError (String msg) {
    this.msg = msg;
  }

  @Override
  public void sendCallback (InspectorCallbacks clientCallbacks) {
    clientCallbacks.genericError(msg);
  }

  @Override
  public boolean waitJPF2stop () {
    return false;
  }

}
