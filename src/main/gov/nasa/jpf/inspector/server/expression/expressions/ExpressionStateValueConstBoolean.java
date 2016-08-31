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
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.programstate.StateReadableConstValue;
import gov.nasa.jpf.vm.ClassLoaderInfo;

/**
 * @author Alf
 * 
 */
public class ExpressionStateValueConstBoolean extends ExpressionStateValueConst {

  private static final String TOKEN_TRUE = "true";
  private static final String TOKEN_FALSE = "false";

  private final boolean value;

  public ExpressionStateValueConstBoolean (boolean value) {
    this.value = value;
  }

  @Override
  public StateReadableConstValue getResultExpression (JPFInspector inspector, InspectorState state) throws JPFInspectorException {
    return new StateReadableConstValue(inspector, ClassLoaderInfo.getCurrentResolvedClassInfo("boolean"), value);
  }

  @Override
  public String getNormalizedExpression () {
    return value ? TOKEN_TRUE : TOKEN_FALSE;
  }

}
