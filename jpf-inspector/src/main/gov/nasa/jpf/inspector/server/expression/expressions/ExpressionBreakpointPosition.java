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

import gov.nasa.jpf.inspector.interfaces.InstructionPosition;
import gov.nasa.jpf.inspector.migration.MigrationUtilities;
import gov.nasa.jpf.inspector.server.breakpoints.BreakPointModes;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.expression.InspectorState.ListenerMethod;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.Path;
import gov.nasa.jpf.vm.Transition;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.jvm.bytecode.JSR;
import gov.nasa.jpf.jvm.bytecode.JSR_W;
import gov.nasa.jpf.jvm.bytecode.RET;
import gov.nasa.jpf.vm.bytecode.ReturnInstruction;

/**
 * Represent expression (Breakpoint) that stops on given position in the program.
 */

// TODO the NEW instruction can stand in place of the call instruction (calls the constructor!!)
// problem is connected with the DirectCalls (cinits) and reexecution of the same instruction !!
public class ExpressionBreakpointPosition extends ExpressionBooleanLeaf {
  private static final boolean DEBUG = false;
  private final JPFInspector inspector;

  final private InstructionPosition instPos;

  /**
   * 
   * @param pos
   *        Position where stop execution. Cann't be null.
   * TODO oldparam _param posHandling
   *        The way how Instruction position is used.
   */
  public ExpressionBreakpointPosition (JPFInspector inspector, InstructionPosition pos) {
    assert pos != null;
    assert inspector != null;
    this.inspector = inspector;

    if (DEBUG) {
      inspector.getDebugPrintStream().println(
          ExpressionBreakpointPosition.class.getSimpleName() + "." + ExpressionBreakpointPosition.class.getSimpleName() + "(pos=" + pos + ")");
    }

    this.instPos = pos;
  }

  public final InstructionPosition getInstructionPos () {
    return instPos;
  }

  @Override
  public boolean evaluateExpression (InspectorState state) {
    assert state != null;
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".evaluateExpression(...)");
    }
    if (state.getListenerMethod() != ListenerMethod.LM_INSTRUCTION_EXECUTED) {
      return false;
    }

    VM vm = state.getJVM();
    assert vm != null;

    int lastThread = MigrationUtilities.getThreadNumber(vm);

    final Path path = vm.getPath();
    final Instruction lastInstr = MigrationUtilities.getLastInstruction(vm);

    final boolean lastInstrHitPos = instPos.hitPosition(lastInstr);
    if (DEBUG) {
      inspector.getDebugPrintStream().println("\tlasInstr=" + instructionPosition(lastInstr) + "\n\tlastInstrHitPos=" + lastInstrHitPos);
    }

    Instruction prevInstr = getInstructionForThread(vm.getSystemState().getTrail(), path, lastThread, 1);
    if (DEBUG) {
      inspector.getDebugPrintStream().println("\tprevInstr=" + instructionPosition(prevInstr));
    }

    if (lastInstrHitPos == false) {
      return false;
    }
    prevInstr = getPrevInstructionInSameMethod(prevInstr, lastInstr);
    final boolean prevInstrHitPos = instPos.hitPosition(prevInstr);
    if (DEBUG) {
      inspector.getDebugPrintStream().println("\tprevInstr=" + instructionPosition(prevInstr) + "\n\tprevInstrHitPos=" + prevInstrHitPos);
    }
    return !prevInstrHitPos;
  }

  @Override
  public BreakPointModes getBPMode () {
    return BreakPointModes.BP_MODE_POSITION_ONLY;
  }

  @Override
  public String getDetails (InspectorState state) {
    if (state != null && evaluateExpression(state)) {
      return "SuT will now enter the position " + instPos.toString(); // TODO is it really "will now enter" or is it "had already evaluated at least part of it"?
    }
    return "";
  }

  /**
   * Analyze the path and returns instruction such that the instruction was executed by specified thread, and then the thread executed exactly instCount
   * instructions
   * 
   * @param tr
   *        Last transition (The last instruction are not saved into the path but only into last transition)
   * @param path
   *        Path to analyze
   * @param threadNum
   *        Thread to process. All other threads are ignored.
   * @param instCount
   *        Number of instructions executed by JPF after returned instruction
   * @return
   */
  static public Instruction getInstructionForThread (Transition tr, Path path, int threadNum, int instCount) {
    assert path != null;
    assert instCount >= 0;

    int instInThread = 0; // Instructions executed by given thread counter
    if (tr.getThreadIndex() == threadNum) {
      instInThread += tr.getStepCount();

      if (instInThread > instCount) {
        return tr.getStep(instInThread - instCount - 1).getInstruction();
      }
    }

    for (int i = path.size() - 1; i >= 0; i--) {
      tr = path.get(i);
      if (tr.getThreadIndex() != threadNum) {
        continue; // Different thread was planned
      }

      instInThread += tr.getStepCount();

      if (instInThread > instCount) {
        return tr.getStep(instInThread - instCount - 1).getInstruction();
      }
    }

    return null;
  }

  /**
   * @param prevInstr
   *        Instruction executed (by the same thread) immediately before the last instruction.
   * @param lastInstr
   *        Last execute instruction
   * @return prevInstruction to executed in the same method as the lastInstr.
   * 
   *         Note: Method requires that the lastInstruction is executed directly after the prevInstr (in one thread)
   */
  static public Instruction getPrevInstructionInSameMethod (Instruction prevInstr, Instruction lastInstr) {
    // Handle special cases Calls and JSR
    if (prevInstr instanceof ReturnInstruction) {
      // We should ignore instructions in called method, we focus on previous instruction in current method
      prevInstr = lastInstr.getPrev();
      // TODO ... problem with direct calls !!!
      // assert prevInstr instanceof InvokeInstruction;
    }

    if (prevInstr instanceof RET) {
      // We should ignore instructions in called subrutine, we focus on previous instruction in current method
      prevInstr = lastInstr.getPrev();
      assert (prevInstr instanceof JSR) || (prevInstr instanceof JSR_W);
    }
    return prevInstr;
  }

  static public String instructionPosition (Instruction inst) {
    if (inst == null) {
      return "(null)";
    }
    return inst.getMethodInfo().getSourceFileName() + ":" + inst.getLineNumber() + "(method=" + inst.getMethodInfo().getName() + ") - " + inst.toString();
  }

  @Override
  public String getNormalizedExpression () {
    StringBuffer sb = new StringBuffer(30);
    sb.append("position=");
    sb.append(instPos.getFileName());
    sb.append(':');
    sb.append(instPos.getLineNumber());

    return sb.toString();
  }

}
