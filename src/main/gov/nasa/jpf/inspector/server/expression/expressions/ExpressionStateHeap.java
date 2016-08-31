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
import gov.nasa.jpf.inspector.server.programstate.StateElementInfo;
import gov.nasa.jpf.inspector.server.programstate.StateReadableValue;
import gov.nasa.jpf.vm.VM;

/**
 * Represents expression #heap[int] that returns a Java object on the heap.
 */
public class ExpressionStateHeap extends ExpressionStateUnaryOperator<ExpressionStateValue> implements ExpressionStateRootNode {

  private final int heapElementIndex;

  public ExpressionStateHeap (ExpressionStateValue child, int heapElementIndex) {
    super(child);

    this.heapElementIndex = heapElementIndex;
  }

  @Override
  public StateReadableValue getResultExpression (JPFInspector inspector, InspectorState state) throws JPFInspectorException {
    assert state != null;

    VM vm = state.getVM();
    JPFInspectorNoVMConnected.checkVM(vm);

    ExpressionStateValue child = getChild();

    StateElementInfo self =
            StateElementInfo.createFromHeapIndex(inspector, vm.getHeap(), heapElementIndex);

    if (child == null) {
      return self;
    } else {
      return child.toHierarchy2(self);
    }
  }

  @Override
  public String getNormalizedExpression () {
    return ExpressionStateHeapEntryList.TOKEN_HASH_HEAP + '[' + heapElementIndex + ']' +
            (getChild() != null ? getChild().getNormalizedExpression() : "");
  }
}
