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

import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorParsingErrorException;

import java.util.List;

/**
 * Server-side component that handles breakpoint management.
 */
public interface BreakPointManagerInterface {

  /**
   * @return Gets list with currently existing Breakpoints. Never gets null, if no breakpoint exists, then empty list is returned.
   */
  List<BreakpointStatus> getBreakpoints();

  /**
   * Creates new (if breakpoint ID is set {@link BreakpointCreationInformation#BP_ID_NOT_DEFINED} or modifies an existing breakpoint.
   * 
   * @param newBP Structure with new information about new BP.
   * @return Get informations about state of the created/modified breakpoint or null if error takes place.
   */
  BreakpointStatus createBreakPoint(BreakpointCreationInformation newBP) throws JPFInspectorParsingErrorException, JPFInspectorGenericErrorException;

  /**
   * Creates new assertion (if breakpoint ID is set {@link BreakpointCreationInformation#BP_ID_NOT_DEFINED} or modifies existing assertion ( with same ID)
   * 
   * @param newAssert Structure with new information about new Assert.
   * @return Get informations about state of the created/modified assertion or null if error takes place.
   */
  AssertStatus createAssert(AssertCreationInformation newAssert) throws JPFInspectorParsingErrorException, JPFInspectorGenericErrorException;

  /**
   * Deletes the breakpoint with the specified ID.
   *
   * @param bpID The breakpoint's ID.
   * @return True if the breakpoint was deleted; false if the breakpoint did not exist.
   */
  boolean deleteBreakpoint(int bpID);

  /**
   * Changes the state of a breakpoint.
   *
   * @param breakpointId The breakpoint's ID.
   * @param newState The new state that the breakpoint should assume.
   * @return True if the breakpoint's state changed; false if the breakpoint does not exist.
   */
  boolean changeBreakpointState(int breakpointId, BreakpointState newState);
}
