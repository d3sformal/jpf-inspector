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
package gov.nasa.jpf.inspector.server.callbacks;

import gov.nasa.jpf.inspector.interfaces.BreakpointStatus;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGTypes;
import gov.nasa.jpf.inspector.interfaces.InspectorStatusChange;
import gov.nasa.jpf.inspector.interfaces.InspectorCallbacks;
import gov.nasa.jpf.inspector.server.callbacks.commands.CallbackCommandBreakpointHit;
import gov.nasa.jpf.inspector.server.callbacks.commands.CallbackCommandChoiceGeneratorNewChoice;
import gov.nasa.jpf.inspector.server.callbacks.commands.CallbackCommandGenericError;
import gov.nasa.jpf.inspector.server.callbacks.commands.CallbackCommandGenericInfo;
import gov.nasa.jpf.inspector.server.callbacks.commands.CallbackCommandSpecifyChoiceToUse;
import gov.nasa.jpf.inspector.server.callbacks.commands.CallbackCommandStateChanged;
import gov.nasa.jpf.inspector.server.callbacks.commands.CallbackCommandUsedChoice;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.jpf.StopHolder;

import java.io.PrintStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Class used to handle clientCallbacks.
 * 
 * This class serializes clientCallbacks in the correct order and can postpone JPF stopped clientCallbacks.
 * 
 * This class should handle all clientCallbacks which should occur after JPF is stopped.
 * (Prevent creation of separate threads for each clientCallbacks and related non-determinism in clientCallbacks order due to scheduling)
 */
public class CallbacksSender extends Thread {
  protected static final boolean DEBUG = false;
  protected PrintStream out;

  private volatile boolean terminating;
  /**
   * Flag if true the enabe waits (for notification) until the running "Callbacks thread" first blocks
   */
  private volatile boolean notifyEnable;
  private final InspectorCallbacks clientCallbacks;
  /**
   * Cyclic dependency - set immediately after stopHolder is created
   */
  private StopHolder stopHolder;

  /**
   * Queue of clientCallbacks scheduled to be sent from the server to the client. This queue also acts as the monitor
   * mutex for this class and as the condition variable for this class.
   *
   * The queue itself is created using {@link Collections#synchronizedList(List)} which seems excessive because we are
   * doing locking manually anyway, but I guess a second failsafe does not hurt.
   */
  private final List<CallbackCommand> callbackQueue;
  private boolean outstandingRemovalsProcessed = true;

  public CallbacksSender (JPFInspector inspector, InspectorCallbacks clientCallbacks) {
    super(CallbacksSender.class.getSimpleName());
    if (DEBUG) {
      inspector.getDebugPrintStream().println(CallbacksSender.class.getSimpleName() + "." + CallbacksSender.class.getSimpleName() + "(...)");
    }

    // Needed for testing and direct API calls. Otherwise, when we are using a shell (graphical or console),
    // we will terminate via a System.exit(0) call so it doesn't really matter in those cases.
    setDaemon(true);

    terminating = false;
    notifyEnable = true;
    callbackQueue = Collections.synchronizedList(new LinkedList<>());

    this.clientCallbacks = clientCallbacks;
    this.out = inspector.getDebugPrintStream();
  }

  /**
   * While it is true that we should not rely on "finalize" methods, when performing automated tests,
   * we may want to terminate the threads of previous Inspector instances. Probably it would be better
   * to implement a custom method and call it at end of program rather than rely on finalization.
   */
  @SuppressWarnings("FinalizeDeclaration")
  @Override
  protected void finalize () throws Throwable {
    terminate();
    super.finalize();
  }

  /**
   * Runs this thread, then blocks until the thread actually starts executing.
   * @param stopHolder The server's {@link StopHolder}.
   */
  public void enableSender (StopHolder stopHolder) {
    // Have to be set exactly once
    assert (stopHolder != null);
    assert (this.stopHolder == null);

    synchronized (callbackQueue) {
      this.stopHolder = stopHolder;
      notifyEnable = true;

      start();

      try {
        callbackQueue.wait();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  public void terminate () {
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".terminate()");
    }
    synchronized (callbackQueue) {
      terminating = true;
      callbackQueue.clear();
      callbackQueue.notifyAll(); // Wake up the callback thread.
      try {
        callbackQueue.wait(); // Wait for the callback thread to terminate.
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".terminate() - end");
    }
  }

  /**
   * This callback thread is responsible for sending clientCallbacks to the client.
   */
  @Override
  public void run () {
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".run()");
    }
    while (!terminating) {
      synchronized (callbackQueue) {
        // Wait until termination or CB to process
        while (!terminating && callbackQueue.isEmpty()) {
          try {
            if (DEBUG) {
              out.println(this.getClass().getSimpleName() + ".run() - waiting for new command");
            }
            if (notifyEnable) {
              callbackQueue.notifyAll(); // Wakes up {@link #enableSender(StopHolder)} method when block in wait();
              notifyEnable = false;
            }
            callbackQueue.wait(); // Wait for a callback to arrive.
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
        }
      }

      CallbackCommand topmostCallback = null;
      synchronized (callbackQueue) {
        if (!callbackQueue.isEmpty()) {
          topmostCallback = callbackQueue.remove(0);
          outstandingRemovalsProcessed = false;
          if (DEBUG) {
            out.println(this.getClass().getSimpleName() + ".run() - CB to process topmostCallback=" + topmostCallback);
          }
        }
      }
      if (topmostCallback != null) {
        if (topmostCallback.waitJPF2stop()) {
          if (DEBUG) {
            out.println(this.getClass().getSimpleName() + ".run() - waitUntilStopped");
          }
          stopHolder.waitUntilStopped();
        }

        synchronized (callbackQueue) {
          if (DEBUG) {
            out.println(this.getClass().getSimpleName() + ".run() - sending SB topmostCallback=" + topmostCallback);
          }
          topmostCallback.sendCallback(clientCallbacks);
          outstandingRemovalsProcessed = true;
          callbackQueue.notifyAll(); // Notify any threads waiting for the callback queue to be empty.
        }
      }

    }

    synchronized (callbackQueue) {
      callbackQueue.notifyAll(); // We are now terminating and we must wake up the terminate method.
    }
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".run() - end");
    }
  }


  /**
   * Gets the interface whose methods will cause new clientCallbacks to be sent to the client via this thread.
   * @return This thread's serializer.
   */
  public InspectorServerCallbacks getCallbackSerializer () {
    return new CallbacksSerializer();
  }

  /**
   * This class implements {@link InspectorCallbacks} in the server. If you call a method of this class,
   * the appropriate callback will be scheduled to be sent to the client.\
   *
   * All methods of this class merely create a new {@link CallbackCommand} and add it to the callback queue.
   */
  private class CallbacksSerializer implements InspectorServerCallbacks {

    /**
     * Queue a new callback to be sent.
     * @param cmdCB The callback to be sent to the client.
     */
    private void planNewCallback (CallbackCommand cmdCB) {
      if (DEBUG) {
        out.println(this.getClass().getSimpleName() + ".planNewCallback(cmdCB=" + cmdCB + ")");
      }

      assert (stopHolder != null); // too early to register CB

      synchronized (callbackQueue) {
        if (DEBUG) {
          out.println(this.getClass().getSimpleName() + ".planNewCallback(cmdCB=" + cmdCB + ") - synch");
        }
        if (!terminating) {
          callbackQueue.add(cmdCB);
          // Wake up the callback thread and, maybe in rare cases, also any threads that wait for the callback queue to be empty.
          callbackQueue.notifyAll();
        }
      }
      if (DEBUG) {
        out.println(this.getClass().getSimpleName() + ".planNewCallback(cmdCB=" + cmdCB + ") - end");
      }
    }
    @Override
    public void notifyStateChange (InspectorStatusChange newState, String details) {
      CallbackCommandStateChanged cb = new CallbackCommandStateChanged(newState, details);
      planNewCallback(cb);
    }

    @Override
    public void genericError (String msg) {
      CallbackCommandGenericError cb = new CallbackCommandGenericError(msg);
      planNewCallback(cb);
    }

    @Override
    public void genericInfo (String msg) {
      CallbackCommandGenericInfo cb = new CallbackCommandGenericInfo(msg);
      planNewCallback(cb);
    }

    @Override
    public void notifyBreakpointHit (BreakpointStatus bp) {
      CallbackCommandBreakpointHit cb = new CallbackCommandBreakpointHit(bp);
      planNewCallback(cb);
    }

    @Override
    public void notifyChoiceGeneratorNewChoice (CGTypes cgType, String cgName, int cgId, String[] choices, int nextChoice, int defaultChoice) {
      CallbackCommandChoiceGeneratorNewChoice cb = new CallbackCommandChoiceGeneratorNewChoice(cgType, cgName, cgId, choices, nextChoice, defaultChoice);
      planNewCallback(cb);
    }

    @Override
    public void specifyChoiceToUse (int maxChoiceIndex) {
      CallbackCommandSpecifyChoiceToUse cb = new CallbackCommandSpecifyChoiceToUse(maxChoiceIndex);
      planNewCallback(cb);
    }

    @Override
    public void notifyUsedChoice (CGTypes cgType, String cgName, int cgId, int usedChoiceIndex, String usedChoice) {
      CallbackCommandUsedChoice cb = new CallbackCommandUsedChoice(cgType, cgName, cgId, usedChoiceIndex, usedChoice);
      planNewCallback(cb);
    }

    @Override
    public void waitUntilCallbackQueueIsEmpty() {
      synchronized (callbackQueue) {
        while (!callbackQueue.isEmpty() || !outstandingRemovalsProcessed) {
          try {
            callbackQueue.wait();
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
        }
      }
    }

  }

}
