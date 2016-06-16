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

import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.migration.MigrationUtilities;
import gov.nasa.jpf.inspector.server.breakpoints.BreakPointModes;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.expression.InspectorState.ListenerMethod;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.jpf.StopHolder;
import gov.nasa.jpf.inspector.utils.expressions.ClassName;
import gov.nasa.jpf.inspector.utils.expressions.MethodName;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.Step;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.Transition;
import gov.nasa.jpf.vm.Instruction;

/**
 * Used for internal Inspector purposes to implement backward single instruction steps.
 * 
 * Note: Should be used in the single JPF transition. Don't support backtracking. Note: Meaningful usage ... take back current transition (backtrack), restart
 * the choice generator to previous state and reexecute the same choice again Note: Inspector has to call {@link #evaluateExpression(InspectorState)} after each
 * executed instruction, no optimalization permitted.
 */
public class ExpressionBreakpointInstruction extends ExpressionBooleanLeaf {
  private static final boolean DEBUG = false;

  private final int threadNum;
  private final Instruction instr;
  private int count; // How many times we reach specified instruction
  private final int hitCount; // / How many times has the specific thread reach the instruction to hit the Breakpoint

  /**
   * 
   * @param threadNum
   *        Specify thread we observe. Instruction from different threads are ignored.
   * @param instr
   *        Instruction we wanted to hit.
   * @param hitCount
   *        How many time has to be instruction executed to hit the breakpoint. If the hit count is 0 then breaks on the first instruction in given thread.
   */
  public ExpressionBreakpointInstruction (int threadNum, Instruction instr, int hitCount) {
    if (DEBUG) {
      System.out.println(this.getClass().getSimpleName() + "." + this.getClass().getSimpleName() + "( threadNum=" + threadNum + ", instr=" + instr + "("
          + (instr != null ? instr.getFilePos() : "?") + "), hitCount=" + hitCount + ")");
    }
    assert instr != null;
    assert hitCount > 0;
    this.threadNum = threadNum;
    this.instr = instr;
    this.hitCount = hitCount;
    count = 0;
  }

  /**
   * Analyze given transition and create BreakpointExpression that hits on the last but one instruction in given transition.
   * 
   * @param tr Transition to analyze. Cann't be null.
   * @return BreakpointExpression. If there is no place where put the BP (only one executed instruction) returns null.
   */
  static ExpressionBreakpointInstruction getBPonPreviousInstruction (Transition tr) {
    assert tr != null;
    int instrIndex = tr.getStepCount() - 1;
    if (instrIndex < 1) {
      return null;
    }
    Step penultimateStep = tr.getStep(instrIndex);
    Instruction penultimateInstr = penultimateStep.getInstruction();

    int sameInstrCount = 1; // One for the final instruction where we hit the BP
    // Count how many time the instruction takes place in the transition (due to cycle repetitions etc.)
    for (Step currentStep = tr.getStep(0); !currentStep.equals(penultimateStep); currentStep = currentStep.getNext()) {
      if (penultimateInstr.equals(currentStep.getInstruction())) {
        sameInstrCount++;
      }
    }

    return new ExpressionBreakpointInstruction(tr.getThreadIndex(), penultimateInstr, sameInstrCount);
  }

  @Override
  public boolean evaluateExpression (InspectorState state) {
    if (state.getListenerMethod() == ListenerMethod.LM_EXECUTE_INSTRUCTION) {
      VM vm = state.getVM();
      assert vm != null;
      ThreadInfo ti = vm.getCurrentThread();
      if (ti.getId() == threadNum) {
        Instruction executedInstr = vm.getInstruction();
        if (instr.equals(executedInstr)) {
          count++;
          return count == hitCount;
        }
      }
    }
    return false;
  }

  @Override
  public BreakPointModes getBPMode () {
    return BreakPointModes.BP_MODE_INTERNAL_INSTRUCTION;
  }

  /**
   * Gather Instruction in given method at given position.
   * 
   * Note: Uses first matching method for methodSpec (use no wildchars in Method name specification)
   * Note: Inspect only currently loaded classes.
   * 
   * @param inspector Inspector with connected JPF.
   * @param methodSpec Specification of method to uses.
   * @param instIdx Instruction index in given method.
   * 
   * @return Instruction in first matching method at given position or null if instruction or method not found.
   */
  public static Instruction findInstruction (JPFInspector inspector, MethodName methodSpec, int instIdx) throws JPFInspectorGenericErrorException {
    assert inspector != null;
    StopHolder sh = inspector.getStopHolder();
    assert sh != null;
    InspectorState inspState = sh.getInspectorState();
    if (inspState == null) {
      throw new JPFInspectorGenericErrorException("Cannot find specified instruction - JPF is not connected to Inspector or execution is not stopped");
    }

    VM vm = inspState.getVM();
    assert (vm != null);

    assert (methodSpec != null);
    ClassName classSpec = methodSpec.getClassNameClass();

    ClassInfo[] cis = MigrationUtilities.getLoadedClasses();
    for (ClassInfo ci : cis) {
      assert (ci != null);
      if (classSpec.isSameClass(ci)) {
        MethodInfo[] mis = ci.getDeclaredMethodInfos();
        for (MethodInfo mi : mis) {
          if (methodSpec.isSameMethod(mi)) {
            return mi.getInstruction(instIdx);
          }
        }
      }
    }
    // No such method exists
    return null;
  }

  @Override
  public String getNormalizedExpression () {
    StringBuilder sb = new StringBuilder();
    sb.append("specific_instruction");
    sb.append(" thread=");
    sb.append(threadNum);
    sb.append(" instruction=");
    MethodInfo mi = instr.getMethodInfo();
    sb.append(mi.getClassName());
    sb.append(':');
    sb.append(mi.getName());
    sb.append(':');
    sb.append(instr.getInstructionIndex());
    sb.append('(');
    sb.append(instr.toString());
    sb.append(')');
    sb.append(" hit_count=");
    sb.append(hitCount);
    return sb.toString();
  }
}
