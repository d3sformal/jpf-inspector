package gov.nasa.jpf.inspector.server.choicegenerators;

import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorState;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGTypes;

public class ChoiceGeneratorStateImpl implements ChoiceGeneratorState {

  private final ChoiceGeneratorsInterface.CGTypes cgType;
  private final String cgName;
  private final String[] choices;
  private final int nextChoice;
  private final int defaultChoice;
  
  
  public ChoiceGeneratorStateImpl(CGTypes cgType, String cgName, String[] choices, int nextChoice, int defaultChoice) {
    this.cgType = cgType;
    this.cgName = cgName;
    this.choices = choices;
    this.nextChoice = nextChoice;
    this.defaultChoice = defaultChoice;
  }

  @Override
  public CGTypes getChoiceGeneratorType() {
    return cgType;
  }

  @Override
  public String getChoiceGeneratorID() {
    return cgName;
  }

  @Override
  public String[] getListOfChoices() {
    return choices;
  }

  @Override
  public int getNextChoicePlannedToUse() {
    return nextChoice;
  }

  @Override
  public int getDefaultForwardThreadChoice() {
    return defaultChoice;
  }

}
