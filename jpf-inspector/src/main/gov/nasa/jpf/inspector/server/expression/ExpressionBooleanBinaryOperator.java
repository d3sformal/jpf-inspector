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

package gov.nasa.jpf.inspector.server.expression;

import gov.nasa.jpf.inspector.server.breakpoints.BreakPointModes;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a boolean expression with two operands. Used as a hit condition.
 */
public abstract class ExpressionBooleanBinaryOperator extends ExpressionBoolean {

  protected List<ExpressionBooleanInterface> children = new ArrayList<>(2);

  public ExpressionBooleanBinaryOperator (ExpressionBooleanInterface left, ExpressionBooleanInterface right) {
    assert left != null;
    assert right != null;

    children.add(left);
    children.add(right);
  }

  @Override
  public BreakPointModes getBPMode () {
    return BreakPointModes.BP_MODE_USER_CONDITION;
  }

}
