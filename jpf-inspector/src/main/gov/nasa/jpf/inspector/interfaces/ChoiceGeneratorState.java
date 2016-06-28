package gov.nasa.jpf.inspector.interfaces;

import gov.nasa.jpf.vm.ChoiceGenerator;

public interface ChoiceGeneratorState {
  
  /**
   * @return Gets type or represented {@link ChoiceGenerator}
   */
  ChoiceGeneratorsInterface.CGTypes getChoiceGeneratorType();
  
  /**
   * @return Get {@link ChoiceGenerator#getId()} of represented CG
   */
  String getChoiceGeneratorID();
  
  /**
   * @return Gets list of all possible choices of given {@link ChoiceGenerator}
   */
  String[] getListOfChoices();
  
  /**
   * @return Gets index in the list {@link #getListOfChoices()} of choices which JPF schedules for next execution
   */
  int getNextChoicePlannedToUse();
  /**
   * @return Gets index in the list {@link #getListOfChoices()} of choice which were used the trace which were backward stepped by user. 
   */
  int getDefaultForwardThreadChoice();
}
