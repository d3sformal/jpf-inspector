package gov.nasa.jpf.inspector.server.jpf;

import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.inspector.interfaces.BreakPointCreationInformation;
import gov.nasa.jpf.inspector.interfaces.CommandsInterface;
import gov.nasa.jpf.inspector.server.breakpoints.BreakpointHandler;
import gov.nasa.jpf.inspector.server.breakpoints.CommandsManager;
import gov.nasa.jpf.inspector.server.breakpoints.DefaultForwardTraceManager;
import gov.nasa.jpf.inspector.server.expression.InspectorState.ListenerMethod;
import gov.nasa.jpf.inspector.server.expression.InspectorStateImpl;
import gov.nasa.jpf.vm.*;
import gov.nasa.jpf.search.Search;

/**
 * InspectorListener mode which is enabled during backward steps processing. Notification only to the specified {@link CommandsInterface} are sent
 * 
 * @author Alf
 *
 * Sooth:
 * In this class, vm.getLastInstruction() calls and similar ones were replaced by arguments from the newer methods.
 * 
 */
public class InspectorListenerModeSilent extends ListenerAdapter {
  private static final boolean DEBUG = false;

  private final JPFInspector inspector;
  private final CommandsManager cmdMgr;
  private final BreakpointHandler bpMgr;
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
  public InspectorListenerModeSilent (JPFInspector inspector, CommandsManager cmdMgr, BreakpointHandler bpMgr, int backwardStepsCnt, int bpID,
                                      DefaultForwardTraceManager dftMgf, StopHolder stopHolder) {
    assert backwardStepsCnt > 0;
    assert bpID != BreakPointCreationInformation.BP_ID_NOT_DEFINED;

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

      VM vm = search.getVM();
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
      VM vm = search.getVM();
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
  }

  @Override

  public void instructionExecuted(VM vm, ThreadInfo currentThread, Instruction nextInstruction, Instruction executedInstruction) {
    if (DEBUG) {
      if ((executedInstruction.getMethodInfo().getClassInfo() != null) && (executedInstruction.getMethodInfo().getClassInfo().getSourceFileName() != null))
      {
        inspector.getDebugPrintStream().println(
                this.getClass().getSimpleName() + ".instructionExecuted() inst=" + executedInstruction + ", loc=" + executedInstruction.getFilePos());
      }
    }

    // backward step was issued at a transition boundary
    // ignore the first instruction of a new transition
    // we must backtrack over one additional transition
    if ((state == InternalState.IS_TRANSITION_END) && currentThread.isFirstStepInsn())
    {
      backwardStepsCnt++;
      return;
    }

    if (state != InternalState.IS_FORWARD_STEPS) {
      reportError();
      return;
    }
    inspState.instructionExecuted(currentThread.getId(), executedInstruction, vm);

    if (bpMgr.checkBreakpoint(inspState, bpID)) {
      // Notify Command manager that backward step is done
      cmdMgr.notifyBackwardStepCompleted(true);
      stopHolder.stopExecution(inspState);
    }
  }


  @Override
  public void choiceGeneratorRegistered(VM vm, ChoiceGenerator<?> nextCG, ThreadInfo currentThread, Instruction executedInstruction) {
    if (DEBUG) {
      ChoiceGenerator<?> cg = nextCG;
      inspector.getDebugPrintStream().println(
              this.getClass().getSimpleName() + ".choiceGeneratorRegistered - cg=" + cg + " processedChoices=" + cg.getProcessedNumberOfChoices());
    }
  }


  @Override
  public void choiceGeneratorSet(VM vm, ChoiceGenerator<?> newCG) {
    if (DEBUG) {
      ChoiceGenerator<?> cg = newCG;
      inspector.getDebugPrintStream().println(
              this.getClass().getSimpleName() + ".choiceGeneratorSet - cg=" + cg + " processedChoices=" + cg.getProcessedNumberOfChoices());
    }
  }


  @Override
  public void choiceGeneratorAdvanced(VM vm, ChoiceGenerator<?> currentCG) {
    if (DEBUG) {
      ChoiceGenerator<?> cg = currentCG;
      inspector.getDebugPrintStream().println(
              this.getClass().getSimpleName() + ".choiceGeneratorAdvanced - cg=" + cg + " processedChoices=" + cg.getProcessedNumberOfChoices());
    }
  }


  @Override
  public void choiceGeneratorProcessed(VM vm, ChoiceGenerator<?> processedCG) {
    if (DEBUG) {
      ChoiceGenerator<?> cg = processedCG;
      //ChoiceGenerator<?> cg = MigrationUtilities.getLastChoiceGenerator(vm);
      inspector.getDebugPrintStream().println(
              this.getClass().getSimpleName() + ".choiceGeneratorProcessed - cg=" + cg + " processedChoices=" + cg.getProcessedNumberOfChoices());
    }
  }

  protected void reportError () {
    cmdMgr.notifyBackwardStepCompleted(false);
    dftMgf.destroyTrace(true);
  }

}
