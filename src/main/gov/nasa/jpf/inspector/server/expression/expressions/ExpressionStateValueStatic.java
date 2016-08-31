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

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.server.programstate.*;
import gov.nasa.jpf.vm.ClassInfo;

/**
 * Represents #static and #static[index] expressions using just the integer index.
 */
public class ExpressionStateValueStatic extends ExpressionStateValue {

  public static final String TOKEN_HASH_STATIC = "#static";

  private final Integer staticFieldIndex;

  public ExpressionStateValueStatic (ExpressionStateValue child, Integer staticFieldIndex) {
    super(child);

    this.staticFieldIndex = staticFieldIndex;
  }

  @Override
  public StateReadableValue toHierarchy2(StateReadableValue parent) throws JPFInspectorException {

    StateReadableValue result;
    if (staticFieldIndex != null) {
      result = StateValueElementInfoField.createStaticFieldFromIndex(parent, staticFieldIndex);
    } else {
      result = StateElementInfo.createStaticClass(parent, parent.getClassInfo());
    }

    ExpressionStateValue child = getChild();
    if (child == null) {
      return result;
    } else {
      return child.toHierarchy2(result);
    }
  }

  public StateNodeInterface getExpressionFromStackFrame(StateStackFrame stackFrame) throws JPFInspectorException {
    StateReadableValue result;
    ClassInfo associatedClass = stackFrame.getClassInfo();

    if (staticFieldIndex != null) {
      StateElementInfo staticEi = StateElementInfo.createStaticClass(stackFrame, associatedClass);
      result = StateValueElementInfoField.createStaticFieldFromIndex(staticEi, staticFieldIndex);
    } else {
      result = StateElementInfo.createStaticClass(stackFrame, associatedClass);
    }

    ExpressionStateValue child = getChild();
    if (child == null) {
      return result;
    } else {
      return child.toHierarchy2(result);
    }
  }

  @Override
  public String getNormalizedExpression () {
    if (staticFieldIndex != null) {
      return '.' + TOKEN_HASH_STATIC + '[' + staticFieldIndex + ']' + (getChild() != null ? getChild().getNormalizedExpression() : "");
    }

    return '.' + TOKEN_HASH_STATIC + (getChild() != null ? getChild().getNormalizedExpression() : "");
  }

}
