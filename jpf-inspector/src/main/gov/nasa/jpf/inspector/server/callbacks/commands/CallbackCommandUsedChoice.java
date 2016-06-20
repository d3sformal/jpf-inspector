package gov.nasa.jpf.inspector.server.callbacks.commands;

import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGTypes;
import gov.nasa.jpf.inspector.interfaces.InspectorCallbacks;
import gov.nasa.jpf.inspector.server.callbacks.CallbackCommand;

public class CallbackCommandUsedChoice implements CallbackCommand {

  private final CGTypes cgType;
  private final String cgName;
  private final int cgId;
  private final int usedChoiceIndex;
  private final String usedChoice;

  public CallbackCommandUsedChoice (CGTypes cgType, String cgName, int cgId, int usedChoiceIndex, String usedChoice) {
    this.cgType = cgType;
    this.cgName = cgName;
    this.cgId = cgId;
    this.usedChoiceIndex = usedChoiceIndex;
    this.usedChoice = usedChoice;
  }

  @Override
  public void sendCallback (InspectorCallbacks clientCallbacks) {
    clientCallbacks.notifyUsedChoice(cgType, cgName, cgId, usedChoiceIndex, usedChoice);
  }

  @Override
  public boolean waitJPF2stop () {
    return false;
  }

}
