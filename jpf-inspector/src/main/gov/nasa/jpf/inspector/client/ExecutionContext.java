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

package gov.nasa.jpf.inspector.client;

/**
 * Indicates from where a command was executed.
 */
public enum ExecutionContext {
  /**
   * The command was executed from the Swing terminal.
   */
  FROM_SWING_TERMINAL,
  /**
   * The command was executed from the commnad-line terminal.
   */
  FROM_COMMAND_LINE_TERMINAL
}
