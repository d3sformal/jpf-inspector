package gov.nasa.jpf.inspector.tests.infrastructure;

import gov.nasa.jpf.inspector.interfaces.BreakPointStatus;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGTypes;
import gov.nasa.jpf.inspector.interfaces.CommandsInterface.InspectorStates;
import gov.nasa.jpf.inspector.interfaces.InspectorCallBacks;

import java.io.PrintStream;
import java.util.Arrays;

public class InspectorCallbacksLoggingDecorator implements InspectorCallBacks {

  private final PrintStream out;
  private final InspectorCallBacks cb;

  public InspectorCallbacksLoggingDecorator (PrintStream out, InspectorCallBacks cb) {
    super();
    this.out = out;
    this.cb = cb;
  }

  @Override
  public void notifyStateChange (InspectorStates newState, String details) {
    out.println(this.getClass().getSimpleName() + ".notifyStateChange(newState=" + newState + ", details=" + details + ")");
    cb.notifyStateChange(newState, details);
  }

  @Override
  public void genericError (String msg) {
    out.println(this.getClass().getSimpleName() + ".genericError(msg=" + msg + ")");
    cb.genericError(msg);
  }

  @Override
  public void genericInfo (String msg) {
    out.println(this.getClass().getSimpleName() + ".genericInfo(msg=" + msg + ")");
    cb.genericInfo(msg);
  }

  @Override
  public void notifyBreakpointHit (BreakPointStatus bp) {
    out.println(this.getClass().getSimpleName() + ".notifyBreakpointHit(bp=" + bp + ")");
    cb.notifyBreakpointHit(bp);
  }

  @Override
  public void notifyChoiceGeneratorNewChoice (CGTypes cgType, String cgName, int cgId, String[] choices, int nextChoice, int defaultChoice) {
    out.println(this.getClass().getSimpleName() + ".notifyChoiceGeneratorNewChoice(cgType=" + cgType + ", cgName=" + cgName + " cgId=" + cgId + ", choices[]="
        + Arrays.toString(choices) + ", nextChoice=" + nextChoice + ", defaultChoice=" + defaultChoice + ")");
    cb.notifyChoiceGeneratorNewChoice(cgType, cgName, cgId, choices, nextChoice, defaultChoice);
  }

  @Override
  public void specifyChoiceToUse (int maxChoiceIndex) {
    out.println(this.getClass().getSimpleName() + ".specifyChoiceToUse(maxChoiceIndex=" + maxChoiceIndex + ")");
    cb.specifyChoiceToUse(maxChoiceIndex);
  }

  @Override
  public void notifyUsedChoice (CGTypes cgType, String cgName, int cgId, int usedChoiceIndex, String usedChoice) {
    out.println(this.getClass().getSimpleName() + ".notifyUsedChoice(cgType=" + cgType + ", cgName=" + cgName + " cgId=" + cgId + ", usedChoiceIndex="
        + usedChoiceIndex + ", usedChoice=" + usedChoice + ")");
    cb.notifyUsedChoice(cgType, cgName, cgId, usedChoiceIndex, usedChoice);
  }

}
