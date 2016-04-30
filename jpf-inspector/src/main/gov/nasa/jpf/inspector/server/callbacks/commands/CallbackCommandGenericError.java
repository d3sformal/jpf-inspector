package gov.nasa.jpf.inspector.server.callbacks.commands;

import gov.nasa.jpf.inspector.interfaces.InspectorCallBacks;
import gov.nasa.jpf.inspector.server.callbacks.CallbackCommand;

public class CallbackCommandGenericError implements CallbackCommand {

  private final String msg;

  public CallbackCommandGenericError (String msg) {
    super();
    this.msg = msg;
  }

  @Override
  public void sendCallback (InspectorCallBacks callbacks) {
    callbacks.genericError(msg);
  }

  @Override
  public boolean waitJPF2stop () {
    return false;
  }

}
