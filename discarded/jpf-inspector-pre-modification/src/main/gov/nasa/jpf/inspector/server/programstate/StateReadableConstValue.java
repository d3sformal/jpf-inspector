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
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorNoSuperClassException;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorNotInstanceException;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorNotSuperClassException;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.programstate.client.PSEVariable;
import gov.nasa.jpf.jvm.ClassInfo;
import gov.nasa.jpf.jvm.ElementInfo;

/**
 * @author Alf
 * 
 */
public class StateReadableConstValue extends StateNode implements StateReadableValueInterface {

  private final ClassInfo type;
  private final Object represendedConstValue; // Wrapped value in case if the base type or ElementInfo in case of reference

  /**
   * @param inspector
   * @param referenceDepth
   */
  public <T extends Object> StateReadableConstValue (JPFInspector inspector, int referenceDepth, ClassInfo constType, T wrappedConstV) {
    super(inspector, referenceDepth);

    // TODO
    setStateExpr("constant");

    represendedConstValue = wrappedConstV;
    type = constType;

  }

  /**
   * Creates different(restricted) view on the represented value.
   * e
   * 
   * @throws JPFInspectorNotSuperClassException
   */
  protected StateReadableConstValue (StateReadableConstValue me, ClassInfo supperClassInfo, String stateExpression) throws JPFInspectorNotSuperClassException {
    super(me, me.getReferenceDepth());

    setStateExpr(stateExpression);

    this.type = supperClassInfo;
    this.represendedConstValue = me.getValue();

    assert (this.type != null);
    if (!StateValue.isPredecessor(this.type, me.getClassInfo())) {
      throw new JPFInspectorNotSuperClassException(this.type, me.getClassInfo());
    }

  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#getClassInfo() */
  @Override
  public ClassInfo getClassInfo () {
    return type;
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#getValue() */
  @Override
  public Object getValue () {
    return represendedConstValue;
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#isReference() */
  @Override
  public boolean isReference () {
    return !type.isPrimitive();
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#getReferenceValue() */
  @Override
  public ElementInfo getReferenceValue () {
    if (isReference()) {
      assert (represendedConstValue == null || represendedConstValue instanceof ElementInfo);
      return (ElementInfo) represendedConstValue;
    }

    return null;
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#createSuper() */
  @Override
  public StateReadableValueInterface createSuper () throws JPFInspectorException {
    ClassInfo superClassInfo = type.getSuperClass();
    if (superClassInfo == null) {
      throw new JPFInspectorNoSuperClassException(type);
    }
    return new StateReadableConstValue(this, superClassInfo, getStateExpr() + '.' + PSEVariable.EXPRESSION_SUPER);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#createPredecessorClass(gov.nasa.jpf.jvm.ClassInfo) */
  @Override
  public StateReadableValueInterface createPredecessorClass (ClassInfo ci) throws JPFInspectorNotSuperClassException {
    return new StateReadableConstValue(this, ci, getStateExpr() + '.' + StateValue.getSimpleName(ci));
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#createThisValue() */
  @Override
  public StateReadableValueInterface createThisValue () throws JPFInspectorException {
    if (type.isArray() || type.isPrimitive()) {
      throw new JPFInspectorNotInstanceException(type);
    }

    return new StateReadableConstValue(this, type, getStateExpr() + "." + PSEVariable.EXPRESSION_VARIABLE_THIS);
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateNode#getResultExpression(java.lang.String, int) */
  @Override
  public ProgramStateEntry getResultExpression (String name, int clientID) throws JPFInspectorException {
    return StateValue.createPSEVariable(this, name, clientID, "user constant", 0, "");
  }

}
