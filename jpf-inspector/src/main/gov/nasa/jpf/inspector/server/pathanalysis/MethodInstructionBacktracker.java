package gov.nasa.jpf.inspector.server.pathanalysis;

import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionBreakpointInstruction;
import gov.nasa.jpf.vm.*;

/**
 * 
 * Filter the path and hides instruction executed in method called from the current one.
 * 
 * {@see #getPreviousStepInCurrentMethod()} can be used to retrive previous instruction/step in current method (skipping instructions in called methods).
 * 
 * @author Alf
 * 
 */
public class MethodInstructionBacktracker {
  private static final boolean DEBUG = false;

  // private final TransitionThreadBackracker transitionBacktracker;
  private final StepThreadBacktracker stepThreadBacktracker;

  private final CallInstructionChecker callChecker = new CallInstructionChecker();
  private final ReturnInstructionChecker returnChecker = new ReturnInstructionChecker();
  private final ThrowInstructionChecker throwChecker = new ThrowInstructionChecker();

  /**
   * Contains the value returned by the last call to {@link #getPreviousStepInCurrentMethod()}. If the method
   * was not yet called, it is null.
   */
  private Step prevReturnedStep = null;
  /**
   * Contains the transition that contains the {@link #prevReturnedStep}.
   */
  private Transition prevReturnedStepTransition = null;
  /**
   * Indicates how many transitions must be backtracked in order to reach the {@link #prevReturnedStepTransition} transition.
   */
  private int prevReturnedTransition2Backrack = 0;

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
   * 
   * @return Previous step in current method/(or in calling method) Null if no previous step exests
   */
  public Step getPreviousStepInCurrentMethod () {
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

    // Store previous state of the backtracker (at first, initialized to the current step).
    prevReturnedStep = stepThreadBacktracker.getCurrentStep();
    prevReturnedStepTransition = stepThreadBacktracker.getCurrentTransition();
    prevReturnedTransition2Backrack = stepThreadBacktracker.getCurrentBackSteppedTransitions();

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
   * 
   * @return Gets number of backtracked transitions
   */
  public int getBacktrackedTransitionCount () {
    return stepThreadBacktracker.getCurrentBackSteppedTransitions();
  }

  public Transition getCurrentTransition () {
    return stepThreadBacktracker.getCurrentTransition();
  }

  /**
   * @return Creates Backward breakpoint which represents position of last Step returned by {@link #getPreviousStepInCurrentMethod()}.
   */
  public BackwardBreakpointCreator createBackwardBreakpointFromCurrentStep () {
    Step currentStep = stepThreadBacktracker.getCurrentStep();
    Transition currentTransition = stepThreadBacktracker.getCurrentTransition();
    int currentTransition2backstep = stepThreadBacktracker.getCurrentBackSteppedTransitions();
    if (currentStep == null || currentTransition == null) {
      return null;
    }

    return new BackwardBreakpointCreator(currentTransition, currentStep, currentTransition2backstep);
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

  /**
   * @return Gets result of previous (not last, but one before) {@link #getPreviousStepInCurrentMethod()} call.
   */
  private Step getPrevReturnedStep() {
    return prevReturnedStep;
  }

  /**
   * @return Gets transition in which {@link #getPrevReturnedStep()} step takes place.
   */
  private Transition getPrevReturnedStepTransition() {
    return prevReturnedStepTransition;
  }

  /**
   * @return Gets number of transitions (including the skipped transition in different threads) which were "backstepped" to obtain
   *         {@link #getPrevReturnedStepTransition()}
   */
  public int getPrevReturnedTransition2Backrack () {
    return prevReturnedTransition2Backrack;
  }

  /**
   * @return Creates Backward breakpoint which represents {@link #getPrevReturnedStep()}.
   */
  public BackwardBreakpointCreator createBackwardBreakpointFromPreviousReturnedStep () {
    if (prevReturnedStep == null || prevReturnedStepTransition == null) {
      return null;
    }

    return new BackwardBreakpointCreator(prevReturnedStepTransition, prevReturnedStep, prevReturnedTransition2Backrack);
  }

}
