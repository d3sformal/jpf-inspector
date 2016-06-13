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
  private static final boolean DEBUG = true;

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
  // then backtracked and stateProcessed are called in turns and after last backtrack, instruction executed methods are expected.
  private enum InternalState {
    /**
     * During the 2016 update, breakpoints start to hit <b>before</b> an instruction is executed, not after. Thus,
     * there is a +-1 error that we attempt to eliminate via this enum member.
     */
    WAIT_FOR_FIRST_INSTRUCTION_EXECUTED,
    /**
     * We are trying to abort the current transition.
     */
    TRANSITION_END,
    BACKTRACKING,
    STATE_PROCESSED,
    FORWARD_STEPS
  }


  private InternalState state = InternalState.WAIT_FOR_FIRST_INSTRUCTION_EXECUTED;

  /**
   * The state diagram looks like this:
   * <pre>
   * Start: TRANSITION_END
   * TRANSITION_END                       --> BACKTRACKING
     STATE_PROCESSED || TRANSITION_END --> BACKTRACKING
     BACKTRACKING    || TRANSITION_END --> STATE_PROCESSED
     BACKTRACKING    || TRANSITION_END --> FORWARD_STEPS
     </pre>
   * @param newState New state of the listener.
   */
  private void setState(InternalState newState) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(state + " --> " + newState);
    }
    state = newState;
  }

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
    if (state != InternalState.TRANSITION_END) {
      reportError("State can only be advanced in the first phase of the backstepping.");
      return;
    }
    setState(InternalState.BACKTRACKING);
    inspState.stateChanged(search, ListenerMethod.LM_STATE_ADVANCED);
    dftMgf.extendTrace(search.getTransition());
    cmdMgr.tryStop(inspState);
  }

  @Override
  public void stateProcessed (Search search) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".stateProcessed()");
    }
    if (state != InternalState.STATE_PROCESSED && // Multiple transition backtracking
        state != InternalState.TRANSITION_END) { // Initial CB -> state processed
      reportError("State cannot be processed while backtracking or in the final phase.");
      return;
    }
    setState(InternalState.BACKTRACKING);
    // dftMgf.extendTrace(search.getTransition());

  }

  @Override
  public void stateBacktracked (Search search) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".stateBacktracked()");
    }
    if (state != InternalState.BACKTRACKING && state != InternalState.TRANSITION_END) { // Initial CB is backtracked (advance or processed has been called
                                                                                              // before in forward stepping)
      reportError("State cannot be backtracked while processing or in the final phase.");
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
      setState(InternalState.STATE_PROCESSED);
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
      setState(InternalState.FORWARD_STEPS);
      return;
    }

    // Should not be reached
    reportError("This should never be reached.");
  }

  @Override
  public void executeInstruction(VM vm, ThreadInfo currentThread, Instruction instructionToExecute) {
    if (DEBUG) {
        inspector.getDebugPrintStream().println(
                this.getClass().getSimpleName() + ".executeInstruction() inst=" + instructionToExecute );
    }
    if (state != InternalState.FORWARD_STEPS) {
      reportError("Moving forwards can only be done in the last phase of the backstepping.");
      return;
    }
    inspState.notifyListenerMethodCall(ListenerMethod.LM_EXECUTE_INSTRUCTION, vm);
    if (bpMgr.checkBreakpoint(inspState, bpID)) {
      // Notify Command manager that backward step is done
      cmdMgr.notifyBackwardStepCompleted(true, "Successfully backstepped.");
      stopHolder.stopExecution(inspState);
    }
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
    if (state == InternalState.WAIT_FOR_FIRST_INSTRUCTION_EXECUTED) {
      setState(InternalState.TRANSITION_END);
      return;
    }

    // backward step was issued at a transition boundary
    // ignore the first instruction of a new transition
    // we must backtrack over one additional transition
    if ((state == InternalState.TRANSITION_END) && currentThread.isFirstStepInsn())
    {
      backwardStepsCnt++;
      return;
    }

    if (state != InternalState.FORWARD_STEPS) {
      reportError("Moving forwards can only be done in the last phase of the backstepping.");
      return;
    }
    inspState.instructionExecuted(currentThread.getId(), executedInstruction, vm);


  }


  @Override
  public void choiceGeneratorRegistered(VM vm, ChoiceGenerator<?> nextCG, ThreadInfo currentThread, Instruction executedInstruction) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(
              this.getClass().getSimpleName() + ".choiceGeneratorRegistered - cg=" + nextCG + " processedChoices=" + nextCG
                      .getProcessedNumberOfChoices());
    }
  }


  @Override
  public void choiceGeneratorSet(VM vm, ChoiceGenerator<?> newCG) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(
              this.getClass().getSimpleName() + ".choiceGeneratorSet - cg=" + newCG + " processedChoices=" + newCG.getProcessedNumberOfChoices());
    }
  }


  @Override
  public void choiceGeneratorAdvanced(VM vm, ChoiceGenerator<?> currentCG) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(
              this.getClass().getSimpleName() + ".choiceGeneratorAdvanced - cg=" + currentCG + " processedChoices=" + currentCG
                      .getProcessedNumberOfChoices());
    }
  }


  @Override
  public void choiceGeneratorProcessed(VM vm, ChoiceGenerator<?> processedCG) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(
              this.getClass().getSimpleName() + ".choiceGeneratorProcessed - cg=" + processedCG + " processedChoices=" + processedCG
                      .getProcessedNumberOfChoices());
    }
  }

  private void reportError(String reason) {
    cmdMgr.notifyBackwardStepCompleted(false, reason);
    dftMgf.destroyTrace(true);
  }

}
