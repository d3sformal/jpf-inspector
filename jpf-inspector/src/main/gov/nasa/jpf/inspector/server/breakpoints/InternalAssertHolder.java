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

package gov.nasa.jpf.inspector.server.breakpoints;

import gov.nasa.jpf.inspector.interfaces.BreakpointCreationInformation;
import gov.nasa.jpf.inspector.interfaces.InspectorCallbacks;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionBreakpointAssert;

/**
 * @author Alf
 * 
 */
public class InternalAssertHolder extends InternalBreakpointHolder {

  private final String position;
  private final String condition;

  public InternalAssertHolder(int newID, InspectorCallbacks callbacks, String position, String condition) {
    super(newID, callbacks, true, false);
    this.position = position;
    this.condition = condition;
  }

  @Override
  public boolean isAssert () {
    return true;
  }

  public void modifyAssertSettings(BreakpointCreationInformation newSettings,
                                   ExpressionBreakpointAssert bpExpAsser) {
    modifyBPSettings(newSettings, bpExpAsser);
  }

  @Override
  public AssertStatusImpl getBreakpointStatus (InspectorState state) {

    String details = null;
    String bpExpressionNormalized = null;
    String normalizedPosition = null;
    String normalizedCondition = null;

    if (bpExpression != null) {
      ExpressionBreakpointAssert expAssert = (ExpressionBreakpointAssert) bpExpression;

      if (state != null) {
        details = expAssert.getDetails(state);
      }

      bpExpressionNormalized = expAssert.getNormalizedExpression();
      normalizedPosition = expAssert.getNormalizedPosition();
      normalizedCondition = expAssert.getNormalizedCondition();
    }

    return new AssertStatusImpl(bpID, bpName, bpHitCounter, bpHitCounterTotal, lowerBound, upperBound, breakpointState,
                                bpExpressionStr,
                                bpExpressionNormalized, details, position, condition, normalizedPosition, normalizedCondition);
  }

}
