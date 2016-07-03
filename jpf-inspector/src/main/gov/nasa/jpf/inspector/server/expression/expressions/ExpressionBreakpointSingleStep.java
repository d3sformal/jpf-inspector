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

package gov.nasa.jpf.inspector.server.expression.expressions;

import gov.nasa.jpf.inspector.interfaces.InstructionPosition;
import gov.nasa.jpf.inspector.server.breakpoints.InstructionPositionImpl;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.expression.InspectorState.ListenerMethod;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.vm.*;
import gov.nasa.jpf.vm.bytecode.InvokeInstruction;

/**
 * Represents the "step_in" and "step_over" hit conditions. These are supposed to be internal and are undocumented.
 * They are, however, used internally by the single-stepping command.
 */
public class ExpressionBreakpointSingleStep extends ExpressionBooleanLeaf {
  private static final boolean DEBUG = true;

  @SuppressWarnings("FieldCanBeLocal") // https://youtrack.jetbrains.com/issue/IDEA-157179
  private final JPFInspector inspector;

  /**
   * Position of the instruction that is about to be executed at the time
   * the stepping command is initiated.
   */
  private final InstructionPosition initialPosition;
  /**
   * Kind of the stepping (step over or step in)
   */
  private final LocationTypes steppingKind;
  /**
   * Active thread when the breakpoint was set.
   */
  private final int threadNum;
  /**
   * Is used to determine correct return in to the calling method in case of Step-over
   */
  private final StackFrame initialStackFrame;
  /**
   * Transition that has to be present in the current Path (or {@link VM#getCurrentTransition()}.
   * This prevents backtracking before the choice when the breakpoint has been created
   */
  private final Transition initialTransition;

  /**
   * Whether we should "step over" or "step in".
   */
  public enum LocationTypes {
    LT_POSITION_LEAVED_STEP_OVER,
    LT_POSITION_LEAVED_STEP_IN,
  }

  /**
   * Initializes this hit condition.
   *
   * @param vm Virtual machine where to obtain current position. Can't be null.
   * @param steppingKind The way how Instruction position is used.
   */
  public ExpressionBreakpointSingleStep (JPFInspector inspector, VM vm, LocationTypes steppingKind) {
    assert vm != null;
    assert inspector != null;

    this.inspector = inspector;
    this.steppingKind = steppingKind;

    this.initialPosition = InstructionPositionImpl.getInstructionPosition(vm.getInstruction());
    this.threadNum = vm.getCurrentThread().getId();
    this.initialStackFrame = vm.getCurrentThread().getTopFrame();
    this.initialTransition = vm.getCurrentTransition();
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
    int currentThreadNumber = vm.getCurrentThread().getId();
    if (currentThreadNumber != threadNum) {
      return false;
    }

    // Check if we didn't backtrack too deep (before place where single step starts)
    if (!checkPath(vm, initialTransition)) {
      if (DEBUG) {
        inspector.getDebugPrintStream().println("We backtracked using forward stepping!");
      }
      return true;
    }

    final Instruction impendingInstruction = vm.getInstruction();

    // Whether we are at the same line as we were when the stepping started.
    final boolean weAreStillOnTheSameLine = initialPosition.hitPosition(impendingInstruction);

    if (DEBUG) {
      inspector.getDebugPrintStream().println(
          "\tlasInstr=" + ExpressionBreakpointPosition.instructionPosition(impendingInstruction) + "\n\tlastInstrHitPos=" + weAreStillOnTheSameLine);
    }

    // The instruction that was last executed by this thread, if any.
    Instruction prevInstr = state.getLastExecutedInstruction(currentThreadNumber);

    if (steppingKind == LocationTypes.LT_POSITION_LEAVED_STEP_OVER) {
      ThreadInfo currentThread = vm.getCurrentThread();
      if (containsStackFrame(currentThread, initialStackFrame)) {
        // We haven't returned from the method yet.
        // Stack frame of method which current instruction belongs to
        StackFrame stackFrame = currentThread.getTopFrame();

        if (stackFrame.equals(initialStackFrame)) {
          // We are in the same method.

          //noinspection RedundantIfStatement (I think it's clearer this way.)
          if (weAreStillOnTheSameLine) {
            return false; // We are still on the same line.
          } else {
            return true; // We are no longer on the same line.
          }
        } else {
          // We have gone deeper into a call and need to wait until we exit.
          return false;
        }
      } else {
        // We have left the method we were in when the stepping was initiated.
        // That is one of the trigger conditions of step_over.
        if (DEBUG) {
          inspector.getDebugPrintStream().println("We stepped out of the method!");
        }
        return true;
      }
    } else if (steppingKind == LocationTypes.LT_POSITION_LEAVED_STEP_IN) {
      if (weAreStillOnTheSameLine == true) {
        return false;
      }
      if (impendingInstruction instanceof InvokeInstruction) {
        return true;
      }

      final boolean prevInstrHitPos = initialPosition.hitPosition(prevInstr);
      if (DEBUG) {
        inspector.getDebugPrintStream().println("tprevInstrHitPos=" + prevInstrHitPos);
      }
      return prevInstrHitPos;
    } else {
      throw new RuntimeException("Internal error - Unknown enum " + (steppingKind != null ? steppingKind.getClass() : LocationTypes.class).getSimpleName()
          + " entry " + steppingKind);
    }
  }

  @Override
  public String getDetails (InspectorState state) {
    // This should never be actually called, because this is usually an internal breakpoint
    // and thus its details won't be displayed to the user.
    if (state != null && evaluateExpression(state)) {
      return "SuT leaves the position " + initialPosition.toString() + ".";
    }
    return "";
  }

  /**
   * Checks whether path or last transition in given JVM contains given transition.
   * This test is used to determine whether the JPF didn't backtrack before the initial instruction.
   * 
   * @param jvm JVM to test.
   * @param tr Transition to search.
   * @return True if the path contains the transition.
   */
  private static boolean checkPath (VM jvm, Transition tr) {
    assert jvm != null;
    Transition curTr = jvm.getCurrentTransition();
    if (curTr.equals(tr)) {
      return true;
    }

    for (Transition pathTr : jvm.getPath()) {
      if (pathTr.equals(tr)) {
        return true;
      }
    }
    return false;
  }

  private static boolean containsStackFrame (ThreadInfo ti, StackFrame sf) {
    for (StackFrame threadsSF : ti) {
      if (threadsSF.equals(sf)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String getNormalizedExpression () {
    if (steppingKind == LocationTypes.LT_POSITION_LEAVED_STEP_IN) {
      return "step_in";
    } else if (steppingKind == LocationTypes.LT_POSITION_LEAVED_STEP_OVER) {
      return "step_over";
    } else {
      throw new RuntimeException("Unknown " + steppingKind.getClass().getSimpleName() + " enum entry: " + steppingKind);
    }
  }
}
