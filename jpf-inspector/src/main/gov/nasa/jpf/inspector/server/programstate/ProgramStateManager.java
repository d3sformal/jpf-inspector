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

package gov.nasa.jpf.inspector.server.programstate;

import gov.nasa.jpf.inspector.common.pse.ProgramStateEntry;
import gov.nasa.jpf.inspector.interfaces.InstructionPosition;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.interfaces.ProgramStateInterface;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.server.attributes.AttributesManager;
import gov.nasa.jpf.inspector.server.breakpoints.InstructionPositionImpl;
import gov.nasa.jpf.inspector.server.expression.ExpressionParser;
import gov.nasa.jpf.inspector.server.expression.ExpressionParserInterface;
import gov.nasa.jpf.inspector.server.expression.ExpressionStateRootNode;
import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionStateAssignment;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.jpf.StopHolder;
import gov.nasa.jpf.inspector.common.pse.PSEThread;
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.ThreadList;
import gov.nasa.jpf.vm.Instruction;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles commands that can be used to inspect program state, e.g. the "print" and "thread" commands.
 *
 * This class is a component of {@link JPFInspector}.
 */
public class ProgramStateManager implements ProgramStateInterface {

  private final JPFInspector inspector;
  private final StopHolder stopHolder; // / Where JPF thread is blocked when Breakpoint or Stop command comes.

  private final ExpressionParserInterface parser;
  private final AttributesManager attributesManager;

  public ProgramStateManager(JPFInspector inspector, StopHolder stopHolder, AttributesManager attributesManager) {
    this.inspector = inspector;
    this.stopHolder = stopHolder;
    parser = new ExpressionParser(inspector);
    this.attributesManager = attributesManager;
  }

  @Override
  public Map<Integer, PSEThread> getThreads (Integer threadNum) throws JPFInspectorException {
    stopHolder.waitUntilStopped();

    Map<Integer, PSEThread> result;

    VM vm = getJVM();
    if (vm == null) {
      return null;
    }

    if (threadNum != null) {
      result = new HashMap<>(1);
      StateThreadInfo sti = new StateThreadInfo(inspector, vm, threadNum);
      result.put(threadNum, sti.toHierarchy3(this.attributesManager));

    } else {
      ThreadList tl = vm.getThreadList();
      result = new HashMap<>(tl.length());

      for (ThreadInfo ti : tl) {
        StateThreadInfo sti = new StateThreadInfo(inspector, vm, ti.getId());
        result.put(ti.getId(), sti.toHierarchy3(this.attributesManager));
      }
    }

    return result;
  }

  @Override
  public Map<Integer, InstructionPosition> getThreadsPC (Integer threadNum) throws JPFInspectorGenericErrorException {
    stopHolder.waitUntilStopped();

    VM vm = getJVM();
    if (vm == null) {
      return null;
    }

    ThreadList tl = vm.getThreadList();

    Map<Integer, InstructionPosition> result;
    if (threadNum != null) {
      result = new HashMap<>(1);
    } else {
      result = new HashMap<>(tl.length());
    }

    for (ThreadInfo ti : tl) {
      if (!((threadNum == null) || (ti.getId() == threadNum))) {
        continue;
      }
      Instruction pc = ti.getPC();
      if (pc == null) {
        // Terminated threads -> no position to store
        continue;
      }
      result.put(ti.getId(), InstructionPositionImpl.getInstructionPosition(pc));
    }
    return result;
  }

  @Override
  public ProgramStateEntry evaluateStateExpression (String expr) throws JPFInspectorException {
    stopHolder.waitUntilStopped();

    // Create a parse tree (hierarchy 1)
    ExpressionStateRootNode parsedExpr = parser.getExpressionStateInterface(expr);
    if (parsedExpr == null) {
      return null;
    }

    // Create "representation of parse tree according current state" (hierarchy 2)
    StateNodeInterface sni = parsedExpr.getResultExpression(inspector, stopHolder.getInspectorState());

    // Create client representation of the state (hiearchy 3)
    return sni.toHierarchy3(this.attributesManager);
  }

  private VM getJVM () throws JPFInspectorGenericErrorException {
    VM vm = stopHolder.getVM();
    if (vm == null) {
      throw new JPFInspectorGenericErrorException("Internal error - JVM not as a part of the state (JPF is not connected");
    }
    return vm;
  }

  @Override
  public void setValue (String expr) throws JPFInspectorException {
    // Create a parse tree
    ExpressionStateAssignment parsedExpr = parser.getExpressionAssignment(expr);
    setValueInternal(parsedExpr);
  }

  /**
   * Executes the assignment. Throws an exception if the assignment fails.
   * 
   * @param esa Parsed representation of the assignment statement
   */
  private void setValueInternal (ExpressionStateAssignment esa) throws JPFInspectorException {
    stopHolder.waitUntilStopped();

    assert (esa != null);

    StateReadableValue rVal = esa.getRValue(inspector, stopHolder.getInspectorState());
    StateWritableValue lVal = esa.getLValue(inspector, stopHolder.getInspectorState());

    lVal.assignValue(rVal);
  }
}
