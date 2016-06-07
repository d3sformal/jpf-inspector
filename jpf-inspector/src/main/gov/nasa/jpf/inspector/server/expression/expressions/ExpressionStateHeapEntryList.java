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

package gov.nasa.jpf.inspector.server.expression.expressions;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNoVMConnected;
import gov.nasa.jpf.inspector.server.expression.ExpressionStateRootNode;
import gov.nasa.jpf.inspector.server.expression.ExpressionStateUnaryOperator;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.programstate.StateHeapEntryList;
import gov.nasa.jpf.inspector.utils.expressions.ClassName;
import gov.nasa.jpf.vm.VM;

public class ExpressionStateHeapEntryList extends ExpressionStateUnaryOperator<ExpressionStateValue> implements ExpressionStateRootNode<ExpressionStateValue> {

  public static final String TOKEN_HASH_HEAP = "#heap";

  private final ClassName heapItemsFilter;

  public ExpressionStateHeapEntryList (ExpressionStateValue child, ClassName heapItemsFilter) {
    super(child);

    assert heapItemsFilter != null;

    this.heapItemsFilter = heapItemsFilter;
  }

  @Override
  public StateHeapEntryList getResultExpression (JPFInspector inspector, InspectorState state) throws JPFInspectorException {

    VM vm = state.getVM();
    JPFInspectorNoVMConnected.checkVM(vm);

    StateHeapEntryList shel = new StateHeapEntryList(inspector, heapItemsFilter, vm, 1);

    return shel;
  }



  /* @see gov.nasa.jpf.inspector.server.expression.ExpressionNodeInterface#getNormalizedExpression() */
  @Override
  public String getNormalizedExpression () {
    // TOKEN_HASH_HEAP : '#heap' ;
    // TOKEN_HASH_HEAP WS? '[' WS? className WS? ']' WS? a=cmdStateExpressionValue[$expFactory]? { $expr = $expFactory.getStateHeap($className.cn, $a.expr); }

    return TOKEN_HASH_HEAP + '[' + heapItemsFilter.getClassName() + ']' + (child != null ? child.getNormalizedExpression() : "");
  }

}
