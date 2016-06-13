//
// Copyright (C) 2011 United States Government as represented by the
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

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNotStackException;
import gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface;
import gov.nasa.jpf.inspector.server.programstate.StateStackFrame;
import gov.nasa.jpf.inspector.server.programstate.StateValueStackSlot;

/**
 * @author Alf
 * 
 */
public class ExpressionStateStackFrameSlot extends ExpressionStateValue {

  private static final String TOKEN_HASH_STACK_SLOT = "#stackSlot";

  private final int slotIndex;

  public ExpressionStateStackFrameSlot (ExpressionStateValue child, int slotIndex) {
    super(child);

    this.slotIndex = slotIndex;
  }

  public StateReadableValueInterface getResultExpression (StateStackFrame ssf) throws JPFInspectorException {
    assert (ssf != null);

    StateValueStackSlot svss = StateValueStackSlot.createSlotFromIndex(ssf, slotIndex);

    if (child == null) {
      return svss;
    } else {
      return child.getResultExpression(svss);
    }

  }

  /*
   * @see
   * gov.nasa.jpf.inspector.server.expression.expressions.ExpressionStateValue#getResultExpression(gov.nasa.jpf.inspector.server.programstate.
   * StateReadableValueInterface
   * )
   */
  @Override
  public StateReadableValueInterface getResultExpression (StateReadableValueInterface parent) throws JPFInspectorException {
    // Not supported operation ... state only StackFrame have slots
    throw new JPFInspectorNotStackException();
  }

  /* @see gov.nasa.jpf.inspector.server.expression.ExpressionNodeInterface#getNormalizedExpression() */
  @Override
  public String getNormalizedExpression () {
    // TOKEN_HASH_STACK_SLOT : '#stackSlot' ;
    return '.' + TOKEN_HASH_STACK_SLOT + '[' + slotIndex + ']' + (child != null ? child.getNormalizedExpression() : "");
  }
}
