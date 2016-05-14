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

package gov.nasa.jpf.inspector.server.breakpoints;

public enum BreakPointModes {
  BP_MODE_NONE, // / No condition is associated
  /**
   * On data choice, scheduler choice, execution end
   */
  BP_MODE_CHOICE_SCHEDULING, // New scheduling choice takes place
  BP_MODE_CHOICE_DATA, // New data choice takes place
  BP_MODE_CHOICE_BOTH,
  BP_MODE_PROPERTY_VIOLATED,
  BP_MODE_GC_BEGIN,
  BP_MODE_GC_END,
  BP_MODE_GC_BOTH,
  BP_MODE_OBJECT_CREATED,
  BP_MODE_OBJECT_RELEASED,
  BP_MODE_SPECIFIC_INSTRUCTION_TYPE,
  BP_MODE_EXCEPTION_THROWN,
  BP_MODE_NOTIFY, // TODO
  BP_MODE_SYNC_BLOCK, // TODO /// Thread is blocked
  BP_MODE_POSITION_ONLY,
  BP_MODE_THREAD_SCHEDULED_IN,
  BP_MODE_THREAD_SCHEDULED_OUT,
  BP_MODE_THREAD_SCHEDULED_BOTH,
  BP_MODE_FIELD_ACCESS_READ,
  BP_MODE_FIELD_ACCESS_WRITE,
  BP_MODE_FIELD_ACCESS, // Both read or write
  BP_MODE_METHOD_INVOKE,
  BP_MODE_STATE_ADVANCED,
  BP_MODE_USER_CONDITION,

  BP_MODE_INTERNAL_INSTRUCTION,
  BP_MODE_INTERNAL_STEP_OUT,
  BP_MODE_INTERNAL_STEP_OVER
}
