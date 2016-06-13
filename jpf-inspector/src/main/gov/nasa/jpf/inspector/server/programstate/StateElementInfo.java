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

package gov.nasa.jpf.inspector.server.programstate;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorInvalidHeapReferenceException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNoSuperClassException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNotInnerClassException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNotInstanceException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNotSuperClassException;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.common.pse.PSEVariable;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.Heap;

/**
 * @author Alf
 * 
 */
public class StateElementInfo extends StateNode implements StateReadableValueInterface {

  private final ElementInfo ei;
  private final ClassInfo ci; // Has to be supper type of the ei type (restrict visible fields) (or visible type of array elements)

  public static StateElementInfo createElementInfoRepresentation (JPFInspector inspector, Heap heap, int heapElementIndex, int referenceDepth)
      throws JPFInspectorException {
    ElementInfo ei = heap.get(heapElementIndex);

    if (ei == null) {
      throw new JPFInspectorInvalidHeapReferenceException(heapElementIndex);
    }
    return new StateElementInfo(inspector, referenceDepth, ei, ei.getClassInfo(), PSEVariable.EXPRESSION_VARIABLE_HEAP + '[' + ei.getObjectRef() + ']');
  }

  public static StateElementInfo createElementInfoRepresentation (StateHeapEntryList stateHeapEntryList, ElementInfo ei) {
    return createElementInfoRepresentation(stateHeapEntryList, ei, 1);
  }

  private static StateElementInfo createElementInfoRepresentation(StateHeapEntryList stateHeapEntryList, ElementInfo ei, int referenceDepth) {
    assert (ei != null);
    return new StateElementInfo(stateHeapEntryList, referenceDepth, ei, ei.getClassInfo(), PSEVariable.EXPRESSION_VARIABLE_HEAP + '[' + ei.getObjectRef() + ']');
  }

  public static StateElementInfo createStaticClass (StateNodeInterface sni, ClassInfo ci) {
    assert (ci != null);
    return new StateElementInfo(sni, 1, ci.getStaticElementInfo(), ci, sni.getStateExpr() + '.' + StateValue.getSimpleName(ci));
  }

  // Does not modify state expression
  public static StateElementInfo createStaticClassHidden (StateNodeInterface sni, ClassInfo ci) {
    assert (ci != null);
    return new StateElementInfo(sni, 1, ci.getStaticElementInfo(), ci, sni.getStateExpr());
  }

  public static StateElementInfo createStaticOuterClass (StateReadableValueInterface srvi) throws JPFInspectorException {
    assert (srvi != null);
    ClassInfo ci = srvi.getClassInfo();

    ClassInfo ciOuter = ci.getEnclosingClassInfo();
    if (ciOuter == null) {
      throw new JPFInspectorNotInnerClassException(ci);
    }

    return new StateElementInfo(srvi, srvi.getReferenceDepth(), ciOuter.getStaticElementInfo(), ciOuter, srvi.getStateExpr() + '.'
        + PSEVariable.EXPRESSION_OUTER_CLASS);

  }

  private StateElementInfo(JPFInspector inspector, int referenceDepth, ElementInfo ei, ClassInfo ci, String stateExpr) {
    super(inspector, referenceDepth);
    setStateExpr(stateExpr);
    this.ei = ei;
    this.ci = ci;
  }

  private StateElementInfo(StateNodeInterface sni, int referenceDepth, ElementInfo ei, ClassInfo ci, String stateExpr) {
    super(sni, referenceDepth);
    setStateExpr(stateExpr);
    this.ei = ei;
    this.ci = ci;
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#getClassInfo() */
  @Override
  public ClassInfo getClassInfo () {
    return ci;
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateNode#getResultExpression() */
  @Override
  public PSEVariable toHierarchy3(String name, int clientID) throws JPFInspectorException {
    final String varName = getStateExpr();
    final String definedIn = getStateExpr();

    return StateValue.createPSEVariable(this, name, clientID, varName, ei.getObjectRef(), definedIn);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#getValue() */
  @Override
  public Object getValue () {
    return ei;
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#isReference() */
  @Override
  public boolean isReference () {
    return true;
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#getReferenceValue() */
  @Override
  public ElementInfo getReferenceValue () {
    return ei;
  }

  /**
   * @param ci Class name whose predecessor to found
   * @param name Name of the predecessor
   * @return Gets ClassInfo of the predecessor which match given name (ignoring packages) or null if no such predecessor exists
   */
  public static ClassInfo isPredecessorTypeName (ClassInfo ci, String name) {
    assert (ci != null);
    assert (name != null);

    while (ci != null) {
      String className = StateValue.getSimpleName(ci);
      // Check if predecessor
      if (name.equals(className)) {
        return ci;
      }
      ci = ci.getSuperClass();
    }

    return null;
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#createSuper() */
  @Override
  public StateReadableValueInterface createSuper () throws JPFInspectorNoSuperClassException {
    ClassInfo superClassInfo = ci.getSuperClass();
    if (superClassInfo == null) {
      throw new JPFInspectorNoSuperClassException(ci);
    }

    return new StateElementInfo(this, getReferenceDepth(), ei, superClassInfo, getStateExpr() + '.' + PSEVariable.EXPRESSION_SUPER);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#createPredecessorClass(gov.nasa.jpf.jvm.ClassInfo) */
  @Override
  public StateReadableValueInterface createPredecessorClass (ClassInfo ci) throws JPFInspectorNotSuperClassException {
    if (!StateValue.isPredecessor(ci, this.ci)) {
      throw new JPFInspectorNotSuperClassException(ci, this.ci);
    }
    return new StateElementInfo(this, getReferenceDepth(), ei, ci, getStateExpr() + '.' + StateValue.getSimpleName(ci));
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#createThisValue() */
  @Override
  public StateReadableValueInterface createThisValue () throws JPFInspectorException {
    if (StateValue.isStaticElementInfo(ei)) {
      throw new JPFInspectorNotInstanceException(ci);
    }
    return new StateElementInfo(this, getReferenceDepth(), ei, ei.getClassInfo(), getStateExpr() + '.' + PSEVariable.EXPRESSION_VARIABLE_THIS);
  }

}
