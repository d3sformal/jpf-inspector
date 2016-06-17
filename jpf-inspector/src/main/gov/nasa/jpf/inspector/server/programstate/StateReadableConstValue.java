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
public class StateReadableConstValue extends StateNode implements StateReadableValueInterface {

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
    super(inspector, 1);

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
    super(me, me.getReferenceDepth());

    setStateExpr(stateExpression);

    this.type = superClassInfo;
    this.represendedConstValue = me.getValue();

    assert (this.type != null);

    if (!StateValue.isPredecessor(this.type, me.getClassInfo())) {
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
  public StateReadableValueInterface createSuper () throws JPFInspectorException {
    ClassInfo superClassInfo = type.getSuperClass();
    if (superClassInfo == null) {
      throw new JPFInspectorNoSuperClassException(type);
    }
    return new StateReadableConstValue(this, superClassInfo, getStateExpr() + '.' + PSEVariable.EXPRESSION_SUPER);
  }

  @Override
  public StateReadableValueInterface createPredecessorClass (ClassInfo ci) throws JPFInspectorNotSuperClassException {
    return new StateReadableConstValue(this, ci, getStateExpr() + '.' + StateValue.getSimpleName(ci));
  }

  @Override
  public StateReadableValueInterface createThisValue () throws JPFInspectorException {
    if (type.isArray() || type.isPrimitive()) {
      throw new JPFInspectorNotInstanceException(type);
    }

    return new StateReadableConstValue(this, type, getStateExpr() + "." + PSEVariable.EXPRESSION_VARIABLE_THIS);
  }

  @Override
  public ProgramStateEntry toHierarchy3() throws JPFInspectorException {
    return StateValue.createPSEVariable(this, "user constant", 0, "");
  }

}
