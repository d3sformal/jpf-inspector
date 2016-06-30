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
