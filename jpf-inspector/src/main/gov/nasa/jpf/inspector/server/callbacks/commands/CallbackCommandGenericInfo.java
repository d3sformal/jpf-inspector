package gov.nasa.jpf.inspector.server.callbacks.commands;

import gov.nasa.jpf.inspector.interfaces.InspectorCallbacks;
import gov.nasa.jpf.inspector.server.callbacks.CallbackCommand;

public class CallbackCommandGenericInfo implements CallbackCommand {

  private final String msg;

  public CallbackCommandGenericInfo (String msg) {
    this.msg = msg;
  }

  @Override
  public void sendCallback (InspectorCallbacks callbacks) {
    callbacks.genericInfo(msg);
  }

  @Override
  public boolean waitJPF2stop () {
    return false;
  }

}
