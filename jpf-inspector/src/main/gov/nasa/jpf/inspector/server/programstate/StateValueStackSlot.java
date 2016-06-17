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
import gov.nasa.jpf.inspector.exceptions.JPFInspectorInvalidNameException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorInvalidSlotIndexException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNoSuperClassException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNotInstanceException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNotSuperClassException;
import gov.nasa.jpf.inspector.common.pse.PSEVariable;
import gov.nasa.jpf.vm.*;

/**
 * Contains information about a stack slot using the {@link StackFrame}, the index and the {@link LocalVarInfo}.
 *
 * Even when the user asks for a local variable or parameter by name, it is converted here to a stack slot index
 * and represented as this class in the second hierarchy.
 */
public class StateValueStackSlot extends StateWritableValue {
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

    return StateValueStackSlot.createSVSSInstance(ssf, ssf.getStateExpr() + '.' + varName, slotIndex);

  }

  public static StateValueStackSlot createHiddenThisSlotValue (StateStackFrame ssf) throws JPFInspectorException {
    assert (ssf != null);

    // Check if not a static method
    StackFrame sf = ssf.getStackFrame();
    MethodInfo mi = sf.getMethodInfo();
    if (mi.isStatic()) {
      throw new JPFInspectorNotInstanceException(mi);
    }

    return StateValueStackSlot.createSVSSInstance(ssf, ssf.getStateExpr(), 0);
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

    return StateValueStackSlot.createSVSSInstance(ssf, ssf.getStateExpr() + '.' + PSEVariable.EXPRESSION_VARIABLE_THIS, 0);
  }



  public static StateValueStackSlot createSlotFromIndex (StateStackFrame ssf, int slotIndex) throws JPFInspectorException {
    return StateValueStackSlot.createSVSSInstance(ssf,
                                                  ssf.getStateExpr() + '.' + PSEVariable.EXPRESSION_VARIABLE_LOCAL_VAR + '[' + slotIndex
            + ']',
                                                  slotIndex);
  }


  private static StateValueStackSlot createSVSSInstance(StateStackFrame ssf,
                                                        String stateExpression,
                                                        int slotIndex)
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
    assert lvi != null;

    // Obtain real type of the field
    String className = Types.getTypeName(lvi.getSignature());
    if (DEBUG) {
      ssf.getInspector().getDebugPrintStream().println(StateValueStackSlot.class.getSimpleName() + ".createSVSSInstance - className=" + className);
    }
    // Can throw NoClassInfoException -> in such a case it is internal error
    // An alternative here is to use getSystemResolvedClassInfo which uses the system class loader rather than the
    // thread's current class loader. I don't know the difference between the two so we'll use the current one for now,
    // and see what develops.
    // The original pre-migration source line was:
    // ClassInfo ciReal = ClassInfo.getResolvedClassInfo(className);
    ClassInfo ciReal = ClassLoaderInfo.getCurrentResolvedClassInfo(className);

    assert (ciReal != null);
    assert (StateWritableValue.isPredecessor(ciReal, ciReal));

    return new StateValueStackSlot(ssf, stateExpression, ciReal, slotIndex, lvi);

  }

  private StateValueStackSlot(StateStackFrame ssf,
                              String stateExpression,
                              ClassInfo ci,
                              int slotIndex,
                              LocalVarInfo lvi) {
    super(ssf, true, ci, stateExpression);

    this.sf = ssf.getStackFrame();
    this.index = slotIndex;

    this.lvi = lvi;
  }

  /**
   * Creates different(restricted) view on the represented value.
   */
  private StateValueStackSlot(StateValueStackSlot me, ClassInfo superClassInfo, String stateExpression) throws JPFInspectorNotSuperClassException {
    super(me, superClassInfo, stateExpression);

    this.sf = me.sf;
    this.index = me.index;
    this.lvi = me.lvi;
  }

  @Override
  public ElementInfo getReferenceValue () {
    return getReferenceValueImpl(sf.getSlot(index));
  }

  @Override
  public PSEVariable toHierarchy3() throws JPFInspectorException {
    final MethodInfo mi = sf.getMethodInfo();
    assert (mi != null);
    final ClassInfo ciMethod = mi.getClassInfo(); // ClassInfo where the executed method is defined

    final String varName = lvi.getName();
    final String definedIn = (ciMethod != null ? ciMethod.getSimpleName() + "." + mi.getName() : "[???]" + mi.getName());

    return StateReadableValue.createPSEVariable(this, varName, index, definedIn);
  }

  @Override
  public Object getValue () {
    return sf.getLocalValueObject(lvi);
  }

  @Override
  public StateReadableValue createSuper () throws JPFInspectorException {
    ClassInfo superClassInfo = ci.getSuperClass();
    if (superClassInfo == null) {
      throw new JPFInspectorNoSuperClassException(ci);
    }
    return new StateValueStackSlot(this, superClassInfo, getStateExpr() + '.' + PSEVariable.EXPRESSION_SUPER);
  }

  @Override
  public StateReadableValue createPredecessorClass (ClassInfo ci) throws JPFInspectorNotSuperClassException {
    return new StateValueStackSlot(this, ci, getStateExpr() + '.' + StateWritableValue.getSimpleName(ci));
  }

  @Override
  public StateReadableValue createThisValue () throws JPFInspectorException {
    if (ci.isArray() || ci.isPrimitive()) {
      throw new JPFInspectorNotInstanceException(ci);
    }

    return new StateValueStackSlot(this, ci, getStateExpr() + "." + PSEVariable.EXPRESSION_VARIABLE_THIS);
  }

  // *************************************************************************
  // ** Modify represented value infrastructure
  // *************************************************************************

  @Override
  protected void assignValueBoolean (boolean newVal) {
    sf.setLocalVariable(index, (newVal ? 1 : 0), false);
  }

  @Override
  protected void assignValueChar (char newVal) {
    sf.setLocalVariable(index, newVal, false);

  }

  @Override
  protected void assignValueByte (byte newVal) {
    sf.setLocalVariable(index, newVal, false);

  }

  @Override
  protected void assignValueShort (short newVal) {
    sf.setLocalVariable(index, newVal, false);
  }

  @Override
  protected void assignValueInt (int newVal) {
    sf.setLocalVariable(index, newVal, false);
  }

  @Override
  protected void assignValueLong (long newVal) {
    sf.setLongLocalVariable(index, newVal);
  }

  @Override
  protected void assignValueFloat (float newVal) {
    sf.setLocalVariable(index, Types.floatToInt(newVal), false);
  }

  @Override
  protected void assignValueDouble (double newVal) {
    sf.setLongLocalVariable(index, Types.doubleToLong(newVal));
  }

  @Override
  protected void assignValueRef (int newValRef) {
    sf.setLocalVariable(index, newValRef, true);
  }

}
