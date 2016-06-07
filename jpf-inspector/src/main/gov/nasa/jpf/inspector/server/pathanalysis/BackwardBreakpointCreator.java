package gov.nasa.jpf.inspector.server.pathanalysis;

import gov.nasa.jpf.inspector.client.commands.CmdBreakpointCreate;
import gov.nasa.jpf.inspector.interfaces.BreakpointState;
import gov.nasa.jpf.inspector.interfaces.BreakPointStatus;
import gov.nasa.jpf.inspector.interfaces.InstructionPosition;
import gov.nasa.jpf.inspector.server.breakpoints.BreakpointHandler;
import gov.nasa.jpf.inspector.server.breakpoints.InstructionPositionImpl;
import gov.nasa.jpf.inspector.server.expression.ExpressionBoolean;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionBreakpointInstruction;
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.Path;
import gov.nasa.jpf.vm.Step;
import gov.nasa.jpf.vm.Transition;

public class BackwardBreakpointCreator {

  private final ExpressionBoolean backwardBreakpoint;
  private final int transition2backtrack;

  BackwardBreakpointCreator (Transition transition, Step step, int transition2backtrack) {
    int backwardBreakpointBound = MethodInstructionBacktracker.getInstrCountInTransition(transition, step);

    this.backwardBreakpoint = new ExpressionBreakpointInstruction(transition.getThreadIndex(), step.getInstruction(), backwardBreakpointBound);
    this.transition2backtrack = transition2backtrack;

  }

  /**
   * Create new internal breakpoint for represented backward step.
   * 
   * @param breakpointMgr Manager where to create new Breakpoint
   * @return BreakpointID of created BP
   */
  public int createBreakpoint (BreakpointHandler breakpointMgr) {
    // TODO Create inspector.server class copy ... don't use inspector.client package version !!
    CmdBreakpointCreate.ConsoleBreakpointCreationExpression newBP = new CmdBreakpointCreate.ConsoleBreakpointCreationExpression();
    newBP.setBounds(1, 1);
    newBP.setState(BreakpointState.BP_STATE_ENABLED);

    BreakPointStatus bps = breakpointMgr.createInternalBreakpoint(newBP, backwardBreakpoint, true);
    if (bps == null) {
      return BreakPointStatus.BP_ID_NOT_DEFINED;
    }
    return bps.getBPID();
  }

  /**
   * @return Gets breakpoint which breaks where requested backward step should stop.
   */
  public ExpressionBoolean getBackwardBreakpoint () {
    return backwardBreakpoint;
  }

  /**
   * @return Get how many transitions has to be backtrackted to be able to start forward processing and apply the breakpoint
   */
  public int getTransitionsToBacktrack () {
    return transition2backtrack;
  }

  public static BackwardBreakpointCreator getBackwardStepInstruction (InspectorState insp) {
    Path path = getPath(insp);
    Transition lastTr = path.getLast();
    if (lastTr == null) {
      // Called to early
      return null;
    }
    int currentThread = lastTr.getThreadIndex();

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

  // public static BackwardBreakpointCreator getBackwardStepOut(InspectorState insp) {
  // CheckCallInstruction checkCall = new CheckCallInstruction();
  //
  // Path path = getPath(insp);
  //
  // MethodInstructionBacktracker mib = new MethodInstructionBacktracker(path);
  //
  // int lineChangeCnt = 0; // How many different lines we meet
  //
  // InstructionPosition ip = null;
  //
  // Step st = mib.getPreviousStepInCurrentMethod();
  // if (st == null) {
  // return null; // No such place exists
  // }
  // ip = InstructionPositionImpl.getInstructionPosition(st.getInstruction());
  //
  // while (true) {
  // st = mib.getPreviousStepInCurrentMethod();
  //
  // if (st == null) {
  // return mib.createBackwardBreakpointFromPreviousReturnedStep(); // First possible place we can go
  // }
  //
  // if (!ip.hitPosition(st.getInstruction())) {
  // lineChangeCnt++;
  // if (lineChangeCnt > 1) {
  // return mib.createBackwardBreakpointFromPreviousReturnedStep();
  // }
  // ip = InstructionPositionImpl.getInstructionPosition(st.getInstruction());
  // }
  //
  // if (checkCall.isCallInstruction(st.getInstruction())) {
  // return mib.createBackwardBreakpointFromCurrentStep();
  // }
  // }
  // }

  static private Path getPath (InspectorState insp) {
    VM vm = insp.getVM();
    vm.updatePath();
    return vm.getPath();
  }

}
