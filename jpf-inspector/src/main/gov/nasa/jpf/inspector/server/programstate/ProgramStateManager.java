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
 * Handles command that can be used to inspect program state like
 * called methods and threads and variable values.
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
    initialStopTest(true, "threads");

    Map<Integer, PSEThread> result = null;

    VM vm = getJVM();
    if (vm == null) {
      return null;
    }

    if (threadNum != null) {
      result = new HashMap<Integer, PSEThread>(1);
      StateThreadInfo sti = new StateThreadInfo(inspector, vm, threadNum);
      result.put(threadNum, sti.getResultExpression("", 0));

    } else {
      ThreadList tl = vm.getThreadList();
      result = new HashMap<Integer, PSEThread>(tl.length());

      for (ThreadInfo ti : tl) {
        StateThreadInfo sti = new StateThreadInfo(inspector, vm, ti.getId(), 2);
        result.put(ti.getId(), sti.getResultExpression("", 0));
      }
    }

    return result;
  }

  @Override
  public Map<Integer, InstructionPosition> getThreadsPC (Integer threadNum) throws JPFInspectorGenericErrorException {
    initialStopTest(true, "threads");
    Map<Integer, InstructionPosition> result = null;

    VM vm = getJVM();
    if (vm == null) {
      return null;
    }

    ThreadList tl = vm.getThreadList();
    if (threadNum != null) {
      result = new HashMap<Integer, InstructionPosition>(1);
    } else {
      result = new HashMap<Integer, InstructionPosition>(tl.length());
    }

    for (ThreadInfo ti : tl) {
      if (!(threadNum == null || (ti.getId() == threadNum))) {
        continue;
      }
      Instruction pc = ti.getPC();
      if (pc == null) {
        // Terminated threads -> no position to store
        continue;
      }
      result.put(Integer.valueOf(ti.getId()), InstructionPositionImpl.getInstructionPosition(pc));
    }
    return result;
  }

  @Override
  public ProgramStateEntry evaluateStateExpression (String expr) throws JPFInspectorException {
    initialStopTest(true, "expression cannot be evaluated");

    // Create a parse tree
    ExpressionStateRootNode<?> parsedExpr = parser.getExpressionStateInterface(expr);
    if (parsedExpr == null) {
      return null;
    }

    // Create "representation of parse tree according current state"
    StateNodeInterface sni = parsedExpr.getResultExpression(inspector, stopHolder.getInspectorState());

    // Create client representation of the state
    return sni.getResultExpression("", 0);
  }

  // TODO Merge with Command Manager and move into StopHolder
  /**
   * Tests if any JPF is associated. Test if the JPF is running or stopped. If JPF runs then it tries to stop or report error.running
   * 
   * @param wait
   * @param msg
   * @return
   */
  public boolean initialStopTest (boolean wait, String msg) throws JPFInspectorGenericErrorException {
    if (inspector.getJPF() == null) {
      throw new JPFInspectorGenericErrorException("No JPF instance to observe");
    }
    boolean wasStopped = stopHolder.isStopped();
    if (!wasStopped) {
      if (!wait) {
        throw new JPFInspectorGenericErrorException("SuT is running - " + msg);
      } else {
        stopHolder.waitUntilStopped();
      }
    }
    return wasStopped;
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

    ExpressionStateRootNode<?> lValueExpr = parser.getExpressionStateInterface(lValue);

    ExpressionStateRootNode<?> rValueExpr = parser.getExpressionStateInterface(rValue);

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
    initialStopTest(true, "value cannot be set");

    assert (esa != null);

    StateReadableValueInterface rVal = esa.getRValue(inspector, stopHolder.getInspectorState());

    StateValue lVal = esa.getLValue(inspector, stopHolder.getInspectorState());

    lVal.assignValue(rVal);
  }
}
