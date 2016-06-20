package gov.nasa.jpf.inspector.server.callbacks.commands;

import gov.nasa.jpf.inspector.interfaces.InspectorStatusChange;
import gov.nasa.jpf.inspector.interfaces.InspectorCallbacks;
import gov.nasa.jpf.inspector.server.callbacks.CallbackCommand;

public class CallbackCommandStateChanged implements CallbackCommand {
  private final String details;
  private final InspectorStatusChange state;
  
   
  public CallbackCommandStateChanged(InspectorStatusChange state, String details) {
    this.state = state;
    this.details = details;
  }

  @Override
  public void sendCallback(InspectorCallbacks clientCallbacks) {
    clientCallbacks.notifyStateChange(state, details);
  }

  @Override
  public boolean waitJPF2stop() {
    return state == InspectorStatusChange.JPF_STOPPED;
  }

}
