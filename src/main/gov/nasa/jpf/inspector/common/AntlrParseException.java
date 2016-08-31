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
 * The runtime exception thrown by ANTLR itself when it encounters an error. This exception's data is then used
 * by other exception classes.
 */
public class AntlrParseException extends RuntimeException {
  private final int column;

  public AntlrParseException(String msg, int column) {
    super(msg);
    this.column = column;
  }

  public int getColumn() {
    return column;
  }
}
