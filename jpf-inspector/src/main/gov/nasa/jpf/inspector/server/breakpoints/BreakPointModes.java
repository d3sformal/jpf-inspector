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

/**
 * Breakpoint modes are used by some hit conditions internally. However, it is a legacy mechanism, doesn't work well
 * with the operator "and" and they should be avoided for any new hit conditions. Do not rely on them in new code.
 */
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
  BP_MODE_NOTIFY, // There was a "to-do" marker next to this value. I don't know why.
  BP_MODE_SYNC_BLOCK, // There was a message here: "Thread is blocked". I don't know what it means.
  BP_MODE_POSITION_ONLY,
  BP_MODE_THREAD_SCHEDULED_IN,
  BP_MODE_THREAD_SCHEDULED_OUT,
  BP_MODE_THREAD_SCHEDULED_BOTH,
  BP_MODE_FIELD_ACCESS_READ,
  BP_MODE_FIELD_ACCESS_WRITE,
  BP_MODE_FIELD_ACCESS, // Both read or write
  BP_MODE_METHOD_INVOKE,
  BP_MODE_STATE_ADVANCED,
  /**
   * The breakpoint's mode is not important.
   */
  BP_MODE_USER_CONDITION,
  /**
   * No special meaning.
   * Indicates the "specific_instruction" hit condition.
   */
  BP_MODE_INTERNAL_INSTRUCTION,
  BP_MODE_INTERNAL_STEP_OUT,
  BP_MODE_INTERNAL_STEP_OVER,
  /**
   * No special meaning.
   * This enumeration exists for legacy reasons only so there is no reason to add additional enumeration values here
   * for new hit conditions.
   */
  BP_UNIMPORTANT
}
