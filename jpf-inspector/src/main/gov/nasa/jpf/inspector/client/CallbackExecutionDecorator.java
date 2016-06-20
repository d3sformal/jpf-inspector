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
 * Well, this class is a mess. As far as I can tell, it only actually does something while in record execution mode
 * and it attempts to synchronize the hidden "wait for callback" commands with the actual callbacks, and it causes the
 * JPF thread or whatever is calling these callbacks to wait (on the recorder object) until the command executes or something.
 * It's weird.
 * 
 * @author Alf
 * 
 */
public final class CallbackExecutionDecorator implements InspectorCallbacks {
  private static final boolean DEBUG = false;
  @SuppressWarnings("FieldCanBeLocal") // IDEA bug
  private final PrintStream debugOutStream;

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


  /**
   * Object on which access to Callbacks are synchronized and threads are blocked.
   * This should be the client's {@link CommandRecorder}.
   * All methods of this class are synchronized on this object. Condition variables are also used.
   */
  private final CommandRecorder syncObj;
  /**
   * The wrapped callback handler.
   */
  private final InspectorCallbacks cb;

  private WORKING_MODE mode;

  /**
   * When this is not null, the command execution thread is waiting for a callback of this kind.
   */
  private CallbackKind method = null;

  /**
   * When this is not null and {@link #method} is {@link CallbackKind#CB_STATE_CHANGE}, then the command execution
   * thread is waiting for a state-change callback that changes to this state.
   */
  private InspectorStatusChange cbStateChange_expectedState = null;

  private boolean isCBwaiting;
  private boolean error;

  /**
   * Initializes a new instance of this decorator.
   * 
   * @param syncObj Object used to synchronized Command and CallBacks thread
   * @param cb Where to send/forward callbacks
   * @param debugOutStream Debug output stream
   */
  public CallbackExecutionDecorator (CommandRecorder syncObj, InspectorCallbacks cb, PrintStream debugOutStream) {
    this.syncObj = syncObj;
    this.cb = cb;
    this.mode = WORKING_MODE.WM_USER_COMMANDS;
    this.debugOutStream = debugOutStream;

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

  /**
   * Blocks execution until a callback of the specified kind arrives from the server.
   *
   * This must only be called by the command execution thread.
   * This only has an effect while executing a record, not during normal command entering.
   *
   * @param wait4method Type of the callback to wait for.
   */
  private void waitForCB(CallbackKind wait4method) {
    synchronized (syncObj) {
      if (mode == WORKING_MODE.WM_EXECUTION_RECORD) {

        // Wait until we either leave record execution mode or until all wait-for-callback commands are processed.
        // In practice, I think this loop shouldn't ever be entered, but what do I know.
        while (method != null && mode == WORKING_MODE.WM_EXECUTION_RECORD) {
          try {
            syncObj.wait();
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
        }

        // If, in the meantime, we left record execution mode, then this command ceases to have any effect.
        if (mode == WORKING_MODE.WM_EXECUTION_RECORD) {
          method = wait4method;

          if (isCBwaiting) {
            // Wake up waiting CB thread
            syncObj.notifyAll();
          }

          // Wait until CB processing is terminated
          try {
            syncObj.wait();
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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


  @Override
  public void notifyStateChange (InspectorStatusChange newState, String details) {
    if (DEBUG) {
      debugOutStream.println(this.getClass().getSimpleName() + ".notifyStateChange( newState=" + newState + ", details=" + details + ")");
    }
    synchronized (syncObj) {
      if (DEBUG) {
        debugOutStream.println(this.getClass().getSimpleName() + ".notifyStateChange - in sync section");
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
      debugOutStream.println(this.getClass().getSimpleName() + ".notifyStateChange( newState=" + newState + ", details=" + details + ") - end");
    }

  }
  @Override
  public void genericError (String msg) {
    if (DEBUG) {
      debugOutStream.println(this.getClass().getSimpleName() + ".genericError(msg=" + msg + ")");
    }

    synchronized (syncObj) {
      if (DEBUG) {
        debugOutStream.println(this.getClass().getSimpleName() + ".genericError - in sync section");
      }

      waitUntilCBIsSpecified(CallbackKind.CB_GENERIC_ERROR);
      cb.genericError(msg);
      unblockCmdThread();
    }
  }
  @Override
  public void genericInfo (String msg) {
    if (DEBUG) {
      debugOutStream.println(this.getClass().getSimpleName() + ".genericInfo(msg=" + msg + ")");
    }

    synchronized (syncObj) {
      if (DEBUG) {
        debugOutStream.println(this.getClass().getSimpleName() + ".genericInfo - in sync section");
      }

      waitUntilCBIsSpecified(CallbackKind.CB_ANY);
      // Not command thread is blocked -> locks are unlocked -> we can call callbacks
      cb.genericInfo(msg);
      // Do not wake up, CMD thread, wait for proper callback
    }

    if (DEBUG) {
      debugOutStream.println(this.getClass().getSimpleName() + ".genericInfo(msg=" + msg + ") - end");
    }
  }
  @Override
  public void notifyBreakpointHit (BreakpointStatus bp) {
    if (DEBUG) {
      debugOutStream.println(this.getClass().getSimpleName() + ".notifyBreakpointHit(bp=" + bp + ")");
    }

    synchronized (syncObj) {
      if (DEBUG) {
        debugOutStream.println(this.getClass().getSimpleName() + ".notifyBreakpointHit - in synch section");
      }

      waitUntilCBIsSpecified(CallbackKind.CB_BREAKPOINT_HIT);
      cb.notifyBreakpointHit(bp);
      unblockCmdThread();
    }

    if (DEBUG) {
      debugOutStream.println(this.getClass().getSimpleName() + ".notifyBreakpointHit(bp=" + bp + ") - end");
    }
  }


  @Override
  public void notifyChoiceGeneratorNewChoice (CGTypes cgType, String cgName, int cgId, String[] choices, int nextChoice, int defaultChoice) {
    if (DEBUG) {
      debugOutStream.println(this.getClass().getSimpleName() + ".notifyChoiceGeneratorNewChoice(...)");
    }

    synchronized (syncObj) {
      if (DEBUG) {
        debugOutStream.println(this.getClass().getSimpleName() + ".notifyChoiceGeneratorNewChoice(...) - in sync section");
      }

      waitUntilCBIsSpecified(CallbackKind.CB_CG_NEW_CHOICE);
      cb.notifyChoiceGeneratorNewChoice(cgType, cgName, cgId, choices, nextChoice, defaultChoice);
      unblockCmdThread();
    }

    if (DEBUG) {
      debugOutStream.println(this.getClass().getSimpleName() + ".notifyChoiceGeneratorNewChoice(...) - end");
    }

  }
  @Override
  public void specifyChoiceToUse (int maxChoiceIndex) {
    if (DEBUG) {
      debugOutStream.println(this.getClass().getSimpleName() + ".specifyChoiceToUse(maxChoiceIndex=" + maxChoiceIndex + ")");
    }

    synchronized (syncObj) {
      if (DEBUG) {
        debugOutStream.println(this.getClass().getSimpleName() + ".specifyChoiceToUse - in sync section");
      }
      waitUntilCBIsSpecified(CallbackKind.CB_CG_CHOICE_TO_USE);
      cb.specifyChoiceToUse(maxChoiceIndex);
      unblockCmdThread();
    }

    if (DEBUG) {
      debugOutStream.println(this.getClass().getSimpleName() + ".specifyChoiceToUse(maxChoiceIndex=" + maxChoiceIndex + ") - end");
    }

  }
  @Override
  public void notifyUsedChoice (CGTypes cgType, String cgName, int cgId, int usedChoiceIndex, String usedChoice) {
    if (DEBUG) {
      debugOutStream.println(this.getClass().getSimpleName() + ".notifyUsedChoice(...)");
    }

    synchronized (syncObj) {
      if (DEBUG) {
        debugOutStream.println(this.getClass().getSimpleName() + ".notifyUsedChoice(...) - in sync section");
      }
      waitUntilCBIsSpecified(CallbackKind.CB_CG_USED_CHOICE);
      cb.notifyUsedChoice(cgType, cgName, cgId, usedChoiceIndex, usedChoice);
      unblockCmdThread();
    }

    if (DEBUG) {
      debugOutStream.println(this.getClass().getSimpleName() + ".notifyUsedChoice(...) - end");
    }
  }

  public void nextCB_genericError () {
    waitForCB(CallbackKind.CB_GENERIC_ERROR);
  }
  public void nextCB_genericInfo () {
    // Nothing to do (Generic infos are ignored)
  }
  public void nextCB_BreakpointHit () {
    waitForCB(CallbackKind.CB_BREAKPOINT_HIT);
  }
  public void nextCB_ChoiceGeneratorNewChoice () {
    waitForCB(CallbackKind.CB_CG_NEW_CHOICE);
  }
  public void nextCB_specifyChoiceToUse () {
    waitForCB(CallbackKind.CB_CG_CHOICE_TO_USE);
  }
  public void nextCB_UsedChoice () {
    waitForCB(CallbackKind.CB_CG_USED_CHOICE);
  }
  public void nextCB_StateChange (InspectorStatusChange expectedState) {
    synchronized (syncObj) {
      cbStateChange_expectedState = expectedState;
      waitForCB(CallbackKind.CB_STATE_CHANGE);
    }
  }

}
