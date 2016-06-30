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

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNotStackException;
import gov.nasa.jpf.inspector.server.programstate.StateReadableValue;
import gov.nasa.jpf.inspector.server.programstate.StateStackFrame;
import gov.nasa.jpf.inspector.server.programstate.StateValueStackSlot;

/**
 * Represents the ".#stackSlot[index]" expression as an integer index.
 */
public class ExpressionStateStackFrameSlot extends ExpressionStateValue {

  private static final String TOKEN_HASH_STACK_SLOT = "#stackSlot";

  private final int slotIndex;

  public ExpressionStateStackFrameSlot (ExpressionStateValue child, int slotIndex) {
    super(child);

    this.slotIndex = slotIndex;
  }

  public StateReadableValue getExpressionFromStackFrame(StateStackFrame ssf) throws JPFInspectorException {
    assert (ssf != null);

    StateValueStackSlot svss = StateValueStackSlot.createSlotFromIndex(ssf, slotIndex);

    if (getChild() == null) {
      return svss;
    } else {
      return getChild().toHierarchy2(svss);
    }

  }


  @Override
  public StateReadableValue toHierarchy2(StateReadableValue parent) throws JPFInspectorException {
    // Not supported operation ... state only StackFrame have slots
    // The grammar itself prevents a stack-slot-expression from following anything but a stack-frame, therefore
    // code should be unable to call this method.
    assert false;
    throw new JPFInspectorNotStackException();
  }

  @Override
  public String getNormalizedExpression () {
    return '.' + TOKEN_HASH_STACK_SLOT + '[' + slotIndex + ']' + (getChild() != null ? getChild().getNormalizedExpression() : "");
  }
}
