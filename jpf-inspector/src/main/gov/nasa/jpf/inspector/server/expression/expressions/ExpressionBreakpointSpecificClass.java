//
// Copyright (C) 2010 United States Government as represented by the
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

package gov.nasa.jpf.inspector.server.expression.expressions;

import gov.nasa.jpf.inspector.migration.MigrationUtilities;
import gov.nasa.jpf.inspector.server.breakpoints.BreakPointModes;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.expression.InspectorState.ListenerMethod;
import gov.nasa.jpf.inspector.utils.ClassName;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.VM;

public class ExpressionBreakpointSpecificClass extends ExpressionBooleanLeaf {
  public enum Mode {
    SC_MODE_CREATED,
    SC_MODE_RELEASED,
    SC_MODE_EXCEPTION_THROWN,
  }

  private final ClassName cn;
  private final Mode mode;

  public ExpressionBreakpointSpecificClass(ClassName cn, Mode mode) {
    this.cn = cn;
    this.mode = mode;
  }

  public ClassName getClassName() {
    return cn;
  }

  @Override
  public boolean evaluateExpression(InspectorState state) {
    if ((mode == Mode.SC_MODE_CREATED && state.getListenerMethod() != ListenerMethod.LM_OBJECT_CREATED)
        || (mode == Mode.SC_MODE_RELEASED && state.getListenerMethod() != ListenerMethod.LM_OBJECT_RELEASED)
        || (mode == Mode.SC_MODE_EXCEPTION_THROWN && state.getListenerMethod() != ListenerMethod.LM_EXCEPTION_THROWN)) {
      return false;
    }
    VM vm = state.getJVM();
    assert vm != null;

    ElementInfo ei = MigrationUtilities.getLastElementInfo(vm);
    ClassInfo ci = ei.getClassInfo();
    return cn.isSameClass(ci);
  }

  @Override
  public BreakPointModes getBPMode() {
    if (mode == Mode.SC_MODE_CREATED) {
      return BreakPointModes.BP_MODE_OBJECT_CREATED;
    } else if (mode == Mode.SC_MODE_RELEASED) {
      return BreakPointModes.BP_MODE_OBJECT_RELEASED;
    } else if (mode == Mode.SC_MODE_EXCEPTION_THROWN) {
      return BreakPointModes.BP_MODE_EXCEPTION_THROWN;
    } else {
      throw new RuntimeException("Internal error - Unknown enum " + Mode.class.getSimpleName() + " entry " + mode);
    }
  }

  @Override
  public String getNormalizedExpression() {
    StringBuffer sb = new StringBuffer(64);

    if (mode == Mode.SC_MODE_CREATED) {
      sb.append("object_created");
    } else if (mode == Mode.SC_MODE_RELEASED) {
      sb.append("object_released");
    } else if (mode == Mode.SC_MODE_EXCEPTION_THROWN) {
      sb.append("exception_thrown");
    } else {
      throw new RuntimeException("Internal error - Unknown enum " + Mode.class.getSimpleName() + " entry " + mode);
    }
    sb.append('=');
    sb.append(cn.getClassName());

    return sb.toString();
  }

}
