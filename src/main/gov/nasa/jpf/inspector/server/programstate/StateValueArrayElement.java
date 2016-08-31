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
import gov.nasa.jpf.inspector.exceptions.JPFInspectorArrayIndexOutOutRangeException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNoSuperClassException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNotArrayException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNotInstanceException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNotSuperClassException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNullValueException;
import gov.nasa.jpf.inspector.server.attributes.AttributesManager;
import gov.nasa.jpf.inspector.server.attributes.attachments.ArrayElementAttachment;
import gov.nasa.jpf.inspector.server.attributes.attachments.AttributeAttachment;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.common.pse.PSEVariable;
import gov.nasa.jpf.vm.ArrayFields;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.Fields;
import gov.nasa.jpf.vm.Heap;

/**
 * Represents an element of an array in the second hierarchy.
 */
public final class StateValueArrayElement extends StateWritableValue {

  /**
   * Index of the represented element in the array
   */
  private final int index;
  /**
   * Representation of the array
   */
  private final ElementInfo ei;

  /**
   * ClassInfo of the whole array (not the array entry)
   */
  private final ClassInfo arrayCi;

  public static StateValueArrayElement createArrayElement (StateReadableValue srvi, int elementIndex) throws JPFInspectorException {
     return createArrayElement(srvi, elementIndex, true);
  }
  public static StateValueArrayElement createArrayElement (StateReadableValue srvi, int elementIndex,
                                                           boolean expandMembers) throws JPFInspectorException {
    assert (srvi != null);

    ClassInfo arrayCi = srvi.getClassInfo();
    if (!arrayCi.isArray()) {
      throw new JPFInspectorNotArrayException(arrayCi);
    }

    ElementInfo ei = srvi.getReferenceValue();
    if (ei == null) {
      throw new JPFInspectorNullValueException(elementIndex, arrayCi);
    }

    // Check array length
    final Fields fields = ei.getFields();
    assert (fields instanceof ArrayFields);

    final ArrayFields afields = (ArrayFields) fields;

    final int arrayLen = afields.arrayLength();

    if (elementIndex < 0 || elementIndex >= arrayLen) {
      throw new JPFInspectorArrayIndexOutOutRangeException(elementIndex, arrayLen, arrayCi);
    }

    return new StateValueArrayElement(srvi, expandMembers, arrayCi.getComponentClassInfo(), srvi.getStateExpr() + '[' + elementIndex + ']', elementIndex, ei,
        arrayCi);
  }

  private StateValueArrayElement(StateNodeInterface sni, boolean expandMembers, ClassInfo ci, String stateExpression, int index, ElementInfo ei,
                                 ClassInfo arrayCi) {
    super(sni, expandMembers, ci, stateExpression);

    this.index = index;
    this.ei = ei;
    this.arrayCi = arrayCi;
  }

  /**
   * Creates different(restricted) view on the represented value.
   * 
   * @throws JPFInspectorNotSuperClassException
   */
  private StateValueArrayElement(StateValueArrayElement me, ClassInfo superClassInfo, String stateExpression) throws JPFInspectorNotSuperClassException {
    super(me, superClassInfo, stateExpression);

    this.ei = me.ei;
    this.index = me.index;
    this.arrayCi = me.arrayCi;
  }

  @Override
  public ElementInfo getReferenceValue () {
    return getReferenceValueImpl(ei.getReferenceElement(index));
  }

  @Override
  public Object getValue () {
    String sig = ci.getSignature();

    switch (sig.charAt(0)) {
    case 'Z':
      return ei.getBooleanElement(index);
    case 'B':
      return ei.getByteElement(index);
    case 'C':
      return ei.getCharElement(index);
    case 'S':
      return ei.getShortElement(index);
    case 'I':
      return ei.getIntElement(index);
    case 'J':
      return ei.getLongElement(index);
    case 'F':
      return ei.getFloatElement(index);
    case 'D':
      return ei.getDoubleElement(index);
    default: // reference Array of Object
      int ref = ei.getReferenceElement(index);
      InspectorState inspState = getInspector().getStopHolder().getInspectorState();
      Heap heap = inspState.getVM().getHeap();
      return heap.get(ref);
    }
  }

  @Override
  public PSEVariable toHierarchy3(AttributesManager attributeManager) throws JPFInspectorException {
    final String varName = "[" + index + "]";
    final String definedIn = StateWritableValue.getSimpleName(arrayCi);

    return StateReadableValue.createPSEVariable(this, varName, index, definedIn,
                                                attributeManager.getAttachmentAttributes(ei, index),attributeManager );
  }

  @Override
  public StateValueArrayElement createSuper () throws JPFInspectorException {
    ClassInfo superClassInfo = ci.getSuperClass();
    if (superClassInfo == null) {
      throw new JPFInspectorNoSuperClassException(ci);
    }
    return new StateValueArrayElement(this, superClassInfo, getStateExpr() + '.' + PSEVariable.EXPRESSION_SUPER);
  }

  @Override
  public StateValueArrayElement createPredecessorClass (ClassInfo ci) throws JPFInspectorNotSuperClassException {
    return new StateValueArrayElement(this, ci, getStateExpr() + '.' + StateWritableValue.getSimpleName(ci));
  }

  @Override
  public StateReadableValue createThisValue () throws JPFInspectorException {
    if (ci.isArray() || ci.isPrimitive()) {
      throw new JPFInspectorNotInstanceException(ci);
    }

    return new StateValueArrayElement(this, ci, getStateExpr() + "." + PSEVariable.EXPRESSION_VARIABLE_THIS);
  }

  // *************************************************************************
  // ** Modify represented value infrastructure
  // *************************************************************************

  @Override
  protected void assignValueBoolean (boolean newVal) {
    ei.getModifiableInstance().setBooleanElement(index, newVal);
  }

  @Override
  protected void assignValueChar (char newVal) {
    ei.getModifiableInstance().setCharElement(index, newVal);
  }

  @Override
  protected void assignValueByte (byte newVal) {
    ei.getModifiableInstance().setByteElement(index, newVal);
  }

  @Override
  protected void assignValueShort (short newVal) {
    ei.getModifiableInstance().setShortElement(index, newVal);
  }

  @Override
  protected void assignValueInt (int newVal) {
    ei.getModifiableInstance().setIntElement(index, newVal);
  }

  @Override
  protected void assignValueLong (long newVal) {
    ei.getModifiableInstance().setLongElement(index, newVal);
  }

  @Override
  protected void assignValueFloat (float newVal) {
    ei.getModifiableInstance().setFloatElement(index, newVal);
  }

  @Override
  protected void assignValueDouble (double newVal) {
    ei.getModifiableInstance().setDoubleElement(index, newVal);
  }

  @Override
  protected void assignValueRef (int newValRef) {
    ei.getModifiableInstance().setReferenceElement(index, newValRef);
  }

  @Override
  public AttributeAttachment getAttributeAttachment() {
    return new ArrayElementAttachment(this.ei, this.index);
  }

}
