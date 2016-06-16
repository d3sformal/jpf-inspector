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

  /**
   * Returns a {@link StateElementInfo} that represents static properties of a class.
   *
   * @param sni This might be either a StackFrame or a value.
   * @param ci We want to get static information about this class.
   */
  public static StateElementInfo createStaticClass (StateNodeInterface sni, ClassInfo ci) {
    assert (ci != null);
    // I'm not sure this works....
    return new StateElementInfo(sni,
                                1,
                                ci.getStaticElementInfo(),
                                ci,
                                sni.getStateExpr() + '.' + StateValue.getSimpleName(ci));
    // I wonder.. shouldn't there be '#static' instead of 'StateValue.getSimpleName(ci)'?
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

  /**
   * Creates a new hierarchy-2 representation of a value.
   *
   * @param sni This is only used to get an instance of the {@link JPFInspector}.
   * @param referenceDepth I'm still not clear as to why this is useful.
   * @param ei The object we need to represent.
   * @param ci The class of the object -- although this might be a restriction or also something else, I'm not clear on that yet.
   * @param stateExpr An expression that, if evaluated, should result in the object we are currently creating.
   */
  private StateElementInfo(StateNodeInterface sni, int referenceDepth, ElementInfo ei, ClassInfo ci, String stateExpr) {
    super(sni, referenceDepth);
    setStateExpr(stateExpr);
    this.ei = ei;
    this.ci = ci;
  }

  @Override
  public ClassInfo getClassInfo () {
    return ci;
  }

  @Override
  public PSEVariable toHierarchy3() throws JPFInspectorException {
    final String varName = getStateExpr();
    final String definedIn = getStateExpr();

    return StateValue.createPSEVariable(this, varName, ei.getObjectRef(), definedIn);
  }

  @Override
  public Object getValue () {
    return ei;
  }

  @Override
  public boolean isReference () {
    return true;
  }

  /**
   * Returns the {@link ElementInfo} object or null if it is null.
   */
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

  @Override
  public StateReadableValueInterface createSuper () throws JPFInspectorNoSuperClassException {
    ClassInfo superClassInfo = ci.getSuperClass();
    if (superClassInfo == null) {
      throw new JPFInspectorNoSuperClassException(ci);
    }

    return new StateElementInfo(this, getReferenceDepth(), ei, superClassInfo, getStateExpr() + '.' + PSEVariable.EXPRESSION_SUPER);
  }

  @Override
  public StateReadableValueInterface createPredecessorClass (ClassInfo ci) throws JPFInspectorNotSuperClassException {
    if (!StateValue.isPredecessor(ci, this.ci)) {
      throw new JPFInspectorNotSuperClassException(ci, this.ci);
    }
    return new StateElementInfo(this, getReferenceDepth(), ei, ci, getStateExpr() + '.' + StateValue.getSimpleName(ci));
  }

  @Override
  public StateReadableValueInterface createThisValue () throws JPFInspectorException {
    if (StateValue.isStaticElementInfo(ei)) {
      throw new JPFInspectorNotInstanceException(ci);
    }
    return new StateElementInfo(this, getReferenceDepth(), ei, ei.getClassInfo(), getStateExpr() + '.' + PSEVariable.EXPRESSION_VARIABLE_THIS);
  }

}
