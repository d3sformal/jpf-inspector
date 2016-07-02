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

import gov.nasa.jpf.inspector.interfaces.InstructionPosition;
import gov.nasa.jpf.inspector.server.breakpoints.InstructionPositionImpl;
import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionBreakpointInstruction;
import gov.nasa.jpf.vm.*;

/**
 * This backtracker has the capacity to keep track of what method and line of code we are in, which is useful
 * for the commands `back_step_over`, `back_step_in` and `back_step_out`.
 */
public class MethodInstructionBacktracker {
  private static final boolean DEBUG = false;

  private final StepThreadBacktracker stepThreadBacktracker;

  private final CallInstructionChecker callChecker = new CallInstructionChecker();
  private final ReturnInstructionChecker returnChecker = new ReturnInstructionChecker();
  private final ThrowInstructionChecker throwChecker = new ThrowInstructionChecker();



  /**
   * Initializes a new instance of {@link MethodInstructionBacktracker}.
   *
   * Internally, this creates a new {@link TransitionThreadBacktracker} using the path and the current thread and
   * a new {@link StepThreadBacktracker} to iterate back through our transition path. This backtracker is initialized
   * to the current step of the current transition.
   *
   * @param path The current transition path, including the current transition (i.e. after a call to {@link VM#updatePath()}).
   */
  public MethodInstructionBacktracker (Path path) {
    if (path == null) {
      throw new IllegalArgumentException("Path cannot be null.");
    }

    Transition currentTransition = path.getLast();
    if (currentTransition != null) {
      int currentThread = currentTransition.getThreadIndex();
      stepThreadBacktracker = new StepThreadBacktracker(new TransitionThreadBacktracker(path, currentThread));
    } else {
      throw new IllegalArgumentException("The path must contain at least one transition.");
    }
  }

  /**
   * Backtracks to the previous step in the current method.
   * Steps in called methods are backtracked through.
   * If the current step is the first step in the current method, we return the caller step that called this method.
   * If the current step is the step where the thread begins, we return null.
   */
  public Step backtrackToPreviousStepInMethod() {
    if (DEBUG) {
      System.out.println(this.getClass().getSimpleName() + ".getPreviousStepInCurrentMethod()");
    }
    return backtracker(0);
  }

  /**
   * Returns the step of the current method's caller that calls this method, i.e. the one containing the call instruction.
   *
   * @return Previous step in method which calls the current method (call instruction for current method), or null if such a step does not exist.
   */
  public Step getCallerOfCurrentMethod () {
    if (DEBUG) {
      System.out.println(this.getClass().getSimpleName() + ".getCallerOfCurrentMethod()");
    }
    return backtracker(1);
  }

  /**
   * Returns the first step we encounter during backtracking, but all steps we encounter before we exit the specified
   * number of methods from the call stack are ignored.
   *
   * Returns null if no such step exists (for example, because we are in the first step of the thread).
   *
   * Note: Use the argument "0" to backtrack to an instruction within the current method (back_step_over).
   *
   * Note: Use the argument "1" to leave the current method and find the instruction in the parent (back_step_out).
   *
   * @param howManyMethodsToLeave How many methods we must exit before we return a step.
   * @return First step we encounter during backtracking that is in the method after we exit the specified number of methods, or null if no such step exists.
   */
  private Step backtracker (int howManyMethodsToLeave) {
    int callStackDepth = howManyMethodsToLeave;
    // Simulation of the depth of the calls stack above (how many methods have been called by)
    // the method in the end of the path.

    // We always want to perform at least one backstep.
    Step step = stepThreadBacktracker.backstep();

    while (step != null) {
      if (returnChecker.isReturnStep(step)) {
        callStackDepth++;
      }
      if (throwChecker.isThrowStep(step)) {
        throw new UnsupportedOperationException(
                "For implementation reasons of the Inspector, it is not possible to backtrack through the throwing of an exception.");
      }
      if (callChecker.isCallStep(step)) {
        callStackDepth--;
      }
      if (DEBUG) {
        System.out.print(" |");
        for (int i = 0; i < callStackDepth; i++) {
          System.out.print(" |");
        }
        if (returnChecker.isReturnStep(step)) {
          System.out.print("-");
        } else if (callChecker.isCallStep(step)) {
          // System.out.print(" ");
        } else {
          System.out.print(" ");
        }

        System.out.println(
                " inst=" + step.getInstruction() + ", location=" + step.getInstruction().getFilePos() + ", source="
                        + step.getInstruction().getSourceLine());
      }

      // Call stack depth indicates how many more methods must we leave before we want to stop.
      // If it's zero, it means that we already want to stop.
      // For now, it's impossible for call stack depth to be less than zero.
      if (callStackDepth <= 0) {
        return step;
      } else {
        step = stepThreadBacktracker.backstep();
      }
    }

    // This means the backstep failed and there is nothing to backtrack to, we are in the first instruction
    // of the thread, we failed.
    return null;
  }

  // ***************************************************************************
  // ** Methods which holds data related to previous result of the getPreviousStepInCurrentMethod() call
  // ***************************************************************************

  /**
   * Gets the number of transitions we backtracked through.
   */
  public int getBacktrackedTransitionCount () {
    return stepThreadBacktracker.getBacktrackedTransitionCount();
  }

  public Transition getCurrentTransition () {
    return stepThreadBacktracker.getCurrentTransition();
  }

  /**
   * Returns the number of steps in this transition, prior to the specified step and including the specified step,
   * are associated with the same instruction as the specified step.
   * The specific instruction only is considered, not all instructions with the same mnemonic.
   * 
   * Useful in {@link BackwardBreakpointCreator} to create an instance of {@link ExpressionBreakpointInstruction}.
   * 
   * @param transition Transition to search through.
   * @param step We are interested in steps strictly before this step.
   * @return The number of times the step's instruction is executed before the step, plus +1 for its execution during
   * the step. Thus, the return value is always 1 or more.
   */
  public static int howManySameInstructionStepsUpToStep(Transition transition, Step step) {
    assert (transition != null);
    assert (step != null);

    final Instruction stInst = step.getInstruction();

    int count = 0;
    Step trStep = transition.getStep(0);
    while (trStep != null) {

      // Same instruction
      if (stInst.equals(trStep.getInstruction())) {
        count++;
      }

      // Are all instruction before processed?
      if (step.equals(trStep)) {
        break;
      }
      trStep = trStep.getNext();
    } // End while

    assert count >= 1;
    return count;
  }

  public BackwardBreakpointCreator backtrackIn(InstructionPosition currentLocation) {
    // We must first leave the current line.
    Step step = backtracker(0); // We must make at least one step.
    while (step != null && currentLocation.hitPosition(step.getInstruction())) {
      step = backtracker(0);
    }
    if (step == null) {
      // Beginning of thread.
      return null;
    }
    // Now we're on the previous line, either in the same method or in the caller.

    InstructionPosition previousLinePosition = InstructionPositionImpl.getInstructionPosition(step.getInstruction());
    // Be careful - we need to remember previous step and transition2backreack
    Step previousStep = null;
    Transition previousTransition = null;
    int previousTransitionsToBacktrack = 0;
    ReturnInstructionChecker returnInstructionChecker = new ReturnInstructionChecker();
    while (step != null && previousLinePosition.hitPosition(step.getInstruction())) {
      previousStep = step;
      previousTransition = getCurrentTransition();
      previousTransitionsToBacktrack = getBacktrackedTransitionCount();
      step = stepThreadBacktracker.backstep();
      if (returnInstructionChecker.isReturnStep(step)) {
        return new BackwardBreakpointCreator(getCurrentTransition(), step, getBacktrackedTransitionCount());
      }
    }
    if (step == null) {
      return null;
    }
    return new BackwardBreakpointCreator(previousTransition, previousStep, previousTransitionsToBacktrack);
  }
}
