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


import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;

/**
 * The server must implement this interface and the client uses it to start or stop execution and to do stepping.
 */
public interface CommandsInterface {

  /**
   * Blocks until JPF is stopped. If JPF is not yet started, it returns immediately. If JPF has started but is currently
   * paused, it returns immediately.
   */
  void waitUntilStopped();


  /**
   * Indicates whether a JPF instance has started but its execution is currently stopped. This is the only time
   * at which program state inspection can take place.
   */
  boolean isPaused();
  /**
   * Stops an execution (like on breakpoint).
   */
  void stop() throws JPFInspectorException;

  /**
   * Resumes stopped execution.
   */
  void start() throws JPFInspectorException;

  /**
   * Causes the JPF to terminate at the earliest opportunity.
   */
  void requestTermination();


  /**
   * Indicates how many instructions should be skipped when stepping.
   * Full details explaining what each transition step means are in the documentation of Commands.
   */
  enum StepType {
    /**
     * Step to any choice generator.
     */
    ST_TRANSITION_ALL,
    /**
     * Step to a data choice generator.
     */
    ST_TRANSITION_DATA,
    /**
     * Step to a scheduling choice generator.
     */
    ST_TRANSITION_SCHED,
    /**
     * Step a single instruction.
     */
    ST_INSTRUCTION,
    /**
     * Step until you are on another line.
     */
    ST_LINE,
    /**
     * Step into a called method.
     */
    ST_STEP_IN,
    /**
     * Step out of the current method.
     */
    ST_STEP_OUT,
    /**
     * Step back until you reach the last breakpoint that was hit.
     */
    BACK_BREAKPOINT_HIT,
    /**
     * Step back until you reach a field access instruction for the given field.
     */
    BACK_FIELD_ACCESS,
  }


  /**
   * Starts a forward step.
   *
   * @param type How far should we step forward.
   */
  void forwardStep(StepType type) throws JPFInspectorException;

  void backstepTransition(StepType transitionKind, int count) throws JPFInspectorGenericErrorException;

  /**
   * Starts a backwards step.
   * @param type How far should we backtrack.
   */
  void backstep(StepType type) throws JPFInspectorException;


  /**
   * Starts a back_field_access backwards step.
   * @param fieldNameExpression Field name, see documentation for back_field_access.
   */
  void backFieldAccessStep(String fieldNameExpression) throws JPFInspectorGenericErrorException;

}
