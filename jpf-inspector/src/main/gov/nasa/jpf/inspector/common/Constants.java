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

import gov.nasa.jpf.inspector.common.pse.PSEThread;

import java.awt.*;

/**
 * Contains constants used throughout JPF Inspector.
 */
public final class Constants {
  /**
   * A {@link PSEThread} has this value in its {@link PSEThread#getThreadTypeName()} getter if the corresponding
   * ThreadInfo object does not have an associated class info.
   */
  public static final String UNKNOWN_THREAD_TYPE_NAME = "(unspecified class)";
  /**
   * The prompt is shown to the user in both graphical shells and is echoed to the output in batch mode
   * if the echo_input option is set.
   */
  public static final String PROMPT = "cmd>";

  public static final Font fontMonospaced = new Font(Font.MONOSPACED, Font.PLAIN, 12);

}
