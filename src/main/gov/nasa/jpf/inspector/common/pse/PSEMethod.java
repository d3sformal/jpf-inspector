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
  public PSEVariableObject getThis () {
    return refThis;
  }

  /**
   * Gets list with represents values stored on the method stack.
   * List is sorted according slot index (but not one-one mapping some types (long, double) occupy 2 slots.
   * (first is this, is present), parameters, local vars, "stack with computed expression in progress"
   * 
   * @return Gets list with represents values stored on the method stack.
   */
  public PSEVariable[] getLocals () {
    return refLocals;
  }

  @Override
  public <T> T visit (PSEVisitor<T> visitor) throws JPFInspectorException {
    return visitor.visitPSEMethod(this);
  }

}
