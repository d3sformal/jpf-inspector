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

import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.inspector.interfaces.AssertCreationInformation;
import gov.nasa.jpf.inspector.interfaces.AssertStatus;
import gov.nasa.jpf.inspector.interfaces.BreakpointCreationInformation;
import gov.nasa.jpf.inspector.interfaces.BreakPointManagerInterface;
import gov.nasa.jpf.inspector.interfaces.BreakpointStatus;
import gov.nasa.jpf.inspector.interfaces.InspectorCallbacks;
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
  protected final Map<Integer, InternalBreakpointHolder> breakpoints;

  private final Stack<BreakPointsMemento> bpMementos;

  protected final JPFInspector inspector;
  private final InspectorCallbacks callBacks;
  private final StopHolder stopHolder;
  private final ExpressionParserInterface expParser; // / Used to parse Breakpoint expression from clients
  private BreakPointsMemento transitionStartMemento; // / Memeto which holds states of breakpoints at the start of the transition

  public BreakpointHandler(JPFInspector inspector, InspectorCallbacks callBacks, StopHolder stopHolder) {
    this.breakpoints = new TreeMap<>();

    this.bpMementos = new Stack<>();
    this.inspector = inspector;
    this.callBacks = callBacks;
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
      bp.setPartialMemento(null);
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
        ibp = new InternalBreakpointHolder(newBP.getBPID(), callBacks, !hidden, firstHit);
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
        iah = new InternalAssertHolder(newAssert.getBPID(), callBacks, true, false, newAssert.getPosition(), newAssert.getCondition());
      }

      iah.modifyBPSettings(newAssert, newBPExpression);

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
   *
   * @param newBP Information about the breakpoint.
   * @param newBPExpression Hit condition for the breakpoint (missing from the information in the previous parameter).
   * @param singleHit If set then the breakpoint is automatically removed after first hit.
   */
  public BreakpointStatus createInternalBreakpoint (BreakpointCreationInformation newBP, ExpressionBooleanInterface newBPExpression, boolean singleHit) {
    try {
      return createBreakPointImpl(newBP, newBPExpression, true, singleHit);
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

  /**
   * Checks whether any breakpoints were hit by the last step and therefore execution should be suspended.
   * @param inspState Common state of the Inspector and SuT
   * @return true if any Breakpoint hit
   * 
   * Note: Executed by the JPF thread.
   */
  public boolean checkBreakpoints (InspectorState inspState) {
    boolean bpHit = false;
    synchronized (breakpoints) {
      for (InternalBreakpointHolder bp : breakpoints.values()) {
        bpHit |= bp.evaluateBreakpoint(inspState);
      }

      if (bpHit) {
        removeSingleHitBreakpoints();
      }
    }

    if (bpHit) {
      stopHolder.stopExecution(inspState); // Execution has to be stopped outside synchronized block
    }
    return bpHit;
  }

  /**
   * Checks if specified breakpoint hits.
   * 
   * @param inspState Program state.
   * @param bpID ID of the breakpoint to check.
   * @return True if the breakpoint hits, false otherwise.
   * 
   *  Note: If the breakpoint is single-hit, then single-hit breakpoints are removed.
   *
   *  Note: Executed by the JPF thread.
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
   * @param inspState Common state of the Inspector and SuT
   */
  public void forwardJPFStep (@SuppressWarnings("UnusedParameters") InspectorState inspState) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".forwardJPFStep");
    }
    // Save state of all BPs
    bpMementos.add(transitionStartMemento);
    transitionStartMemento = new BreakPointsMemento();
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
   * Empty public interface of the Breapkoints mementos
   */
  public interface BreakPointPartialMemento {
  }

  /**
   * Holds traversing related state (hit counts) of all breakpoints
   * 
   */
  private class BreakPointsMemento {
    // TODO confusing name here
    private final Map<Integer, BreakPointPartialMemento> bpMementos = new HashMap<>();

    /**
     * Store state of all breakpoints
     */
    public BreakPointsMemento () {
      synchronized (BreakpointHandler.this.breakpoints) {
        for (InternalBreakpointHolder bp : breakpoints.values()) {
          BreakPointPartialMemento bpm = bp.createPartialMemento();
          bpMementos.put(bp.getBPID(), bpm);
        }
      }
    }

    /**
     * Restore state of all breakpoints. If state state for given BP not exists, null is passed.
     */
    public void restoreState () {
      BreakPointPartialMemento bpResetMemento = InternalBreakpointHolder.createInitialStateMemento();
      synchronized (BreakpointHandler.this.breakpoints) {

        for (InternalBreakpointHolder bp : breakpoints.values()) {
          int bpID = bp.getBPID();
          BreakPointPartialMemento bpm = bpMementos.get(bpID);
          if (bpm == null) {
            // We backtrack before place where memento has been defined -> reset the counts
            bpm = bpResetMemento;
          }
          bp.setPartialMemento(bpm);
        }

      }
    }
  }

}
