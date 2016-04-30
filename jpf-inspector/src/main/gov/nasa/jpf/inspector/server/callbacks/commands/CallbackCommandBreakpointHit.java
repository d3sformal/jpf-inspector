package gov.nasa.jpf.inspector.server.callbacks.commands;

import gov.nasa.jpf.inspector.interfaces.BreakPointStatus;
import gov.nasa.jpf.inspector.interfaces.InspectorCallBacks;
import gov.nasa.jpf.inspector.server.callbacks.CallbackCommand;

public class CallbackCommandBreakpointHit implements CallbackCommand {

  private final BreakPointStatus bp;

  public CallbackCommandBreakpointHit (BreakPointStatus bp) {
    super();
    this.bp = bp;
  }

  @Override
  public void sendCallback (InspectorCallBacks callbacks) {
    callbacks.notifyBreakpointHit(bp);
  }

  @Override
  public boolean waitJPF2stop () {
    return false;
  }

}
