package gov.nasa.jpf.inspector.server.jpf;

import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.inspector.server.breakpoints.BreakPointHandler;
import gov.nasa.jpf.inspector.server.breakpoints.CommandsManager;
import gov.nasa.jpf.inspector.server.breakpoints.DefaultForwardTraceManager;
import gov.nasa.jpf.inspector.server.choicegenerators.ChoiceGeneratorNotifications;
import gov.nasa.jpf.inspector.server.expression.InspectorState.ListenerMethod;
import gov.nasa.jpf.inspector.server.expression.InspectorStateImpl;
import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.search.Search;

/**
 * Standard listener used in JPF-Inspector when all parts of inspector are notified about incoming events.
 * 
 * @author Alf
 * 
 */
public class InspectorListenerModeNotifications extends ListenerAdapter {
  private static final boolean DEBUG = false;

  private final JPFInspector inspector;
  private final CommandsManager cmdMgr;
  private final BreakPointHandler bpMgr;
  private final ChoiceGeneratorNotifications cgNotify;
  private final DefaultForwardTraceManager dftMgr;

  private final InspectorStateImpl inspState = new InspectorStateImpl();

  private final boolean searchMultipleError; // continue or not if property is violated

  /**
   * Inspector we serve for.
   * 
   * @param searchMultipleError Original value of the "search.multiple_error" property in JPF configuration.
   * @param inspector
   * @param dftMgr
   */
  public InspectorListenerModeNotifications (JPFInspector inspector, CommandsManager cmdMgr, BreakPointHandler bpMgr, ChoiceGeneratorNotifications cgNotify,
      DefaultForwardTraceManager dftMgr, boolean searchMultipleError) {
    this.inspector = inspector;
    this.cmdMgr = cmdMgr;
    this.bpMgr = bpMgr;
    this.cgNotify = cgNotify;
    this.dftMgr = dftMgr;
    this.searchMultipleError = searchMultipleError;

  }

  @Override
  public void stateAdvanced (Search search) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".stateAdvanced()");
    }
    inspState.stateChanged(search, ListenerMethod.LM_STATE_ADVANCED);
    bpMgr.forwardJPFStep(inspState);
    bpMgr.checkBreakpoints(inspState);
    dftMgr.forwardStep(search);
    cmdMgr.tryStop(inspState);
  }

  @Override
  public void stateProcessed (Search search) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".stateProcessed()");
    }
    inspState.stateChanged(search, ListenerMethod.LM_NOT_IN_LIST);
    cmdMgr.tryStop(inspState);
  }

  @Override
  public void stateBacktracked (Search search) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".stateBacktracked()");
    }
    inspState.stateChanged(search, ListenerMethod.LM_STATE_BACKTRACKED);
    bpMgr.backwardJPFStep(inspState);
    // dftMgr.extendTrace(search.getTransition());
    cmdMgr.tryStop(inspState);
  }

  @Override
  public void searchStarted (Search search) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".searchStarted()");
    }
    inspState.stateChanged(search, ListenerMethod.LM_NOT_IN_LIST);
    cmdMgr.tryStop(inspState);
  }

  @Override
  public void searchConstraintHit (Search search) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".searchConstraintHit()");
    }
    inspState.stateChanged(search, ListenerMethod.LM_SEARCH_CONSTRAINT_HIT);
    cmdMgr.tryStop(inspState);
  }

  @Override
  public void searchFinished (Search search) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".searchFinished()");
    }
  }

  @Override
  public void instructionExecuted (JVM vm) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(
          this.getClass().getSimpleName() + ".instructionExecuted(lastInstr=" + vm.getLastInstruction() + ", loc=" + vm.getLastInstruction().getFileLocation()
              + ")");
    }
    inspState.instructionExecuted(vm);
    bpMgr.checkBreakpoints(inspState);
  }

  @Override
  public void classLoaded (JVM vm) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".classLoaded()");
    }
  }

  @Override
  public void gcBegin (JVM vm) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".gcBegin()");
    }
    inspState.notifyListenerMethodCall(ListenerMethod.LM_GC_BEGIN, vm);
    bpMgr.checkBreakpoints(inspState);
  }

  @Override
  public void gcEnd (JVM vm) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".gcEnd()");
    }
    inspState.notifyListenerMethodCall(ListenerMethod.LM_GC_END, vm);
    bpMgr.checkBreakpoints(inspState);
  }

  @Override
  public void objectCreated (JVM vm) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".objectCreated()");
    }
    inspState.notifyListenerMethodCall(ListenerMethod.LM_OBJECT_CREATED, vm);
    bpMgr.checkBreakpoints(inspState);
  }

  @Override
  public void objectReleased (JVM vm) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".objectCreated()");
    }
    inspState.notifyListenerMethodCall(ListenerMethod.LM_OBJECT_RELEASED, vm);
    bpMgr.checkBreakpoints(inspState);
  }

  @Override
  public void exceptionThrown (JVM vm) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".objectCreated()");
    }
    inspState.notifyListenerMethodCall(ListenerMethod.LM_EXCEPTION_THROWN, vm);
    bpMgr.checkBreakpoints(inspState);
  }

  @Override
  public void choiceGeneratorSet (JVM vm) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(
          this.getClass().getSimpleName() + ".choiceGeneratorSet() - cg=" + vm.getLastChoiceGenerator() + " processedChoices="
              + vm.getLastChoiceGenerator().getProcessedNumberOfChoices());
    }
  }

  @Override
  public void choiceGeneratorAdvanced (JVM vm) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(
          this.getClass().getSimpleName() + ".choiceGeneratorAdvanced() - cg=" + vm.getLastChoiceGenerator() + " processedChoices="
              + vm.getLastChoiceGenerator().getProcessedNumberOfChoices());
    }
    inspState.notifyListenerMethodCall(ListenerMethod.LM_CHOICE_GENERATOR_ADVANCED, vm);
    cgNotify.notifyChoiceGeneratorAdvance(vm.getLastChoiceGenerator(), inspState);
    bpMgr.checkBreakpoints(inspState);

  }

  @Override
  public void choiceGeneratorRegistered (JVM vm) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(
          this.getClass().getSimpleName() + ".choiceGeneratorRegistered() - cg=" + vm.getLastChoiceGenerator() + " processedChoices="
              + vm.getLastChoiceGenerator().getProcessedNumberOfChoices());
    }
  }

  @Override
  public void choiceGeneratorProcessed (JVM vm) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(
          this.getClass().getSimpleName() + ".choiceGeneratorProcessed() - cg=" + vm.getLastChoiceGenerator() + " processedChoices="
              + vm.getLastChoiceGenerator().getProcessedNumberOfChoices());
    }
  }

  @Override
  public void threadScheduled (JVM vm) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".threadScheduled()");
    }
    inspState.notifyListenerMethodCall(ListenerMethod.LM_THREAD_SCHEDULED, vm);
    bpMgr.checkBreakpoints(inspState);
  }

  @Override
  public void propertyViolated (Search search) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".propertyViolated()");
    }
    inspState.stateChanged(search, ListenerMethod.LM_PROPERTY_VIOLATED);

    // Simulate behavior of the JPF with original settings
    if (!searchMultipleError) {
      inspector.getStopHolder().terminateAfterResume();
    }

    bpMgr.checkBreakpoints(inspState);
    cmdMgr.tryStop(inspState);

    super.propertyViolated(search);
  }

}
