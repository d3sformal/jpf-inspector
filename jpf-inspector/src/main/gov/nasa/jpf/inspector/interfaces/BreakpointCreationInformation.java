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

package gov.nasa.jpf.inspector.interfaces;

import java.io.Serializable;

/**
 * All information typed by the user who executed the "create breakpoint" command.
 */
public interface BreakpointCreationInformation extends Serializable {

  Integer DEFAULT_LOWER_BOUND = 0; // Default value of the lower bound of the hit counter
  Integer DEFAULT_UPPER_BOUND = Integer.MAX_VALUE;

  /**
   * Used as undefined value of the breakpoint ID. This value specify that you
   * creates new breakpoint and Inspector assign ID alone.
   * The JPF never returns breakpoint with this ID.
   */
  int BP_ID_NOT_DEFINED = -1;

  /**
   * Get unique BP identifier
   */
  int getBPID();

  /**
   * @return String representation of the expression used to create/update the breakpoint.
   *         Note: This expression is parsed by the server part.
   */
  String getBPExpression();

  /**
   * Gets the name of the breakpoint, if any. If no name was specified, returns null.
   */
  String getName();

  /**
   * Gets the action that should occur when the breakpoint is hit.
   */
  BreakpointState getState();

  /**
   * Specify lower bound for breakpoint's hitCounter.
   * The breakpoint will only if the {@link BreakpointStatus#getHitCounter()} will be equal or higher then specified number.
   * <br>Note: The Upper bound condition must hold too to hit the breakpoint
   * 
   * @return Lower bound of hitCounter to hit the breakpoint.
   */
  Integer bpHitCountLowerBound();

  /**
   * Specify upper bound for breakpoint's hitCounter.
   * The breakpoint will only if the {@link BreakpointStatus#getHitCounter()} will be equal or lower then specified number.
   * <br>Note: The lower bound condition must hold too to hit the breakpoint
   * 
   * @return Upper bound of hitCounter to hit the breakpoint.
   */
  Integer bpHitCountUpperBound();

}
