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
package gov.nasa.jpf.inspector.server.pathanalysis;

import gov.nasa.jpf.inspector.common.BreakpointCreationExpression;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.interfaces.BreakpointState;
import gov.nasa.jpf.inspector.interfaces.BreakpointStatus;
import gov.nasa.jpf.inspector.interfaces.CommandsInterface;
import gov.nasa.jpf.inspector.interfaces.InstructionPosition;
import gov.nasa.jpf.inspector.server.breakpoints.BreakpointHandler;
import gov.nasa.jpf.inspector.server.breakpoints.BreakpointHitLocation;
import gov.nasa.jpf.inspector.server.breakpoints.CommandsManager;
import gov.nasa.jpf.inspector.server.breakpoints.InstructionPositionImpl;
import gov.nasa.jpf.inspector.server.expression.ExpressionBoolean;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionBreakpointInstruction;
import gov.nasa.jpf.inspector.utils.Debugging;
import gov.nasa.jpf.inspector.utils.expressions.FieldName;
import gov.nasa.jpf.vm.*;
import gov.nasa.jpf.vm.bytecode.FieldInstruction;

/**
 * An instance of this class is created when a backwards-stepping command is executed. It creates a single breakpoint,
 * determines the number of transitions that must be backtracked, passes the number to the silent InspectorListener
 * and then leaves scope to be garbaged collected.
 *
 * All of this happens inside the {@link CommandsManager#backstep(CommandsInterface.StepType)} method.
 */
public final class BackwardBreakpointCreator {

  private final ExpressionBoolean breakpointHitCondition;
  private final int numberOfTransitionsToBacktrack;

  /**
   * Initializes a new instance of the {@link BackwardBreakpointCreator}.
   * This constructor is protected and not private because it's called from the Backtracker classes as well. This class is sealed, there are no inheritors, but we need this to be package-private.
   *
   * @param transition The transition containing the step we should backtrack to.
   * @param step Step that we should backtrack to. This step contains the instruction that we want to put a breakpoint on.
   * @param numberOfTransitionsToBacktrack Number of transitions to backtrack, including the current one.
   */
  protected BackwardBreakpointCreator(Transition transition, Step step, int numberOfTransitionsToBacktrack) {
    assert transition != null;
    assert step != null;
    assert numberOfTransitionsToBacktrack > 0;

    int stepToBreakOn = MethodInstructionBacktracker.howManySameInstructionStepsUpToStep(transition, step);

    this.breakpointHitCondition = new ExpressionBreakpointInstruction(transition.getThreadIndex(),
                                                                      step.getInstruction(),
                                                                      stepToBreakOn);
    this.numberOfTransitionsToBacktrack = numberOfTransitionsToBacktrack;

  }

  /**
   * Initializes a new instance of the {@link BackwardBreakpointCreator}.
   *
   * @param transition The transition containing the step we should backtrack to.
   * @param instruction The instruction that we want to put a breakpoint on.
   * @param numberOfSkippedInstructions The number of steps containing the instruction that we want to put a breakpoint on, that are in the transition prior to the the step we want to break on, excluding the actual step we want to break on.
   * @param descendHowManyTransitions Number of transitions to backtrack, including the final one.
   */
  private BackwardBreakpointCreator(Transition transition, Instruction instruction, int numberOfSkippedInstructions, int descendHowManyTransitions) {
    assert transition != null;
    assert instruction != null;
    assert numberOfSkippedInstructions >= 0;
    assert descendHowManyTransitions >= 1;

    this.numberOfTransitionsToBacktrack = descendHowManyTransitions;
    this.breakpointHitCondition = new ExpressionBreakpointInstruction(transition.getThreadIndex(),
                                                                     instruction,
                                                                     numberOfSkippedInstructions + 1);
  }

  /**
   * Creates a new internal breakpoint for the represented backward step.
   *
   * This is called immediately after the {@link BackwardBreakpointCreator} is initialized from the {@link CommandsManager}.
   * 
   * @param breakpointMgr Holder of breakpoints.
   * @return Breakpoint ID of the created breakpoint.
   */
  public int createBreakpoint (BreakpointHandler breakpointMgr) throws JPFInspectorGenericErrorException {
    BreakpointCreationExpression newBP = new BreakpointCreationExpression();
    newBP.setSingleHitBounds();
    newBP.setState(BreakpointState.ENABLED);

    BreakpointStatus bps = breakpointMgr.createInternalBreakpoint(newBP, breakpointHitCondition);
    if (bps == null) {
      throw new JPFInspectorGenericErrorException("Internal breakpoint could not be created.");
    }
    return bps.getBPID();
  }

  /**
   * Gets the hit condition which breaks where requested backward step should stop.
   */
  @SuppressWarnings("unused") // Used for debugging.
  public ExpressionBoolean getBreakpointHitCondition() {
    return breakpointHitCondition;
  }

  /**
   * Gets how many transitions have to be backtracked to be able to start forward processing and apply the breakpoint.
   */
  public int getTransitionsToBacktrack () {
    return numberOfTransitionsToBacktrack;
  }

  // The following methods create the creator based on the backstep type that was requested.

  /**
   * Returns a creator for back_step_instruction.
   *
   * Specification:
   * Undoes the last instruction. Specifically, backtracks through the JPF transition path and then re-executes transitions until it stops just before the last instruction that was executed.
   *
   * @param inspectorState The current Inspector state used to get the transition path.
   * @return Creator with all target information collected. Returns null on failure.
   */
  public static BackwardBreakpointCreator getBackwardStepInstruction (InspectorState inspectorState) {
    Path path = updateAndGetPath(inspectorState);
    Transition currentTransition = path.getLast();
    if (currentTransition == null) {
      // Called too early,
      assert false; // This should not happen.
      return null;
    }
    int currentThread = currentTransition.getThreadIndex();

    StepThreadBacktracker stb = new StepThreadBacktracker(new TransitionThreadBacktracker(path, currentThread));

    // Get the immediately preceding instruction:
    Step targetStep = stb.backstep();

    if (targetStep == null) {
      return null; // No previous instruction exists, this is the start of the thread.
    }

    return new BackwardBreakpointCreator(stb.getCurrentTransition(), targetStep, stb.getBacktrackedTransitionCount());
  }

  /**
   * Returns a creator for back_step_over.
   *
   * Specification:
   *
   * Undoes instructions on the current and previous source code lines in the current method (all instructions in
   * nested method calls are undone as well). Specifically, backtracks through the JPF transition path and then
   * re-executes transitions until it stops just before the first instruction on the code line just before
   * the current code line.
   *
   * If prior to the back step, execution is stopped at the first code line of a method, then we undo all instructions
   * on the line of the caller as well.
   *
   * @param inspectorState The current Inspector state used to get the transition path.
   * @return Creator with all target information collected.
   */
  public static BackwardBreakpointCreator getBackwardStepLine (InspectorState inspectorState) {
    Path path = updateAndGetPath(inspectorState);
    MethodInstructionBacktracker methodInstructionBacktracker = new MethodInstructionBacktracker(path);

    Instruction currentInstruction = inspectorState.getVM().getInstruction();
    InstructionPosition currentLocation = InstructionPositionImpl.getInstructionPosition(currentInstruction);
    Debugging.getLogger().info("Determining target instruction: back_step_over first step: " + currentInstruction + " at line " + currentLocation.getLineNumber());

    // First step back.
    Step step = methodInstructionBacktracker.backtrackToPreviousStepInMethod();

    // Undo instructions on the current line
    while ((step != null) && currentLocation.hitPosition(step.getInstruction())) {
      step = methodInstructionBacktracker.backtrackToPreviousStepInMethod();
    }

    if (step == null) {
      return null; // We have reached the beginning of the thread or the method.
    }

    // We are now either on the previous line or in the caller.
    if (step.getInstruction().getMethodInfo() != currentInstruction.getMethodInfo()) {
      // We are in the caller - this happened because we were already at the first line of a method.
      // However, that does not matter. For consistency, we will backtrack to the beginning of the line anyway.
    }


    // We are on the previous line - stop after the first instruction on given line
    InstructionPosition previousLinePosition = InstructionPositionImpl.getInstructionPosition(step.getInstruction());

    // Be careful - we need to remember previous step and transition2backreack
    Step previousStep = null;
    Transition previousTransition = null;
    int previousTransitionsToBacktrack = 0;

    while (step != null && previousLinePosition.hitPosition(step.getInstruction())) {
      previousStep = step;
      previousTransition = methodInstructionBacktracker.getCurrentTransition();
      previousTransitionsToBacktrack = methodInstructionBacktracker.getBacktrackedTransitionCount();
      step = methodInstructionBacktracker.backtrackToPreviousStepInMethod();
    }

    // previousStep now contains the first step on the previous code line
    // previousTransition and previousTransitionsToBacktrack hold information relating to previousStep
    // Yes, we could have named it better, as this confuses the meaning of "previous' as in "previous code line"
    //  and "previous" as in "the step we considered before but is actually after"
    return new BackwardBreakpointCreator(previousTransition, previousStep, previousTransitionsToBacktrack);
  }

  /**
   * Returns a creator for back_step_in.
   *
   * Specification:
   *
   * Undoes all instructions on the current line, then undoes instructions on the previous code line in the same method
   * until it backtracks into a method call, in which case it stops just before the called method's return instruction.
   *
   * If no method call occurs on the previous code line, the command behaves as back_step_over.
   *
   * If there is no previous code line (because prior to the back step, execution is stopped at the first code
   * line of a method), we consider the line of the caller method to be the previous line.
   *
   * Implementation-wise, what actually happens is that we backtrack to the beginning of the appropriate transition
   * and then step forward until we reach the return instruction, just like in all the other back-stepping instructions.
   *
   * @param inspectorState The current Inspector state used to get the transition path.
   * @return Creator with all target information collected.
   */
  public static BackwardBreakpointCreator getBackwardStepIn (InspectorState inspectorState) {
    Path path = updateAndGetPath(inspectorState);

    Transition currentTransition = path.getLast();
    if (currentTransition == null) {
      return null;
    }

    Instruction currentInstruction = inspectorState.getVM().getInstruction();
    InstructionPosition currentLocation = InstructionPositionImpl.getInstructionPosition(currentInstruction);

    MethodInstructionBacktracker methodInstructionBacktracker = new MethodInstructionBacktracker(path);

    return methodInstructionBacktracker.backtrackIn(currentLocation);
  }

  /**
   * Returns a creator for back_step_out.
   *
   * Undoes all instructions that have been executed since this method was entered, backsteps out of the method
   * and then undoes instructions in the parent until it stops at the beginning of the source code line of the callsite.
   *
   *
   * @param inspectorState The current Inspector state used to get the transition path.
   * @return Creator with all target information collected.
   */
  public static BackwardBreakpointCreator getBackwardStepOut (InspectorState inspectorState) {
    Path path = updateAndGetPath(inspectorState);

    MethodInstructionBacktracker mib = new MethodInstructionBacktracker(path);
    Step callerStep = mib.getCallerOfCurrentMethod();

    if (callerStep == null) {
      return null; // No callsite could be found, perhaps because we are the root method.
    }

    Transition currentTransition = mib.getCurrentTransition();
    int currentTransition2backstep = mib.getBacktrackedTransitionCount();
    return new BackwardBreakpointCreator(currentTransition, callerStep, currentTransition2backstep);
  }

  /**
   * Gets the current JPF transition path <b>including</b> the current transition.
   */
  private static Path updateAndGetPath(InspectorState insp) {
    VM vm = insp.getVM();
    vm.updatePath();
    return vm.getPath();
  }

  /**
   * Returns a creator for back_breakpoint_hit.
   *
   * Undoes all instructions until it reaches the step, identified by an instruction and a transition,
   * during which the last breakpoint was hit. Execution breaks just before that instruction.
   *
   * It is possible that such a step does not exist. For example, perhaps no breakpoint was yet hit.
   * Or, the last breakpoint was already backtracked through and its step is not in the transition path.
   * In that case, this command fails.
   *
   * @param lastBreakpointHitLocation Location in the transition path of where the last breakpoint hit,
   *                                  or null if no breakpoint hit yet.
   * @param inspState The Inspector state.
   */
  public static BackwardBreakpointCreator getBackBreakpointHit(BreakpointHitLocation lastBreakpointHitLocation,
                                                               InspectorState inspState)
          throws JPFInspectorGenericErrorException {
    if (lastBreakpointHitLocation == null) {
      throw new JPFInspectorGenericErrorException("No breakpoint was yet hit.");
    }
    inspState.getVM().updatePath();
    Path path = inspState.getVM().getPath();
    int targetTransitionIndex = lastBreakpointHitLocation.getIndexOfTransitionInPath();
    if (path.size() <= targetTransitionIndex) {
      throw new JPFInspectorGenericErrorException("You have already backtracked through the transition where the last breakpoint was hit (not enough transitions).");
    }
    Transition targetTransition = path.get(targetTransitionIndex);

    int instructionsToSkip = lastBreakpointHitLocation.getNumberOfSkippedInstructions();
    Step targetStep = null;
    for (Step step : targetTransition) {
      if (step.getInstruction().equals(lastBreakpointHitLocation.getInstruction())) {
        if (instructionsToSkip == 0) {
          targetStep = step;
          break;
        } else {
          instructionsToSkip--;
        }
      }
    }
    if (targetStep == null) {
      throw new JPFInspectorGenericErrorException("You have already backtracked through the instruction where the last breakpoint was hit (target instruction not found).");
    }

    int descendHowManyTransitions = path.size() - targetTransitionIndex;
    assert descendHowManyTransitions > 0;
    BackwardBreakpointCreator backwardBreakpointCreator =
            new BackwardBreakpointCreator(targetTransition, lastBreakpointHitLocation.getInstruction(),
                                          lastBreakpointHitLocation.getNumberOfSkippedInstructions(),
                                          descendHowManyTransitions);
    return  backwardBreakpointCreator;
  }

  public static BackwardBreakpointCreator getBackwardStepTransition(InspectorState inspState,
                                                                    CommandsInterface.StepType type,
                                                                    int descendHowManyTransitions)
          throws JPFInspectorGenericErrorException {
    assert descendHowManyTransitions >= 1;

    Path path = updateAndGetPath(inspState);
    Transition targetTransition = null;
    int remainingTransitionsToSkip = descendHowManyTransitions - 1;
    for (int i = path.size() - 1; i >= 0; i--) {
      Transition possibleTargetTransition = path.get(i);
      boolean correctType;
      switch (type) {
        case ST_TRANSITION_ALL: correctType = true; break;
        case ST_TRANSITION_SCHED:
          correctType = possibleTargetTransition.getChoiceGenerator() instanceof ThreadChoiceGenerator; break;
        case ST_TRANSITION_DATA:
          correctType = !(possibleTargetTransition.getChoiceGenerator() instanceof ThreadChoiceGenerator); break;
        default:
          throw new JPFInspectorGenericErrorException("This step type is not legal at this point.");
      }
      if (correctType) {
        if (remainingTransitionsToSkip == 0) {
          targetTransition = possibleTargetTransition;
          break;
        } else {
          remainingTransitionsToSkip--;
        }
      }
    }
    if (targetTransition == null) {
      throw new JPFInspectorGenericErrorException(
              "There aren't that many transitions beginning with the specified choice generator type in the transition path.");
    }

    if (targetTransition.getStepCount() <= 0) {
      if (descendHowManyTransitions == 1) {
        throw new JPFInspectorGenericErrorException("You are already at the beginning of this transaction. Use 'back_step_transition 2' to backtrack further.");
      }
      throw new JPFInspectorGenericErrorException("The target transition has no steps and cannot be backtracked to.");
    }
    BackwardBreakpointCreator bbc = new BackwardBreakpointCreator(targetTransition,
                                                                  targetTransition.getStep(0),
                                                                  descendHowManyTransitions);
    return bbc;
  }

  /**
   * Creates a creator for the back_field_access backstepping command.
   *
   * Undoes all instructions until it reaches the GETFIELD, PUTFIELD, GETSTATIC or PUTSTATIC instruction inside which a choice generator was triggered. Then execution breaks at the instruction that immediately follows the field instruction, using the same choice as before.
   *
   * @param fieldName Only instructions associated with this field are eligible for the backwards breakpoint.
   * @param inspectorState The current Inspector state.
   */
  public static BackwardBreakpointCreator getBackwardFieldAccess(FieldName fieldName,
                                                                 InspectorState inspectorState)
          throws JPFInspectorGenericErrorException {
    Path path = updateAndGetPath(inspectorState);
    Transition targetTransition = null;
    int descendHowManyTransitions = 0;
    for (int i = path.size() - 1; i >= 0; i--) {
      Transition possibleTargetTransition = path.get(i);

      if (possibleTargetTransition.getStepCount() <= 0) {
        // This usually means that it's the CURRENT transition, i.e. we are just before the bottom half of the instruction
        // that caused this transition. In that case, we backtrack.
        continue;
      }
      Instruction firstInstruction = possibleTargetTransition.getStep(0).getInstruction();
      if (firstInstruction instanceof FieldInstruction) {
        FieldInfo fieldInfo = ((FieldInstruction)firstInstruction).getFieldInfo();
        if (fieldName.isSameField(fieldInfo)) {
          // Bingo!
          targetTransition = possibleTargetTransition;
          descendHowManyTransitions = path.size() - i;
          break;
        } else {
          // It's a different field.
        }
      } else {
        // We are only interested in field instructions.
      }
    }
    if (targetTransition == null) {
      throw new JPFInspectorGenericErrorException(
              "No field matching the argument is accessed in the transition path.");
    }
    assert targetTransition.getStepCount() > 0;
    BackwardBreakpointCreator bbc = new BackwardBreakpointCreator(targetTransition,
                                                                  targetTransition.getStep(0),
                                                                  descendHowManyTransitions);
    return bbc;
  }
}
