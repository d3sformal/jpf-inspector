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

import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.expression.InspectorState.ListenerMethod;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.jpf.StopHolder;
import gov.nasa.jpf.inspector.utils.Debugging;
import gov.nasa.jpf.inspector.utils.expressions.ClassName;
import gov.nasa.jpf.inspector.utils.expressions.MethodName;
import gov.nasa.jpf.vm.*;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents the undocumented "specific_instruction" hit condition that hits when the specific instruction is about to be executed.
 *
 * Used for internal Inspector purposes to implement backward single instruction steps.
 * 
 * Note: Should only be used during the transition it was created for.
 *
 * Note: The way this is is used is that the Inspector backtracks some transitions, restarts the choice generator
 * to a previous state and reexecutes the same choice again in the transition where this breakpoint should happen.
 *
 * Note: The Inspector has to call {@link #evaluateExpression(InspectorState)} before each instruction to be executed,
 * no optimization is permitted because we must count all the instructions we want to skip.
 */
public class ExpressionBreakpointInstruction extends ExpressionBooleanLeaf {
  private static final boolean DEBUG = true;

  /**
   * We are only interested in the actions of this thread.
   * However, because we only work during a single transition, this seems granted because the transition will always
   * be executed by the only thread which is the one we're interested in. After we hit the breakpoint, it is removed.
   *
   * In theory, it might be possible for something to go wrong here, but user breakpoints are disabled during
   * backwards-stepping and the console command for creating this hit condition is undocumented (for good reason)
   * so we'll just assert that all threads reaching this breakpoint must have the index {@link #threadNum}.
   */
  private final int threadNum;
  /**
   * We want to stop right before we execute this instruction.
   */
  private final Instruction instruction;
  /**
   * The number of instructions, of any kind, we have already went through.
   */
  private int count = 0;
  /**
   * Index of the step before which we want to stop.
   */
  private final int hitCount;

  /**
   * Initializes a new instance of the {@link ExpressionBreakpointInstruction}.
   *
   * @param threadNum The thread to observe. Instructions from different threads are ignored and should not actually happen.
   * @param instruction The specific instruction that we want to break on.
   * @param hitCount At which step should we finally trigger? (i.e. we should skip a number of steps equal to `hitCount - 1`)
   */
  public ExpressionBreakpointInstruction (int threadNum, Instruction instruction, int hitCount) {
    if (DEBUG) {
      Debugging.getLogger().info(this.getClass().getSimpleName() + "(threadNum=" + threadNum + ", instruction=" + instruction + "("
          + (instruction != null ? instruction.getFilePos() : "?") + "), hitCount=" + hitCount + ")");
    }
    assert instruction != null;
    assert hitCount >= 0;
    this.threadNum = threadNum;
    this.instruction = instruction;
    this.hitCount = hitCount;
  }



  @Override
  public boolean evaluateExpression (InspectorState state) {
    if (state.getListenerMethod() == ListenerMethod.LM_EXECUTE_INSTRUCTION) {
      VM vm = state.getVM();
      assert vm != null;
      ThreadInfo ti = vm.getCurrentThread();
      assert ti.getId() == threadNum;
      if (ti.getId() == threadNum) {
        Instruction executedInstr = vm.getInstruction();
        Debugging.getLogger().info("Backtracking: Forward stepping: Testing against the same thread (" + executedInstr.getMnemonic() + "," + executedInstr.getFileLocation() + ")");
        if (count == hitCount) {
          if (Objects.equals(instruction.getMnemonic(), executedInstr.getMnemonic())) {
            return true;
          } else {
            Debugging.getLogger().severe("The new transition that was created after backstepping does not have the same instructions as the last one. Are you sure your application is deterministic?");
            return true;
          }
        }
        count++;
      }
    }
    return false;
  }

  /**
   * This method is used for the undocumented command "specific_instruction".
   *
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


    // The following block used to be the simple assignment
    // "ClassInfo[] cis = ClassLoader.getLoadedClass();"
    // However, that method is no longer available in JPF8.
    ArrayList<ClassInfo> loadedClasses = new ArrayList<>();
    for (ClassInfo loadedClass : ClassLoaderInfo.getCurrentClassLoader()) {
      loadedClasses.add(loadedClass);
    }
    ClassInfo[] cis = new ClassInfo[loadedClasses.size()];
    cis = loadedClasses.toArray(cis);
    // End of block.

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
    MethodInfo mi = instruction.getMethodInfo();
    sb.append(mi.getClassName());
    sb.append(':');
    sb.append(mi.getName());
    sb.append(':');
    sb.append(instruction.getInstructionIndex());
    sb.append('(');
    sb.append(instruction.toString());
    sb.append(')');
    sb.append(" hit_count=");
    sb.append(hitCount);
    return sb.toString();
  }
}
