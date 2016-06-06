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
