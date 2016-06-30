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
