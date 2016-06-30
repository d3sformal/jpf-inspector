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

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.jpf.StopHolder;

/**
 * This enumeration is used by callback handling classes.
 *
 * Note that the server itself doesn't keep track of its state. Thus, these are not "states" as much as they are
 * message that the Inspector's state changed. This enum is used just to inform the client.
 */
public enum InspectorStatusChange {
  /**
   * JPF becomes started at the completion of the method {@link JPFInspector#bindWithJPF(JPF)}.
   */
  JPF_STARTED,
  /**
   * JPF becomes running whenever it resumes execution after leaving the method {@link StopHolder#stopExecution(InspectorState)}.
   * Notably, the state JPF is in between its start and the first time it is stopped is JPF_STARTED, not JPF_RUNNING.
   */
  JPF_RUNNING,
  /**
   * JPF becomes stopped immediately befpre the wait() call inside the method {@link StopHolder#stopExecution(InspectorState)}.
   */
  JPF_STOPPED,
  /**
   * JPF becomes terminating when the method {@link StopHolder#notifyClientTerminating()} is run. This method is
   * called when JPF wants to terminate or when the Search object does.
   */
  JPF_TERMINATING
}
