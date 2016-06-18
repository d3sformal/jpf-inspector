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

package gov.nasa.jpf.inspector.client;

import gov.nasa.jpf.inspector.client.commands.CmdCallback;
import gov.nasa.jpf.inspector.interfaces.BreakpointStatus;
import gov.nasa.jpf.inspector.interfaces.CallbackKind;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGTypes;
import gov.nasa.jpf.inspector.interfaces.InspectorStatusChange;
import gov.nasa.jpf.inspector.interfaces.InspectorCallbacks;

import java.io.PrintStream;

/**
 * 
 * Decorator used during (re)execution of recorded commands.
 * It is used to postpone command thread until expected callback is sent.
 * (wait until Breakpoint hits, single step is executed, etc.)
 * 
 * Note: Used by {@link CmdCallback}
 * 
 * Has 2 working modes - pass through {@link WORKING_MODE#WM_USER_COMMANDS}
 * or blocking {@link WORKING_MODE#WM_EXECUTION_RECORD}.
 * 
 * @author Alf
 * 
 */
public class CallbackExecutionDecorator implements InspectorCallbacks {

  protected static final boolean DEBUG = false;
  protected final PrintStream out;

  /**
   * Way in which decorator works.
   */
  public enum WORKING_MODE {
    /**
     * In this mode (equivalent to standard execution of user commands from frontends)
     * are callbacks immediately passed to decorated CB processor.
     */
    WM_USER_COMMANDS,

    /**
     * In this mode (equivalent to replay/execution of recorded trace)
     * callbacks blocks, until nextCB_* method is called (rendezvous)
     */
    WM_EXECUTION_RECORD,
  }

  // *************************************************************************
  // Protected entries
  // *************************************************************************

  private final Object syncObj; // Object on which access to Callbacks are synchronized and threads are blocked
  private final InspectorCallbacks cb; // Where forward callbacks

  protected WORKING_MODE mode;

  protected CallbackKind method = null; // If not null -> Command thread waits for specified CallBack

  private InspectorStatusChange cbStateChange_expectedState = null;

  private boolean isCBwaiting;
  protected boolean error;

  /**
   * 
   * @param syncObj Object used to synchronized Command and CallBacks thread
   * @param cb Where to send/forward callbacks
   * @param outStream Debug output stream
   */
  public CallbackExecutionDecorator (Object syncObj, InspectorCallbacks cb, PrintStream outStream) {
    this.syncObj = syncObj;
    this.cb = cb;
    this.mode = WORKING_MODE.WM_USER_COMMANDS;
    this.out = outStream;

    this.isCBwaiting = false;
    this.error = false;
  }

  public boolean getError () {
    return error;
  }

  /**
   * Set working mode of the decorator.
   * 
   * @param newMode New mode to set
   * @return Gets previous mode.
   */
  public WORKING_MODE setNewMode (WORKING_MODE newMode) {
    synchronized (syncObj) {
      WORKING_MODE oldMode = mode;
      if (newMode != null) {
        mode = newMode;

        // Unblock blocked callbacks (and/or Command thread)
        if (newMode.equals(WORKING_MODE.WM_USER_COMMANDS)) {
          syncObj.notifyAll();
        }

        if (oldMode == WORKING_MODE.WM_USER_COMMANDS && newMode == WORKING_MODE.WM_EXECUTION_RECORD) {
          // Start new replay of execution record -> clear errors
          error = false;
        }
      }
      return oldMode;
    }
  }

  // **************************************************************************
  // **************************************************************************
  // **************************************************************************

  // Block CB thread until Command thread specifies which CB is expected
  // Null means pass any possible callback
  private void waitUntilCBIsSpecified(CallbackKind calledCallback) {
    synchronized (syncObj) {
      if (mode == WORKING_MODE.WM_EXECUTION_RECORD) {

        while (method == null && mode == WORKING_MODE.WM_EXECUTION_RECORD) {
          isCBwaiting = true;
          try {
            syncObj.wait();
          } catch (InterruptedException e) {
            // Ignore
          }
          isCBwaiting = false;
        }

        if (method != null && mode == WORKING_MODE.WM_EXECUTION_RECORD) {
          // Null means any callback is possible
          if (!(CallbackKind.CB_ANY.equals(calledCallback) || method.equals(calledCallback))) {
            // Set error state
            error = true;
          }
        }
      }
    }
  }

  // Blocks Command thread until first callback is executed
  private void waitForCB(CallbackKind wait4method) {
    synchronized (syncObj) {
      if (mode == WORKING_MODE.WM_EXECUTION_RECORD) {

        while (method != null && mode == WORKING_MODE.WM_EXECUTION_RECORD) {
          try {
            syncObj.wait();
          } catch (InterruptedException e) {
            // Ignore
          }
        }

        if (mode == WORKING_MODE.WM_EXECUTION_RECORD) {
          method = wait4method;

          if (isCBwaiting) {
            // WakeUP waiting CB thread
            syncObj.notifyAll();
          }

          // Wait until CB processing is terminated
          try {
            syncObj.wait();
          } catch (InterruptedException e) {
            // Ignore
          }
        }
      }
    }
  }

  // Called by CB thread to wake up blocked Command thread
  private void unblockCmdThread() {
    synchronized (syncObj) {
      assert (isCBwaiting == false);
      method = null;
      syncObj.notifyAll();
    }
  }

  // **************************************************************************
  // **************************************************************************
  // **************************************************************************

  @Override
  public void notifyStateChange (InspectorStatusChange newState, String details) {
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".notifyStateChange( newState=" + newState + ", details=" + details + ")");
    }
    synchronized (syncObj) {
      if (DEBUG) {
        out.println(this.getClass().getSimpleName() + ".notifyStateChange - in sync section");
      }

      if (mode == WORKING_MODE.WM_EXECUTION_RECORD) {
        waitUntilCBIsSpecified(CallbackKind.CB_STATE_CHANGE);

        if (cbStateChange_expectedState != null && !newState.equals(cbStateChange_expectedState)) {
          error = true;
        }
      }

      cb.notifyStateChange(newState, details);

      unblockCmdThread();
    }

    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".notifyStateChange( newState=" + newState + ", details=" + details + ") - end");
    }

  }

  public void nextCB_StateChange (InspectorStatusChange expectedState) {
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".nextCB_StateChange(expectedState=" + expectedState + ")");
    }

    synchronized (syncObj) {
      cbStateChange_expectedState = expectedState;
      waitForCB(CallbackKind.CB_STATE_CHANGE);
    }

    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".nextCB_StateChange(expectedState=" + expectedState + ") - end");
    }
  }

  /* **************************************************************************/
  /* **************************************************************************/
  /* **************************************************************************/

  @Override
  public void genericError (String msg) {
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".genericError(msg=" + msg + ")");
    }

    synchronized (syncObj) {
      if (DEBUG) {
        out.println(this.getClass().getSimpleName() + ".genericError - in sync section");
      }

      waitUntilCBIsSpecified(CallbackKind.CB_STATE_CHANGE);
      cb.genericError(msg);
      unblockCmdThread();
    }
  }

  public void nextCB_genericError () {
    waitForCB(CallbackKind.CB_GENERIC_ERROR);
  }

  /* **************************************************************************/
  /* **************************************************************************/
  /* **************************************************************************/

  @Override
  public void genericInfo (String msg) {
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".genericInfo(msg=" + msg + ")");
    }

    synchronized (syncObj) {
      if (DEBUG) {
        out.println(this.getClass().getSimpleName() + ".genericInfo - in sync section");
      }

      waitUntilCBIsSpecified(CallbackKind.CB_ANY);
      // Not command thread is blocked -> locks are unlocked -> we can call callbacks
      cb.genericInfo(msg);
      // Do not wake up, CMD thread, wait for proper callback
    }

    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".genericInfo(msg=" + msg + ") - end");
    }
  }

  public void nextCB_genericInfo () {
    // Nothing to do (Generic infos are ignored)
  }

  /* **************************************************************************/
  /* **************************************************************************/
  /* **************************************************************************/
  @Override
  public void notifyBreakpointHit (BreakpointStatus bp) {
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".notifyBreakpointHit(bp=" + bp + ")");
    }

    synchronized (syncObj) {
      if (DEBUG) {
        out.println(this.getClass().getSimpleName() + ".notifyBreakpointHit - in synch section");
      }

      waitUntilCBIsSpecified(CallbackKind.CB_BREAKPOINT_HIT);
      cb.notifyBreakpointHit(bp);
      unblockCmdThread();
    }

    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".notifyBreakpointHit(bp=" + bp + ") - end");
    }
  }

  public void nextCB_BreakpointHit () {
    waitForCB(CallbackKind.CB_BREAKPOINT_HIT);
  }

  /* **************************************************************************/
  /* **************************************************************************/
  /* **************************************************************************/

  @Override
  public void notifyChoiceGeneratorNewChoice (CGTypes cgType, String cgName, int cgId, String[] choices, int nextChoice, int defaultChoice) {
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".notifyChoiceGeneratorNewChoice(...)");
    }

    synchronized (syncObj) {
      if (DEBUG) {
        out.println(this.getClass().getSimpleName() + ".notifyChoiceGeneratorNewChoice(...) - in sync section");
      }

      waitUntilCBIsSpecified(CallbackKind.CB_CG_NEW_CHOICE);
      cb.notifyChoiceGeneratorNewChoice(cgType, cgName, cgId, choices, nextChoice, defaultChoice);
      unblockCmdThread();
    }

    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".notifyChoiceGeneratorNewChoice(...) - end");
    }

  }

  public void nextCB_ChoiceGeneratorNewChoice () {
    waitForCB(CallbackKind.CB_CG_NEW_CHOICE);
  }

  /* **************************************************************************/
  /* **************************************************************************/
  /* **************************************************************************/

  @Override
  public void specifyChoiceToUse (int maxChoiceIndex) {
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".specifyChoiceToUse(maxChoiceIndex=" + maxChoiceIndex + ")");
    }

    synchronized (syncObj) {
      if (DEBUG) {
        out.println(this.getClass().getSimpleName() + ".specifyChoiceToUse - in sync section");
      }
      waitUntilCBIsSpecified(CallbackKind.CB_CG_CHOICE_TO_USE);
      cb.specifyChoiceToUse(maxChoiceIndex);
      unblockCmdThread();
    }

    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".specifyChoiceToUse(maxChoiceIndex=" + maxChoiceIndex + ") - end");
    }

  }

  public void nextCB_specifyChoiceToUse () {
    waitForCB(CallbackKind.CB_CG_CHOICE_TO_USE);
  }

  /* **************************************************************************/
  /* **************************************************************************/
  /* **************************************************************************/

  @Override
  public void notifyUsedChoice (CGTypes cgType, String cgName, int cgId, int usedChoiceIndex, String usedChoice) {
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".notifyUsedChoice(...)");
    }

    synchronized (syncObj) {
      if (DEBUG) {
        out.println(this.getClass().getSimpleName() + ".notifyUsedChoice(...) - in sync section");
      }
      waitUntilCBIsSpecified(CallbackKind.CB_CG_USED_CHOICE);
      cb.notifyUsedChoice(cgType, cgName, cgId, usedChoiceIndex, usedChoice);
      unblockCmdThread();
    }

    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".notifyUsedChoice(...) - end");
    }
  }

  public void nextCB_UsedChoice () {
    waitForCB(CallbackKind.CB_CG_USED_CHOICE);
  }

}
