package gov.nasa.jpf.inspector.server.jpf;

import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.inspector.interfaces.BreakPointCreate;
import gov.nasa.jpf.inspector.interfaces.CommandsInterface;
import gov.nasa.jpf.inspector.server.breakpoints.BreakPointHandler;
import gov.nasa.jpf.inspector.server.breakpoints.CommandsManager;
import gov.nasa.jpf.inspector.server.breakpoints.DefaultForwardTraceManager;
import gov.nasa.jpf.inspector.server.expression.InspectorState.ListenerMethod;
import gov.nasa.jpf.inspector.server.expression.InspectorStateImpl;
import gov.nasa.jpf.jvm.ChoiceGenerator;
import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.SystemState;
import gov.nasa.jpf.search.Search;

/**
 * InspectorListener mode which is enabled during backward steps processing. Notification only to the specified {@link CommandsInterface} are sent
 * 
 * @author Alf
 * 
 */
public class InspectorListenerModeSilent extends ListenerAdapter {
  private static final boolean DEBUG = false;

  private final JPFInspector inspector;
  private final CommandsManager cmdMgr;
  private final BreakPointHandler bpMgr;
  private final DefaultForwardTraceManager dftMgf;
  private final StopHolder stopHolder;
  private final int bpID;
  private int backwardStepsCnt;

  private final InspectorStateImpl inspState = new InspectorStateImpl();

  // For internals checks
  // expected usage - at first advanced method is called to notify that transition is completed
  // then baktracked and stateProcessed are called in turns and after last backtrack, instruction executed methods are expected.
  private enum InternalState {
    IS_TRANSITION_END,
    IS_BACKTRACKING,
    IS_STATE_PROCESSED,
    IS_FORWARD_STEPS
  };

  private InternalState state = InternalState.IS_TRANSITION_END;

  /**
   * Inspector we serve for.
   * 
   * @param inspector
   */
  public InspectorListenerModeSilent (JPFInspector inspector, CommandsManager cmdMgr, BreakPointHandler bpMgr, int backwardStepsCnt, int bpID,
      DefaultForwardTraceManager dftMgf, StopHolder stopHolder) {
    assert backwardStepsCnt > 0;
    assert bpID != BreakPointCreate.BP_ID_NOT_DEFINED;

    this.cmdMgr = cmdMgr;
    this.bpMgr = bpMgr;
    this.inspector = inspector;
    this.dftMgf = dftMgf;
    this.stopHolder = stopHolder;

    this.bpID = bpID;
    this.backwardStepsCnt = backwardStepsCnt;
  }

  @Override
  public void stateAdvanced (Search search) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".stateAdvanced()");
    }
    if (state != InternalState.IS_TRANSITION_END) {
      reportError();
      return;
    }
    state = InternalState.IS_BACKTRACKING;
    inspState.stateChanged(search, ListenerMethod.LM_STATE_ADVANCED);
    dftMgf.extendTrace(search.getTransition());
    cmdMgr.tryStop(inspState);
  }

  @Override
  public void stateProcessed (Search search) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".stateProcessed()");
    }
    if (state != InternalState.IS_STATE_PROCESSED && // Multiple transition backtracking
        state != InternalState.IS_TRANSITION_END) { // Initial CB -> state processed
      reportError();
      return;
    }
    state = InternalState.IS_BACKTRACKING;
    // dftMgf.extendTrace(search.getTransition());

  }

  @Override
  public void stateBacktracked (Search search) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".stateBacktracked()");
    }
    if (state != InternalState.IS_BACKTRACKING && state != InternalState.IS_TRANSITION_END) { // Initial CB is backtracked (advance or processed has been called
                                                                                              // before in forward stepping)
      reportError();
      return;
    }
    inspState.stateChanged(search, ListenerMethod.LM_STATE_BACKTRACKED);
    backwardStepsCnt--;
    if (backwardStepsCnt > 0) {
      dftMgf.extendTrace(search.getTransition());

      JVM vm = search.getVM();
      // SystemState ss = vm.getSystemState();
      // ss.setBoring(true); // Force backtracking
      // Force backtracking
      search.requestBacktrack();

      // Disable forward steps - mark all choices from CGs as processed
      SystemState ss = vm.getSystemState();
      ChoiceGenerator<?> cg = ss.getChoiceGenerator();
      while (cg != null) {
        int choices = cg.getTotalNumberOfChoices();
        int processedChoices = cg.getProcessedNumberOfChoices();
        int advanceChoice = choices - processedChoices;
        cg.advance(advanceChoice);
        assert (cg.hasMoreChoices() == false);
        // Go to parent
        cg = cg.getCascadedParent();
      }
      state = InternalState.IS_STATE_PROCESSED;
      return;
    }
    if (backwardStepsCnt == 0) {
      // Backtracking terminated, use the same choices before
      JVM vm = search.getVM();
      SystemState ss = vm.getSystemState();
      ChoiceGenerator<?> cg = ss.getChoiceGenerator();
      
      // we must skip choices that have been processed before this call
      // special case: there might be a cascade with multiple CGs at one instruction
      	// it is not possible to move back a CG in the cascade if there is one processed choice (the first one) -> we must manually "set all choices as being explored", so that this CG is explored again from the start (for the parent choice) on the way forward
      // this does not apply to the top CG in the cascade (which does not have a cascaded parent)
      while ((cg.getCascadedParent() != null) && (cg.getProcessedNumberOfChoices() == 1)) {
        // Set this to last value && end delegate to predecessor
        cg.advance(cg.getTotalNumberOfChoices() - cg.getProcessedNumberOfChoices());
        cg = cg.getCascadedParent();
      }
      // CG undo last choice
      int processedChoices = cg.getProcessedNumberOfChoices();
      assert (processedChoices > 0); // Which choice which used for path we are backtracking??
      cg.reset();
      cg.advance(processedChoices - 1);
      // "Force" forward step
      ss.setForced(true);
      state = InternalState.IS_FORWARD_STEPS;
      return;
    }

    // Should not be reached
    reportError();
    return;
  }

  @Override
  public void instructionExecuted (JVM vm) {
    if (DEBUG) {
    	if ((vm.getLastInstruction().getMethodInfo().getClassInfo() != null) && (vm.getLastInstruction().getMethodInfo().getClassInfo().getSourceFileName() != null))
    	{
    		inspector.getDebugPrintStream().println(
    			this.getClass().getSimpleName() + ".instructionExecuted() inst=" + vm.getLastInstruction() + ", loc=" + vm.getLastInstruction().getFilePos());
    	}
    }
    
    // backward step was issued at a transition boundary
    // ignore the first instruction of a new transition
    // we must backtrack over one additional transition
    if ((state == InternalState.IS_TRANSITION_END) && vm.getLastThreadInfo().isFirstStepInsn())
    {
    	backwardStepsCnt++;
    	return;
    }

    if (state != InternalState.IS_FORWARD_STEPS) {
      reportError();
      return;
    }
    inspState.instructionExecuted(vm);

    if (bpMgr.checkBreakpoint(inspState, bpID)) {
      // Notify Command manager that backward step is done
      cmdMgr.notifyBackwardStepCompleted(true);
      stopHolder.stopExecution(inspState);
    }
  }

  @Override
  public void choiceGeneratorRegistered (JVM vm) {
    if (DEBUG) {
      ChoiceGenerator<?> cg = vm.getLastChoiceGenerator();
      inspector.getDebugPrintStream().println(
          this.getClass().getSimpleName() + ".choiceGeneratorRegistered - cg=" + cg + " processedChoices=" + cg.getProcessedNumberOfChoices());
    }
  }

  @Override
  public void choiceGeneratorSet (JVM vm) {
    if (DEBUG) {
      ChoiceGenerator<?> cg = vm.getLastChoiceGenerator();
      inspector.getDebugPrintStream().println(
          this.getClass().getSimpleName() + ".choiceGeneratorSet - cg=" + cg + " processedChoices=" + cg.getProcessedNumberOfChoices());
    }
  }

  @Override
  public void choiceGeneratorAdvanced (JVM vm) {
    if (DEBUG) {
      ChoiceGenerator<?> cg = vm.getLastChoiceGenerator();
      inspector.getDebugPrintStream().println(
          this.getClass().getSimpleName() + ".choiceGeneratorAdvanced - cg=" + cg + " processedChoices=" + cg.getProcessedNumberOfChoices());
    }
  }

  @Override
  public void choiceGeneratorProcessed (JVM vm) {
    if (DEBUG) {
      ChoiceGenerator<?> cg = vm.getLastChoiceGenerator();
      inspector.getDebugPrintStream().println(
          this.getClass().getSimpleName() + ".choiceGeneratorProcessed - cg=" + cg + " processedChoices=" + cg.getProcessedNumberOfChoices());
    }
  }

  protected void reportError () {
    cmdMgr.notifyBackwardStepCompleted(false);
    dftMgf.destroyTrace(true);
  }

}
