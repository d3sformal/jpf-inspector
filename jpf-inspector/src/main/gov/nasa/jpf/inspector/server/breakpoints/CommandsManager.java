//
// Copyright (C) 2010-2011 Pavel Jančík
// Copyright (C) 2016 Petr Hudeček
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

package gov.nasa.jpf.inspector.server.breakpoints;

import gov.nasa.jpf.inspector.common.BreakpointCreationExpression;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorParsingErrorException;
import gov.nasa.jpf.inspector.interfaces.*;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.server.expression.ExpressionBoolean;
import gov.nasa.jpf.inspector.server.expression.ExpressionParser;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionBreakpointChoiceGenerator;
import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionBreakpointInstructionType;
import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionBreakpointSingleStep;
import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionBreakpointSingleStep.LocationTypes;
import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionBreakpointStepOut;
import gov.nasa.jpf.inspector.server.jpf.InspectorListener;
import gov.nasa.jpf.inspector.server.jpf.InspectorListenerModeSilent;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.jpf.StopHolder;
import gov.nasa.jpf.inspector.server.pathanalysis.BackwardBreakpointCreator;
import gov.nasa.jpf.inspector.utils.Debugging;
import gov.nasa.jpf.inspector.utils.expressions.FieldName;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.search.Search;

/**
 * Handles start and stop commands, stepping, and stopping and resuming of the SuT.
 */
public class CommandsManager implements CommandsInterface {
  private static final boolean DEBUG = true;

  /**
   *  If true, then the inspector should continue execution.
   *  If false, it should stop on next step because of a user command (not breakpoint).
   *  Calling "break" is the primary way to set this variable to false.
   *
   *  Note: The Inspector thread sets this variable, the JPF thread reads this variable. It's not really thread-safe,
   *  but we only use it to break a running program and don't care about a few instructions here or there and also the
   *  user is unlikely to type commands quickly enough for it to be a problem.
   */
  private boolean run = true;
  /**
   * If true, JPF should terminate at the earliest opportunity.
   *
   * Note: The Inspector sets this variable. The JPF thread reads this variable.
   */
  private boolean shouldTerminate;

  private final JPFInspector inspector;
  private final StopHolder stopHolder;
  private final InspectorCallbacks serverCallbacks;
  private final DefaultForwardTraceManager dftMgr;
  private final BreakpointHandler breakpointHandler;
  private final ExpressionParser expressionParser;

  public CommandsManager (JPFInspector inspector, StopHolder stopHolder, BreakpointHandler breakpointHandler,
                          InspectorCallbacks serverCallbacks,
                          DefaultForwardTraceManager dftMgr) {
    this.inspector = inspector;
    this.stopHolder = stopHolder;
    this.serverCallbacks = serverCallbacks;
    this.breakpointHandler = breakpointHandler;
    this.expressionParser = new ExpressionParser(inspector);
    this.dftMgr = dftMgr;
    newJPF();
  }

  @Override
  public synchronized void start () throws JPFInspectorGenericErrorException {
    if (inspector.getJPF() == null) {
      throw new JPFInspectorGenericErrorException("No JPF instance to start.");
    }
    run = true;
    stopHolder.resumeExecution();
  }

  @Override
  public void requestTermination() {
    shouldTerminate = true;
  }

  @Override
  public void waitUntilStopped() {
    if (inspector.getJPF() == null) {
      return;
    }
    inspector.getStopHolder().waitUntilStopped();
  }

  @Override
  public synchronized  boolean isPaused() {
    return (inspector.getJPF() != null) && stopHolder.isStopped();
  }

  @Override
  public synchronized void stop () throws JPFInspectorGenericErrorException {
    if (inspector.getJPF() == null) {
      throw new JPFInspectorGenericErrorException("No JPF instance to stop.");
    }
    run = false; // Stops on the next event.
  }

  /**
   * Determines whether JPF should be stopped and if so, stops it.
   * This method may only be called from the JPF thread.
   */
  public void tryStop (InspectorState inspState) {
    if (run == false) {
      stopHolder.stopExecution(inspState);
      run = true;
    }
  }
  public void tryTerminate(Search search) {
    if (shouldTerminate) {
      search.terminate();
    }
  }

  /**
   * Reinitialization. Reset instance into startup state.
   */
  public void newJPF () {
    run = true;
  }

  @Override
  public void backstepTransition(StepType transitionKind, int count) throws JPFInspectorGenericErrorException {
    initialStopTest(true, "cannot execute backward step");
    InspectorState inspState = stopHolder.getInspectorState();
    BackwardBreakpointCreator backwardBreakpointCreator = BackwardBreakpointCreator.getBackwardStepTransition(inspState,
                                                                                                              transitionKind,
                                                                                                              count);
    assert backwardBreakpointCreator != null;
    createBackwardsBreakpointAndResumeExecution(inspState, backwardBreakpointCreator);
  }

  @Override
  public void backstep(StepType type) throws JPFInspectorGenericErrorException {
    initialStopTest(true, "cannot execute backward step");

    // Instantiate the creator and discover the instruction to backtrack to
    InspectorState inspState = stopHolder.getInspectorState();
    BackwardBreakpointCreator bbc;
    if (type == StepType.BACK_BREAKPOINT_HIT) {
      bbc = BackwardBreakpointCreator.getBackBreakpointHit(breakpointHandler.getLastBreakpointHitLocation(), inspState);
    } else if (type == StepType.ST_INSTRUCTION) {
      bbc = BackwardBreakpointCreator.getBackwardStepInstruction(inspState);
    } else if (type == StepType.ST_LINE) {
      bbc = BackwardBreakpointCreator.getBackwardStepLine(inspState);
    } else if (type == StepType.ST_STEP_IN) {
      bbc = BackwardBreakpointCreator.getBackwardStepIn(inspState);
    } else if (type == StepType.ST_STEP_OUT) {
      bbc = BackwardBreakpointCreator.getBackwardStepOut(inspState);
    } else {
      throw new RuntimeException("Unsupported " + type.getClass().getSimpleName() + " entry " + type);
    }
    if (bbc == null) {
      throw new JPFInspectorGenericErrorException(
              "Backwards step not possible (there is no appropriate step left for this thread to backtrack to).");
    }

    createBackwardsBreakpointAndResumeExecution(inspState, bbc);

  }

  /**
   * This is a dark-magic method that does, roughly the following:
   *
   * 1. Creates a breakpoint based on the {@link BackwardBreakpointCreator} and adds it to the manager.
   * 2. Enables the InspectorListener's silent mode.
   * 3. Breaks the current transition.
   * 4. Wakes up the JPF thread.
   *
   * @param inspState The current Inspector state.
   * @param bbc The creator that we will call upon to create a backstepping breakpoint.
   */
  private void createBackwardsBreakpointAndResumeExecution(InspectorState inspState, BackwardBreakpointCreator bbc) throws JPFInspectorGenericErrorException {
    assert bbc != null;
    // Create the breakpoint on that specific instruction
    int bpID = bbc.createBreakpoint(breakpointHandler);
    BreakpointStatus breakpointStatus = breakpointHandler.getBreakpoint(bpID);
    Debugging.getLogger()
            .info("The following breakpoint would be created:\n--> " + breakpointStatus.getNormalizedBreakpointExpression());
    Debugging.getLogger().info("We will need to backtrack " + bbc.getTransitionsToBacktrack() + " transitions.");


    // Enable silent mode in JPF Listener
    InspectorListener listener = inspector.getInspectorListener();
    assert listener != null : "Internal error - if JPF is connected then Listener has to be set";
    InspectorListenerModeSilent listenerSilentMode =
            new InspectorListenerModeSilent(inspector, this, breakpointHandler,
                                            bbc.getTransitionsToBacktrack(),
                                            bpID, dftMgr, stopHolder);
    listener.pushMode(listenerSilentMode);

    Search search = inspState.getSearch();
    assert search != null : "Internal error - not specified search";

    // reset the root CG if we might backtrack to it
    /*
    ChoiceGenerator<?>[] allCGs = search.getVM().getSystemState().getChoiceGenerators();
    if (bbc.getTransitionsToBacktrack() >= allCGs.length) {
      allCGs[0].reset();
    }
    */

    // Stop current transition (to prevent invoke more instruction than necessary) - only if this makes sense - instruction/throw/object_created
    //search.getVM().breakTransition(); // We cannot add new transition
    //search.requestBacktrack();
    search.getVM().ignoreState();

    // Resume execution -> now silent backtrack and "breakpoint hit in single forward step should occur"
    stopHolder.resumeExecution();

    // The rest happens in the silent-mode Inspector listener.
    // 1. It backtracks the determined number of transitions.
    // 1a. During this, it updates the default forward trace.
    // 2. It restores the first transition's CG to the same choice as before.
    // 3. It starts forward execution until it reaches the breakpoint we just created.
    // 4. It calls this.notifyBackwardStep().
    // 4a. In which the normal notifications-mode Inspector listener is reactived and the backstepping ends.
  }



  @Override
  public void backFieldAccessStep(String fieldNameExpression) throws JPFInspectorGenericErrorException {
    try {
      FieldName fieldName = expressionParser.getFieldName(fieldNameExpression);

      BackwardBreakpointCreator bbc = BackwardBreakpointCreator.getBackwardFieldAccess(fieldName,
                                                                                       stopHolder.getInspectorState());

      if (bbc == null) {
        throw new JPFInspectorGenericErrorException(
                "Field-access backtracking not possible (there is no appropriate step left for this thread to backtrack to).");
      }
      createBackwardsBreakpointAndResumeExecution(stopHolder.getInspectorState(), bbc);
    } catch (JPFInspectorParsingErrorException e) {
      throw new JPFInspectorGenericErrorException(
              "The argument to back_field_access must be of the form [classname]:[fieldname].\n" + e.getMessage());
    }
  }

  @Override
  public void forwardStep (StepType type) throws JPFInspectorGenericErrorException {
    initialStopTest(true, "cannot execute forward step");

    BreakpointCreationExpression newBP = new BreakpointCreationExpression();
    newBP.setBounds(null, null, "<=", 1);
    newBP.setState(BreakpointState.ENABLED);

    ExpressionBoolean bpExpression;
    if (type == StepType.ST_TRANSITION_DATA) {
      bpExpression = new ExpressionBreakpointChoiceGenerator(BreakPointModes.BP_MODE_CHOICE_DATA);
    } else if (type == StepType.ST_TRANSITION_SCHED) {
      bpExpression = new ExpressionBreakpointChoiceGenerator(BreakPointModes.BP_MODE_CHOICE_SCHEDULING);
    } else if (type == StepType.ST_TRANSITION_ALL) {
      bpExpression = new ExpressionBreakpointChoiceGenerator(BreakPointModes.BP_MODE_CHOICE_BOTH);
    } else if (type == StepType.ST_INSTRUCTION) {
      bpExpression = new ExpressionBreakpointInstructionType(InstructionType.ANY);
    } else if (type == StepType.ST_LINE) {
      bpExpression = new ExpressionBreakpointSingleStep(inspector, stopHolder.getJVM(), LocationTypes.LT_POSITION_LEAVED_STEP_OVER);
    } else if (type == StepType.ST_STEP_IN) {
      bpExpression = new ExpressionBreakpointSingleStep(inspector, stopHolder.getJVM(), LocationTypes.LT_POSITION_LEAVED_STEP_IN);
    } else if (type == StepType.ST_STEP_OUT) {
      ThreadInfo ti = stopHolder.getJVM().getCurrentThread();
      bpExpression = ExpressionBreakpointStepOut.getStepOutToCaller(ti);
    } else {
      throw new RuntimeException("Unsupported " + type.getClass().getSimpleName() + " entry " + type);
    }

    assert (bpExpression != null);
    breakpointHandler.createInternalBreakpoint(newBP, bpExpression, true);

    stopHolder.resumeExecution();
  }

  /**
   * @param success If true, JPF stopped on backward step breakpoint
   */
  public void notifyBackwardStepCompleted (boolean success, String reason) {

    InspectorListener listener = inspector.getInspectorListener();
    assert listener != null : "Internal error - if JPF is connected then Listener has to be set";

    // Restore original mode
    if (DEBUG) {
      inspector.getDebugPrintStream().println("Popping listener.");
    }
    listener.popMode();

    if (!success) {
      serverCallbacks.genericError("Backward step failed (" + reason + ")");
    }
  }

  /**
   * Tests if any JPF is associated. Test if the JPF is running or stopped. If JPF runs then it tries to stop or report error.running
   *
   */
  public boolean initialStopTest (boolean wait, String msg) throws JPFInspectorGenericErrorException {
    if (inspector.getJPF() == null) {
      throw new JPFInspectorGenericErrorException("No JPF instance to observe");
    }
    boolean wasStopped = stopHolder.isStopped();
    if (!wasStopped) {
      if (!wait) {
        throw new JPFInspectorGenericErrorException("SuT is running - " + msg);
      } else {
        run = false;
        stopHolder.waitUntilStopped();
      }
    }
    return wasStopped;
  }


}
