package gov.nasa.jpf.inspector.server.pathanalysis;

import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionBreakpointInstruction;
import gov.nasa.jpf.vm.Path;
import gov.nasa.jpf.vm.Step;
import gov.nasa.jpf.vm.Transition;
import gov.nasa.jpf.vm.Instruction;

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
  private final StepThreadBacktracker stepBacktracker;

  private final CheckCallInstruction callChecker = new CheckCallInstruction();
  private final CheckReturnInstruction returnChecker = new CheckReturnInstruction();
  private final CheckThrowInstruction throwChecker = new CheckThrowInstruction();

  private Step prevReturnedStep = null; // previous value returned by {@link #getPreviousStepInCurrentMethod()}
  private Transition prevReturnedStepTransition = null; // transition where prevReturnedStep takes place
  private int prevReturnedTransition2Backrack = 0; // how many transition has to be backtracked to get prevReturnedStepTransition

  /**
   * @param path Path to analyze. Cannot be null.
   */
  public MethodInstructionBacktracker (Path path) {
    if (path == null) {
      throw new IllegalArgumentException("Path cannot be null");
    }

    Transition lastTr = path.getLast();
    if (lastTr != null) {
      int currentThread = lastTr.getThreadIndex();
      stepBacktracker = new StepThreadBacktracker(new TransitionThreadBacktracker(path, currentThread));
    } else {
      stepBacktracker = null;
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
   * 
   * @return Previsou step in method which calls the current method (call instruction for current method)
   */
  public Step getCallerOfCurrentMethod () {
    if (DEBUG) {
      System.out.println(this.getClass().getSimpleName() + ".getCallerOfCurrentMethod()");
    }
    return backtracker(1);
  }

  /**
   * @param method2leave How many method show we leave to return first instruction.
   * @return First step in method which is in specified depth in the call stack
   * 
   *         <br> Note: Use with 0 to take instruction in current method
   *         <br> Note: Use with 1 to leave current method -> find where current method is called
   */
  private Step backtracker (int method2leave) {
    int callStackDepth = method2leave; // Simulation of the depth of the calls stack above (how many methods have been called by) the method in the end of the
                                       // path.

    // Store previous state
    prevReturnedStep = stepBacktracker.getCurrentStep();
    prevReturnedStepTransition = stepBacktracker.getCurrentTransition();
    prevReturnedTransition2Backrack = stepBacktracker.getCurrentBackSteppedTransitions();

    Step step = stepBacktracker.getPreviousStep();

    while (step != null) {
      if (returnChecker.isReturnStep(step)) {
        callStackDepth++;
      }
      if (throwChecker.isThrowStep(step)) {
        // Can decrease depth multiple time (or not at all) -> additional info needed
        // TODO
        throw new UnsupportedOperationException("backward path ananlysis through throw exception is not supported now");
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

        System.out.println(" inst=" + step.getInstruction() + ", location=" + step.getInstruction().getFilePos() + ", source="
            + step.getInstruction().getSourceLine());
      }

      // Filter out all "Step" called by current method
      if (callStackDepth <= 0) {
        return step;
      }
      // Process next step;
      step = stepBacktracker.getPreviousStep();
    }

    // prevStep == null -> we reach start of the thread
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
    return stepBacktracker.getCurrentBackSteppedTransitions();
  }

  public Transition getCurrentTransition () {
    return stepBacktracker.getCurrentTransition();
  }

  /**
   * @return Creates Backward breakpoint which represents position of last Step returned by {@link #getPreviousStepInCurrentMethod()}.
   */
  public BackwardBreakpointCreator createBackwardBreakpointFromCurrentStep () {
    Step currentStep = stepBacktracker.getCurrentStep();
    Transition currentTransition = stepBacktracker.getCurrentTransition();
    int currentTransition2backstep = stepBacktracker.getCurrentBackSteppedTransitions();
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
