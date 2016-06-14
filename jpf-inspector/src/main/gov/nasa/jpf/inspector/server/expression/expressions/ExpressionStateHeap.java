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
import gov.nasa.jpf.inspector.server.programstate.StateElementInfo;
import gov.nasa.jpf.inspector.server.programstate.StateNodeInterface;
import gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface;
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
  public StateReadableValueInterface getResultExpression (JPFInspector inspector, InspectorState state) throws JPFInspectorException {
    assert state != null;

    VM vm = state.getVM();
    JPFInspectorNoVMConnected.checkVM(vm);

    ExpressionStateValue child = getChild();
    int referenceDepth = child != null ? 0 : 1;

    StateElementInfo self =
            StateElementInfo.createElementInfoRepresentation(inspector, vm.getHeap(), heapElementIndex, referenceDepth);

    if (child == null) {
      return self;
    } else {
      return child.getResultExpression(self);
    }
  }

  @Override
  public String getNormalizedExpression () {
    return ExpressionStateHeapEntryList.TOKEN_HASH_HEAP + '[' + heapElementIndex + ']' +
            (child != null ? child.getNormalizedExpression() : "");
  }
}
