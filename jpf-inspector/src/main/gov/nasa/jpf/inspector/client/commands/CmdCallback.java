package gov.nasa.jpf.inspector.client.commands;

import gov.nasa.jpf.inspector.client.CallbackExecutionDecorator;
import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.CommandRecorder;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.CommandsInterface.InspectorStates;
import gov.nasa.jpf.inspector.interfaces.InspectorCallBacks;
import gov.nasa.jpf.inspector.interfaces.InspectorCallBacks.CB_METHODS;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;

import java.io.PrintStream;

/**
 * Represents a hidden callback command that suspends execution of commands until a callback of the specified
 * kind is received from the server. This only works in record execution mode.
 */
public class CmdCallback extends ClientCommand {

  private final CB_METHODS cbType;

  // Valid only for CB_STATE_CHANGE - ignored otherwise
  // In CB_STATE_CHANGE - null means any state, otherwise states have to equal
  private final InspectorStates state;

  public CmdCallback (CB_METHODS cbType) {
    this.cbType = cbType;
    this.state = null;
  }

  /**
   * Represents {@link InspectorCallBacks#notifyStateChange(InspectorStates, String)} with give state.
   * 
   * @param state State used in State changed notifications
   */
  public CmdCallback (InspectorStates state) {
    this.cbType = CB_METHODS.CB_STATE_CHANGE;
    this.state = state;
  }

  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    CallbackExecutionDecorator cb = client.getDecoratedCallbacks();

    switch (cbType) {
    case CB_STATE_CHANGE:
      cb.nextCB_StateChange(state);
      break;
    case CB_GENERIC_ERROR:
      cb.nextCB_genericError();
      break;
    case CB_GENERIC_INFO:
      cb.nextCB_genericInfo();
      break;
    case CB_BREAKPOINT_HIT:
      cb.nextCB_BreakpointHit();
      break;
    case CB_CG_NEW_CHOICE:
      cb.nextCB_ChoiceGeneratorNewChoice();
      break;
    case CB_CG_CHOICE_TO_USE:
      cb.nextCB_specifyChoiceToUse();
      break;
    case CB_CG_USED_CHOICE:
      cb.nextCB_UsedChoice();
      break;
    default:
      throw new RuntimeException("Unknown enum " + cbType.getClass().getName() + " entry " + cbType);
    }

    // Errors are reported in CommandRecorder.execute (to prevent multiplication of error log in each replay)

  }

  @Override
  public String getNormalizedCommand () {
    switch (cbType) {
    case CB_STATE_CHANGE:
      return "callback_state_change state=" + inspectorState2String(state);
    case CB_GENERIC_ERROR:
      return "callback_generic_error";
    case CB_GENERIC_INFO:
      return "callback_generic_info";
    case CB_BREAKPOINT_HIT:
      return "callback_breakpoint_hit";
    case CB_CG_NEW_CHOICE:
      return "callback_choice_generator_new_choice";
    case CB_CG_CHOICE_TO_USE:
      return "callback_choice_generator_choice_to_use";
    case CB_CG_USED_CHOICE:
      return "callback_choice_generator_used_choice";
    default:
      throw new RuntimeException("Unknown enum " + cbType.getClass().getName() + " entry " + cbType);
    }
  }

  @Override
  public void recordCommand (CommandRecorder rec) {
    rec.recordCallback(this);
  }

  @Override
  public boolean isHiddenCommand () {
    return true;
  }

  private static String inspectorState2String(InspectorStates state) {
    assert (state != null);

    switch (state) {
    case JPF_RUNNING:
      return "running";
    case JPF_STARTED:
      return "started";
    case JPF_STOPPED:
      return "stopped";
    case JPF_TERMINATING:
      return "terminating";
    default:
      throw new RuntimeException("Unknown enum " + state.getClass().getName() + " entry " + state);
    }
  }
}
