package gov.nasa.jpf.inspector.server.callbacks.commands;

import gov.nasa.jpf.inspector.interfaces.BreakpointStatus;
import gov.nasa.jpf.inspector.interfaces.InspectorCallbacks;
import gov.nasa.jpf.inspector.server.callbacks.CallbackCommand;

public class CallbackCommandBreakpointHit implements CallbackCommand {

  private final BreakpointStatus bp;

  public CallbackCommandBreakpointHit (BreakpointStatus bp) {
    this.bp = bp;
  }

  @Override
  public void sendCallback (InspectorCallbacks callbacks) {
    callbacks.notifyBreakpointHit(bp);
  }

  @Override
  public boolean waitJPF2stop () {
    return false;
  }

}
