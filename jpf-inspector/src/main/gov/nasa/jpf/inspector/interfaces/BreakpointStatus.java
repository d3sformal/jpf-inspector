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

import gov.nasa.jpf.inspector.server.breakpoints.BreakPointModes;

/**
 * All information about a specific breakpoint. The user of this interface can merely read the information,
 * as this interface only provides getters.
 */
public interface BreakpointStatus extends BreakpointCreationInformation {

  /**
   * Gets the breakpoint's unique identifier.
   */
  @Override
  int getBPID();

  /**
   * Counts number of the breakpoint invocation position. (How many times has been location specified in condition reached by the current trace)
   */
  int getHitCounter();

  /**
   * Gets total number of the breakpoint invocation. How many times has been breakpoint reached
   */
  int getHitCounterTotal();

  BreakPointModes getBPMode();

  /**
   * Additional information about the Breakpoint for user.
   */
  String getDetails();

  String getNormalizedBreakpointExpression();
}
