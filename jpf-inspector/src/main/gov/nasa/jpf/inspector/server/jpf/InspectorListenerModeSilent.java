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
import gov.nasa.jpf.inspector.interfaces.BreakpointCreationInformation;
import gov.nasa.jpf.inspector.server.breakpoints.BreakpointHandler;
import gov.nasa.jpf.inspector.server.breakpoints.CommandsManager;
import gov.nasa.jpf.inspector.server.breakpoints.DefaultForwardTraceManager;
import gov.nasa.jpf.inspector.server.expression.InspectorState.ListenerMethod;
import gov.nasa.jpf.inspector.server.expression.InspectorStateImpl;
import gov.nasa.jpf.inspector.utils.Debugging;
import gov.nasa.jpf.vm.*;
import gov.nasa.jpf.search.Search;

/**
 * InspectorListener mode which is enabled during backward steps processing.
 * Only select notifications are sent.
 *
 * This listener starts being active at the point a backwards step is initiated and stops being active at the point
 * when the breakpoint created by the backwards step is hit.
 */
public class InspectorListenerModeSilent extends ListenerAdapter {
  private static final boolean DEBUG = true;

  @SuppressWarnings("FieldCanBeLocal") // IDEA bug
  private final JPFInspector inspector;
  private final CommandsManager commandsManager;
  private final BreakpointHandler breakpointHandler;
  private final DefaultForwardTraceManager defaultForwardTraceManager;
  private final StopHolder stopHolder;
  private final int targetBreakpointId;
  private int remainingTransitionsToBacktrack;

  private final InspectorStateImpl inspectorState = new InspectorStateImpl();

  /**
   * Represents the state of this listener.
   *
   * The state diagram looks like this:
   *
   * ```
   * Start: WAIT_FOR_FIRST_INSTRUCTION_EXECUTED
   * WAIT_FOR_FIRST_INSTRUCTION_EXECUTED --> TRANSITION_END
   * TRANSITION_END                      --> BACKTRACKING
   * STATE_PROCESSED || TRANSITION_END   --> BACKTRACKING
   * BACKTRACKING    || TRANSITION_END   --> STATE_PROCESSED
   * BACKTRACKING    || TRANSITION_END   --> FORWARD_STEPS
   * ```
   */
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
    /**
     * We are now moving forward in the final phase and will only stop when we reach the target breakpoint.
     */
    FORWARD_STEPS
  }


  /**
   * Do NOT set this directly. Use {@link #setState(InternalState)} instead.
   */
  private InternalState state = InternalState.WAIT_FOR_FIRST_INSTRUCTION_EXECUTED;

  /**
   * Sets the listener's internal state.
   *
   * See {{@link InternalState}} for the state diagram.
   *
   * @param newState New state of the listener.
   */
  private void setState(InternalState newState) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println("Backtracking: " + state + " --> " + newState);
    }
    state = newState;
  }

  /**
   * Initializes the Inspector listener's silent mode.
   * @param inspector The Inspector server.
   * @param commandsManager The commands manager.
   * @param breakpointHandler The breakpoint handler.
   * @param transitionsToBacktrack Number of transitions to backtrack.
   * @param targetBreakpointId ID of the breakpoint created by the backwards-step-creator.
   * @param defaultForwardTraceManager The default forward trace manager.
   * @param stopHolder The stop holder.
   */
  public InspectorListenerModeSilent (JPFInspector inspector, CommandsManager commandsManager,
                                      BreakpointHandler breakpointHandler, int transitionsToBacktrack,
                                      int targetBreakpointId,
                                      DefaultForwardTraceManager defaultForwardTraceManager, StopHolder stopHolder) {
    assert transitionsToBacktrack > 0 : "No transitions are to be backtracked.";
    assert targetBreakpointId != BreakpointCreationInformation.BP_ID_NOT_DEFINED : "No breakpoint ID is set.";

    this.commandsManager = commandsManager;
    this.breakpointHandler = breakpointHandler;
    this.inspector = inspector;
    this.defaultForwardTraceManager = defaultForwardTraceManager;
    this.stopHolder = stopHolder;

    this.targetBreakpointId = targetBreakpointId;
    this.remainingTransitionsToBacktrack = transitionsToBacktrack;
  }

  @Override
  public void stateAdvanced (Search search) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println("Backtracking: State advanced. Extending forward trace.");
    }
    if (state == InternalState.FORWARD_STEPS) {
      reportError("The target instruction was not found in the target transition. Perhaps the backtracker did not make the right choice at the choice generator or something not part of a state changed the course of the transition. THE SYSTEM IS NOW IN AN INCONSISTENT STATE. Please execute 'terminate' or restart the Inspector.");
      return;
    }
    if (state != InternalState.TRANSITION_END) {
      reportError("State can only be advanced in the first phase of the backstepping.");
      return;
    }

    setState(InternalState.BACKTRACKING);
    inspectorState.stateChanged(search, ListenerMethod.LM_STATE_ADVANCED);
    defaultForwardTraceManager.extendTrace(search.getTransition());
    commandsManager.tryStop(inspectorState);
  }

  @Override
  public void stateProcessed (Search search) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println("Backtracking: State processed. Nothing will be done except moving to BACKTRACKING.");
    }
    if (state != InternalState.STATE_PROCESSED && // Multiple transition backtracking
        state != InternalState.TRANSITION_END) { // Initial CB -> state processed
      reportError("State cannot be processed while backtracking or in the final phase.");
      return;
    }
    setState(InternalState.BACKTRACKING);
    // defaultForwardTraceManager.extendTrace(search.getTransition());
  }

  @Override
  public void stateBacktracked (Search search) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println("Backtracking: State backtracked.");
    }
    if (state != InternalState.BACKTRACKING && state != InternalState.TRANSITION_END) {
      // Initial CB is backtracked (advance or processed has been called before in forward stepping)
      reportError("State cannot be backtracked while processing or in the final phase.");
      return;
    }
    inspectorState.stateChanged(search, ListenerMethod.LM_STATE_BACKTRACKED);
    remainingTransitionsToBacktrack--;
    if (remainingTransitionsToBacktrack > 0) {
      defaultForwardTraceManager.extendTrace(search.getTransition());

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
    if (remainingTransitionsToBacktrack == 0) {
      // Backtracking terminated, use the same choices before
      VM vm = search.getVM();
      SystemState ss = vm.getSystemState();
      ChoiceGenerator<?> cg = ss.getChoiceGenerator();
      
      // we must skip choices that have been processed before this call
      // special case: there might be a cascade with multiple CGs at one instruction
      // it is not possible to move back a CG in the cascade if there is one processed choice (the first one)
      // -> we must manually "set all choices as being explored",
      // so that this CG is explored again from the start (for the parent choice) on the way forward
      // this does not apply to the top CG in the cascade (which does not have a cascaded parent)

      while ((cg.getCascadedParent() != null) && (cg.getProcessedNumberOfChoices() == 1)) {
        Debugging.getLogger().info("Moving through a CG with this number of choices: " + cg.getTotalNumberOfChoices());
        // Set this to last value && end delegate to predecessor
        cg.advance(cg.getTotalNumberOfChoices() - cg.getProcessedNumberOfChoices());
        cg = cg.getCascadedParent();
      }
      // CG undo last choice
      int processedChoices = cg.getProcessedNumberOfChoices();
      assert (processedChoices > 0) : "There are no processed choices.";
         // Which choice which used for path we are backtracking??

      cg.reset();
      cg.advance(processedChoices - 1);
      // "Force" forward step
      ss.setForced(true);
      setState(InternalState.FORWARD_STEPS);
      return;
    }

    // Should not be reached
    reportError("This should never be reached: There are less than 0 remaining transitions.");
  }

  @Override
  public void executeInstruction(VM vm, ThreadInfo currentThread, Instruction instructionToExecute) {
    if (DEBUG) {
        inspector.getDebugPrintStream().println(
                this.getClass().getSimpleName() + ".executeInstruction() inst=" + instructionToExecute );
    }
    if (state == InternalState.WAIT_FOR_FIRST_INSTRUCTION_EXECUTED) {
      Debugging.getLogger().info("We were apparently broken somewhere else beside prior to an instruction. Go figure.");
      state = InternalState.TRANSITION_END;
      return;
    }
    if (state != InternalState.FORWARD_STEPS) {
      reportError("Moving forwards can only be done in the last phase of the backstepping.");
      return;
    }
    inspectorState.notifyListenerMethodCall(ListenerMethod.LM_EXECUTE_INSTRUCTION, vm);
    if (breakpointHandler.checkBreakpoint(inspectorState, targetBreakpointId)) {
      // Notify Command manager that backward step is done
      commandsManager.notifyBackwardStepCompleted(true, "Successfully backstepped.");
      stopHolder.stopExecution(inspectorState);
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
      remainingTransitionsToBacktrack++;
      return;
    }

    if (state != InternalState.FORWARD_STEPS) {
      reportError("Moving forwards can only be done in the last phase of the backstepping.");
      return;
    }
    inspectorState.instructionExecuted(currentThread.getId(), executedInstruction, vm);


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
    commandsManager.notifyBackwardStepCompleted(false, reason);
    defaultForwardTraceManager.destroyTrace(true);
  }

}
