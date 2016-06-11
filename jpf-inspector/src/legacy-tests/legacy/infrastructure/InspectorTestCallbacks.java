package gov.nasa.jpf.inspector.tests.legacy.infrastructure;

import gov.nasa.jpf.inspector.interfaces.BreakPointStatus;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGTypes;
import gov.nasa.jpf.inspector.interfaces.CommandsInterface.InspectorStates;
import gov.nasa.jpf.inspector.interfaces.InspectorCallBacks;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;

import java.io.PrintStream;

/**
 * Helps to check Callbacks from JPF-Inspector. Is intended to be used by test drivers.
 * 
 * Conventions: - synchronization - If any callback is reported to the user (return from nextCB_* and waitForCB_*) than JPF-Inspector is stopped in the callback
 * method (JPF thread is blocked) to resume execution call {@link #finishCallback()} method. - nextCB_* methods block and wait for a next callback from the
 * inspector if the callback is different then expected one, test fails - waitForCB_ methods block and ignore all Inspector callbacks from the inspector until
 * the specified one take place - cached values of the callback parameters are accessible only before {@link #finishCallback()} call
 * 
 * @author Alf
 * 
 */
public class InspectorTestCallbacks implements InspectorCallBacks {

  protected static final boolean DEBUG = false;
  protected final PrintStream out = System.out;

  protected JPFInspector inspector;

  protected enum CB_METHODS {
    CB_STATE_CHANGE,
    CB_GENERIC_ERROR,
    CB_GENERC_INFO,
    CB_BREAKPOINT_HIT,
    CB_CG_NEW_CHOICE,
    CB_CG_CHOICE_TO_USE,
    CB_CG_USED_CHOICE
  }

  protected enum WAIT_STATE {
    NEXT_CB,
    WAIT_FOR_CB
  };

  protected CB_METHODS method = null;
  protected WAIT_STATE state = null;

  protected InspectorStates cbStateChange_expectedState = null;

  // Cached CB parameters
  protected InspectorStates cbStateChange_newState = null;
  protected String cbStateChange_details = null;

  protected BreakPointStatus cbBreakpointHit_bp = null;

  protected void cleanUpChackedParameters () {
    cbStateChange_newState = null;
    cbStateChange_details = null;
    cbBreakpointHit_bp = null;
  }

  public synchronized void finishCallback () {
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".finishCallback()");
    }

    notifyAll();

    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".finishCallback() - end");
    }
  }

  // Blocked threads are alternating at first driver, CB thread, driver, CB thread
  protected synchronized void block () {
    try {
      notifyAll(); // Unblock currently
      wait();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void setInspector (JPFInspector inspector) {
    assert (inspector != null);
    this.inspector = inspector;
  }

  /***************************************************************************/
  /***************************************************************************/
  /***************************************************************************/

  @Override
  public synchronized void notifyStateChange (InspectorStates newState, String details) {
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".notifyStateChange( newState=" + newState + ", details=" + details + ")");
    }
    if (method == CB_METHODS.CB_STATE_CHANGE) {
      cleanUpChackedParameters();

      cbStateChange_newState = newState;
      cbStateChange_details = details;
      InspectorTest
          .assertEquals("real:" + newState.toString() + "!= expected:" + cbStateChange_expectedState.toString(), newState, cbStateChange_expectedState);
      method = null;
      cbStateChange_expectedState = null;
      block();
    } else if (state == WAIT_STATE.NEXT_CB) {
      InspectorTest.fail("Unexpected \"notifyStateChange\" callback while waiting for " + method);
    } else if (newState == InspectorStates.JPF_TERMINATING) {
      // InspectorTest.fail("Unexpected SuT termination");
    }

    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".notifyStateChange( newState=" + newState + ", details=" + details + ") - end");
    }
  }

  public InspectorStates getCbStateChange_newState () {
    return cbStateChange_newState;
  }

  public String getCbStateChange_details () {
    return cbStateChange_details;
  }

  /**
   * Waits for first state change callback and checks if JPF-Inspector is in expected state
   * 
   * @param expectedState
   */
  public synchronized void waitForCB_StateChange (InspectorStates expectedState) {
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".waitForCB_StateChange(expectedState=" + expectedState + ")");
    }
    method = CB_METHODS.CB_STATE_CHANGE;
    state = WAIT_STATE.WAIT_FOR_CB;
    cbStateChange_expectedState = expectedState;
    block();
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".waitForCB_StateChange(expectedState=" + expectedState + ") - end");
    }
  }

  public synchronized void nextCB_StateChange (InspectorStates expectedState) {
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".nextCB_StateChange(expectedState=" + expectedState + ")");
    }
    method = CB_METHODS.CB_STATE_CHANGE;
    state = WAIT_STATE.WAIT_FOR_CB;
    cbStateChange_expectedState = expectedState;

    block();
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".nextCB_StateChange(expectedState=" + expectedState + ") - end");
    }
  }

  /**
   * Resumes the execution and waits for next callback.
   */
  public synchronized void nextCB_Resume_and_StateChange (InspectorStates expectedState) throws JPFInspectorException {
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".nextCB_Resume_and_StateChange(expectedState=" + expectedState + ")");
    }
    inspector.start();
    waitForCB_StateChange(expectedState);
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".nextCB_Resume_and_StateChange(expectedState=" + expectedState + ") - end");
    }
  }

  /***************************************************************************/
  /***************************************************************************/
  /***************************************************************************/

  @Override
  public synchronized void genericError (String msg) {
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".genericError(msg=" + msg + ")");
    }

    if (method == CB_METHODS.CB_GENERIC_ERROR) {
      cleanUpChackedParameters();
      method = null;
      block();
    } else if (state == WAIT_STATE.NEXT_CB) {
      InspectorTest.fail("Unexpected \"genericError\" callback while waiting for " + method);
    }

    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".genericError(msg=" + msg + ") - end");
    }
  }

  public synchronized void waitForCB_genericError () {
    method = CB_METHODS.CB_GENERIC_ERROR;
    state = WAIT_STATE.WAIT_FOR_CB;

    block();
  }

  public synchronized void nextCB_genericError () {
    method = CB_METHODS.CB_GENERIC_ERROR;
    state = WAIT_STATE.WAIT_FOR_CB;

    block();
  }

  /***************************************************************************/
  /***************************************************************************/
  /***************************************************************************/

  // Ignoring generic info methods - they do not contain any valuable information for tests
  @Override
  public synchronized void genericInfo (String msg) {
  }

  // @Override
  // public synchronized void genericInfo (String msg) {
  // if (DEBUG) {
  // out.println(this.getClass().getSimpleName() + ".genericInfo(msg=" + msg + ")");
  // }
  //
  // if (method == CB_METHODS.CB_GENERC_INFO) {
  // cleanUpChackedParameters();
  // method = null;
  // block();
  // } else if (state == WAIT_STATE.NEXT_CB) {
  // InspectorTest.fail("Unexpected \"genericInfo\" callback while waiting for " + method);
  // }
  //
  // if (DEBUG) {
  // out.println(this.getClass().getSimpleName() + ".genericInfo(msg=" + msg + ") - end");
  // }
  // }
  //
  // public synchronized void waitForCB_genericInfo () {
  // method = CB_METHODS.CB_GENERC_INFO;
  // state = WAIT_STATE.WAIT_FOR_CB;
  //
  // block();
  // }
  //
  // public synchronized void nextCB_genericInfo () {
  // method = CB_METHODS.CB_GENERC_INFO;
  // state = WAIT_STATE.WAIT_FOR_CB;
  //
  // block();
  // }
  /***************************************************************************/
  /***************************************************************************/
  /***************************************************************************/
  @Override
  public synchronized void notifyBreakpointHit (BreakPointStatus bp) {
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".notifyBreakpointHit(bp=" + bp + ")");
    }

    if (method == CB_METHODS.CB_BREAKPOINT_HIT) {
      cleanUpChackedParameters();
      cbBreakpointHit_bp = bp;
      method = null;
      block();
    } else if (state == WAIT_STATE.NEXT_CB) {
      InspectorTest.fail("Unexpected \"notifyBreakpointHit\" callback while waiting for " + method);
    }

    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".notifyBreakpointHit(bp=" + bp + ") - end");
    }
  }

  public BreakPointStatus getCbBreakpointHit_bp () {
    return cbBreakpointHit_bp;
  }

  public synchronized void waitForCB_BreakpointHit () {
    method = CB_METHODS.CB_BREAKPOINT_HIT;
    state = WAIT_STATE.WAIT_FOR_CB;

    block();
  }

  public synchronized void nextCB_BreakpointHit () {
    method = CB_METHODS.CB_BREAKPOINT_HIT;
    state = WAIT_STATE.WAIT_FOR_CB;

    block();
  }

  /***************************************************************************/
  /***************************************************************************/
  /***************************************************************************/

  @Override
  public synchronized void notifyChoiceGeneratorNewChoice (CGTypes cgType, String cgName, int cgId, String[] choices, int nextChoice, int defaultChoice) {
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".notifyChoiceGeneratorNewChoice(...)");
    }

    if (method == CB_METHODS.CB_CG_NEW_CHOICE) {
      cleanUpChackedParameters();
      method = null;
      block();
    } else if (state == WAIT_STATE.NEXT_CB) {
      InspectorTest.fail("Unexpected \"notifyChoiceGeneratorNewChice\" callback while waiting for " + method);
    }

    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".notifyChoiceGeneratorNewChoice(...) - end");
    }

  }

  public synchronized void waitForCB_ChoiceGeneratorNewChoice () {
    method = CB_METHODS.CB_CG_NEW_CHOICE;
    state = WAIT_STATE.WAIT_FOR_CB;

    block();
  }

  public synchronized void nextCB_ChoiceGeneratorNewChoice () {
    method = CB_METHODS.CB_CG_NEW_CHOICE;
    state = WAIT_STATE.WAIT_FOR_CB;

    block();
  }

  /***************************************************************************/
  /***************************************************************************/
  /***************************************************************************/

  @Override
  public synchronized void specifyChoiceToUse (int maxChoiceIndex) {
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".specifyChoiceToUse(maxChoiceIndex=" + maxChoiceIndex + ")");
    }

    if (method == CB_METHODS.CB_CG_CHOICE_TO_USE) {
      cleanUpChackedParameters();
      method = null;
      block();
    } else if (state == WAIT_STATE.NEXT_CB) {
      InspectorTest.fail("Unexpected \"specifyChoiceToUse\" callback while waiting for " + method);
    }

    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".specifyChoiceToUse(maxChoiceIndex=" + maxChoiceIndex + ") - end");
    }

  }

  public synchronized void waitForCB_specifyChoiceToUse () {
    method = CB_METHODS.CB_CG_CHOICE_TO_USE;
    state = WAIT_STATE.WAIT_FOR_CB;

    block();
  }

  public synchronized void nextCB_specifyChoiceToUse () {
    method = CB_METHODS.CB_CG_CHOICE_TO_USE;
    state = WAIT_STATE.WAIT_FOR_CB;

    block();
  }

  /***************************************************************************/
  /***************************************************************************/
  /***************************************************************************/

  @Override
  public synchronized void notifyUsedChoice (CGTypes cgType, String cgName, int cgId, int usedChoiceIndex, String usedChoice) {
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".notifyUsedChoice(...)");
    }

    if (method == CB_METHODS.CB_CG_USED_CHOICE) {
      cleanUpChackedParameters();
      method = null;
      block();
    } else if (state == WAIT_STATE.NEXT_CB) {
      InspectorTest.fail("Unexpected \"notifyUsedChoice\" callback while waiting for " + method);
    }

    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".notifyUsedChoice(...) - end");
    }
  }

  public synchronized void waitForCB_UsedChoice () {
    method = CB_METHODS.CB_CG_USED_CHOICE;
    state = WAIT_STATE.WAIT_FOR_CB;

    block();
  }

  public synchronized void nextCB_UsedChoice () {
    method = CB_METHODS.CB_CG_USED_CHOICE;
    state = WAIT_STATE.WAIT_FOR_CB;

    block();
  }

}
