package gov.nasa.jpf.inspector.server.callbacks;

import gov.nasa.jpf.inspector.interfaces.BreakPointStatus;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGTypes;
import gov.nasa.jpf.inspector.interfaces.CommandsInterface.InspectorStates;
import gov.nasa.jpf.inspector.interfaces.InspectorCallBacks;
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
 * Class used to handle callbacks.
 * 
 * This class serializes callbacks in correct order and can postpone JPF stopped callbacks.
 * 
 * This class should handle all callbacks which should occur after JPF is stopped. (Prevent creation of separate threads for each callbacks and related
 * non-determinism in callbacks order due to scheduling)
 * 
 * @author alf
 */
public class CallbacksSender extends Thread {
  protected static final boolean DEBUG = false;
  protected PrintStream out;

  private volatile boolean terminating;
  private volatile boolean notifyEnable; // Flag if true the enabe waits (for notification) until the running "Callbacks thread" first blocks
  private final InspectorCallBacks callbacks;
  private StopHolder stopHolder; // Cyclic dependency - set immediately after stopHolder is created

  private final List<CallbackCommand> cmdCBQueue;

  public CallbacksSender (JPFInspector inspector, InspectorCallBacks callbacks) {
    super(CallbacksSender.class.getSimpleName());
    if (DEBUG) {
      inspector.getDebugPrintStream().println(CallbacksSender.class.getSimpleName() + "." + CallbacksSender.class.getSimpleName() + "(...)");
    }
    setDaemon(true);

    terminating = false;
    notifyEnable = true;
    cmdCBQueue = Collections.synchronizedList(new LinkedList<>());

    this.callbacks = callbacks;
    this.out = inspector.getDebugPrintStream();
  }

  @Override
  protected void finalize () throws Throwable {
    terminate();
    super.finalize();
  }

  public void enableSender (StopHolder stopHolder) {
    // Have to be set exactly once
    assert (stopHolder != null);
    assert (this.stopHolder == null);

    synchronized (cmdCBQueue) {
      this.stopHolder = stopHolder;
      notifyEnable = true;

      start();

      try {
        cmdCBQueue.wait();
      } catch (InterruptedException e) {
      }
    }
  }

  public void terminate () {
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".terminate()");
    }
    synchronized (cmdCBQueue) {
      terminating = true;
      cmdCBQueue.clear();
      cmdCBQueue.notifyAll(); // Wake up "Callback thread"
      try {
        cmdCBQueue.wait();
      } catch (InterruptedException e) {
      }
    }
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".terminate() - end");
    }
  }

  // "Callback thread"
  @Override
  public void run () {
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".run()");
    }
    while (!terminating) {
      synchronized (cmdCBQueue) {
        // Wait until termination or CB to process
        while (!terminating && cmdCBQueue.isEmpty()) {
          try {
            if (DEBUG) {
              out.println(this.getClass().getSimpleName() + ".run() - waiting for new command");
            }
            if (notifyEnable) {
              cmdCBQueue.notifyAll(); // Wakes up {@link #enableSender(StopHolder)} method when block in wait();
              notifyEnable = false;
            }
            cmdCBQueue.wait();
          } catch (InterruptedException e) {
          }
        }
      }

      if (!cmdCBQueue.isEmpty()) {
        CallbackCommand cmdCB = cmdCBQueue.remove(0);
        if (DEBUG) {
          out.println(this.getClass().getSimpleName() + ".run() - CB to process cmdCB=" + cmdCB);
        }

        if (cmdCB.waitJPF2stop()) {
          if (DEBUG) {
            out.println(this.getClass().getSimpleName() + ".run() - waitUntilStopped");
          }
          stopHolder.waitUntilStopped();
        }
        if (DEBUG) {
          out.println(this.getClass().getSimpleName() + ".run() - sending SB cmdCB=" + cmdCB);
        }
        cmdCB.sendCallback(callbacks);
      }
    }

    synchronized (cmdCBQueue) {
      cmdCBQueue.notifyAll(); // WakeUP terminate method
    }
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".run() - end");
    }
  }

  /**
   * Queue new Classback to be executed
   * 
   * @param cmdCB
   */
  private void planNewCallback (CallbackCommand cmdCB) {
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".planNewCallback(cmdCB=" + cmdCB + ")");
    }

    assert (stopHolder != null); // too early to register CB

    synchronized (cmdCBQueue) {
      if (DEBUG) {
        out.println(this.getClass().getSimpleName() + ".planNewCallback(cmdCB=" + cmdCB + ") - synch");
      }
      if (!terminating) {
        cmdCBQueue.add(cmdCB);
        cmdCBQueue.notify(); // Wake up "Callback thread" if blocked
      }
    }
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".planNewCallback(cmdCB=" + cmdCB + ") - end");
    }
  }

  public InspectorCallBacks getCallbackSerializer () {
    return new CallbacksSerializer();
  }

  private class CallbacksSerializer implements InspectorCallBacks {

    @Override
    public void notifyStateChange (InspectorStates newState, String details) {
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
    public void notifyBreakpointHit (BreakPointStatus bp) {
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

  }

}
