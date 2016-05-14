package gov.nasa.jpf.inspector.server.callbacks.commands;

import gov.nasa.jpf.inspector.interfaces.InspectorCallBacks;
import gov.nasa.jpf.inspector.server.callbacks.CallbackCommand;

public class CallbackCommandSpecifyChoiceToUse implements CallbackCommand {

  private final int maxChoiceIndex;

  public CallbackCommandSpecifyChoiceToUse (int maxChoiceIndex) {
    super();
    this.maxChoiceIndex = maxChoiceIndex;
  }

  @Override
  public void sendCallback (InspectorCallBacks callbacks) {
    callbacks.specifyChoiceToUse(maxChoiceIndex);
  }

  @Override
  public boolean waitJPF2stop () {
    return true;
  }

}
