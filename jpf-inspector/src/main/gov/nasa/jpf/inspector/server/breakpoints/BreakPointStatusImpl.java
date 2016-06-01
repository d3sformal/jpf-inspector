//
// Copyright (C) 2010 United States Government as represented by the
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

import gov.nasa.jpf.inspector.interfaces.BreakpointState;
import gov.nasa.jpf.inspector.interfaces.BreakPointStatus;

public class BreakPointStatusImpl implements BreakPointStatus {

  private static final long serialVersionUID = 5739124503398446657L;

  private final int bpID; // ID of the breakpoint

  private BreakPointModes bpMode = null;
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
  public BreakPointStatusImpl (int bpID, String bpName, int bpHitCounter, int bpHitCounterTotal, Integer lowerBound, Integer upperBound,
                               BreakpointState bpState, BreakPointModes bpMode, String bpExpression, String bpExpressionNormalized, String details) {
    this.bpID = bpID;
    this.bpName = bpName;
    this.bpHitCounter = bpHitCounter;
    this.bpHitCounterTotal = bpHitCounterTotal;
    this.bpState = bpState;
    this.bpMode = bpMode;
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
  public BreakPointModes getBPMode () {
    return bpMode;
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
