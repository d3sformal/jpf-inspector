package gov.nasa.jpf.inspector.server.callbacks.commands;

import gov.nasa.jpf.inspector.interfaces.InspectorCallbacks;
import gov.nasa.jpf.inspector.server.callbacks.CallbackCommand;

public class CallbackCommandSpecifyChoiceToUse implements CallbackCommand {

  private final int maxChoiceIndex;

  public CallbackCommandSpecifyChoiceToUse (int maxChoiceIndex) {
    this.maxChoiceIndex = maxChoiceIndex;
  }

  @Override
  public void sendCallback (InspectorCallbacks clientCallbacks) {
    clientCallbacks.specifyChoiceToUse(maxChoiceIndex);
  }

  @Override
  public boolean waitJPF2stop () {
    return true;
  }

}
