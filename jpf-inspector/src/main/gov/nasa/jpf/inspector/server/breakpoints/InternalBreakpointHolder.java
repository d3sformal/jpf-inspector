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

import gov.nasa.jpf.inspector.interfaces.BreakpointCreationInformation;
import gov.nasa.jpf.inspector.interfaces.BreakpointState;
import gov.nasa.jpf.inspector.interfaces.BreakpointStatus;
import gov.nasa.jpf.inspector.interfaces.InspectorCallbacks;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.server.breakpoints.BreakpointHandler.BreakpointPartialMemento;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanInterface;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.utils.Debugging;
import gov.nasa.jpf.inspector.utils.InspectorConfiguration;
import gov.nasa.jpf.vm.MethodInfo;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents an active breakpoint.
 * This must be public so that the counter can be reset by {@link InspectorConfiguration#staticReset()}.
 */
public class InternalBreakpointHolder implements Comparable<InternalBreakpointHolder> {
  /**
   * ID of the next breakpoint to be created.
   */
  private static int bpIDCounter = 1;

  /**
   * {@link InspectorConfiguration#staticReset()} uses this to reset the static state of the Inspector.
   */
  public static void resetBreakpointIdCounter() {
    bpIDCounter = 1;
  }
  private static final Object bpIDCounterLock = new Object();
  private static Logger log = Debugging.getSimpleLogger();

  /**
   * Generates a new, yet unused, breakpoint ID.
   * Breakpoint ID's are generated in order, starting at 1, in ascending order.
   */
  private static int getNextBpID() {
    synchronized (bpIDCounterLock) {
      return bpIDCounter++;
    }
  }

  private final InspectorCallbacks callbacks;

  /**
   * ID of the breakpoint
   */
  protected final int bpID;
  /**
   * Marks whether the breakpoint is visible to user or it's BP for the internal purposes of the checker
   */
  private final boolean userBP;
  /**
   * If true, then remove this breakpoint on the first breakpointHit (this or some other breakpoint)
   */
  private final boolean singleHitBreakpoint;

  protected BreakPointModes bpMode = BreakPointModes.BP_MODE_NONE;
  protected BreakpointState breakpointState = BreakpointState.ENABLED;
  protected String bpName = "";

  protected int bpHitCounter = 0;
  protected int bpHitCounterTotal = 0;

  protected ExpressionBooleanInterface bpExpression = null;
  protected String bpExpressionStr = null;

  protected Integer lowerBound = null;
  protected Integer upperBound = null;

  /**
   * Creates empty server breakpoint representation
   * 
   * @param newID A new, not yet used, breakpoint ID, or else -1 to generate a new one.
   * @param callbacks Callbacks client class.
   * @param userBP This breakpoint is created by the client inspector side (should by reported to user)
   * @param sigleHitBP This breakpoint should be removed if the first breakpoint hits (this or some other BP)
   */
  @SuppressWarnings("AssignmentToMethodParameter")
  public InternalBreakpointHolder (int newID, InspectorCallbacks callbacks, boolean userBP, boolean sigleHitBP) {
    if (newID == BreakpointCreationInformation.BP_ID_NOT_DEFINED) {
      newID = getNextBpID();
    } else {
      synchronized (bpIDCounterLock) {
        if (bpIDCounter < newID) {
          bpIDCounter = newID + 1;
        }
      }
    }

    this.bpID = newID;
    this.callbacks = callbacks;
    this.userBP = userBP;
    this.singleHitBreakpoint = sigleHitBP;
  }

  public void modifyBPSettings (BreakpointCreationInformation newSettings, ExpressionBooleanInterface newBPExpression) {
    assert newSettings != null;

    if (newSettings.getName() != null) {
      bpName = newSettings.getName();
    }
    if (newSettings.getState() != null) {
      breakpointState = newSettings.getState();
    }
    if (newSettings.bpHitCountLowerBound() != null) {
      lowerBound = newSettings.bpHitCountLowerBound();
    }
    if (newSettings.bpHitCountUpperBound() != null) {
      upperBound = newSettings.bpHitCountUpperBound();
    }

    if (newBPExpression != null) {
      bpMode = newBPExpression.getBPMode();
      bpExpression = newBPExpression;
      bpExpressionStr = newSettings.getBPExpression();
    }

    assert bpExpression != null; // bpExpression has to be specified in the first modify call
  }

  public int getBPID () {
    return bpID;
  }

  /**
   * Returns true if the breakpoint was created by the user, i.e. it is NOT an internal breakpoint used for stepping.
   */
  public boolean isUserBreakpoint () {
    return userBP;
  }

  public boolean isSingleHitBP () {
    return singleHitBreakpoint;
  }

  public boolean isAssert () {
    return false;
  }

  public BreakpointStatus getBreakpointStatus (InspectorState state) {

    String details = null;
    String bpExpressionNormalized = null;

    if (bpExpression != null) {
      if (state != null) {
        details = bpExpression.getDetails(state);
      }
      bpExpressionNormalized = bpExpression.getNormalizedExpression();
    }

    return new BreakPointStatusImpl(bpID, bpName, bpHitCounter, bpHitCounterTotal, lowerBound, upperBound,
                                    breakpointState, bpMode, bpExpressionStr,
                                    bpExpressionNormalized, details);
  }

  /**
   * Tests whether breakpoints hits. Uses hit point counter, ...
   * 
   * @return True if breakpoint hit and should break the execution (BP is in the enabled state, expected Hit count, etc...)
   */
  public boolean evaluateBreakpoint (InspectorState state) {
    boolean bpHitted = false;

    // Hit condition
    if (bpExpression != null) {
      try {
        bpHitted = bpExpression.evaluateExpression(state);
      } catch (JPFInspectorException e) {
        // Ignore the exception Breakpoint cannot be evaluated --> thus does not hold
      }
    }

    MethodInfo topFrameMethodInfo = state.getVM().getCurrentThread().getTopFrameMethodInfo();
    if (bpHitted && topFrameMethodInfo != null) {
      if (log.isLoggable(Level.FINE)) {
        log.fine("Now checking within class: " + topFrameMethodInfo.getClassName());
      }
      if (InspectorConfiguration.getInstance().isClassIgnored(topFrameMethodInfo.getClassName())) {
        bpHitted = false;
      }
    }

    if (bpHitted) {
      bpHitCounter++;
      bpHitCounterTotal++;
    }

    boolean bpShouldExecuteAction = bpHitted;
    if (lowerBound != null && bpHitCounter < lowerBound) {
      bpShouldExecuteAction = false;
    }

    if (upperBound != null && bpHitCounter > upperBound) {
      bpShouldExecuteAction = false;
    }



    if (bpShouldExecuteAction && isUserBreakpoint()) {
      if (breakpointState == BreakpointState.DISABLED) {
        // No action expected
      } else if (breakpointState == BreakpointState.LOGGING) {
        callbacks.notifyBreakpointHit(getBreakpointStatus(state));
      } else if (breakpointState == BreakpointState.ENABLED) {
        callbacks.notifyBreakpointHit(getBreakpointStatus(state));
      } else {
        throw new RuntimeException("Unknown " + BreakpointState.class.getSimpleName() + " entry " + breakpointState);
      }
    }

    return bpShouldExecuteAction && breakpointState == BreakpointState.ENABLED;

  }

  public BreakpointPartialMemento createPartialMemento () {
    return new InternalBreakpointHolderPartialMemento(bpHitCounter);
  }

  /**
   * Creates a memento which restores a breakpoint's path hit count to zero. This is used if we backtrack to a position
   * before the definition of a breakpoint.
   *
   * @return Memento which resets path hit counts.
   */
  public static BreakpointPartialMemento createInitialStateMemento () {
    return new InternalBreakpointHolderPartialMemento(0);
  }

  /**
   * The partial memento remembers a single breakpoint's path hit count.
   */
  private static class InternalBreakpointHolderPartialMemento implements BreakpointPartialMemento {
    private int bpHitCounter = 0;

    public InternalBreakpointHolderPartialMemento(int bpHitCounter) {
      this.bpHitCounter = bpHitCounter;
    }
  }

  /**
   * Restores this breakpoint's path hit count from a memento.
   * @param bpm A memento that stores a path hit count.
   */
  public void setPathCounterFromMemento(BreakpointPartialMemento bpm) {
    if (bpm == null) {
      // Default setting
      bpHitCounter = 0;
      return;
    }

    if (bpm instanceof InternalBreakpointHolderPartialMemento) {
      InternalBreakpointHolderPartialMemento ibphm = (InternalBreakpointHolderPartialMemento) bpm;
      bpHitCounter = ibphm.bpHitCounter;
    } else {
      throw new RuntimeException("Internal error - Unexpected usage invalid memento type " + bpm.getClass().getName() + " expecting "
          + InternalBreakpointHolderPartialMemento.class.getSimpleName());
    }
  }

  @Override
  public int compareTo (InternalBreakpointHolder o) {
    if (o == null) {
      return -1;
    }
    return this.getBPID() - o.getBPID();
  }

  @Override
  public int hashCode () {
    final int prime = 31;
    int result = 1;
    result = prime * result + bpID;
    return result;
  }

  @SuppressWarnings("RedundantIfStatement")
  @Override
  public boolean equals (Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    InternalBreakpointHolder other = (InternalBreakpointHolder) obj;
    if (bpID != other.bpID) {
      return false;
    }
    return true;
  }

}
