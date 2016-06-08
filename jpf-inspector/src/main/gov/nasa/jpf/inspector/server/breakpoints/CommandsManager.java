//
// Copyright (C) 2010 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
// 
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
// 
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//  

package gov.nasa.jpf.inspector.server.breakpoints;

import gov.nasa.jpf.inspector.client.commands.CmdBreakpointCreate;
import gov.nasa.jpf.inspector.interfaces.BreakpointState;
import gov.nasa.jpf.inspector.interfaces.CommandsInterface;
import gov.nasa.jpf.inspector.interfaces.InspectorCallBacks;
import gov.nasa.jpf.inspector.interfaces.InstructionType;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.migration.MigrationUtilities;
import gov.nasa.jpf.inspector.server.expression.ExpressionBoolean;
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
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.search.Search;

/**
 * Handles start and stop commands, stepping, and stopping and resuming of the SuT.
 */
public class CommandsManager implements CommandsInterface {

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

  private final JPFInspector inspector;
  private final StopHolder stopHolder;
  private final InspectorCallBacks callbacks;
  private final DefaultForwardTraceManager dftMgr;
  private final BreakpointHandler breakpointMgr;

  public CommandsManager (JPFInspector inspector, StopHolder stopHolder, BreakpointHandler breakpointMgr, InspectorCallBacks callbacks,
                          DefaultForwardTraceManager dftMgr) {
    this.inspector = inspector;
    this.stopHolder = stopHolder;
    this.callbacks = callbacks;
    this.breakpointMgr = breakpointMgr;
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
  public void waitUntilStopped() {
    if (inspector.getJPF() == null) return;
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

  /** State of the Inspector and the JPF (JPF Thread calls this. Checks whether stop if required. */
  public void tryStop (InspectorState inspState) {
    if (run == false) {
      stopHolder.stopExecution(inspState);
      run = true;
    }
  }

  /**
   * Reinitialization. Reset instance into startup state.
   */
  public void newJPF () {
    run = true;
  }

  @Override
  public void backwardStep (StepType type) throws JPFInspectorGenericErrorException {
    initialStopTest(true, "cannot execute backward step");

    InspectorState inspState = stopHolder.getInspectorState();
    BackwardBreakpointCreator bbc = null;
    if (type == StepType.ST_TRANSITION_DATA) {
      // TODO
      // throw new NotImplementedException();
    } else if (type == StepType.ST_TRANSITION_SCHED) {
      // TODO
      // throw new NotImplementedException();
    } else if (type == StepType.ST_TRANSITION_ALL) {
      // TODO
      // throw new NotImplementedException();
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
      throw new JPFInspectorGenericErrorException("Back step not possible");
    }

    int bpID = bbc.createBreakpoint(breakpointMgr);

    // According stepType find
    // a) how much transitions it is required to backtrack
    // b) create breakpoint for specific position in transition (handle repetitive invocations??)

    // Enable silent mode in JPF Listener
    InspectorListener listener = inspector.getInspectorListener();
    assert listener != null : "Internal error - if JPF is connected then Listener has to be set";
    InspectorListenerModeSilent listenerSilentMode = new InspectorListenerModeSilent(inspector, this, breakpointMgr, bbc.getTransitionsToBacktrack(), bpID,
        dftMgr, stopHolder);
    listener.pushMode(listenerSilentMode);

    Search search = inspState.getSearch();
    assert search != null : "Internal error - not specified search";

     // reset the root CG if we might backtrack to it
  ChoiceGenerator[] allCGs = search.getVM().getSystemState().getChoiceGenerators();
  if (bbc.getTransitionsToBacktrack() >= allCGs.length) allCGs[0].reset();
    
  // Stop current transition (to prevent invoke more instruction than necessary) - only if this makes sense - instruction/throw/object_created
  //search.getVM().breakTransition(); // We cannot add new transition
  //search.requestBacktrack();
  search.getVM().ignoreState();

  // Resume execution -> now silent backtrack and "breakpoint hit in single forward step should occur"
  stopHolder.resumeExecution();

  /*
   * Rest is done in the listenerSilentMode Receives silent backtrack notifications Updates default trace Restores CG to same choice as before Start forward
   * execution In the this.notifyBackwardStep() Enable normal mode in JPF Listener
   */
  }

  @Override
  public void forwardStep (StepType type) throws JPFInspectorGenericErrorException {
    initialStopTest(true, "cannot execute forward step");

    // TODO Create inspector.server class copy ... don't use inspector.client package version !!
    CmdBreakpointCreate.ConsoleBreakpointCreationExpression newBP = new CmdBreakpointCreate.ConsoleBreakpointCreationExpression();
    newBP.setBounds(null, null, "<=", 1);
    newBP.setState(BreakpointState.BP_STATE_ENABLED);

    ExpressionBoolean bpExpression;
    if (type == StepType.ST_TRANSITION_DATA) {
      bpExpression = new ExpressionBreakpointChoiceGenerator(BreakPointModes.BP_MODE_CHOICE_DATA);
    } else if (type == StepType.ST_TRANSITION_SCHED) {
      bpExpression = new ExpressionBreakpointChoiceGenerator(BreakPointModes.BP_MODE_CHOICE_SCHEDULING);
    } else if (type == StepType.ST_TRANSITION_ALL) {
      bpExpression = new ExpressionBreakpointChoiceGenerator(BreakPointModes.BP_MODE_CHOICE_BOTH);
    } else if (type == StepType.ST_INSTRUCTION) {
      bpExpression = new ExpressionBreakpointInstructionType(InstructionType.IT_ANY);
    } else if (type == StepType.ST_LINE) {
      bpExpression = new ExpressionBreakpointSingleStep(inspector, stopHolder.getJVM(), LocationTypes.LT_POSITION_LEAVED_STEP_OVER);
    } else if (type == StepType.ST_STEP_IN) {
      bpExpression = new ExpressionBreakpointSingleStep(inspector, stopHolder.getJVM(), LocationTypes.LT_POSITION_LEAVED_STEP_IN);
    } else if (type == StepType.ST_STEP_OUT) {
      ThreadInfo ti = MigrationUtilities.getLastThreadInfo(stopHolder.getJVM());
      bpExpression = ExpressionBreakpointStepOut.getStepOutToCaller(ti);
    } else {
      throw new RuntimeException("Unsupported " + type.getClass().getSimpleName() + " entry " + type);
    }

    assert (bpExpression != null);
    breakpointMgr.createInternalBreakpoint(newBP, bpExpression, true);

    stopHolder.resumeExecution();
  }

  /**
   * @param success If true, JPF stopped on backward step breakpoint
   */
  public void notifyBackwardStepCompleted (boolean success) {

    InspectorListener listener = inspector.getInspectorListener();
    assert listener != null : "Internal error - if JPF is connected then Listener has to be set";

    // Restore original mode
    listener.popMode();

    if (!success) {
      callbacks.genericError("Backward step failed.");
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
