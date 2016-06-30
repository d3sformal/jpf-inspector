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

package gov.nasa.jpf.inspector.server.expression.expressions;

import gov.nasa.jpf.inspector.server.breakpoints.BreakPointModes;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.expression.InspectorState.ListenerMethod;
import gov.nasa.jpf.inspector.utils.expressions.ClassName;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.VM;

/**
 * Represents hit conditions that hit whenever an object of the specified class is created, released by the garbage
 * collector, or when an exception of the specified class is thrown.
 */
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
    VM vm = state.getVM();
    assert vm != null;

    ElementInfo ei = state.getLastCreatedOrReleasedElementInfo();
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
    StringBuilder sb = new StringBuilder(64);

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
