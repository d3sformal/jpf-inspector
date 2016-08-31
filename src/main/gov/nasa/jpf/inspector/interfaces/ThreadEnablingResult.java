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
 * Represents the result of an "enable thread" or "disable thread" operation.
 */
public enum ThreadEnablingResult {
  /**
   * A thread's suppression status was flipped.
   */
  THREAD_SUCCESSFULLY_CHANGED_STATE,
  /**
   * If enabling, the thread was already enabled. If disabling, the thread was already disabled.
   */
  THREAD_STATE_UNCHANGED
}
