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
 * @author Alf
 * 
 */
public class StateReadableConstValue extends StateNode implements StateReadableValueInterface {

  private final ClassInfo type;
  private final Object represendedConstValue; // Wrapped value in case if the base type or ElementInfo in case of reference

  public <T> StateReadableConstValue (JPFInspector inspector, int referenceDepth, ClassInfo constType, T wrappedConstV) {
    super(inspector, referenceDepth);
    if (wrappedConstV == null) {
      setStateExpr("null");
    } else {
      setStateExpr("!ERROR - Constants cannot be loaded lazily!");
    }
    represendedConstValue = wrappedConstV;
    type = constType;

  }

  /**
   * Creates different(restricted) view on the represented value.
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
