//
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


/**
 * Indicates whether a thread should be permitted to be scheduled when a choice generator chooses a thread to schedule.
 */
public enum ThreadSuppressionStatus {
  /**
   * The thread should be allowed to be scheduled to be run. This is the default state.
   */
  SCHEDULE_AS_NORMAL,
  /**
   * The thread cannot be scheduled to run.
   */
  DO_NOT_SCHEDULE;


  @Override
  public String toString() {
    if (this == SCHEDULE_AS_NORMAL) {
      return "enabled";
    } else {
      return "disabled";
    }
  }
}
