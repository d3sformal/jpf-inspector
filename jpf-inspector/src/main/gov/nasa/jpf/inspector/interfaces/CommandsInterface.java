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


import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;

public interface CommandsInterface {

  static public enum InspectorStates {
    JPF_STARTED,
    JPF_RUNNING,
    JPF_STOPPED,
    JPF_TERMINATING
  }

  /**
   * Stops an execution (like on breakpoint)
   */
  public void stop () throws JPFInspectorException;

  /**
   * Resumes stopped execution
   */
  public void start () throws JPFInspectorException;

  static public enum StepType {
    ST_TRANSITION_ALL,
    ST_TRANSITION_DATA,
    ST_TRANSITION_SCHED,
    ST_INSTRUCTION,
    ST_LINE,
    ST_STEP_IN,
    ST_STEP_OUT
  }

  public void forwardStep (StepType type) throws JPFInspectorException;

  public void backwardStep (StepType type) throws JPFInspectorException;

}
