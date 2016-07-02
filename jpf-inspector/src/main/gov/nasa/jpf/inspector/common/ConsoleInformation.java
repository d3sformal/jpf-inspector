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

package gov.nasa.jpf.inspector.common;

/**
 * Contains static information about the current Inspector client console.
 */
public final class ConsoleInformation {
  /**
   * Represents the maximum length that a user-input text can have before it's broken on another line.
   *
   * It works like this:
   * If the user's command or expression within the command ends with a parse error, we want to report the position
   * at which the error occurred. For example, if the user writes "create breakpoint invalidcontinuation", something
   * like this will be displayed:
   *
   * ERR: Unexpected 'invalidcontinuation'
   * create breakpoint invalidcontinuation"
   *                   ^
   *
   * Notice the little arrow pointing at the position of the error. If the line the arrow is pointing to is greater
   * than MAX_ERROR_LINE_LENGTH, then it should be broken up into several lines.
   */
  public static final int MAX_ERROR_LINE_LENGTH = 50;
}
