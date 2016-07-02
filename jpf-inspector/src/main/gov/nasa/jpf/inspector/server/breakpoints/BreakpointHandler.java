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

import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.inspector.interfaces.*;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorParsingErrorException;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanInterface;
import gov.nasa.jpf.inspector.server.expression.ExpressionParser;
import gov.nasa.jpf.inspector.server.expression.ExpressionParserInterface;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionBreakpointAssert;
import gov.nasa.jpf.inspector.server.jpf.InspectorListener;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.jpf.StopHolder;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.Step;
import gov.nasa.jpf.vm.Transition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

/**
 * Holds list of all breakpoints. Manages changes in the breakpoints.
 * 
 */
public class BreakpointHandler implements BreakPointManagerInterface {
  private static final boolean DEBUG = false;
  /**
   * Holds map of the user defined Breakpoint to the internal breakpoint representation.
   *
   * The key is the breakpoint ID.
   * The value is the actual breakpoint.
   *
   * This object also acts as the mutex monitor for synchronizing access between the command thread and the JPF thread.
   */
  private final Map<Integer, InternalBreakpointHolder> breakpoints;
  /**
   * Indicates whether execution should be stopped before the next instruction is executed. This field is set by
   * {@link #checkBreakpoints(InspectorState)} and reset when the execution stops.
   *
   * This field is only accessed from the JPF thread.
   */
  private boolean breakExecutionBeforeNextInstruction = false;
  private boolean rememberTheNextBreakpointToBreakExecution = false;

  private final Stack<BreakpointsMemento> bpMementos;

  /**
   * The Inspector server.
   */
  private final JPFInspector inspector;
  private final InspectorCallbacks serverCallbacks;
  private final StopHolder stopHolder;
  private BreakpointHitLocation lastBreakpointHitLocation = null;
  /**
   * Used to parse hit condition expressions from clients
   */
  private final ExpressionParserInterface expParser;
  /**
   * Memento which holds states of breakpoints at the start of the transition
   */
  private BreakpointsMemento transitionStartMemento;

  public BreakpointHandler(JPFInspector inspector, InspectorCallbacks serverCallbacks, StopHolder stopHolder) {
    this.breakpoints = new TreeMap<>();

    this.bpMementos = new Stack<>();
    this.inspector = inspector;
    this.serverCallbacks = serverCallbacks;
    this.stopHolder = stopHolder;
    this.expParser = new ExpressionParser(inspector);
    this.transitionStartMemento = null;
  }

  /**
   * New JPF is connected to the Inspector
   */
  public void newJPF () {
    // Set initial state for all breakpoints
    for (InternalBreakpointHolder bp : breakpoints.values()) {
      bp.setPathCounterFromMemento(null);
    }
    bpMementos.clear();
  }

  /**
   * Gets list with currently existing Breakpoints created by user.
   */
  @Override
  public List<BreakpointStatus> getBreakpoints() {
    List<BreakpointStatus> result = new ArrayList<>(breakpoints.size());
    synchronized (breakpoints) {
      for (InternalBreakpointHolder bph : breakpoints.values()) {
        if (bph.isUserBreakpoint()) {
          result.add(bph.getBreakpointStatus(null));
        }
      }
    }
    return result;
  }

  public BreakpointStatus getBreakpoint(int id) {
    synchronized (breakpoints) {
      assert breakpoints.containsKey(id);
      return breakpoints.get(id).getBreakpointStatus(null);
    }
  }

  /**
   * Creates a new (if breakpoint ID is -1) or modifies an existing breakpoint.
   * 
   * @param newBP Structure with new information about a breakpoint.
   * @return Get informations about state of the created/modified breakpoint or null if error takes place.
   */
  @Override
  public BreakpointStatus createBreakPoint (BreakpointCreationInformation newBP) throws JPFInspectorParsingErrorException, JPFInspectorGenericErrorException {

    ExpressionBooleanInterface newBPExpression = expParser.getBreakpointExpression(newBP.getBPExpression());

    return createBreakPointImpl(newBP, newBPExpression, false, false);
  }

  @Override
  public AssertStatus createAssert (AssertCreationInformation newAssert) throws JPFInspectorParsingErrorException, JPFInspectorGenericErrorException {

    // Position BP
    ExpressionBooleanInterface newBPExpressionPos = expParser.getBreakpointExpression("pos=" + newAssert.getPosition());
    // Assertion expression BP
    ExpressionBooleanInterface newBPExpressionCond = expParser.getBreakpointExpression(newAssert.getCondition());
    ExpressionBreakpointAssert newBPExpression = expParser.getExpressionFactory().getBreakpointAssert(newBPExpressionPos, newBPExpressionCond);

    return createAssertImpl(newAssert, newBPExpression);
  }

  /**
   * Creates or modifies a breakpoint.
   * @param newBP Information about the breakpoint, including its ID, if modifying an existing one.
   * @param hidden True for internal breakpoints, no error reports etc hidden, false for standard BP created by user. Cannot be null.
   * @param newBPExpression Parsed expression from the newBP create. Is passed separately to be handle internal BP, which are not supported in the expression grammar.
   * @param firstHit Whether this breakpoint should be removed when it's first hit. This never happens to user
   *                 breakpoints, only to internal breakpoints.
   */
  private BreakpointStatus createBreakPointImpl(BreakpointCreationInformation newBP,
                                                ExpressionBooleanInterface newBPExpression,
                                                boolean hidden,
                                                boolean firstHit) throws JPFInspectorGenericErrorException {
    assert newBP != null;
    assert newBPExpression != null;

    synchronized (breakpoints) {
      InternalBreakpointHolder ibp = null;
      if (newBP.getBPID() != BreakpointCreationInformation.BP_ID_NOT_DEFINED) {
        ibp = breakpoints.get(newBP.getBPID());
        if (ibp == null) {
          throw new JPFInspectorGenericErrorException("Breakpoint with ID " + newBP.getBPID() + " does not exist and so can't be modified");
        }
      }
      if (ibp == null) {
        ibp = new InternalBreakpointHolder(newBP.getBPID(), serverCallbacks, !hidden, firstHit);
      }
      ibp.modifyBPSettings(newBP, newBPExpression);
      breakpoints.put(ibp.getBPID(), ibp);
      // Check for error
      if (newBP.getBPExpression() != null) {
        if (!newBP.getBPExpression().trim().isEmpty()) {
          return null;
        }
      }
      return ibp.getBreakpointStatus(null);
    } // end sync
  }

  private AssertStatus createAssertImpl (AssertCreationInformation newAssert, ExpressionBreakpointAssert newBPExpression) throws JPFInspectorGenericErrorException {
    assert newAssert != null;
    assert newBPExpression != null;

    synchronized (breakpoints) {
      InternalAssertHolder iah = null;
      if (newAssert.getBPID() != BreakpointCreationInformation.BP_ID_NOT_DEFINED) {
        InternalBreakpointHolder ibp = breakpoints.get(newAssert.getBPID());
        if (ibp == null) {
          throw new JPFInspectorGenericErrorException("Assertion with ID " + newAssert.getBPID() + " does not exist so it can't be modified.");
        }
        if (ibp.isAssert() == false) {
          throw new JPFInspectorGenericErrorException("The breakpoint with ID " + newAssert.getBPID() + " is not an assertion.");
        }

        assert (ibp instanceof InternalAssertHolder);
        iah = (InternalAssertHolder) ibp;
      }
      if (iah == null) {
        iah = new InternalAssertHolder(newAssert.getBPID(), serverCallbacks, newAssert.getPosition(), newAssert.getCondition());
      }

      iah.modifyAssertSettings(newAssert, newBPExpression);

      breakpoints.put(iah.getBPID(), iah);

      // Check for error
      if (newAssert.getBPExpression() != null) {
        if (!newAssert.getBPExpression().trim().isEmpty()) {
          return null;
        }
      }
      return iah.getBreakpointStatus(null);
    } // end sync
  }

  /**
   * Creates an internal breakpoint and returns information about it. An internal breakpoint is "hidden", not created
   * and not displayed to the user. It is used for stepping commands.
   *  @param newBP Information about the breakpoint.
   * @param newBPExpression Hit condition for the breakpoint (missing from the information in the previous parameter).
   */
  public BreakpointStatus createInternalBreakpoint(BreakpointCreationInformation newBP,
                                                   ExpressionBooleanInterface newBPExpression) {
    try {
      return createBreakPointImpl(newBP, newBPExpression, true, true);
    } catch (JPFInspectorGenericErrorException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean deleteBreakpoint(int bpID) {
    synchronized (breakpoints) {
      InternalBreakpointHolder removedBP = breakpoints.remove(bpID);
      return removedBP != null;
    }
  }

  @Override
  public boolean changeBreakpointState(int breakpointId, BreakpointState newState) {
    synchronized (breakpoints) {
      if (breakpoints.containsKey(breakpointId)) {
        breakpoints.get(breakpointId).breakpointState = newState;
        return true;
      } else {
        return false;
      }
    }
  }

  /**
   * Checks whether any breakpoints were hit by the last step and therefore execution should be suspended.
   * @param inspState Common state of the Inspector and SuT
   *
   * Note: Executed by the JPF thread.
   */
  public void checkBreakpoints (InspectorState inspState) {
    boolean bpHit = false;
    boolean wasAtLeastOneNonInternal = false;
    synchronized (breakpoints) {
      for (InternalBreakpointHolder bp : breakpoints.values()) {
        boolean hit = bp.evaluateBreakpoint(inspState);
        if (hit && bp.isUserBreakpoint()) {
          wasAtLeastOneNonInternal = true;
        }
        bpHit |= hit;
      }

      if (bpHit) {
        removeSingleHitBreakpoints();
      }
    }

    if (bpHit) {
      breakExecutionBeforeNextInstruction = true;
      if (wasAtLeastOneNonInternal) {
        this.rememberTheNextBreakpointToBreakExecution = true;
      }
    }
  }

  /**
   * Checks if the specified breakpoint hits.
   * 
   * @param inspState Program state.
   * @param bpID ID of the breakpoint to check.
   * @return True if the breakpoint hits, false otherwise.
   * 
   *  Note: If the breakpoint is single-hit, then all single-hit breakpoints are removed.
   *
   *  Note: Executed by the JPF thread and only during a backstep.
   */
  public boolean checkBreakpoint (InspectorState inspState, int bpID) {
    boolean bpHit;
    synchronized (breakpoints) {
      InternalBreakpointHolder bp = breakpoints.get(bpID);
      assert bp != null : "Internal error - unknown Breakpoint ID";
      bpHit = bp.evaluateBreakpoint(inspState);

      if (bpHit) {
        removeSingleHitBreakpoints();
      }
    }

    return bpHit;
  }

  /**
   * Remove all breakpoints whose {@link InternalBreakpointHolder#isSingleHitBP()} method returns true.
   * Synchronized by "breakpoints".
   */
  private void removeSingleHitBreakpoints () {
    synchronized (breakpoints) {
      Iterator<Integer> it = breakpoints.keySet().iterator() ;
      while (it.hasNext()) {
        InternalBreakpointHolder bp = breakpoints.get(it.next());
        if (bp.isSingleHitBP()) {
          it.remove();
        }
      }
    }
  }

  /**
   * Notification from the {@link InspectorListener} about executed forward step {@link ListenerAdapter#stateAdvanced(gov.nasa.jpf.search.Search)}.
   *
   */
  public void forwardJPFStep() {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".forwardJPFStep");
    }
    // Save state of all BPs
    bpMementos.add(transitionStartMemento);
    transitionStartMemento = new BreakpointsMemento();
  }

  /**
   * Notification from the {@link InspectorListener} about executed backward step {@link ListenerAdapter#stateBacktracked(gov.nasa.jpf.search.Search)}.
   * 
   * @param inspState
   *        Common state of the Inspector and SuT
   */
  public void backwardJPFStep (@SuppressWarnings("UnusedParameters") InspectorState inspState) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".backwardJPFStep");
    }
    // Restore state of Breakpoints (hit counters
    transitionStartMemento = bpMementos.pop();
    if (transitionStartMemento != null) {
      transitionStartMemento.restoreState();
    }
  }

  /**
   * Stops execution if such a thing was scheduled by a breakpoint triggering.
   * This method is called by the JPF thread just as an instruction is about to be executed.
   *
   * @param inspState The current Inspector state.
   */
  public void breakIfBreakScheduled(InspectorState inspState) {
    if (breakExecutionBeforeNextInstruction) {
      breakExecutionBeforeNextInstruction = false;

      if (rememberTheNextBreakpointToBreakExecution) {
        Instruction upcomingInstruction = inspState.getVM().getInstruction();
        // Store the current position for back_breakpoint_hit purposes.
        inspState.getVM().updatePath();
        Transition currentTransition = inspState.getVM().getCurrentTransition();
        int instructionsToSkip = 0;
        for (Step step : currentTransition) {
          if (step.getInstruction().equals(upcomingInstruction)) {
            instructionsToSkip++;
          }
        }
        lastBreakpointHitLocation = new BreakpointHitLocation(inspState.getVM().getPathLength() - 1, upcomingInstruction, instructionsToSkip);
        rememberTheNextBreakpointToBreakExecution = false;
      }
      // Now break.
      stopHolder.stopExecution(inspState); // Execution has to be stopped outside a synchronized block.
    }
  }

  /**
   * Gets the location of the instruction where we last stopped execution because of a breakpoint, or null
   * if such a thing has yet to pass.
   * @return The location of the breakpoint, or null.
   */
  public BreakpointHitLocation getLastBreakpointHitLocation() {
    return lastBreakpointHitLocation;
  }

  /**
   * Marker interface for partial mementos which remember a single breakpoint's path hit count.
   */
  public interface BreakpointPartialMemento {
  }

  /**
   * Holds traversing related state (hit counts) of all breakpoints
   * 
   */
  private class BreakpointsMemento {
    private final Map<Integer, BreakpointPartialMemento> partialMementos = new HashMap<>();

    /**
     * Initializes a new memento by storing the current path hit count of all breakpoints.
     */
    public BreakpointsMemento() {
      synchronized (breakpoints) {
        for (InternalBreakpointHolder bp : breakpoints.values()) {
          BreakpointPartialMemento bpm = bp.createPartialMemento();
          partialMementos.put(bp.getBPID(), bpm);
        }
      }
    }

    /**
     * Restores the path hit count of all breakpoints from this memento.
     * If this memento does not contain information about an existing breakpoint, the path hit count is reset to zero.
     */
    public void restoreState () {
      BreakpointPartialMemento bpResetMemento = InternalBreakpointHolder.createInitialStateMemento();
      synchronized (breakpoints) {

        for (InternalBreakpointHolder bp : breakpoints.values()) {
          int bpID = bp.getBPID();
          BreakpointPartialMemento bpm = partialMementos.get(bpID);
          if (bpm == null) {
            // We backtrack before place where memento has been defined -> reset the counts
            bpm = bpResetMemento;
          }
          bp.setPathCounterFromMemento(bpm);
        }
      }
    }
  }

}
