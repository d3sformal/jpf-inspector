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


import gov.nasa.jpf.JPF;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.jpf.StopHolder;
import gov.nasa.jpf.search.Search;

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
    ST_TRANSITION_ALL,
    ST_TRANSITION_DATA,
    ST_TRANSITION_SCHED,
    /**
     * Step a single instruction.
     */
    ST_INSTRUCTION,
    /**
     * Step until you are on the next line.
     */
    ST_LINE,
    ST_STEP_IN,
    ST_STEP_OUT
  }


  void forwardStep(StepType type) throws JPFInspectorException;

  void backwardStep(StepType type) throws JPFInspectorException;

}
