package gov.nasa.jpf.inspector.server.callbacks.commands;

import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGTypes;
import gov.nasa.jpf.inspector.interfaces.InspectorCallbacks;
import gov.nasa.jpf.inspector.server.callbacks.CallbackCommand;

public class CallbackCommandChoiceGeneratorNewChoice implements CallbackCommand {

  private final CGTypes cgType;
  private final String cgName;
  private final int cgId;
  private final String[] choices;
  private final int nextChoice;
  private final int defaultChoice;

  public CallbackCommandChoiceGeneratorNewChoice (CGTypes cgType, String cgName, int cgId, String[] choices, int nextChoice, int defaultChoice) {
    this.cgType = cgType;
    this.cgName = cgName;
    this.cgId = cgId;
    this.choices = choices;
    this.nextChoice = nextChoice;
    this.defaultChoice = defaultChoice;
  }

  @Override
  public void sendCallback (InspectorCallbacks callbacks) {
    callbacks.notifyChoiceGeneratorNewChoice(cgType, cgName, cgId, choices, nextChoice, defaultChoice);
  }

  @Override
  public boolean waitJPF2stop () {
    return false;
  }

}
