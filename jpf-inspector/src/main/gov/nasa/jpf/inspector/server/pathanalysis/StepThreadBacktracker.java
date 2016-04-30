package gov.nasa.jpf.inspector.server.pathanalysis;

import gov.nasa.jpf.jvm.Path;
import gov.nasa.jpf.jvm.Step;
import gov.nasa.jpf.jvm.Transition;
import gov.nasa.jpf.jvm.bytecode.Instruction;

/**
 * Backward step iterator for given thread. Seamlessly hides transition boundaries and repetition of instruction at transition boundaries. (Top/Bottom halves)
 * 
 * @see <a href="http://babelfish.arc.nasa.gov/trac/jpf/wiki/devel/choicegenerator">JPF Devel/ChoiceGenerators</a>
 * @author Alf
 * 
 */
class StepThreadBacktracker {
  private static final int GET_PREVIOUS_STEP_NOT_CALLED = -1;
  private final TransitionThreadBacktracker ttb;

  private Step[] currentTransitionSteps = null; // Steps of the currently processed transition
  private int currentTransitionStepIndex = -1; // Step to processed. Steps after this index are processed, steps before waits for processing.

  private int prevReturnedStepIndex = GET_PREVIOUS_STEP_NOT_CALLED; // Index of the step (in PrevTransition) returned by previous (not last but one before the
                                                                    // last) {@link
  // #getPreviousStep()}
  private Transition prevReturnedTransition = null; // Transition used to obtain previous (not last but one before the last) step returned by {@link
                                                    // #getPreviousStep()}
  private int prevReturnedTransition2Backrack = 0; // Number of transition to backtrack to obtain {@link #prevReturnedTransition}.

  public StepThreadBacktracker (Path path, int threadId) {
    super();
    this.ttb = new TransitionThreadBacktracker(path, threadId);
  }

  public StepThreadBacktracker (TransitionThreadBacktracker ttb) {
    super();
    this.ttb = ttb;
  }

  /**
   * @return Gets step with previous instruction/step executed in given thread or null if no such exists (previous call gets the first instruction/step executed
   * by given thread)
   */
  public Step getPreviousStep () {
    prevReturnedStepIndex = currentTransitionStepIndex;
    prevReturnedTransition = ttb.getCurrentTransition();
    ;
    prevReturnedTransition2Backrack = ttb.getBacksteppedTransitions();

    if (currentTransitionStepIndex <= 0) {
      getNewTransition();
      if (currentTransitionSteps == null) {
        return null;
      }
    }
    assert (currentTransitionStepIndex > 0); // Single instruction transition with bottom half of executed instruction??
    currentTransitionStepIndex--;
    return currentTransitionSteps[currentTransitionStepIndex];
  }

  private void getNewTransition () {
    assert (currentTransitionStepIndex == 0 || currentTransitionStepIndex == GET_PREVIOUS_STEP_NOT_CALLED); // Otherwise no reason to call this method
    Step prevStep = null;
    Transition prevTransition = ttb.getCurrentTransition(); // will be prevTransition
    if (prevTransition != null) {
      prevStep = prevTransition.getStep(0);
    }

    // Move to older transition
    Transition currentTransition = ttb.getPreviousTransition();
    if (currentTransition == null) {
      // We have processed whole trace for given thread
      currentTransitionSteps = null;
      currentTransitionStepIndex = 0;
      return;
    }

    currentTransitionSteps = transition2StepArray(currentTransition);
    currentTransitionStepIndex = currentTransitionSteps.length;
    if (currentTransitionStepIndex > 0) {
      // Check if the last step in current transition is not the bottom half of the first step in previous (consequent) transition
      if (prevStep != null) {
        Instruction instCT = currentTransitionSteps[currentTransitionStepIndex - 1].getInstruction();
        assert (instCT != null);

        if (instCT.equals(prevStep.getInstruction())) {
          currentTransitionStepIndex--; // Bottom half of the instruction should be ignored
        }
      }
    }
  }

  /**
   * Convert transition into array of steps. (To have fast random access to transition Steps)
   * 
   * @param tr
   * Transition to process
   * @return Array with steps which occur in transition
   */
  static public Step[] transition2StepArray (Transition tr) {
    assert (tr != null);
    int stepsCnt = tr.getStepCount();
    Step[] steps = new Step[stepsCnt];

    Step step = tr.getStep(0);
    for (int i = 0; i < stepsCnt; i++) {
      steps[i] = step;
      step = step.getNext();
    }
    return steps;
  }

  /**
   * @return Gets result of the last {@link #getPreviousStep()} method call.
   */
  public Step getCurrentStep () {
    if (currentTransitionStepIndex == GET_PREVIOUS_STEP_NOT_CALLED) {
      return null;
    }
    return currentTransitionSteps[currentTransitionStepIndex];
  }

  /**
   * @return Gets transition where {@link #getCurrentStep()} step is stored.
   */
  public Transition getCurrentTransition () {
    return ttb.getCurrentTransition();
  }

  /**
   * @return Gets number of transitions (including the skipped transition in different threads) which were "backstepped" to obtain
   * {@link #getCurrentTransition()} transition
   */
  public int getCurrentBackSteppedTransitions () {
    return ttb.getBacksteppedTransitions();
  }

  /**
   * @return Gets result of previous (not last, but one before) {@link #getPreviousStep()} call.
   */
  public Step getReturnedPrevStep () {
    if (prevReturnedTransition == null) {
      return null;
    }
    return prevReturnedTransition.getStep(prevReturnedStepIndex);
  }

  /**
   * Gets transition of previous (not last, but one before) step returned by {@link #getPreviousStep()} call.
   * 
   * @return Gets transition of previous step.
   */
  public Transition getPrevReturnedTransition () {
    return prevReturnedTransition;
  }

  /**
   * @return Gets number of transitions (including the skipped transition in different threads) which were "backstepped" to obtain
   * {@link #getPrevReturnedTransition()}
   */
  public int getPrevReturnedBackSteppedTransitions () {
    return prevReturnedTransition2Backrack;
  }

  /**
   * @return Creates Backward breakpoint which represents {@link StepThreadBacktracker#getReturnedPrevStep()}.
   */
  public BackwardBreakpointCreator createBackwardBreakpointFromPreviousReturnedStep () {
    Step prevStep = getPreviousStep();
    if (prevStep == null || prevReturnedTransition == null) {
      return null;
    }

    return new BackwardBreakpointCreator(prevReturnedTransition, prevStep, prevReturnedTransition2Backrack);
  }

}
