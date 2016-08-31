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
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.programstate.StateHeapEntryList;
import gov.nasa.jpf.inspector.utils.expressions.ClassName;
import gov.nasa.jpf.vm.VM;

/**
 * Represents a heap entry list returned by the expression "#heap[*]" or "#heap[class-name-filter]".
 */
public class ExpressionStateHeapEntryList implements ExpressionStateRootNode {

  public static final String TOKEN_HASH_HEAP = "#heap";

  private final ClassName heapItemsFilter;

  public ExpressionStateHeapEntryList (ClassName heapItemsFilter) {
    assert heapItemsFilter != null;

    this.heapItemsFilter = heapItemsFilter;
  }

  @Override
  public StateHeapEntryList getResultExpression (JPFInspector inspector, InspectorState state) throws JPFInspectorException {

    VM vm = state.getVM();
    JPFInspectorNoVMConnected.checkVM(vm);

    StateHeapEntryList shel = new StateHeapEntryList(inspector, heapItemsFilter, vm);

    return shel;
  }

  @Override
  public String getNormalizedExpression () {
    return TOKEN_HASH_HEAP + '[' + heapItemsFilter.getClassName() + ']';
  }

}
