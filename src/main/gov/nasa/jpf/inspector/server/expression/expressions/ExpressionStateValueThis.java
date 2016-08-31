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
import gov.nasa.jpf.inspector.server.programstate.StateNodeInterface;
import gov.nasa.jpf.inspector.server.programstate.StateReadableValue;
import gov.nasa.jpf.inspector.server.programstate.StateStackFrame;
import gov.nasa.jpf.inspector.server.programstate.StateValueStackSlot;

/**
 * @author Alf
 * 
 */
public class ExpressionStateValueThis extends ExpressionStateValue {

  private static final String TOKEN_HASH_THIS = "#this";

  public ExpressionStateValueThis (ExpressionStateValue child) {
    super(child);
  }

  @Override
  public StateReadableValue toHierarchy2(StateReadableValue parent) throws JPFInspectorException {
    assert (parent != null);

    StateReadableValue srvi = parent.createThisValue();

    ExpressionStateValue child = getChild();
    if (child == null) {
      return srvi;
    } else {
      return child.toHierarchy2(srvi);
    }
  }

  public StateNodeInterface getExpressionFromStackFrame(StateStackFrame parentMethod) throws JPFInspectorException {
    // We should get the instance of the class that runs the method associated with the stack frame in
    // the parameter.

    // I don't get this:
    //
    // "This" is stored in slot 0, however it is guaranteed only when method is called
    // (not during method execution)
    // There is not a better solution if "this" should be an assignable value :-(
    // otherwise StateElementInfo from the sf.getThis() can be created
    StateValueStackSlot svss = StateValueStackSlot.createThisSlotValue(parentMethod);

    ExpressionStateValue child = getChild();
    if (child == null) {
      return svss;
    } else {
      return child.toHierarchy2(svss);
    }
  }

  @Override
  public String getNormalizedExpression () {
    // TOKEN_HASH_THIS : '#this' ;
    // TOKEN_HASH_THIS WS? a=cmdStateExpressionValue[$expFactory]? { $expr = $expFactory.getStateValueThis($a.expr); }

    return '.' + TOKEN_HASH_THIS + (getChild() != null ? getChild().getNormalizedExpression() : "");
  }
}
