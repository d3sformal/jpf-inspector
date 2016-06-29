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
   * @param numberOfTransitionsToBacktrack Number of transitions to backtrack.
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

  public BackwardBreakpointCreator(Transition transition, Instruction instruction, int numberOfSkippedInstructions, int descendHowManyTransitions) {
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
    newBP.setBounds(1, 1);
    newBP.setState(BreakpointState.ENABLED);

    BreakpointStatus bps = breakpointMgr.createInternalBreakpoint(newBP, breakpointHitCondition, true);
    if (bps == null) {
      throw new JPFInspectorGenericErrorException("Internal breakpoint could not be created.");
    }
    return bps.getBPID();
  }

  /**
   * Gets the hit condition which breaks where requested backward step should stop.
   */
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
    Debugging.getLogger().info("Backtracking: back_step_over first step: " + currentInstruction + " at line " + currentLocation.getLineNumber());

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
    ReturnInstructionChecker returnInstructionChecker = new ReturnInstructionChecker();
    Path path = updateAndGetPath(inspectorState);

    Transition currentTransition = path.getLast();
    if (currentTransition == null) {
      return null;
    }
    int currentThread = currentTransition.getThreadIndex();
    StepThreadBacktracker stepBacktracker = new StepThreadBacktracker(new TransitionThreadBacktracker(path, currentThread));

    Instruction currentInstruction = inspectorState.getVM().getInstruction();
    InstructionPosition currentLocation = InstructionPositionImpl.getInstructionPosition(currentInstruction);

    MethodInstructionBacktracker methodInstructionBacktracker = new MethodInstructionBacktracker(path);

    return methodInstructionBacktracker.backtrackIn(currentLocation);
    /*
    // First step back.
    // TODO we should ignore all instructions on the current line.... oh god
    Step step = stepBacktracker.backstep();
    // Undo instructions on the current line
    while ((step != null) && currentLocation.hitPosition(step.getInstruction())) {
      step = stepBacktracker.backstep();
    }

    if (step == null) {
      return null; // We have reached the beginning of the thread.
    }

    // We are now either on the previous line or in the caller or in a callee.
    if (step.getInstruction().getMethodInfo() != currentInstruction.getMethodInfo()) {
      // TODO Check whether we are now in caller: If yes, continue as normal.

      // We are in the caller or calleee - we should stop now.
      return new BackwardBreakpointCreator(stepBacktracker.getCurrentTransition(), step, stepBacktracker.getBacktrackedTransitionCount());
    }

    // We are on the previous line - stop after the first instruction on given line or when method changes
    InstructionPosition previousLinePosition = InstructionPositionImpl.getInstructionPosition(step.getInstruction());
    Step previousStep = null;
    Transition previousTransition = null;
    int previousTransitionsToBacktrack = 0;
    while ((step != null) && previousLinePosition.hitPosition(step.getInstruction())) {
      previousStep = step;
      previousTransition = stepBacktracker.getCurrentTransition();
      previousTransitionsToBacktrack = stepBacktracker.getBacktrackedTransitionCount();
      step = stepBacktracker.backstep();
    }

    if (step == null) {
      return null; // We have reached the beginning of the thread or the method.
    }

    if (step.getInstruction().getMethodInfo() != currentInstruction.getMethodInfo() && returnInstructionChecker.isReturnStep(step) ) {
      return new BackwardBreakpointCreator(stepBacktracker.getCurrentTransition(), step, stepBacktracker.getBacktrackedTransitionCount());
    } else {
      return new BackwardBreakpointCreator(previousTransition, previousStep, previousTransitionsToBacktrack);
    }
    */
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

  public static BackwardBreakpointCreator getBackBreakpointHit(BreakpointHitLocation lastBreakpointHitLocation, InspectorState inspState) throws JPFInspectorGenericErrorException {
    if (lastBreakpointHitLocation == null) {
      throw new JPFInspectorGenericErrorException("No breakpoint was yet hit.");
    }
    inspState.getVM().updatePath();
    Path path = inspState.getVM().getPath();
    int targetIndex = 0;
    for (Transition transition : path) {
      if (transition.equals(lastBreakpointHitLocation.getTransition())) {
        break;
      }
      targetIndex++;
    }
    int descendHowManyTransitions = path.size() - targetIndex;
    if (descendHowManyTransitions <= 0) {
      throw new JPFInspectorGenericErrorException("The last breakpoint was not hit in a transition in the current transition path.");
    }
    return new BackwardBreakpointCreator(lastBreakpointHitLocation.getTransition(), lastBreakpointHitLocation.getInstruction(), lastBreakpointHitLocation.getNumberOfSkippedInstructions(), descendHowManyTransitions);
  }

  public static BackwardBreakpointCreator getBackwardStepTransition(InspectorState inspState, CommandsInterface.StepType type) throws JPFInspectorGenericErrorException {
    // TODO awaiting specification
    throw new JPFInspectorGenericErrorException("Not yet implemented.");
  }

  /**
   * Creates a creator for the back_field_access backstepping command.
   *
   * Undoes all instructions until it reaches the GETFIELD, PUTFIELD, GETSTATIC or PUTSTATIC instruction inside which a choice generator was triggered. Then execution breaks at the instruction that immediately follows the field instruction, using the same choice as before.
   *
   * @param fieldName Only instructions associated with this field are eligible for the backwards breakpoint.
   * @param inspectorState The current Inspector state.
   */
  public static BackwardBreakpointCreator getBackwardFieldAccess(FieldName fieldName, InspectorState inspectorState)
          throws JPFInspectorGenericErrorException {
    // TODO awaiting specification
    throw new JPFInspectorGenericErrorException("Not yet implemented.");
  }
}
