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

import gov.nasa.jpf.inspector.interfaces.AssertStatus;
import gov.nasa.jpf.inspector.interfaces.BreakpointState;

/**
 * @author Alf
 * 
 */
public class AssertStatusImpl extends BreakPointStatusImpl implements AssertStatus {

  private static final long serialVersionUID = 4953042021810745621L;

  private final String position;
  private final String condition;

  private final String normalizedPosition;
  private final String normalizedCondition;

  public AssertStatusImpl (int bpID, String bpName, int bpHitCounter, int bpHitCounterTotal, Integer lowerBound, Integer upperBound, BreakpointState bpState,
      BreakPointModes bpMode, String bpExpression, String bpExpressionNormalized, String details, String position, String condition, String normalizedPosition,
      String normalizedCondition) {
    super(bpID, bpName, bpHitCounter, bpHitCounterTotal, lowerBound, upperBound, bpState, bpMode, bpExpression, bpExpressionNormalized, details);

    this.position = position;
    this.condition = condition;
    this.normalizedPosition = normalizedPosition;
    this.normalizedCondition = normalizedCondition;
  }

  @Override
  public String getPosition () {
    return position;
  }

  @Override
  public String getCondition () {
    return condition;
  }

  @Override
  public String getNormalizedPosition () {
    return normalizedPosition;
  }

  @Override
  public String getNormalizedCondition () {
    return normalizedCondition;
  }

}
