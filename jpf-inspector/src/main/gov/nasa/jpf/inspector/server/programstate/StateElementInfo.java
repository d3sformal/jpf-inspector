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
 * Represents a reference object.
 */
public final class StateElementInfo extends StateReadableValue {

  /**
   * JPF view of the represented object.
   */
  private final ElementInfo ei;
  /**
   * Has to be supper type of the ei type (restrict visible fields) (or visible type of array elements)
   */
  private final ClassInfo ci;

  public static StateElementInfo createFromHeapIndex(JPFInspector inspector,
                                                     Heap heap,
                                                     int heapElementIndex
                                                     )
      throws JPFInspectorException {

    ElementInfo ei = heap.get(heapElementIndex);

    if (ei == null) {
      throw new JPFInspectorInvalidHeapReferenceException(heapElementIndex);
    }
    return new StateElementInfo(inspector,
                                true,
                                ei,
                                ei.getClassInfo(),
                                PSEVariable.EXPRESSION_VARIABLE_HEAP + '[' + ei.getObjectRef() + ']');
  }

  public static StateElementInfo createElementInfoRepresentation (StateHeapEntryList stateHeapEntryList,
                                                                  ElementInfo ei) {
    return createElementInfoRepresentation(stateHeapEntryList, ei, true);
  }

  private static StateElementInfo createElementInfoRepresentation(StateHeapEntryList stateHeapEntryList,
                                                                  ElementInfo ei,
                                                                  boolean expandMembers) {
    assert (ei != null);
    return new StateElementInfo(stateHeapEntryList.getInspector(), expandMembers, ei, ei.getClassInfo(), PSEVariable.EXPRESSION_VARIABLE_HEAP + '[' + ei.getObjectRef() + ']');
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
    return new StateElementInfo(sni.getInspector(),
                                true,
                                ci.getStaticElementInfo(),
                                ci,
                                sni.getStateExpr() + '.' + StateWritableValue.getSimpleName(ci));
    // I wonder.. shouldn't there be '#static' instead of 'StateWritableValue.getSimpleName(ci)'?
  }

  public static StateElementInfo createStaticOuterClass (StateReadableValue srvi) throws JPFInspectorException {
    assert (srvi != null);
    ClassInfo ci = srvi.getClassInfo();

    ClassInfo ciOuter = ci.getEnclosingClassInfo();
    if (ciOuter == null) {
      throw new JPFInspectorNotInnerClassException(ci);
    }

    return new StateElementInfo(srvi.getInspector(), srvi.shouldExpandMembers(), ciOuter.getStaticElementInfo(), ciOuter, srvi.getStateExpr() + '.'
        + PSEVariable.EXPRESSION_OUTER_CLASS);

  }

  /**
   * Creates a new hierarchy-2 representation of an object.
   *
   * @param inspector The JPF Inspector server.
   * @param expandMembers If this element info is printed out, this will determine whether its fields, too.
   * @param ei The object we need to represent.
   * @param ci The class of the object -- although this might be a restriction or also something else, I'm not clear on that yet.
   * @param stateExpr An expression that, if evaluated, should result in the object we are currently creating.
   */
  private StateElementInfo(JPFInspector inspector, boolean expandMembers, ElementInfo ei, ClassInfo ci, String stateExpr) {
    super(inspector, expandMembers);
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

    return StateReadableValue.createPSEVariable(this, varName, ei.getObjectRef(), definedIn);
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
      String className = StateWritableValue.getSimpleName(ci);
      // Check if predecessor
      if (name.equals(className)) {
        return ci;
      }
      ci = ci.getSuperClass();
    }

    return null;
  }

  @Override
  public StateReadableValue createSuper () throws JPFInspectorNoSuperClassException {
    ClassInfo superClassInfo = ci.getSuperClass();
    if (superClassInfo == null) {
      throw new JPFInspectorNoSuperClassException(ci);
    }

    return new StateElementInfo(this.getInspector(), shouldExpandMembers(), ei, superClassInfo, getStateExpr() + '.' + PSEVariable.EXPRESSION_SUPER);
  }

  @Override
  public StateReadableValue createPredecessorClass (ClassInfo ci) throws JPFInspectorNotSuperClassException {
    if (!StateWritableValue.isPredecessor(ci, this.ci)) {
      throw new JPFInspectorNotSuperClassException(ci, this.ci);
    }
    return new StateElementInfo(this.getInspector(), shouldExpandMembers(), ei, ci, getStateExpr() + '.' + StateWritableValue.getSimpleName(ci));
  }

  @Override
  public StateReadableValue createThisValue () throws JPFInspectorException {
    if (StateWritableValue.isStaticElementInfo(ei)) {
      throw new JPFInspectorNotInstanceException(ci);
    }
    return new StateElementInfo(this.getInspector(), shouldExpandMembers(), ei, ei.getClassInfo(), getStateExpr() + '.' + PSEVariable.EXPRESSION_VARIABLE_THIS);
  }

}
