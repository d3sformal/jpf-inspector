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
