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
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorInvalidNameException;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorInvalidSlotIndexException;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorNoSuperClassException;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorNotInstanceException;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorNotSuperClassException;
import gov.nasa.jpf.inspector.migration.MigrationUtilities;
import gov.nasa.jpf.inspector.common.pse.PSEVariable;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.LocalVarInfo;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.StackFrame;
import gov.nasa.jpf.vm.Types;

/**
 * @author Alf
 * 
 */
public class StateValueStackSlot extends StateValue {
  public static final boolean DEBUG = false;

  public static final int INVALID_SLOT_INDEX = -1;

  private final StackFrame sf;
  private final int index;

  private final LocalVarInfo lvi;

  public static StateValueStackSlot createNamedSlotValue (StateStackFrame ssf, String varName) throws JPFInspectorException {
    int slotIndex = ssf.namedSlotIndex(varName);
    if (slotIndex == StateValueStackSlot.INVALID_SLOT_INDEX) {
      throw new JPFInspectorInvalidNameException(varName);
    }

    return StateValueStackSlot.createSVSSInstance(ssf, 1, ssf.getStateExpr() + '.' + varName, slotIndex);

  }

  public static StateValueStackSlot createHiddenThisSlotValue (StateStackFrame ssf) throws JPFInspectorException {
    assert (ssf != null);

    // Check if not a static method
    StackFrame sf = ssf.getStackFrame();
    MethodInfo mi = sf.getMethodInfo();
    if (mi.isStatic()) {
      throw new JPFInspectorNotInstanceException(mi);
    }

    return StateValueStackSlot.createSVSSInstance(ssf, 1, ssf.getStateExpr(), 0);
  }

  /**
   * Creates representation of "this" instance.
   */
  public static StateValueStackSlot createThisSlotValue (StateStackFrame ssf) throws JPFInspectorException {
    assert (ssf != null);

    // Check if not a static method
    StackFrame sf = ssf.getStackFrame();
    MethodInfo mi = sf.getMethodInfo();
    if (mi.isStatic()) {
      throw new JPFInspectorNotInstanceException(mi);
    }

    return StateValueStackSlot.createSVSSInstance(ssf, 1, ssf.getStateExpr() + '.' + PSEVariable.EXPRESSION_VARIABLE_THIS, 0);
  }

  public static StateValueStackSlot createSlotFromIndex (StateStackFrame ssf, int slotIndex, int referenceDepth) throws JPFInspectorException {
    return StateValueStackSlot.createSVSSInstance(ssf, referenceDepth, ssf.getStateExpr() + '.' + PSEVariable.EXPRESSION_VARIABLE_LOCAL_VAR + '[' + slotIndex
        + ']', slotIndex);
  }

  public static StateValueStackSlot createSlotFromIndex (StateStackFrame ssf, int slotIndex) throws JPFInspectorException {
    return StateValueStackSlot.createSlotFromIndex(ssf, slotIndex, ssf.getReferenceDepth() - 1);
  }

  /**
   * @param ssf
   * @param slotIndex
   * @param referenceDepth
   * TODO This param no longer exists:  _param ci Type of the entry. Have to be super type of the slot type. If null real type of the value is used.
   * @param stateExpression
   * 
   * @throws JPFInspectorInvalidSlotIndexException
   */
  static protected StateValueStackSlot createSVSSInstance (StateStackFrame ssf, int referenceDepth, String stateExpression, int slotIndex)
      throws JPFInspectorInvalidSlotIndexException {
    assert (ssf != null);

    StackFrame sf = ssf.getStackFrame();

    // Check if index is in range
    final int maxSlotIndex = sf.getTopPos();

    if (slotIndex < 0 || slotIndex > maxSlotIndex) {
      throw new JPFInspectorInvalidSlotIndexException(slotIndex, sf.getMethodInfo(), maxSlotIndex);
    }

    LocalVarInfo lvi = sf.getLocalVarInfo(slotIndex);
    if (lvi == null) {
      // Created faked (raw) LocalVarInfo - represents int
      lvi = new LocalVarInfo("???-RawView-no Name or Type provided", "I", "I", 0, sf.getMethodInfo().getLastInsn().getPosition(), slotIndex);

    }
    assert (lvi != null);

    // Obtain real type of the field
    String className = Types.getTypeName(lvi.getSignature());
    if (DEBUG) {
      ssf.getInspector().getDebugPrintStream().println(StateValueStackSlot.class.getSimpleName() + ".createSVSSInstance - className=" + className);
    }
    // Can throw NoClassInfoException -> in such a case it is internal error
    ClassInfo ciReal = MigrationUtilities.getResolvedClassInfo_StateValueStackSlot(className);

    assert (ciReal != null);
    assert (StateValue.isPredecessor(ciReal, ciReal));

    return new StateValueStackSlot(ssf, referenceDepth, stateExpression, ciReal, slotIndex, lvi);

  }

  protected StateValueStackSlot (StateStackFrame ssf, int referenceDepth, String stateExpression, ClassInfo ci, int slotIndex, LocalVarInfo lvi) {
    super(ssf, referenceDepth, ci, stateExpression);

    this.sf = ssf.getStackFrame();
    this.index = slotIndex;

    this.lvi = lvi;
  }

  /**
   * Creates different(restricted) view on the represented value.
   */
  protected StateValueStackSlot (StateValueStackSlot me, ClassInfo superClassInfo, String stateExpression) throws JPFInspectorNotSuperClassException {
    super(me, superClassInfo, stateExpression);

    this.sf = me.sf;
    this.index = me.index;
    this.lvi = me.lvi;
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#getReferenceValue() */
  @Override
  public ElementInfo getReferenceValue () {
    return getReferenceValueImpl(sf.getSlot(index));
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateNode#getResultExpression(java.lang.String, int) */
  @Override
  public PSEVariable getResultExpression (String name, int clientID) throws JPFInspectorException {
    final MethodInfo mi = sf.getMethodInfo();
    assert (mi != null);
    final ClassInfo ciMethod = mi.getClassInfo(); // ClassInfo where the executed method is defined

    final String varName = lvi.getName();
    final String definedIn = (ciMethod != null ? ciMethod.getSimpleName() + "." + mi.getName() : "[???]" + mi.getName());

    return StateValue.createPSEVariable(this, name, clientID, varName, index, definedIn);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#getValue() */
  @Override
  public Object getValue () {
    return sf.getLocalValueObject(lvi);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#createSuper() */
  @Override
  public StateReadableValueInterface createSuper () throws JPFInspectorException {
    ClassInfo superClassInfo = ci.getSuperClass();
    if (superClassInfo == null) {
      throw new JPFInspectorNoSuperClassException(ci);
    }
    return new StateValueStackSlot(this, superClassInfo, getStateExpr() + '.' + PSEVariable.EXPRESSION_SUPER);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#createPredecessorClass(gov.nasa.jpf.jvm.ClassInfo) */
  @Override
  public StateReadableValueInterface createPredecessorClass (ClassInfo ci) throws JPFInspectorNotSuperClassException {
    return new StateValueStackSlot(this, ci, getStateExpr() + '.' + StateValue.getSimpleName(ci));
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#createThisValue() */
  @Override
  public StateReadableValueInterface createThisValue () throws JPFInspectorException {
    if (ci.isArray() || ci.isPrimitive()) {
      throw new JPFInspectorNotInstanceException(ci);
    }

    return new StateValueStackSlot(this, ci, getStateExpr() + "." + PSEVariable.EXPRESSION_VARIABLE_THIS);
  }

  // *************************************************************************
  // ** Modify represented value infrastructure
  // *************************************************************************

  /* @see gov.nasa.jpf.inspector.server.programstate.StateValue#assignValueBoolean(boolean) */
  @Override
  protected void assignValueBoolean (boolean newVal) {
    sf.setLocalVariable(index, (newVal ? 1 : 0), false);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateValue#assignValueChar(char) */
  @Override
  protected void assignValueChar (char newVal) {
    sf.setLocalVariable(index, newVal, false);

  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateValue#assignValueByte(byte) */
  @Override
  protected void assignValueByte (byte newVal) {
    sf.setLocalVariable(index, newVal, false);

  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateValue#assignValueShort(short) */
  @Override
  protected void assignValueShort (short newVal) {
    sf.setLocalVariable(index, newVal, false);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateValue#assignValueInt(int) */
  @Override
  protected void assignValueInt (int newVal) {
    sf.setLocalVariable(index, newVal, false);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateValue#assignValueLong(long) */
  @Override
  protected void assignValueLong (long newVal) {
    sf.setLongLocalVariable(index, newVal);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateValue#assignValueFloat(float) */
  @Override
  protected void assignValueFloat (float newVal) {
    sf.setLocalVariable(index, Types.floatToInt(newVal), false);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateValue#assignValueDouble(double) */
  @Override
  protected void assignValueDouble (double newVal) {
    sf.setLongLocalVariable(index, Types.doubleToLong(newVal));
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateValue#assignValueRef(int) */
  @Override
  protected void assignValueRef (int newValRef) {
    sf.setLocalVariable(index, newValRef, true);
  }

}
