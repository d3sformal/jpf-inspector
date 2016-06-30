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

package gov.nasa.jpf.inspector.server.jpf;

import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.inspector.interfaces.InspectorStatusChange;
import gov.nasa.jpf.inspector.interfaces.InspectorCallbacks;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.VM;

/**
 * Handles resuming and stopping JPF execution. This is a very important class central to the server's operation.
 * 
 * This class also holds current state of the SuT (JVM).
 *
 * Implementors: When modifying this class, make sure to think about thread safety and synchronization.
 */
public class StopHolder {
  private static final boolean DEBUG = false;

  /**
   * State of the stopped JVM.
   */
  private InspectorState inspState;
  /**
   * Inspector server.
   */
  private final JPFInspector inspector;
  private final InspectorCallbacks callbacks;

  /**
   * Guarded by class instance lock.
   */
  private boolean terminating = false;
  private boolean terminatingClientNotified = false;
  private boolean stopped = false;
  private boolean condTerminateAfterResume = false;

  public StopHolder (JPFInspector inspector, InspectorCallbacks callbacks) {
    this.inspector = inspector;
    this.callbacks = callbacks;
  }

  /**
   * Notifies the client that JPF is now stopped and then blocks execution until it is woken up by a notify call.
   * Executed by the JPF thread.
   *
   * Note: To prevent deadlocks cannot hold any lock if this methods is called.
   *
   * @param inspState Current state of the JVM and Inspector. Can't be null.
   */
  public void stopExecution (InspectorState inspState) {
    assert inspState != null;

    boolean terminate; // Local synchronized version of this.terminating variable
    synchronized (this) {
      terminate = terminating; // Use single value even if we leave synchronized block

      try {
        if (!terminate) {
          this.inspState = inspState;
          stopped = true;

          callbacks.notifyStateChange(InspectorStatusChange.JPF_STOPPED, getLocationDetails(inspState));

          notifyAll(); // Notify all threads waiting for JPF to be stopped (they are woken up after the wait)
          wait();

          stopped = false;
          this.inspState = null;

          checkConditionTerminateAfterResume();

        }
      } catch (InterruptedException e) {
        stopped = false;
        if (DEBUG) {
          inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ": INTERRUPTED.");
        }
      }
    } // End of synchronized block

    // Cannot be in synchronized block when send and callback
    if (terminate) {
      notifyClientTerminating();
    } else {
      callbacks.notifyStateChange(InspectorStatusChange.JPF_RUNNING, getLocationDetails(inspState));
    }
  }

  public synchronized void terminating () {
    terminating = true;
  }

  public synchronized void newJPF () {
    terminating = false;
    terminatingClientNotified = false;
  }

  /**
   * Gets current state of the SuT or NULL if execution not stopped.
   */
  public VM getJVM () {
    return inspState.getVM();
  }

  /**
   * Gets current state of the JPF and of the inspector or NULL if execution not stopped.
   */
  public InspectorState getInspectorState () {
    return inspState;
  }

  public synchronized boolean isStopped () {
    return stopped;
  }

  /**
   *
   * Blocks until the JPF (SuT) becomes stopped. If JPF is already stopped, then returns immediately.
   *
   * This method is thread-safe.
   */
  public synchronized void waitUntilStopped () {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".waitUntilStopped()");
    }
    try {
      while (!isStopped()) {
        wait();
      }
    } catch (InterruptedException ignored) {
    }
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".waitUntilStopped() - exit");
    }
  }
  /**
   * The only blocked thread should be the JPF thread in the {@link #stopExecution(InspectorState)} method
   *
   * Threads which calls {@link #waitUntilStopped()}
   * a) before {@link #stopExecution(InspectorState)} are notified in {@link #stopExecution(InspectorState)}
   * b) after {@link #stopExecution(InspectorState)} are not blocked and pass through {@link #waitUntilStopped()} method.
   */
  public synchronized void resumeExecution () {
    assert (isStopped()); // Illegal usage


    this.stopped = false;
    notifyAll();
    // This used to be notify() only. However, we are only using a small, constant number of threads,
    // notify() always risks acting as notifyAll() anyway because of spurious wakeups, and the performance gains are minimal.
    // Also, I don't fully understand it and don't want to debug multithreading right now, and it may work better with notifyAll.
  }

  private static String getLocationDetails(InspectorState inspState) {
    StringBuilder sb = new StringBuilder(100);

    VM vm = inspState.getVM();
    assert vm != null;
    Instruction instr = vm.getInstruction();
    if (instr == null) {
      return null;
    }

    sb.append("SuT ");
    if (vm.getCurrentThread() != null) {
      sb.append("(thread ")
        .append(vm.getCurrentThread().getId())
        .append(") ");
    }

    String sourceline = "source unavailable";
    if (instr.getSourceLine() != null) {
      sourceline = "source: " + instr.getSourceLine().trim();
    }
    sb.append("will now execute ")
            .append(instr.getMethodInfo().getSourceFileName())
            .append(":")
            .append(instr.getLineNumber())
            .append(" (")
            .append(instr.toString())
            .append("), ")
            .append(sourceline);
    return sb.toString();
  }

  public void notifyClientTerminating () {
    if (!terminatingClientNotified) {
      callbacks.notifyStateChange(InspectorStatusChange.JPF_TERMINATING, null);
      terminatingClientNotified = true;
      synchronized (this) {
        stopped = true;
        notifyAll(); // Added to unblock commands waiting for JPF to be stopped or terminated.
      }
    }
  }

  /**
   * Terminates the search after the resume if forward step is planned.
   * <p>Note: Forward step is detected by the actual InspectorListener
   * <p>Note: Needed to be able to do backward steps after property violation.
   * 
   * <p>Note: If more such "hook are required use Command pattern"
   */
  public void terminateAfterResume () {
    condTerminateAfterResume = true;
  }

  /**
   * Check if terminate the search if forward step is planned.
   */
  private void checkConditionTerminateAfterResume () {
    if (condTerminateAfterResume) {
      condTerminateAfterResume = false;

      // Check which step is planned after the resume
      InspectorListener listener = inspector.getInspectorListener();
      ListenerAdapter listenerMode = listener.getCurrentMode();
      if (!(listenerMode instanceof InspectorListenerModeSilent)) {
        // Not a backward step
        inspState.getSearch().terminate();
      }
    }
  }

  /**
   * Returns the StopHolder to the initial state it should be in when JPF is launched. We call this method because
   * JPF may end and restart a number of times during a single execution of JPF Shell.
   */
  public void clearFlags() {
    this.condTerminateAfterResume = false;
  }
}
