///
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
///

package gov.nasa.jpf.inspector.server.expression;

/**
 * For attribute access, local access and field access hit conditions,
 * indicates what kind of instructions we are interested in.
 */
public enum AccessMode {
  /**
   * Any instruction that access what we are interested in will do.
   */
  ANY_ACCESS,
  /**
   * Only instructions that read the value will trigger the hit condtion.
   */
  READ,
  /**
   * Only instructions that set the value will trigger the hit condition.
   */
  WRITE
}
