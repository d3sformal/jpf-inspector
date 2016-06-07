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

package gov.nasa.jpf.inspector.server.expression;

import gov.nasa.jpf.inspector.interfaces.CommandsInterface.StepType;
import gov.nasa.jpf.inspector.interfaces.InstructionPosition;
import gov.nasa.jpf.inspector.interfaces.InstructionType;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.server.breakpoints.BreakPointModes;
import gov.nasa.jpf.inspector.server.breakpoints.InstructionPositionImpl;
import gov.nasa.jpf.inspector.server.expression.expressions.*;
import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionBreakpointSpecificClass.Mode;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.programstate.relop.RelationOperator;
import gov.nasa.jpf.inspector.server.programstate.relop.RelationOperatorFactory;
import gov.nasa.jpf.inspector.utils.expressions.ClassName;
import gov.nasa.jpf.inspector.utils.expressions.Expressions;
import gov.nasa.jpf.inspector.utils.expressions.FieldName;
import gov.nasa.jpf.inspector.utils.expressions.MethodName;
import gov.nasa.jpf.inspector.utils.parser.GenericErrorRuntimeException;
import gov.nasa.jpf.vm.Instruction;

public class ExpressionFactory {
  private final JPFInspector inspector;
  private final RelationOperatorFactory relopFactory;

  public ExpressionFactory (JPFInspector inspector) {
    assert inspector != null;
    this.inspector = inspector;
    this.relopFactory = new RelationOperatorFactory();
  }

  public ExpressionBreakpointCustomHitCondition getCustomHitCondition(String name, Expressions expressions) {
    return new ExpressionBreakpointCustomHitCondition(inspector, name, expressions);
            /*
            TODO throw error if custom class cannot be instantiated
    throw new GenericErrorRuntimeException(
            new JPFInspectorGenericErrorException("Custom hit conditions are not yet implemented."));
            */
  }
  public ExpressionBreakpointPosition getExpBreakpointPosition (String fileName, Integer lineNum) {
    if (lineNum == null) {
      return null;
    }
    InstructionPosition bpPosition = new InstructionPositionImpl(fileName, lineNum);

    return new ExpressionBreakpointPosition(inspector, bpPosition);
  }

  public ExpressionBreakpointPropertyViolated getBreakpoointPropertyViolated () {
    return new ExpressionBreakpointPropertyViolated();
  }

  public ExpressionBreakpointSpecificClass getBreakpointObjectCreated (ClassName cn) {
    return new ExpressionBreakpointSpecificClass(cn, Mode.SC_MODE_CREATED);
  }

  public ExpressionBreakpointSpecificClass getBreakpointObjectReleased (ClassName cn) {
    return new ExpressionBreakpointSpecificClass(cn, Mode.SC_MODE_RELEASED);
  }

  public ExpressionBreakpointSpecificClass getBreakpointExpecptionThrown (ClassName cn) {
    return new ExpressionBreakpointSpecificClass(cn, Mode.SC_MODE_EXCEPTION_THROWN);
  }

  public ExpressionBreakpointInstructionType getBreakpointInstructionType (InstructionType instType) {
    return new ExpressionBreakpointInstructionType(instType);
  }

  public ExpressionBreakpointGarbageCollection getBreakpointGarbageCollection (BreakPointModes bpMode) {
    assert (bpMode == BreakPointModes.BP_MODE_GC_BEGIN) || (bpMode == BreakPointModes.BP_MODE_GC_END) || (bpMode == BreakPointModes.BP_MODE_GC_BOTH);
    return new ExpressionBreakpointGarbageCollection(bpMode);
  }

  public ExpressionBreakpointChoiceGenerator getBreakpointChoiceGenerator (BreakPointModes bpMode) {
    assert (bpMode == BreakPointModes.BP_MODE_CHOICE_BOTH) || (bpMode == BreakPointModes.BP_MODE_CHOICE_DATA)
        || (bpMode == BreakPointModes.BP_MODE_CHOICE_SCHEDULING);
    return new ExpressionBreakpointChoiceGenerator(bpMode);
  }

  public ExpressionBreakpointThreadScheduled getBreakpointThreadScheduled (BreakPointModes bpMode, Integer threadNum) {
    assert (bpMode == BreakPointModes.BP_MODE_THREAD_SCHEDULED_BOTH) || (bpMode == BreakPointModes.BP_MODE_THREAD_SCHEDULED_IN)
        || (bpMode == BreakPointModes.BP_MODE_THREAD_SCHEDULED_OUT);
    return new ExpressionBreakpointThreadScheduled(bpMode, threadNum);
  }

  public ExpressionBreakpointFieldAccess getBreakpointFieldAccess (BreakPointModes bpMode, FieldName fn) {
    assert (bpMode == BreakPointModes.BP_MODE_FIELD_ACCESS) || (bpMode == BreakPointModes.BP_MODE_FIELD_ACCESS_READ)
        || (bpMode == BreakPointModes.BP_MODE_FIELD_ACCESS_WRITE);
    assert fn != null;
    return new ExpressionBreakpointFieldAccess(bpMode, fn);
  }

  public ExpressionBreakpointMethodInvoke getBreakpointMethodInvoke (BreakPointModes bpMode, MethodName mn) {
    assert (bpMode == BreakPointModes.BP_MODE_METHOD_INVOKE);
    assert mn != null;
    return new ExpressionBreakpointMethodInvoke(bpMode, mn);
  }

  public ExpressionStateThread getStateThread (Integer threadNum, ExpressionStateStackFrame sf) {
    return new ExpressionStateThread(threadNum, sf);
  }

  public ExpressionStateStackFrame getStateStackFrame (Integer stackFrameNum, ExpressionStateStackFrameSlot sfs) {
    return new ExpressionStateStackFrame(sfs, stackFrameNum);
  }

  public ExpressionStateStackFrame getStateStackFrameName (Integer stackFrameNum, ExpressionStateValueName name) {
    return new ExpressionStateStackFrame(name, stackFrameNum);
  }

  public ExpressionStateStackFrame getStateStackFrameThis (Integer stackFrameNum, ExpressionStateValueThis child) {
    return new ExpressionStateStackFrame(child, stackFrameNum);
  }

  public ExpressionStateStackFrame getStateStackFrameStatic (Integer stackFrameNum, ExpressionStateValueStatic child) {
    return new ExpressionStateStackFrame(child, stackFrameNum);
  }

  public ExpressionStateValueFieldIndex getStateValueFieldIndex (int fieldIndex, ExpressionStateValue child) {
    return new ExpressionStateValueFieldIndex(child, fieldIndex);
  }

  public ExpressionStateValueName getStateValueName (String varName, ExpressionStateValue child) {
    return new ExpressionStateValueName(child, varName);
  }

  public ExpressionStateValueOuterClass getStateValueOuterClass (ExpressionStateValue child) {
    return new ExpressionStateValueOuterClass(child);
  }

  public ExpressionStateStackFrameSlot getStateValueStackFrameSlot (int slotIndex, ExpressionStateValue child) {
    return new ExpressionStateStackFrameSlot(child, slotIndex);
  }

  public ExpressionStateValueStatic getStateValueStaticArea (Integer staticFieldIndex, ExpressionStateValue child) {
    return new ExpressionStateValueStatic(child, staticFieldIndex);
  }

  public ExpressionStateValueSuper getStateValueSuper (ExpressionStateValue child) {
    return new ExpressionStateValueSuper(child);
  }

  public ExpressionStateValueThis getStateValueThis (ExpressionStateValue child) {
    return new ExpressionStateValueThis(child);
  }

  public ExpressionStateValueArrayIndex getStateValueArrayIndex (ExpressionStateValue child, int elementIndex) {
    return new ExpressionStateValueArrayIndex(child, elementIndex);
  }

  public ExpressionStateHeap getStateHeap (int elementIndex, ExpressionStateValue child) {
    return new ExpressionStateHeap(child, elementIndex);
  }

  public ExpressionStateHeapEntryList getStateHeap (ClassName cn, ExpressionStateValue child) {
    return new ExpressionStateHeapEntryList(child, cn);
  }

  public ExpressionBreakpointOperatorAnd getBreakpointOperatorAnd (ExpressionBoolean left, ExpressionBoolean right) {
    return new ExpressionBreakpointOperatorAnd(left, right);
  }

  public ExpressionBreakpointOperatorOr getBreakpointOperatorOr (ExpressionBoolean left, ExpressionBoolean right) {
    return new ExpressionBreakpointOperatorOr(left, right);
  }

  /**
   * @throws GenericErrorRuntimeException Wrapped {@link JPFInspectorGenericErrorException}
   */
  public ExpressionBreakpointStepOut getBreakpointStepOut (int threadId, int stackDepth) throws GenericErrorRuntimeException {
    try {
      return ExpressionBreakpointStepOut.getStepOutToCaller(inspector, threadId, stackDepth);
    } catch (JPFInspectorGenericErrorException e) {
      // Rethrow as {@link RuntimeException} - have to be caught (in {@link ExpressionParser}).
      throw new GenericErrorRuntimeException(e);
    }
  }

  public ExpressionBreakpointStateAdvanced getBreakpointStateAdvanced () {
    return new ExpressionBreakpointStateAdvanced();
  }

  /**
   * @throws GenericErrorRuntimeException Wrapped {@link JPFInspectorGenericErrorException}
   */
  public ExpressionBreakpointSingleStep getBreakpointSingleStep (StepType stepType) throws GenericErrorRuntimeException {
    try {
      return ExpressionBreakpointSingleStep.createBreakpointSingleStep(inspector, stepType);
    } catch (JPFInspectorGenericErrorException e) {
      // Rethrow as {@link RuntimeException} - have to be caught (in {@link ExpressionParser}).
      throw new GenericErrorRuntimeException(e);
    }
  }

  public ExpressionBreakpointInstruction getBreakpointInstruction (int threadId, MethodName methodName, int instructionIndex, int hitCount)
      throws GenericErrorRuntimeException {
    try {
      Instruction inst = ExpressionBreakpointInstruction.findInstruction(inspector, methodName, instructionIndex);
      if (inst == null) {
        return null;
      }
      return new ExpressionBreakpointInstruction(threadId, inst, hitCount);
    } catch (JPFInspectorGenericErrorException e) {
      // Rethrow as {@link RuntimeException} - have to be caught (in {@link ExpressionParser}).
      throw new GenericErrorRuntimeException(e);
    }
  }

  public ExpressionStateStaticAreaEntryList getStateStaticArea (ClassName cn, ExpressionStateValue child) {
    return new ExpressionStateStaticAreaEntryList(child, cn);
  }

  public ExpressionStateValueConstBoolean getConstValueBoolean (boolean value) {
    return new ExpressionStateValueConstBoolean(value);
  }

  public ExpressionStateValueConstDouble getConstValueDouble (double value) {
    return new ExpressionStateValueConstDouble(value);
  }

  public ExpressionStateValueConstFloat getConstValueFloat (float value) {
    return new ExpressionStateValueConstFloat(value);
  }

  public ExpressionStateValueConstChar getConstValueChar (char value) {
    return new ExpressionStateValueConstChar(value);
  }

  public ExpressionStateValueConstInteger getConstValueInteger (int value) {
    return new ExpressionStateValueConstInteger(value);
  }

  public ExpressionStateValueConstLong getConstValueLong (long value) {
    return new ExpressionStateValueConstLong(value);
  }

  public ExpressionStateValueConstNull getConstValueNull () {
    return new ExpressionStateValueConstNull();
  }

  public ExpressionStateValueConstString getConstValueString (String value) {
    return new ExpressionStateValueConstString(value);
  }

  public ExpressionStateAssignment getStateAssignment (ExpressionStateRootNode<?> lValue, ExpressionStateRootNode<?> rValue) {
    return new ExpressionStateAssignment(lValue, rValue);
  }

  public ExpressionBreakpointCompare getBreakpointCompare (ExpressionStateRootNode<?> left, RelationOperator relOper, ExpressionStateRootNode<?> right) {
    return new ExpressionBreakpointCompare(left, right, relOper, inspector);
  }

  public RelationOperatorFactory getRelopFactory () {
    return relopFactory;
  }

  public ExpressionBreakpointAssert getBreakpointAssert (ExpressionBooleanInterface position, ExpressionBooleanInterface condition) {
    return new ExpressionBreakpointAssert(position, condition);
  }
}
