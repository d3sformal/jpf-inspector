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
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNoVMConnected;
import gov.nasa.jpf.inspector.server.expression.ExpressionStateRootNode;
import gov.nasa.jpf.inspector.server.expression.ExpressionStateUnaryOperator;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.programstate.StateNodeInterface;
import gov.nasa.jpf.inspector.server.programstate.StateThreadInfo;
import gov.nasa.jpf.vm.VM;

/**
 * Represents the "#thread" and "#thread[i]" expressions.
 */
public class ExpressionStateThread extends ExpressionStateUnaryOperator<ExpressionStateStackFrame> implements
    ExpressionStateRootNode {

  private static final String TOKEN_HASH_THREAD = "#thread";

  private final Integer threadNum;

  /**
   * 
   * @param threadNum For which thread we should be uses in the expression
   * @param child Follower in the expression
   */
  public ExpressionStateThread (Integer threadNum, ExpressionStateStackFrame child) {
    super(child);

    this.threadNum = threadNum;
  }

  @Override
  public StateNodeInterface getResultExpression (JPFInspector inspector, InspectorState state) throws JPFInspectorException {
    assert state != null;

    VM vm = state.getVM();
    JPFInspectorNoVMConnected.checkVM(vm);

    StateThreadInfo sti = new StateThreadInfo(inspector, vm, threadNum);

    ExpressionStateStackFrame child = getChild();
    if (child == null) {
      return sti;
    } else {
      return child.getResultExpression(sti);
    }
  }


  @Override
  public String getNormalizedExpression () {
    String childExp = "";
    if (getChild() != null) {
      childExp = getChild().getNormalizedExpression();
    }

    if (threadNum == null) {
      return TOKEN_HASH_THREAD + childExp;
    }
    return TOKEN_HASH_THREAD + '[' + threadNum + ']' + childExp;
  }
}
