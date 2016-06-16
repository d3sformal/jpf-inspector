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

package gov.nasa.jpf.inspector.common.pse;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionStateStackFrame;
import gov.nasa.jpf.inspector.server.programstate.StateStackFrame;
import gov.nasa.jpf.inspector.utils.InstructionWrapper;
import gov.nasa.jpf.vm.StackFrame;

/**
 * Method on the stack (equivalent of the {@link StackFrame}).
 * Linked classes:
 * - {@link StateStackFrame}
 * - {@link ExpressionStateStackFrame})
 */
public class PSEMethod extends ProgramStateEntry {

  private static final long serialVersionUID = -7827046837686843489L;

  public static final String EXPRESSION_METHOD_KEY_WORD = "#stackFrame";

  private final InstructionWrapper inst; // Represents the call instruction

  // Note: Parameter names are stored in the MethodInfo (in class files)
  private PSEVariable[] refLocals;
  private final PSEVariableObject refThis;

  public PSEMethod(InstructionWrapper inst,
                   PSEVariable[] refLocals, PSEVariableObject refThis) {
    assert refLocals != null;

    this.inst = inst;
    this.refLocals = refLocals;
    this.refThis = refThis;
  }

  public InstructionWrapper getCallInstruction () {
    return inst;
  }

  /**
   * @return Gets representation of the instance (class in static case) on which current method operates
   */
  public PSEVariableObject getThis () throws JPFInspectorException {
    return refThis;
  }

  /**
   * Gets list with represents values stored on the method stack.
   * List is sorted according slot index (but not one-one mapping some types (long, double) occupy 2 slots.
   * (first is this, is present), parameters, local vars, "stack with computed expression in progress"
   * 
   * @return Gets list with represents values stored on the method stack.
   */
  public PSEVariable[] getLocals () throws JPFInspectorException {
    return refLocals;
  }

  @Override
  public <T> T visit (PSEVisitor<T> visitor) throws JPFInspectorException {
    return visitor.visitPSEMethod(this);
  }

}
