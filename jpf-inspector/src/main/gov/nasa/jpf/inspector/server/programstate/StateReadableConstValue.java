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

import gov.nasa.jpf.inspector.common.pse.ProgramStateEntry;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNoSuperClassException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNotInstanceException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNotSuperClassException;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.common.pse.PSEVariable;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;

/**
 * Represents a literal in a hierarchy-1 expression.
 */
public class StateReadableConstValue extends StateReadableValue {

  /**
   * Type of the literal (Integer, Boolean, ...)
   */
  private final ClassInfo type;
  /**
   * Wrapped value in case if the base type or ElementInfo in case of reference (i.e. "null" or a string)
   */
  private final Object represendedConstValue;

  /**
   * Creates a new hierarchy-2 representation of a literal.
   *
   * @param inspector The Inspector server.
   * @param constType Type of the literal (Integer, Boolean, ...)
   * @param wrappedConstV Value of the literal (an Integer, a Boolean, ...). If it's null, it's null. If it's a string, then this is an {@link ElementInfo} object of type String.
   * @param <T> Type of the literal (as Java type).
   */
  public <T> StateReadableConstValue(JPFInspector inspector, ClassInfo constType, T wrappedConstV) {
    super(inspector, true);

    if (wrappedConstV == null) {
      setStateExpr("null");
    } else {
      setStateExpr(wrappedConstV.toString());
    }

    represendedConstValue = wrappedConstV;
    type = constType;
  }

  /**
   * Creates a different (restricted) view on the represented value.
   * @param me The literal that should be restricted.
   * @param superClassInfo The class that it should be restricted to.
   */
  private StateReadableConstValue(StateReadableConstValue me,
                                  ClassInfo superClassInfo,
                                  String stateExpression) throws JPFInspectorNotSuperClassException {
    super(me.getInspector(), true);

    setStateExpr(stateExpression);

    this.type = superClassInfo;
    this.represendedConstValue = me.getValue();

    assert (this.type != null);

    if (!StateWritableValue.isPredecessor(this.type, me.getClassInfo())) {
      throw new JPFInspectorNotSuperClassException(this.type, me.getClassInfo());
    }

  }

  @Override
  public ClassInfo getClassInfo () {
    return type;
  }

  @Override
  public Object getValue () {
    return represendedConstValue;
  }

  @Override
  public boolean isReference () {
    return !type.isPrimitive();
  }

  @Override
  public ElementInfo getReferenceValue () {
    if (isReference()) {
      assert (represendedConstValue == null || represendedConstValue instanceof ElementInfo);
      return (ElementInfo) represendedConstValue;
    }

    return null;
  }

  @Override
  public StateReadableValue createSuper () throws JPFInspectorException {
    ClassInfo superClassInfo = type.getSuperClass();
    if (superClassInfo == null) {
      throw new JPFInspectorNoSuperClassException(type);
    }
    return new StateReadableConstValue(this, superClassInfo, getStateExpr() + '.' + PSEVariable.EXPRESSION_SUPER);
  }

  @Override
  public StateReadableValue createPredecessorClass (ClassInfo ci) throws JPFInspectorNotSuperClassException {
    return new StateReadableConstValue(this, ci, getStateExpr() + '.' + StateWritableValue.getSimpleName(ci));
  }

  @Override
  public StateReadableValue createThisValue () throws JPFInspectorException {
    if (type.isArray() || type.isPrimitive()) {
      throw new JPFInspectorNotInstanceException(type);
    }

    return new StateReadableConstValue(this, type, getStateExpr() + "." + PSEVariable.EXPRESSION_VARIABLE_THIS);
  }

  @Override
  public boolean shouldExpandMembers() {
    // For primitives, this does not matter.
    // For the null literal, this does not matter either.
    // However, strings are printed out as object, including their instance and static fields (they do have some),
    //   and so, to avoid exception, members must be expanded.
    return true;
  }

  @Override
  public ProgramStateEntry toHierarchy3() throws JPFInspectorException {
    return StateReadableValue.createPSEVariable(this, "user constant", 0, "");
  }

}
