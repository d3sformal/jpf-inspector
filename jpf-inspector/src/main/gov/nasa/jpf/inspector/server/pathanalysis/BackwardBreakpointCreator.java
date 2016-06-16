package gov.nasa.jpf.inspector.server.pathanalysis;

import gov.nasa.jpf.inspector.common.BreakpointCreationExpression;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.interfaces.BreakpointState;
import gov.nasa.jpf.inspector.interfaces.BreakpointStatus;
import gov.nasa.jpf.inspector.interfaces.CommandsInterface;
import gov.nasa.jpf.inspector.interfaces.InstructionPosition;
import gov.nasa.jpf.inspector.server.breakpoints.BreakpointHandler;
import gov.nasa.jpf.inspector.server.breakpoints.CommandsManager;
import gov.nasa.jpf.inspector.server.breakpoints.InstructionPositionImpl;
import gov.nasa.jpf.inspector.server.expression.ExpressionBoolean;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionBreakpointInstruction;
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.Path;
import gov.nasa.jpf.vm.Step;
import gov.nasa.jpf.vm.Transition;

/**
 * An instance of this class is created when a backwards-stepping command is executed. It creates a single breakpoint,
 * determines the number of transitions that must be backtracked, passes the number to the silent InspectorListener
 * and then leaves scope to be garbaged collected.
 *
 * All of this happens inside the {@link CommandsManager#backwardStep(CommandsInterface.StepType)} method.
 */
public class BackwardBreakpointCreator {

  private final ExpressionBoolean breakpointHitCondition;
  private final int numberOfTransitionsToBacktrack;

  /**
   * Initializes a new instance of the {@link BackwardBreakpointCreator}.
   * This constructor is protected and not private because it's called from the Backtracker classes as well.
   *
   * @param transition The current transition. This is only used to get the current thread index. Maybe should be refactored.
   * @param step Step that we should backtrack to. This step contains the instruction that we want to put a breakpoint on.
   * @param numberOfTransitionsToBacktrack Number of transitions to backtrack.
   */
  protected BackwardBreakpointCreator(Transition transition, Step step, int numberOfTransitionsToBacktrack) {
    int backwardBreakpointBound = MethodInstructionBacktracker.getInstrCountInTransition(transition, step);

    this.breakpointHitCondition = new ExpressionBreakpointInstruction(transition.getThreadIndex(),
                                                                      step.getInstruction(),
                                                                      backwardBreakpointBound);
    this.numberOfTransitionsToBacktrack = numberOfTransitionsToBacktrack;

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
    newBP.setState(BreakpointState.BP_STATE_ENABLED);

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

  public static BackwardBreakpointCreator getBackwardStepInstruction (InspectorState insp) {
    Path path = getPath(insp);
    Transition currentTransition = path.getLast();
    if (currentTransition == null) {
      // Called to early
      return null;
    }
    int currentThread = currentTransition.getThreadIndex();

    TransitionThreadBacktracker ttb = new TransitionThreadBacktracker(path, currentThread);
    StepThreadBacktracker stb = new StepThreadBacktracker(ttb);

    stb.getPreviousStep(); // Last instruction;
    Step targetStep = stb.getPreviousStep();

    if (targetStep == null) {
      return null; // No previous instruction
    }

    return new BackwardBreakpointCreator(ttb.getCurrentTransition(), targetStep, ttb.getBacksteppedTransitions());
  }

  public static BackwardBreakpointCreator getBackwardStepLine (InspectorState insp) {
    Path path = getPath(insp);
    MethodInstructionBacktracker bpa = new MethodInstructionBacktracker(path);

    Step st = bpa.getPreviousStepInCurrentMethod();
    InstructionPosition ip = InstructionPositionImpl.getInstructionPosition(st.getInstruction());

    // Undo instructions on the current line
    while ((st != null) && ip.hitPosition(st.getInstruction())) {
      st = bpa.getPreviousStepInCurrentMethod();
    }

    if (st == null) {
      return null; // No such place exists
    }

    // We are on the previous line - stop after the first instruction on given line
    // Be careful - we need to remember previous step and transition2backreack
    Step prevStep = null;
    Transition prevTransition = null;
    int prevTr2Backrack = 0;
    ip = InstructionPositionImpl.getInstructionPosition(st.getInstruction());
    while (st != null && ip.hitPosition(st.getInstruction())) {
      prevStep = st;
      prevTransition = bpa.getCurrentTransition();
      prevTr2Backrack = bpa.getBacktrackedTransitionCount();

      st = bpa.getPreviousStepInCurrentMethod();
    }

    // in the prev* variables we hold the first instruction on given line
    return new BackwardBreakpointCreator(prevTransition, prevStep, prevTr2Backrack);
  }

  public static BackwardBreakpointCreator getBackwardStepIn (InspectorState insp) {
    CheckCallInstruction checkCall = new CheckCallInstruction();

    Path path = getPath(insp);

    Transition lastTr = path.getLast();
    if (lastTr == null) {
      return null;
    }
    int currentThread = lastTr.getThreadIndex();
    TransitionThreadBacktracker trBacktracker = new TransitionThreadBacktracker(path, currentThread);
    StepThreadBacktracker stepBacktracker = new StepThreadBacktracker(trBacktracker);

    int lineChangeCnt = 0; // How many different lines we meet
    int callInstructionCnt = 0; // How many call instructions we reached
    InstructionPosition ip = null;

    do {
      Step st = stepBacktracker.getPreviousStep();
      if (st == null) {
        return null; // No such place exists
      }

      if (ip == null) {
        // Initial cycle run
        ip = InstructionPositionImpl.getInstructionPosition(st.getInstruction());
      }

      if (checkCall.isCallInstruction(st.getInstruction())) {
        callInstructionCnt++;
      }
      if (!ip.hitPosition(st.getInstruction())) {
        lineChangeCnt++;
        ip = InstructionPositionImpl.getInstructionPosition(st.getInstruction());
      }

    } while (lineChangeCnt < 2 && callInstructionCnt < 2);

    return stepBacktracker.createBackwardBreakpointFromPreviousReturnedStep();
  }

  public static BackwardBreakpointCreator getBackwardStepOut (InspectorState insp) {
    Path path = getPath(insp);
    MethodInstructionBacktracker mib = new MethodInstructionBacktracker(path);
    Step st = mib.getCallerOfCurrentMethod();
    if (st == null) {
      return null; // No such place exists
    }
    return mib.createBackwardBreakpointFromCurrentStep();
  }

  /**
   * Gets the current JPF transition path <b>including</b> the current transition.
   */
  static private Path getPath (InspectorState insp) {
    VM vm = insp.getVM();
    vm.updatePath();
    return vm.getPath();
  }

}
