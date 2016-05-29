package gov.nasa.jpf.inspector.client;

import gov.nasa.jpf.inspector.client.commands.CmdCallback;
import gov.nasa.jpf.inspector.interfaces.BreakPointStatus;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGTypes;
import gov.nasa.jpf.inspector.interfaces.CommandsInterface.InspectorStates;
import gov.nasa.jpf.inspector.interfaces.InspectorCallBacks;

/**
 * Monitors and records callbacks into list of executed commands.
 * These recorded callbacks are "hidden commands" that are not displayed to the user.
 * 
 * @author Alf *
 */
public class CallbackRecordingDecorator implements InspectorCallBacks {

  private final InspectorCallBacks cb;
  private final CommandRecorder cmdRecorder;

  public CallbackRecordingDecorator (InspectorCallBacks cb, CommandRecorder recorder) {
    super();
    this.cb = cb;
    this.cmdRecorder = recorder;
  }

  @Override
  public void notifyStateChange (InspectorStates newState, String details) {
    CmdCallback cmdCB = new CmdCallback(newState);
    cmdRecorder.recordCallback(cmdCB);

    cb.notifyStateChange(newState, details);
  }

  @Override
  public void genericError (String msg) {
    CmdCallback cmdCB = new CmdCallback(CB_METHODS.CB_GENERIC_ERROR);
    cmdRecorder.recordCallback(cmdCB);

    cb.genericError(msg);
  }

  @Override
  public void genericInfo (String msg) {
    CmdCallback cmdCB = new CmdCallback(CB_METHODS.CB_GENERIC_INFO);
    cmdRecorder.recordCallback(cmdCB);

    cb.genericInfo(msg);
  }

  @Override
  public void notifyBreakpointHit (BreakPointStatus bp) {
    CmdCallback cmdCB = new CmdCallback(CB_METHODS.CB_BREAKPOINT_HIT);
    cmdRecorder.recordCallback(cmdCB);

    cb.notifyBreakpointHit(bp);
  }

  @Override
  public void notifyChoiceGeneratorNewChoice (CGTypes cgType, String cgName, int cgId, String[] choices, int nextChoice, int defaultChoice) {
    CmdCallback cmdCB = new CmdCallback(CB_METHODS.CB_CG_NEW_CHOICE);
    cmdRecorder.recordCallback(cmdCB);

    cb.notifyChoiceGeneratorNewChoice(cgType, cgName, cgId, choices, nextChoice, defaultChoice);
  }

  @Override
  public void specifyChoiceToUse (int maxChoiceIndex) {
    CmdCallback cmdCB = new CmdCallback(CB_METHODS.CB_CG_CHOICE_TO_USE);
    cmdRecorder.recordCallback(cmdCB);

    cb.specifyChoiceToUse(maxChoiceIndex);
  }

  @Override
  public void notifyUsedChoice (CGTypes cgType, String cgName, int cgId, int usedChoiceIndex, String usedChoice) {
    CmdCallback cmdCB = new CmdCallback(CB_METHODS.CB_CG_USED_CHOICE);
    cmdRecorder.recordCallback(cmdCB);

    cb.notifyUsedChoice(cgType, cgName, cgId, usedChoiceIndex, usedChoice);
  }

}
