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
import gov.nasa.jpf.inspector.server.programstate.StateNodeInterface;
import gov.nasa.jpf.inspector.server.programstate.StateReadableConstValue;
import gov.nasa.jpf.vm.ClassLoaderInfo;

/**
 * @author Alf
 * 
 */
public class ExpressionStateValueConstDouble extends ExpressionStateValueConst {

  public static final String TOKEN_NAN = "NaN";
  public static final String TOKEN_POSITIVE_INFINITY2 = "+inf";
  public static final String TOKEN_NEGATIVE_INFINITY2 = "-inf";

  private final double value;

  public ExpressionStateValueConstDouble (double value) {
    this.value = value;
  }

  @Override
  public StateNodeInterface getResultExpression (JPFInspector inspector, InspectorState state) throws JPFInspectorException {
    return new StateReadableConstValue(inspector, ClassLoaderInfo.getCurrentResolvedClassInfo("double"), value);
  }

  @Override
  public String getNormalizedExpression () {
    if (value == Double.POSITIVE_INFINITY) {
      // TOKEN_POSITIVE_INFINITY2 : '+inf' ;
      return TOKEN_POSITIVE_INFINITY2;
    }

    if (value == Double.NEGATIVE_INFINITY) {
      // TOKEN_NEGATIVE_INFINITY2 : '-inf' ;
      return TOKEN_NEGATIVE_INFINITY2;
    }

    if (Double.isNaN(value)) {
      // TOKEN_NAN : 'NaN' ; // Not-a-number
      return TOKEN_NAN;
    }

    // Ordinary number
    return Double.toString(value);
  }

}
