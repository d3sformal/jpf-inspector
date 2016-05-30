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

import gov.nasa.jpf.inspector.interfaces.BreakPointCreationInformation;
import gov.nasa.jpf.inspector.interfaces.BreakPointStates;
import gov.nasa.jpf.inspector.interfaces.BreakPointStatus;
import gov.nasa.jpf.inspector.interfaces.InspectorCallBacks;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.server.breakpoints.BreakPointHandler.BreakPointPartialMemento;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanInterface;
import gov.nasa.jpf.inspector.server.expression.InspectorState;

class InternalBreakpointHolder implements Comparable<InternalBreakpointHolder> {

  private static int bpIDCounter = 1;
  private static Object bpIDCounterLock = new Object();

  /**
   * Generates new Breakpoint IDs
   * 
   * @return Get new Breakpoint ID.
   */
  public static int getNextBpID () {
    synchronized (bpIDCounterLock) {
      return bpIDCounter++;
    }
  }

  private final InspectorCallBacks callbacks;

  protected final int bpID; // ID of the breakpoint
  protected final boolean userBP; // / Mark whether the breakpoint is visible to user or it's BP for the internal purposes of the checker
  protected final boolean sigleHitBP; // / Remove this breakpoint on the first breakpointHit (this or some other BP)

  protected BreakPointModes bpMode = BreakPointModes.BP_MODE_NONE;
  protected BreakPointStates bpState = BreakPointStates.BP_STATE_ENABLED;
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
   * @param newID
   * @param callbacks
   * @param userBP
   *        This breakpoint is created by the client inspector side (should by reported to user)
   * @param sigleHitBP
   *        This breakpoint should be removed if the first breakpoint hits (this or some other BP)
   */
  public InternalBreakpointHolder (int newID, InspectorCallBacks callbacks, boolean userBP, boolean sigleHitBP) {
    if (newID == BreakPointCreationInformation.BP_ID_NOT_DEFINED) {
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
    this.sigleHitBP = sigleHitBP;
  }

  public void modifyBPSettings (BreakPointCreationInformation newSettings, ExpressionBooleanInterface newBPExpression) {
    assert newSettings != null;

    if (newSettings.getName() != null) {
      bpName = newSettings.getName();
    }
    if (newSettings.getState() != null) {
      bpState = newSettings.getState();
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

  public boolean isUserBreakpoint () {
    return userBP;
  }

  public boolean isSingleHitBP () {
    return sigleHitBP;
  }

  public boolean isAssert () {
    return false;
  }

  public BreakPointStatus getBreakpointStatus (InspectorState state) {

    String details = null;
    String bpExpressionNormalized = null;

    if (bpExpression != null) {
      if (state != null) {
        details = bpExpression.getDetails(state);
      }
      bpExpressionNormalized = bpExpression.getNormalizedExpression();
    }

    return new BreakPointStatusImpl(bpID, bpName, bpHitCounter, bpHitCounterTotal, lowerBound, upperBound, bpState, bpMode, bpExpressionStr,
        bpExpressionNormalized, details);
  }

  /**
   * Tests whether breakpoints hits. Uses hit point counter, ...
   * 
   * @return True if breakpoint hit and should break the execution (BP is in the enabled state, expected Hit count, etc...)
   */
  public boolean evaluateBreakpoint (InspectorState state) {
    boolean bpHitted = false;
    if (bpExpression != null) {
      try {
        bpHitted = bpExpression.evaluateExpression(state);
      } catch (JPFInspectorException e) {
        // Ignore the exception Breakpoint cannot be evaluated --> thus does not hold
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
      if (bpState == BreakPointStates.BP_STATE_DISABLED) {
        // No action expected
      } else if (bpState == BreakPointStates.BP_STATE_LOGGING) {
        callbacks.notifyBreakpointHit(getBreakpointStatus(state));
      } else if (bpState == BreakPointStates.BP_STATE_ENABLED) {
        callbacks.notifyBreakpointHit(getBreakpointStatus(state));
      } else {
        throw new RuntimeException("Unknow " + BreakPointStates.class.getSimpleName() + " entry " + bpState);
      }
    }

    if (bpShouldExecuteAction && bpState == BreakPointStates.BP_STATE_ENABLED) {
      return true;
    }

    return false;
  }

  public BreakPointPartialMemento createPartialMemento () {
    return new InternalBreakPointHolderPartialMemento(bpHitCounter);
  }

  /**
   * Creates memento which restores Breakpoint into the initial state. Used if backtracked before definition of Breakpoint.
   * 
   * @return Memento which resets hitCounts
   */
  public static BreakPointPartialMemento createInitialStateMemento () {
    return new InternalBreakPointHolderPartialMemento(0);
  }

  private static class InternalBreakPointHolderPartialMemento implements BreakPointPartialMemento {
    private int bpHitCounter = 0;

    public InternalBreakPointHolderPartialMemento (int bpHitCounter) {
      this.bpHitCounter = bpHitCounter;
    }
  }

  public void setPartialMemento (BreakPointPartialMemento bpm) {
    if (bpm == null) {
      // Default setting
      bpHitCounter = 0;
      return;
    }

    if (bpm instanceof InternalBreakPointHolderPartialMemento) {
      InternalBreakPointHolderPartialMemento ibphm = (InternalBreakPointHolderPartialMemento) bpm;
      bpHitCounter = ibphm.bpHitCounter;
    } else {
      throw new RuntimeException("Internal error - Unexpected usage invalid memento type " + bpm.getClass().getName() + " expecting "
          + InternalBreakPointHolderPartialMemento.class.getSimpleName());
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

  @Override
  public boolean equals (Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    InternalBreakpointHolder other = (InternalBreakpointHolder) obj;
    if (bpID != other.bpID)
      return false;
    return true;
  }

}
