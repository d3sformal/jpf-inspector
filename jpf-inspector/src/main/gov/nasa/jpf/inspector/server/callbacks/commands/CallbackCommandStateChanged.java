package gov.nasa.jpf.inspector.server.callbacks.commands;

import gov.nasa.jpf.inspector.interfaces.CommandsInterface;
import gov.nasa.jpf.inspector.interfaces.CommandsInterface.InspectorStates;
import gov.nasa.jpf.inspector.interfaces.InspectorCallBacks;
import gov.nasa.jpf.inspector.server.callbacks.CallbackCommand;

public class CallbackCommandStateChanged implements CallbackCommand {
  private final String details;
  private final CommandsInterface.InspectorStates state;
  
   
  public CallbackCommandStateChanged(InspectorStates state, String details) {
    this.state = state;
    this.details = details;
  }

  @Override
  public void sendCallback(InspectorCallBacks callbacks) {
    callbacks.notifyStateChange(state, details);
  }

  @Override
  public boolean waitJPF2stop() {
    return state == InspectorStates.JPF_STOPPED;
  }

}
