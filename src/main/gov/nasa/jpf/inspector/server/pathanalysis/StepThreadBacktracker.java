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

import gov.nasa.jpf.inspector.utils.Debugging;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.Step;
import gov.nasa.jpf.vm.Transition;

/**
 * Backward step iterator for a given thread using the given {@link TransitionThreadBacktracker}.
 * Seamlessly hides transition boundaries and repetition of instructions at transition boundaries (top/bottom halves).
 * 
 * @see <a href="http://babelfish.arc.nasa.gov/trac/jpf/wiki/devel/choicegenerator">JPF Devel/ChoiceGenerators</a>
 */
class StepThreadBacktracker {
  /**
   * Indicates that we have not yet called the {@link #backstep()} method. This constant must be less than zero.
   */
  private static final int BACKSTEP_NOT_YET_CALLED = -1;
  /**
   * We use this {@link TransitionThreadBacktracker} to get previous transitions. We are responsible for initializing it.
   */
  private final TransitionThreadBacktracker ttb;

  /**
   * Steps of the currently processed transition
   */
  private Step[] currentTransitionSteps = null;
  /**
   * Step to process. Steps after this index are processed, steps before wait for processing.
   * It is the step that was last returned from the {@link #backstep()} method.
   */
  private int currentTransitionStepIndex = BACKSTEP_NOT_YET_CALLED;

  /**
   * Initializes a new {@link StepThreadBacktracker} that only uses transitions given by the specified {@link TransitionThreadBacktracker}.
   *
   * @param ttb The {@link TransitionThreadBacktracker} that supplies transitions. The transition backtracker must be
   *            uninitialized, i.e. still before its first call.
   */
  public StepThreadBacktracker (TransitionThreadBacktracker ttb) {
    this.ttb = ttb;
    assert this.ttb.getBacksteppedTransitions() == 0 : "This transition thread backtracker was already used.";
  }

  /**
   * Performs a step back in the transition path using its {@link TransitionThreadBacktracker}, then returns the step
   * that it back-stepped to. The {@link TransitionThreadBacktracker} only considers transitions of the specified thread.
   *
   * If the backstep fails (because there is no step we can backstep to), null is returned. This happens if our current
   * step is the first step executed by the specified thread.
   *
   * @return The step we backstepped to, or null if the backstep failed.
   */
  public Step backstep() {
    Debugging.getLogger().info("Determining target instruction: Backstep: Commencing.");
    // If we are the start of a transition, and before we begin, we must request a new transition.
    if (currentTransitionStepIndex <= 0) {
      if (!requestPreviousTransition()) {
        return null;
      }
      Debugging.getLogger().info("Determining target instruction: Backstep: Transition successfully requested. It has " + currentTransitionSteps.length + " steps.");
    }
    assert (currentTransitionStepIndex > 0); // Single instruction transition with bottom half of executed instruction??

    // Perform the backstep now.
    currentTransitionStepIndex--;
    Debugging.getLogger().info("Determining target instruction: Backstepped into " + currentTransitionSteps[currentTransitionStepIndex].getInstruction() +" at line " + currentTransitionSteps[currentTransitionStepIndex].getLineString() + ".");
    return currentTransitionSteps[currentTransitionStepIndex];
  }


  /**
   * Uses the {@link TransitionThreadBacktracker} to backtrack to the previous transition and sets this backtracker's
   * current transition to that transition.
   *
   * After this method terminates, the {@link #currentTransitionStepIndex} will be set to the number of steps of the
   * transition, i.e. a number greater by 1 than the topmost step. It is the responsibility of {@link #backstep()} to
   * decrement it. {@link #backstep()} is the only method to call this one.
   *
   * @return True if the previous transition was loaded; false if there is no previous transition.
   */
  private boolean requestPreviousTransition() {
    assert (currentTransitionStepIndex == 0 ||
            currentTransitionStepIndex == BACKSTEP_NOT_YET_CALLED); // Otherwise no reason to call this method

    Step previousStep = null;
    Transition previousTransition = ttb.getCurrentTransition(); // will be previousTransition
    if (previousTransition != null) {
      previousStep = previousTransition.getStep(0);
    }

    // Move to older transition
    Transition currentTransition = ttb.backtrackToPreviousTransition();
    while (currentTransition != null && currentTransition.getStepCount() == 0) {
      currentTransition = ttb.backtrackToPreviousTransition();
    }
    if (currentTransition == null) {
      // We have processed whole trace for given thread
      currentTransitionSteps = null;
      currentTransitionStepIndex = 0;
      return false;
    }




    currentTransitionSteps = transition2StepArray(currentTransition);
    currentTransitionStepIndex = currentTransitionSteps.length;
    Debugging.getLogger().warning("Transition: " + currentTransition);
    assert currentTransitionStepIndex > 0 : "The transition has no steps.";
    if (currentTransitionStepIndex > 0) {
      // Check if the last step in current transition is not the bottom half
      // of the first step in previous (consequent) transition
      if (previousStep != null) {
        Instruction instCT = currentTransitionSteps[currentTransitionStepIndex - 1].getInstruction();
        assert (instCT != null);

        if (instCT.equals(previousStep.getInstruction())) {
          currentTransitionStepIndex--; // Bottom half of the instruction should be ignored
        }
      }
    }
    return true;
  }

  /**
   * Converts a transition into an array of steps (in order to have fast random access to transition Steps).
   * 
   * @param transition Transition to process
   * @return Array with steps which occur in the transition.
   */
  private static Step[] transition2StepArray(Transition transition) {
    assert (transition != null);
    int stepsCnt = transition.getStepCount();
    Step[] steps = new Step[stepsCnt];

    Step step = transition.getStep(0);
    for (int i = 0; i < stepsCnt; i++) {
      steps[i] = step;
      step = step.getNext();
    }
    return steps;
  }

  /**
   * Gets the transition where the currently processed step is contained.
   */
  public Transition getCurrentTransition () {
    return ttb.getCurrentTransition();
  }

  /**
   * Returns the number of transitions we backtracked through.
   * "0" means we haven't begun yet.
   * "1" means we backtracked through the topmost transition only.
   *
   * This includes transitions executed by threads other than the one we are interested in.
   */
  public int getBacktrackedTransitionCount() {
    return ttb.getBacksteppedTransitions();
  }
}