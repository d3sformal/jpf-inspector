package gov.nasa.jpf.shell.panels.searchgraph;

import gov.nasa.jpf.jvm.JVM;

public class TransitionEdgeInfo extends EdgeInfo {

  private String edgeText = "";

	//State information
	boolean endState, newState;

  /**
   * @param vm the virtual machine that will be filling this edge with info
   */
  public TransitionEdgeInfo(JVM vm){
    super(vm.getChoiceGenerator().getNextChoice().toString());
    if (vm.getChoiceGenerator().getChoiceType().isAssignableFrom(Integer.class) ) {
      edgeText = vm.getChoiceGenerator().getNextChoice().toString();
    }
  }

  @Override
  public String getEdgeText(){
    return edgeText;
  }


}
