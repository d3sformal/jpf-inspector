//
// Copyright (C) 2010 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
// 
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
// 
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//  

package gov.nasa.jpf.inspector.server.expression.expressions;

import gov.nasa.jpf.inspector.interfaces.CommandsInterface.StepType;
import gov.nasa.jpf.inspector.interfaces.InstructionPosition;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.migration.MigrationUtilities;
import gov.nasa.jpf.inspector.server.breakpoints.BreakPointModes;
import gov.nasa.jpf.inspector.server.breakpoints.InstructionPositionImpl;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.expression.InspectorState.ListenerMethod;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.jpf.SearchInspectorExtension;
import gov.nasa.jpf.inspector.server.jpf.StopHolder;
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.Path;
import gov.nasa.jpf.vm.StackFrame;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.Transition;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.bytecode.InvokeInstruction; // TODO change of invoke here
import gov.nasa.jpf.vm.bytecode.ReturnInstruction;
import gov.nasa.jpf.search.DFSearch;
import gov.nasa.jpf.search.Search;

import java.util.Iterator;

/**
 * Represents the "step_in" and "step_over" hit conditions. These are supposed to be internal and are undocumented.
 * They are, however, used internally by the single-stepping command.
 */
public class ExpressionBreakpointSingleStep extends ExpressionBooleanLeaf {
  private static final boolean DEBUG = false;
  private final JPFInspector inspector;

  final private InstructionPosition instPos;
  final private LocationTypes posHandling;
  final private int threadNum;
  final private StackFrame topStackFrame; // Is used to determine correct return in to the calling method in case of Step-over
  final private Transition reqTransition; // Transition that has to be present in the current Path (or {@link JVM#getCurrentTransition()}. This prevents
                                          // backtracking before Choice when the breakpoint has been created

  final private boolean breakIfNoNextLine; // If set the Breakpoint hits if the JPF backtrack before the transition/step where BP takes place. Can have sence
                                           // only for some Search classes.

  public enum LocationTypes {
    /**
     * The breakpoint hits if the previous instruction (in the same method invocation)
     * was at the given ({@link #instPos}) {@link InstructionPosition} while current instruction
     * is at different {@link InstructionPosition}
     */
    LT_POSITION_LEAVED_STEP_OVER,

    /**
     * The breakpoint hits if the previous instruction (not necessary in the same method invocation)
     * was at the given ({@link #instPos}) {@link InstructionPosition} while current instruction
     * is at different {@link InstructionPosition}
     */
    LT_POSITION_LEAVED_STEP_IN,
  }

  /**
   * This is apparently called only from the grammar? That's a pretty big piece of code for an undocumented command.
   * Maybe I'm missing something...
   */
  public static ExpressionBreakpointSingleStep createBreakpointSingleStep (JPFInspector inspector, StepType stepType) throws JPFInspectorGenericErrorException {
    assert inspector != null;
    StopHolder sh = inspector.getStopHolder();
    assert sh != null;
    InspectorState inspState = sh.getInspectorState();
    if (inspState == null) {
      throw new JPFInspectorGenericErrorException(
          "cannot create breakpoint, JPF has to be connected and stopped - program state is required to create given breakpoint.");
    }

    VM vm = inspState.getVM();
    assert (vm != null);

    LocationTypes lt;
    assert (stepType != null);
    if (stepType == StepType.ST_STEP_IN) {
      lt = LocationTypes.LT_POSITION_LEAVED_STEP_IN;
    } else if (stepType == StepType.ST_LINE) {
      lt = LocationTypes.LT_POSITION_LEAVED_STEP_OVER;
    } else {
      throw new JPFInspectorGenericErrorException("Step type (" + stepType + ") is not supported by " + ExpressionBreakpointSingleStep.class.getSimpleName()
          + " breakpoint.");
    }

    return new ExpressionBreakpointSingleStep(inspector, vm, lt);
  }

  /**
   * 
   * @param vm Virtual machine where to obtain current position. Can't be null.
   * @param posHandling The way how Instruction position is used.
   */
  public ExpressionBreakpointSingleStep (JPFInspector inspector, VM vm, LocationTypes posHandling) {
    assert vm != null;
    assert inspector != null;

    this.inspector = inspector;
    this.posHandling = posHandling;

    this.instPos = InstructionPositionImpl.getInstructionPosition(MigrationUtilities.getLastInstruction(vm));
    this.threadNum = vm.getCurrentThread().getId();

    // TODO this is now probably wrong since we moved from "after" to "before" execution
    if (MigrationUtilities.getLastInstruction(vm) instanceof InvokeInstruction) {
      // Stack frame for called method is already created - of the of top2 stack frame, which represents the method with invoke instruction
      this.topStackFrame = vm.getCurrentThread().getTopFrame().getPrevious();
    } else {
      this.topStackFrame = vm.getCurrentThread().getTopFrame();
    }
    this.reqTransition = vm.getCurrentTransition();

    Search search = vm.getJPF().getSearch();
    this.breakIfNoNextLine = (search instanceof DFSearch) || (search instanceof SearchInspectorExtension);

    if (DEBUG) {
      inspector.getDebugPrintStream().println(
          ExpressionBreakpointSingleStep.class.getSimpleName() + "." + ExpressionBreakpointPosition.class.getSimpleName() + "(instPos=" + instPos
              + ", posHandling=" + posHandling + ", threadNum=" + threadNum + ", reqTransition=" + reqTransition + ", posHandling=" + posHandling + ")");
    }

  }

  public final InstructionPosition getInstructionPos () {
    return instPos;
  }

  public final LocationTypes getPosHandling () {
    return posHandling;
  }

  @Override
  public boolean evaluateExpression (InspectorState state) {
    assert state != null;
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".evaluateExpression(...)");
    }
    if (state.getListenerMethod() != ListenerMethod.LM_EXECUTE_INSTRUCTION) {
      return false;
    }

    VM vm = state.getVM();
    assert vm != null;

    // Check if we are in the same thread
    int lastThread = vm.getCurrentThread().getId();
    if (lastThread != threadNum) {
      return false;
    }

    // Check if we don't backtrack to depth (before place where single step starts)
    if (!checkPath(vm, reqTransition)) {
      return breakIfNoNextLine;
    }

    final Path path = vm.getPath();
    final Instruction lastInstr = MigrationUtilities.getLastInstruction(vm);
    // final Step lastStep = vm.getSystemState().getTrail().getLastStep();
    // final Instruction xxx = lastStep.getInstruction();
    // assert lastInstr.equals(xxx);

    final boolean lastInstrHitPos = instPos.hitPosition(lastInstr);
    if (DEBUG) {
      inspector.getDebugPrintStream().println(
          "\tlasInstr=" + ExpressionBreakpointPosition.instructionPosition(lastInstr) + "\n\tlastInstrHitPos=" + lastInstrHitPos);
    }

    Instruction prevInstr = state.getLastExecutedInstruction(lastThread);
    // Instruction prevInstr = ExpressionBreakpointPosition.getInstructionForThread(vm.getSystemState().getTrail(), path, lastThread, 1);

    // TODO this may now be false:
    if (prevInstr instanceof ReturnInstruction) {
      // Implies that previous instruction in method is invoke (no jump, part of single basic block)
      prevInstr = lastInstr.getPrev();
      assert (prevInstr instanceof InvokeInstruction);
    }
    if (DEBUG) {
      inspector.getDebugPrintStream().println("\tprevInstr=" + ExpressionBreakpointPosition.instructionPosition(prevInstr));
    }

    if (posHandling == LocationTypes.LT_POSITION_LEAVED_STEP_OVER) {
      ThreadInfo currentThread = vm.getCurrentThread();
      if (containsStackFrame(currentThread, topStackFrame)) {
        // Stack frame of method which current(last) instruction belongs to
        StackFrame stackFrame = currentThread.getTopFrame();
        if (lastInstr instanceof InvokeInstruction) {
          // top Stack frame is callee frame, we are interested in caller stack frame
          stackFrame = stackFrame.getPrevious();
        }
        if (stackFrame.equals(topStackFrame)) {
          if (lastInstr instanceof ReturnInstruction) {
            // Return from call -> check next instruction
            return false;
          }
          return !lastInstrHitPos;
        } else {
          return false;
        }
      } else {
        return true;
      }
    } else if (posHandling == LocationTypes.LT_POSITION_LEAVED_STEP_IN) {
      if (lastInstrHitPos == true) {
        return false;
      }
      if (lastInstr instanceof InvokeInstruction) {
        return true;
      }

      final boolean prevInstrHitPos = instPos.hitPosition(prevInstr);
      if (DEBUG) {
        inspector.getDebugPrintStream().println("tprevInstrHitPos=" + prevInstrHitPos);
      }
      return prevInstrHitPos;
    } else {
      throw new RuntimeException("Internal error - Unknown enum " + (posHandling != null ? posHandling.getClass() : LocationTypes.class).getSimpleName()
          + " entry " + posHandling);
    }
  }

  @Override
  public String getDetails (InspectorState state) {
    if (state != null && evaluateExpression(state)) {
      return "SuT leaves the position " + instPos.toString() + ".";
    }
    return "";
  }

  @Override
  public BreakPointModes getBPMode () {
    return BreakPointModes.BP_MODE_INTERNAL_STEP_OVER;
  }

  /**
   * Checks whether path or last transition in given JVM contains given transition.
   * This test is used to determine whether the JPF doesn't backtrack before position represented by Transition (each step creates a transition)
   * 
   * @param jvm JVM to test
   * @param tr Transition to search.
   * @return True if the path contains the transition
   */
  private static boolean checkPath (VM jvm, Transition tr) {
    assert jvm != null;
    Transition curTr = jvm.getCurrentTransition();
    if (curTr.equals(tr)) {
      return true;
    }

    Iterator<Transition> itTransitions = jvm.getPath().iterator();
    while (itTransitions.hasNext()) {
      Transition pathTr = itTransitions.next();
      if (pathTr.equals(tr)) {
        return true;
      }
    }
    return false;
  }

  private static boolean containsStackFrame (ThreadInfo ti, StackFrame sf) {

    Iterator<StackFrame> sfIt = ti.iterator();
    while (sfIt.hasNext()) {
      StackFrame threadsSF = sfIt.next();
      if (threadsSF.equals(sf)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String getNormalizedExpression () {
    if (posHandling == LocationTypes.LT_POSITION_LEAVED_STEP_IN) {
      return "step_in";
    } else if (posHandling == LocationTypes.LT_POSITION_LEAVED_STEP_OVER) {
      return "step_over";
    } else {
      throw new RuntimeException("Unknown " + posHandling.getClass().getSimpleName() + " enum entry: " + posHandling);
    }
  }
}
