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
package gov.nasa.jpf.inspector.common;

import gov.nasa.jpf.inspector.interfaces.BreakpointCreationInformation;
import gov.nasa.jpf.inspector.interfaces.BreakpointState;

/**
 * Represents the "create breakpoint" command arguments.
 * This class is instantiated in the ANTLR console grammar: take care when refactoring.
 * This class is also instantiated by some classes in the server package.
 */
public class BreakpointCreationExpression implements BreakpointCreationInformation {

  private static final long serialVersionUID = -2742213729061140415L;

  // Null values means not set by user.
  private String bpDefExpression = null;

  private BreakpointState bpState = null;
  private String bpName = null;

  private Integer lowerBound = null;
  private Integer upperBound = null;

  @Override
  public int getBPID () {
    return BreakpointCreationInformation.BP_ID_NOT_DEFINED;
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
  public Integer bpHitCountLowerBound () {
    return lowerBound;
  }

  @Override
  public Integer bpHitCountUpperBound () {
    return upperBound;
  }

  @Override
  public String getBPExpression () {
    return bpDefExpression;
  }

  public void setState (BreakpointState bpState) {
    assert bpState != null;
    this.bpState = bpState;
  }

  public void setName (String bpName) {
    assert bpName != null;
    this.bpName = bpName;
  }

  public void setBPExpression (String bpDefExpression) {
    assert bpDefExpression != null;
    this.bpDefExpression = bpDefExpression;
  }

  public void setBounds (Integer lower, String lowerSign, String upperSign, Integer upper) {
    if (lower != null) {
      assert lowerSign != null;
      lowerBound = lower;
      if ("<".equals(lowerSign)) {
        lowerBound++;
      }
    } else {
      lowerBound = DEFAULT_LOWER_BOUND;
    }

    if (upper != null) {
      assert upperSign != null;
      upperBound = upper;
      if ("<".equals(upperSign)) {
        upperBound--;
      }
    } else {
      upperBound = DEFAULT_UPPER_BOUND;
    }
  }

  public void setBounds (int lower, int upper) {
    lowerBound = lower;
    upperBound = upper;
  }

  private static String breakPointState2NormalizedString(BreakpointState bpState) {
    assert (bpState != null);

    switch (bpState) {
    case DISABLED:
      return "disable"; // dis
    case ENABLED:
      return "enable"; // en
    case LOGGING:
      return "log"; // log
    default:
      throw new RuntimeException("Internal error: Unknown " + bpState.getClass().getName() + " entry: " + bpState);
    }
  }

  /**
   * @return Gets normalized version of create breakpoint command, but without breakpoint expression (the part which is directly sent to server)
   */
  public static String getNormalizedExpressionPrefix (BreakpointCreationInformation bpc) {
    assert (bpc != null);

    StringBuilder sb = new StringBuilder(256);
    sb.append("create breakpoint");

    String name = bpc.getName();
    if (name != null) {
      sb.append(" name=");
      sb.append(name);
    }

    BreakpointState state = bpc.getState();
    if (state != null) {
      // State is specified
      sb.append(" state=");
      sb.append(breakPointState2NormalizedString(state));
    }

    Integer lowerBound = bpc.bpHitCountLowerBound();
    Integer upperBound = bpc.bpHitCountUpperBound();
    boolean hc_printed = false;
    if (lowerBound != null && !lowerBound.equals(BreakpointCreationInformation.DEFAULT_LOWER_BOUND)) {
      sb.append(' ');
      sb.append(lowerBound);
      sb.append("<=hit_count");
      hc_printed = true;
    }

    if (upperBound != null && !upperBound.equals(BreakpointCreationInformation.DEFAULT_UPPER_BOUND)) {
      if (hc_printed == false) {
        sb.append(" hit_count");
      }
      sb.append("<=");
      sb.append(upperBound);
    }

    return sb.toString();
  }

  @Override
  public String toString () {
    return getNormalizedExpressionPrefix(this) + ' ' + bpDefExpression;
  }

}
