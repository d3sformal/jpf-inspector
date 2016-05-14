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

import gov.nasa.jpf.inspector.interfaces.JPFInspectorException;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorArrayIndexOutOutRangeException;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorNoSuperClassException;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorNotArrayException;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorNotInstanceException;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorNotSuperClassException;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorNullValueException;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.programstate.client.PSEVariable;
import gov.nasa.jpf.jvm.ArrayFields;
import gov.nasa.jpf.jvm.ClassInfo;
import gov.nasa.jpf.jvm.ElementInfo;
import gov.nasa.jpf.jvm.Fields;
import gov.nasa.jpf.jvm.Heap;

/**
 * @author Alf
 * 
 */
public class StateValueArrayElement extends StateValue {

  protected final int index; // Index of the represented element in the array
  protected final ElementInfo ei; // Representation of the array

  protected final ClassInfo arrayCi; // ClassInfo of the whole array (not the array entry)

  public static StateValueArrayElement createArrayElement (StateReadableValueInterface srvi, int elementIndex, int referenceDepth) throws JPFInspectorException {
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

    return new StateValueArrayElement(srvi, referenceDepth, arrayCi.getComponentClassInfo(), srvi.getStateExpr() + '[' + elementIndex + ']', elementIndex, ei,
        arrayCi);
  }

  protected StateValueArrayElement (StateNodeInterface sni, int referenceDepth, ClassInfo ci, String stateExpression, int index, ElementInfo ei,
      ClassInfo arrayCi) {
    super(sni, referenceDepth, ci, stateExpression);

    this.index = index;
    this.ei = ei;
    this.arrayCi = arrayCi;
  }

  /**
   * Creates different(restricted) view on the represented value.
   * 
   * @throws JPFInspectorNotSuperClassException
   */
  protected StateValueArrayElement (StateValueArrayElement me, ClassInfo superClassInfo, String stateExpression) throws JPFInspectorNotSuperClassException {
    super(me, superClassInfo, stateExpression);

    this.ei = me.ei;
    this.index = me.index;
    this.arrayCi = me.arrayCi;
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#getReferenceValue() */
  @Override
  public ElementInfo getReferenceValue () {
    return getReferenceValueImpl(ei.getReferenceElement(index));
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#getValue() */
  @Override
  public Object getValue () {
    String sig = ci.getSignature();

    switch (sig.charAt(0)) {
    case 'Z':
      return Boolean.valueOf(ei.getBooleanElement(index));
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
      Heap heap = inspState.getJVM().getHeap();
      return heap.get(ref);
    }
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateNode#getResultExpression(java.lang.String, int) */
  @Override
  public PSEVariable getResultExpression (String name, int clientID) throws JPFInspectorException {
    final String varName = "[" + index + "]";
    final String definedIn = StateValue.getSimpleName(arrayCi);

    return StateValue.createPSEVariable(this, name, clientID, varName, index, definedIn);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#createSuper() */
  @Override
  public StateValueArrayElement createSuper () throws JPFInspectorException {
    ClassInfo superClassInfo = ci.getSuperClass();
    if (superClassInfo == null) {
      throw new JPFInspectorNoSuperClassException(ci);
    }
    return new StateValueArrayElement(this, superClassInfo, getStateExpr() + '.' + PSEVariable.EXPRESSION_SUPER);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#createPredecessorClass(gov.nasa.jpf.jvm.ClassInfo) */
  @Override
  public StateValueArrayElement createPredecessorClass (ClassInfo ci) throws JPFInspectorNotSuperClassException {
    return new StateValueArrayElement(this, ci, getStateExpr() + '.' + StateValue.getSimpleName(ci));
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#createThisValue() */
  @Override
  public StateReadableValueInterface createThisValue () throws JPFInspectorException {
    if (ci.isArray() || ci.isPrimitive()) {
      throw new JPFInspectorNotInstanceException(ci);
    }

    return new StateValueArrayElement(this, ci, getStateExpr() + "." + PSEVariable.EXPRESSION_VARIABLE_THIS);
  }

  // *************************************************************************
  // ** Modify represented value infrastructure
  // *************************************************************************

  /* @see gov.nasa.jpf.inspector.server.programstate.StateValue#assignValueBoolean(boolean) */
  @Override
  protected void assignValueBoolean (boolean newVal) {
    ei.setBooleanElement(index, newVal);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateValue#assignValueChar(char) */
  @Override
  protected void assignValueChar (char newVal) {
    ei.setCharElement(index, newVal);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateValue#assignValueByte(byte) */
  @Override
  protected void assignValueByte (byte newVal) {
    ei.setByteElement(index, newVal);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateValue#assignValueShort(short) */
  @Override
  protected void assignValueShort (short newVal) {
    ei.setShortElement(index, newVal);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateValue#assignValueInt(int) */
  @Override
  protected void assignValueInt (int newVal) {
    ei.setIntElement(index, newVal);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateValue#assignValueLong(long) */
  @Override
  protected void assignValueLong (long newVal) {
    ei.setLongElement(index, newVal);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateValue#assignValueFloat(float) */
  @Override
  protected void assignValueFloat (float newVal) {
    ei.setFloatElement(index, newVal);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateValue#assignValueDouble(double) */
  @Override
  protected void assignValueDouble (double newVal) {
    ei.setDoubleElement(index, newVal);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateValue#assignValueRef(int) */
  @Override
  protected void assignValueRef (int newValRef) {
    ei.setReferenceElement(index, newValRef);
  }

}
