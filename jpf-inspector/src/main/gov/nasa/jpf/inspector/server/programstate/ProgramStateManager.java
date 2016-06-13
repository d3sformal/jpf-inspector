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

package gov.nasa.jpf.inspector.server.programstate;

import gov.nasa.jpf.inspector.common.pse.ProgramStateEntry;
import gov.nasa.jpf.inspector.interfaces.InstructionPosition;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.interfaces.ProgramStateInterface;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
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

  public ProgramStateManager (JPFInspector inspector, StopHolder stopHolder) {
    this.inspector = inspector;
    this.stopHolder = stopHolder;
    parser = new ExpressionParser(inspector);
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
      result.put(threadNum, sti.toHierarchy3("", 0));

    } else {
      ThreadList tl = vm.getThreadList();
      result = new HashMap<>(tl.length());

      for (ThreadInfo ti : tl) {
        StateThreadInfo sti = new StateThreadInfo(inspector, vm, ti.getId(), 2);
        result.put(ti.getId(), sti.toHierarchy3("", 0));
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
    return sni.toHierarchy3("", 0);
  }

  private VM getJVM () throws JPFInspectorGenericErrorException {
    VM vm = stopHolder.getJVM();
    if (vm == null) {
      throw new JPFInspectorGenericErrorException("Internal error - JVM not as a part of the state (JPF is not connected");
    }
    return vm;
  }

  /* @see gov.nasa.jpf.inspector.interfaces.ProgramStateInterface#setValue(java.lang.String) */
  @Override
  public void setValue (String expr) throws JPFInspectorException {
    // Create a parse tree
    ExpressionStateAssignment parsedExpr = parser.getExpressionAssignment(expr);
    setValueInternal(parsedExpr);
  }

  /* @see gov.nasa.jpf.inspector.interfaces.ProgramStateInterface#setValue(java.lang.String, java.lang.String) */
  @Override
  public void setValue (String lValue, String rValue) throws JPFInspectorException {

    ExpressionStateRootNode lValueExpr = parser.getExpressionStateInterface(lValue);

    ExpressionStateRootNode rValueExpr = parser.getExpressionStateInterface(rValue);

    ExpressionStateAssignment parsedExpr = parser.getExpressionFactory().getStateAssignment(lValueExpr, rValueExpr);

    setValueInternal(parsedExpr);
  }

  /**
   * Executes the assignment
   * 
   * @param esa Parsed representation of the assignment statement
   * @throws JPFInspectorException
   */
  private void setValueInternal (ExpressionStateAssignment esa) throws JPFInspectorException {
    stopHolder.waitUntilStopped();

    assert (esa != null);

    StateReadableValueInterface rVal = esa.getRValue(inspector, stopHolder.getInspectorState());

    StateValue lVal = esa.getLValue(inspector, stopHolder.getInspectorState());

    lVal.assignValue(rVal);
  }
}
