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

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.inspector.interfaces.InspectorStatusChange;
import gov.nasa.jpf.inspector.interfaces.InspectorCallbacks;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.VM;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

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
  private final InspectorCallbacks serverCallbacks;

  /**
   * Guarded by class instance lock.
   */
  private boolean terminating = false;

  private boolean terminatingClientNotified = false;
  private boolean stopped = false;
  private boolean condTerminateAfterResume = false;

  /**
   * The access lock is used to synchronize modifications to this StopHolder object.
   * The access lock must never be held by a thread for a long time. It is used to ensure simplicity and methods should
   * use it to ensure that only a single method modifies a StopHolder's property at a time.
   */
  private ReentrantLock accessLock = new ReentrantLock();
  private Condition accessLockConditionVariable = accessLock.newCondition();

  private boolean hasJpfStoppedAtLeastOnce;
  private ReentrantLock lockJpfHasResumed;
  public synchronized boolean tryAcquireResumeExclusionLock() {
    /*
    if (!hasJpfStoppedAtLeastOnce) {
      return false; // We have not yet stopped, and so we are still either running, or in a pre-JPF-init state.
    }
    return lockJpfHasResumed.tryLock(); // If JPF is not running, we prevent it from running. If JPF is running, we fail.
    */
    return true;
  }
  public void releaseResumeExclusionLock() {
   // lockJpfHasResumed.unlock();
  }

  public StopHolder (JPFInspector inspector, InspectorCallbacks serverCallbacks) {
    this.inspector = inspector;
    this.serverCallbacks = serverCallbacks;
  }

  /**
   * Notifies clients that JPF is now stopped and then blocks execution until it is woken up by a notify call.
   * When execution is resumed, this notifies clients that execution is resumed.
   *
   * Executed by the JPF thread.
   *
   * Note: To prevent deadlocks, do not hold any lock when you call this method.
   *
   * Synchronized by the access lock.
   *
   * @param inspState Current state of the JVM and Inspector. Can't be null.
   */
  public void stopExecution (InspectorState inspState) {
    assert inspState != null;

    boolean terminate; // Local synchronized version of this.terminating variable
    accessLock.lock();
      terminate = terminating; // Use single value even if we leave this critical section

      try {
        if (!terminate) {
          this.inspState = inspState;
          stopped = true;

          serverCallbacks.notifyStateChange(InspectorStatusChange.JPF_STOPPED, getLocationDetails(inspState));
          //hasJpfStoppedAtLeastOnce = true;

          accessLockConditionVariable.signalAll(); // Notify all threads waiting for JPF to be stopped (they are woken up after the wait)
          accessLockConditionVariable.await();

          stopped = false;

          //lockJpfHasResumed.lock();

          this.inspState = null;

          checkConditionTerminateAfterResume();

        }
      } catch (InterruptedException e) {
        stopped = false;
        if (DEBUG) {
          inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ": INTERRUPTED.");
        }
      }
    accessLock.unlock();


    // Cannot be in synchronized block when send and callback
    if (terminate) {
      notifyClientTerminating();
    } else {
      serverCallbacks.notifyStateChange(InspectorStatusChange.JPF_RUNNING, getLocationDetails(inspState));
    }
  }

  /**
   * Asks the JPF thread to terminate the next time it enters {@link #stopExecution(InspectorState)}.
   *
   * Synchronized by the access lock.
   */
  public void terminating () {
    accessLock.lock();
    terminating = true;
    accessLock.unlock();
  }

  /**
   * Resets some local fields of this object. Called when the Inspector binds with a new {@link JPF} instance.
   *
   * Synchronized by the access lock.
   */
  public void newJPF () {
    accessLock.lock();
    terminating = false;
    terminatingClientNotified = false;
    accessLock.unlock();
  }

  /**
   * Gets current JPF virtual machine, or null if execution is not stopped.
   *
   * Unsynchronized.
   */
  public VM getVM() {
    InspectorState localCopy = inspState;
    if (localCopy == null) {
      return null;
    } else {
      return localCopy.getVM();
    }
  }

  /**
   * Gets current state of the JPF and of the inspector, or null if execution is not stopped.
   *
   * Unsynchronized.
   */
  public InspectorState getInspectorState () {
    return inspState;
  }

  /**
   * Gets a value that indicates whether JPF thread is currently waiting inside the method {@link #stopExecution(InspectorState)}.
   *
   * Synchronized by the acess lock.
   */
  public boolean isStopped () {
    accessLock.lock();
    try {
      return stopped;
    } finally {
      accessLock.unlock();
    }
  }

  /**   *
   * Blocks until the JPF (SuT) becomes stopped. If JPF is already stopped, then returns immediately.
   *
   * Synchronized on the access lock.
   */
  public void waitUntilStopped () {
    accessLock.lock();
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".waitUntilStopped()");
    }
    try {
      while (!isStopped()) {
        accessLockConditionVariable.await();
      }
    } catch (InterruptedException ignored) {
    }
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".waitUntilStopped() - exit");
    }
    accessLock.unlock();
  }
  /**
   * Sets the field {@link #stopped} to false and wakes up any threads waiting on the access lock.
   *
   * The only blocked thread should be the JPF thread in the {@link #stopExecution(InspectorState)} method
   *
   * Threads which calls {@link #waitUntilStopped()}
   * a) before {@link #stopExecution(InspectorState)} are notified in {@link #stopExecution(InspectorState)}
   * b) after {@link #stopExecution(InspectorState)} are not blocked and pass through {@link #waitUntilStopped()} method.
   */
  public void resumeExecution () {
    accessLock.lock();
    assert (isStopped()); // Illegal usage


    this.stopped = false;
    accessLockConditionVariable.signalAll();
    // This used to be notify() only. However, we are only using a small, constant number of threads,
    // notify() always risks acting as notifyAll() anyway because of spurious wakeups, and the performance gains are minimal.
    // Also, I don't fully understand it and don't want to debug multithreading right now, and it may work better with notifyAll.

    accessLock.unlock();
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

  /**
   * Sets the field {@link #stopped} to false, and informs clients that JPF has terminated.
   */
  public void notifyClientTerminating () {
    if (!terminatingClientNotified) {
      serverCallbacks.notifyStateChange(InspectorStatusChange.JPF_TERMINATING, null);
      terminatingClientNotified = true;
      accessLock.lock();
        stopped = true;
        accessLockConditionVariable.signalAll(); // Added to unblock commands waiting for JPF to be stopped or terminated.
      accessLock.unlock();
    }
  }

  /**
   * Terminates the search after the next resume if forward step is planned.
   *
   * <p>Note: Forward step is detected by the actual InspectorListener
   * <p>Note: Needed to be able to do backward steps after property violation.
   * <p>Note: If more such "hook are required use Command pattern"
   *
   * Unsynchronized.
   */
  public void terminateAfterResume () {
    condTerminateAfterResume = true;
  }

  /**
   * Check if terminate the search if forward step is planned.
   *
   * Unsynchronized.
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
   *
   * Unsynchronized.
   */
  public void clearFlags() {
    this.condTerminateAfterResume = false;
  }
}
