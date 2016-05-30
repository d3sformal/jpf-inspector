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

import gov.nasa.jpf.inspector.interfaces.BreakPointCreationInformation;
import gov.nasa.jpf.inspector.interfaces.InspectorCallBacks;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionBreakpointAssert;

/**
 * @author Alf
 * 
 */
public class InternalAssertHolder extends InternalBreakpointHolder {

  private final String position;
  private final String condition;

  public InternalAssertHolder (int newID, InspectorCallBacks callbacks, boolean userBP, boolean sigleHitBP, String position, String condition) {
    super(newID, callbacks, userBP, sigleHitBP);
    this.position = position;
    this.condition = condition;
  }

  @Override
  public boolean isAssert () {
    return true;
  }

  public void modifyBPSettings (BreakPointCreationInformation newSettings, ExpressionBreakpointAssert bpExpAsser) {
    super.modifyBPSettings(newSettings, bpExpAsser);
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

    return new AssertStatusImpl(bpID, bpName, bpHitCounter, bpHitCounterTotal, lowerBound, upperBound, bpState, bpMode, bpExpressionStr,
        bpExpressionNormalized, details, position, condition, normalizedPosition, normalizedCondition);
  }

}
