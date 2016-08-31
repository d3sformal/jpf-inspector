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

import gov.nasa.jpf.inspector.interfaces.BreakpointState;
import gov.nasa.jpf.inspector.interfaces.BreakpointStatus;


/**
 * Provides read-only information about a breakpoint.
 */
public class BreakpointStatusImplementation implements BreakpointStatus {

  private static final long serialVersionUID = 5739124503398446657L;

  private final int bpID; // ID of the breakpoint

  private BreakpointState bpState = null;
  private String bpName = null;

  private String bpExpression = null;
  private String bpExpressionNormalized = null;

  private int bpHitCounter = 0;
  private int bpHitCounterTotal = 0;

  private Integer lowerBound = null;
  private Integer upperBound = null;

  private String details = null;

  // Common constructor
  public BreakpointStatusImplementation(int bpID, String bpName, int bpHitCounter, int bpHitCounterTotal, Integer lowerBound, Integer upperBound,
                                        BreakpointState bpState, String bpExpression, String bpExpressionNormalized, String details) {
    this.bpID = bpID;
    this.bpName = bpName;
    this.bpHitCounter = bpHitCounter;
    this.bpHitCounterTotal = bpHitCounterTotal;
    this.bpState = bpState;
    this.bpExpression = bpExpression;
    this.bpExpressionNormalized = bpExpressionNormalized;
    this.upperBound = upperBound;
    this.lowerBound = lowerBound;
    this.details = details;
  }

  @Override
  public int getBPID () {
    return bpID;
  }

  @Override
  public int getHitCounter () {
    return bpHitCounter;

  }

  @Override
  public int getHitCounterTotal () {
    return bpHitCounterTotal;
  }

  @Override
  public String getName () {
    return bpName;
  }

  @Override
  public BreakpointState getState () {
    return bpState;
  }

  @Override
  public String getBPExpression () {
    return bpExpression;
  }

  @Override
  public Integer bpHitCountLowerBound () {
    return lowerBound;
  }

  @Override
  public Integer bpHitCountUpperBound () {
    return upperBound;
  }

  @Override
  public String getDetails () {
    return details;
  }

  @Override
  public String getNormalizedBreakpointExpression () {
    return bpExpressionNormalized;
  }

}
