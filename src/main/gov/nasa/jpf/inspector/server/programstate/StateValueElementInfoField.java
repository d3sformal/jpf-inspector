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
import gov.nasa.jpf.inspector.exceptions.JPFInspectorInvalidFieldIndexException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorInvalidFieldNameException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorInvalidStaticFieldIndexException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorInvalidStaticFieldNameException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNoStaticElementInfoException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNoSuperClassException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNotInnerClassException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNotInstanceException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNotSuperClassException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNullValueException;
import gov.nasa.jpf.inspector.common.pse.PSEVariable;
import gov.nasa.jpf.inspector.server.attributes.AttributesManager;
import gov.nasa.jpf.inspector.server.attributes.attachments.AttributeAttachment;
import gov.nasa.jpf.inspector.server.attributes.attachments.FieldAttachment;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.Fields;
import gov.nasa.jpf.vm.StaticElementInfo;

/**
 * @author Alf
 * 
 */
public final class StateValueElementInfoField extends StateWritableValue {

  private final ElementInfo ei;
  private final FieldInfo fieldInfo;


  public static StateValueElementInfoField createInstanceNamedField (StateReadableValue srvi, String varName) throws JPFInspectorException {

    ClassInfo ci = srvi.getClassInfo();
    FieldInfo fi = ci.getInstanceField(varName);

    if (fi == null) {
      throw new JPFInspectorInvalidFieldNameException(varName);
    }

    ElementInfo ei = srvi.getReferenceValue();
    if (ei == null) {
      // Represents null value
      throw new JPFInspectorNullValueException(varName, srvi.getClassInfo());
    }

    return new StateValueElementInfoField(srvi, true, srvi.getStateExpr() + '.' + varName, ei, fi);
  }

  public static StateReadableValue createStaticNamedField (StateStackFrame ssf, String varName) throws JPFInspectorException {

    ClassInfo ci = ssf.getClassInfo();
    return StateValueElementInfoField.createStaticNamedFieldGeneric(ssf, ci, varName);
  }

  public static StateReadableValue createStaticNamedField (StateReadableValue srvi, String varName) throws JPFInspectorException {

    ClassInfo ci = srvi.getClassInfo();
    return StateValueElementInfoField.createStaticNamedFieldGeneric(srvi, ci, varName);
  }

  private static StateReadableValue createStaticNamedFieldGeneric(StateNodeInterface sni, ClassInfo ci, String varName)
      throws JPFInspectorInvalidStaticFieldNameException, JPFInspectorNoStaticElementInfoException {
    FieldInfo fi = ci.getStaticField(varName);

    if (fi == null) {
      throw new JPFInspectorInvalidStaticFieldNameException(varName);
    }

    StaticElementInfo sei = ci.getStaticElementInfo();
    if (sei == null) {
      throw new JPFInspectorNoStaticElementInfoException(ci);
    }

    return new StateValueElementInfoField(sni, true, sni.getStateExpr() + '.' + varName, sei, fi);
  }
  public static StateValueElementInfoField createStaticFieldFromIndex (StateReadableValue srvi,
                                                                       int fieldIndex
                                                                       )throws JPFInspectorException
  {
    return createStaticFieldFromIndex(srvi, fieldIndex, true);

  }
  public static StateValueElementInfoField createStaticFieldFromIndex (StateReadableValue srvi,
                                                                       int fieldIndex,
                                                                       boolean expandMembers)
      throws JPFInspectorException {

    ClassInfo ci = srvi.getClassInfo();

    if (fieldIndex < 0 || fieldIndex >= ci.getNumberOfStaticFields()) {
      throw new JPFInspectorInvalidStaticFieldIndexException(fieldIndex, ci);
    }

    FieldInfo fi = ci.getStaticField(fieldIndex);
    assert (fi != null);

    StaticElementInfo sei = ci.getStaticElementInfo();
    assert (sei != null);

    return new StateValueElementInfoField(srvi,
                                          expandMembers,
                                          srvi.getStateExpr() + '.' + PSEVariable.EXPRESSION_STATIC + '[' + fieldIndex + ']',
                                          sei,
                                          fi);
  }
  public static StateValueElementInfoField createFieldFromIndex (StateReadableValue srvi,
                                                                 int fieldIndex
                                                                 )
          throws JPFInspectorException {
    return createFieldFromIndex(srvi, fieldIndex, true);
  }
  public static StateValueElementInfoField createFieldFromIndex (StateReadableValue srvi,
                                                                 int fieldIndex,
                                                                 boolean expandMembers)
      throws JPFInspectorException {

    ClassInfo ci = srvi.getClassInfo();

    if (fieldIndex < 0 || fieldIndex >= ci.getNumberOfInstanceFields()) {
      throw new JPFInspectorInvalidFieldIndexException(fieldIndex, ci);
    }

    FieldInfo fi = ci.getInstanceField(fieldIndex);
    assert (fi != null);

    ElementInfo ei = srvi.getReferenceValue();
    if (ei == null) {
      // Represents null value
      throw new JPFInspectorNullValueException(PSEVariable.EXPRESSION_VARIABLE_FIELD + '[' + fieldIndex + "] (" + fi.getName() + ')', srvi.getClassInfo());
    }

    return new StateValueElementInfoField(srvi, expandMembers, srvi.getStateExpr() + '.' + PSEVariable.EXPRESSION_VARIABLE_FIELD + '[' + fieldIndex + ']', ei,
        fi);
  }

  /**
   * @return Creates representation of the outer class (or throw exception if no enclosing class exists)
   */
  public static StateReadableValue createOuterClass (StateReadableValue srvi) throws JPFInspectorException {
    assert (srvi != null);

    ClassInfo ci = srvi.getClassInfo();
    ClassInfo ciOuter = ci.getEnclosingClassInfo();
    if (ciOuter == null) {
      throw new JPFInspectorNotInnerClassException(ci);
    }

    String newStateExpr = srvi.getStateExpr() + "." + PSEVariable.EXPRESSION_OUTER_CLASS;
    ElementInfo ei = srvi.getReferenceValue();
    if (StateWritableValue.isStaticElementInfo(ei)) {
      return StateElementInfo.createStaticOuterClass(srvi);
    }
    // Outer class can exists
    if (ei == null) {
      throw new JPFInspectorNotInnerClassException(ci);
    }
    // Implementation based on {@link DynamicElementInfo#getEnclosingElementInfo()}
    for (FieldInfo fi : ci.getDeclaredInstanceFields()) {
      // Note name can be compiler dependent
      // after the '$' is number representing depth of nesting
      if (fi.getName().startsWith("this$")) {
        return new StateValueElementInfoField(srvi, srvi.shouldExpandMembers(), newStateExpr, ei, fi);
      }
    }

    assert false; // Should be have outer class but not Field "this$"num not exists
    throw new JPFInspectorNotInnerClassException(ci);
  }

  private StateValueElementInfoField(StateNodeInterface sni, boolean expandMembers, String stateExpression, ElementInfo ei, FieldInfo fieldInfo) {
    super(sni, expandMembers, fieldInfo.getTypeClassInfo(), stateExpression);

    assert (ei != null);
    this.ei = ei;
    this.fieldInfo = fieldInfo;
  }

  /**
   * Creates different(restricted) view on the represented value.
   * 
   * @throws JPFInspectorNotSuperClassException
   */
  private StateValueElementInfoField(StateValueElementInfoField me, ClassInfo superClassInfo, String stateExpression)
      throws JPFInspectorNotSuperClassException {
    super(me, superClassInfo, stateExpression);

    this.ei = me.ei;
    this.fieldInfo = me.fieldInfo;
  }

  @Override
  public ElementInfo getReferenceValue () {
    if (fieldInfo.isReference()) {
      int ref = ei.getReferenceField(fieldInfo);
      return getReferenceValueImpl(ref);
    }

    return null;
  }

  @Override
  public Object getValue () {
    final Fields fields = ei.getFields();

    return fieldInfo.getValueObject(fields);
  }

  @Override
  public PSEVariable toHierarchy3(AttributesManager attributeManager) throws JPFInspectorException {
    final String varName = fieldInfo.getName();
    final String definedIn = StateWritableValue.getSimpleName(fieldInfo.getClassInfo());

    return StateReadableValue.createPSEVariable(this, varName, fieldInfo.getFieldIndex(), definedIn,
                                                attributeManager.getAttachmentAttributes(ei, fieldInfo), attributeManager );
  }

  /**
   * 
   * At first look up the field in given class, then in predecessors.
   * Look for both instance fields and static fields.
   * Ignores field visibility.
   * 
   * @param ci Class where to find field with given name
   * @param fieldName Name of the field to find
   * @return Representation of the field or null if no field with given name exists.
   */
  public static FieldInfo fieldNameJavaBasedLookup (ClassInfo ci, String fieldName) {
    FieldInfo fi;

    while (ci != null) {
      fi = ci.getDeclaredInstanceField(fieldName);
      if (fi != null) {
        return fi;
      }
      fi = ci.getDeclaredStaticField(fieldName);
      if (fi != null) {
        return fi;
      }
      ci = ci.getSuperClass();
    }
    return null;
  }

  @Override
  public StateValueElementInfoField createSuper () throws JPFInspectorException {
    ClassInfo superClassInfo = ci.getSuperClass();
    if (superClassInfo == null) {
      throw new JPFInspectorNoSuperClassException(ci);
    }
    return new StateValueElementInfoField(this, superClassInfo, getStateExpr() + '.' + PSEVariable.EXPRESSION_SUPER);
  }

  @Override
  public StateValueElementInfoField createPredecessorClass (ClassInfo ci) throws JPFInspectorNotSuperClassException {
    return new StateValueElementInfoField(this, ci, getStateExpr() + '.' + StateWritableValue.getSimpleName(ci));
  }

  @Override
  public StateValueElementInfoField createThisValue () throws JPFInspectorException {
    if (ci.isArray() || ci.isPrimitive()) {
      throw new JPFInspectorNotInstanceException(ci);
    }

    return new StateValueElementInfoField(this, ci, getStateExpr() + '.' + PSEVariable.EXPRESSION_VARIABLE_THIS);
  }

  // *************************************************************************
  // ** Modify represented value infrastructure
  // *************************************************************************

  @Override
  protected void assignValueBoolean (boolean newVal) {
    ei.getModifiableInstance().setBooleanField(fieldInfo, newVal);
  }

  @Override
  protected void assignValueChar (char newVal) {
    ei.getModifiableInstance().setCharField(fieldInfo, newVal);
  }

  @Override
  protected void assignValueByte (byte newVal) {
    ei.getModifiableInstance().setByteField(fieldInfo, newVal);
  }

  @Override
  protected void assignValueShort (short newVal) {
    ei.getModifiableInstance().setShortField(fieldInfo, newVal);
  }

  @Override
  protected void assignValueInt (int newVal) {
    ei.getModifiableInstance().setIntField(fieldInfo, newVal);
  }

  @Override
  protected void assignValueLong (long newVal) {
    ei.getModifiableInstance().setLongField(fieldInfo, newVal);
  }

  @Override
  protected void assignValueFloat (float newVal) {
    ei.getModifiableInstance().setFloatField(fieldInfo, newVal);
  }

  @Override
  protected void assignValueDouble (double newVal) {
    ei.getModifiableInstance().setDoubleField(fieldInfo, newVal);
  }

  @Override
  protected void assignValueRef (int newValRef) {
    ei.getModifiableInstance().setReferenceField(fieldInfo, newValRef);
  }

  @Override
  public AttributeAttachment getAttributeAttachment() {
    return new FieldAttachment(this.ei, this.fieldInfo);
  }

}
