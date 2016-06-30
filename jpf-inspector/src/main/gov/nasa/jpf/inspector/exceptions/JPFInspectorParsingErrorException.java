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

package gov.nasa.jpf.inspector.exceptions;

import org.antlr.runtime.RecognitionException;

/**
 * Represents any error during parsing.
 */
public class JPFInspectorParsingErrorException extends JPFInspectorException {

  public static final int DEFAULT_LINE_LENGTH = 80;

  private static final long serialVersionUID = 4938978736234959309L;

  private final String expression;
  private final int position;

  /**
   * Initializes a new instance of this exception.
   *
   * @param msg Message to display to the user.
   * @param expression The command or expression at which the parsing error occured.
   *                   If null, then the expression is not printed back to the user.
   * @param position Zero based position index where error takes place. If the expression is null, then the position is ignored.
   */
  public JPFInspectorParsingErrorException (String msg, String expression, int position) {
    super(msg);
    this.expression = expression;
    this.position = position;
  }

  public JPFInspectorParsingErrorException (String msg, String expression, RecognitionException recognitionException) {
    super(msg);
    this.expression = expression;
    this.position = recognitionException.charPositionInLine;
  }

  public String getExpression () {
    return expression;
  }

  public int getPosition () {
    return position;
  }

  private static final int LAST_LINE_SPACE = 15; // How many spaces should be left before "^" character

  public String expressError (int maxLineLength) {
    assert (maxLineLength >= 6);

    if (expression == null) {
      return "";
    }
    if (position < 0 || position > expression.length()) {
      return expression;
    }

    int linePos = position % maxLineLength;
    int linesToPrint = (position / maxLineLength) + 1;

    StringBuilder sb = new StringBuilder((linesToPrint + 1) * (maxLineLength + 1));
    for (int i = 0; i < linesToPrint - 1; i++) {
      sb.append(expression.substring(i * maxLineLength, (i + 1) * maxLineLength));
      sb.append('\n');
    }

    if (linePos < maxLineLength - 3) {
      // command text...
      // ^
      if ((linesToPrint * maxLineLength) < expression.length()) {
        sb.append(expression.substring((linesToPrint - 1) * maxLineLength, linesToPrint * maxLineLength - 3));
        sb.append("...\n");
      } else {
        // Last line is not completely full
        sb.append(expression.substring((linesToPrint - 1) * maxLineLength));
        sb.append('\n');
      }
      for (int i = 0; i < linePos; i++) {
        sb.append(' ');
      }
      sb.append('^');
    } else {
      // (linePos >= maxLineLength - 3) {
      // Append full last line
      sb.append(expression.substring((linesToPrint - 1) * maxLineLength, linesToPrint * maxLineLength));
      sb.append('\n');

      // the last line -> command text
      // marker line ---> texti... ^

      // Calculate how many chars can overflow to marker line
      int lastLineMaxLen = maxLineLength - LAST_LINE_SPACE - 3;
      if (lastLineMaxLen < 0) {
        lastLineMaxLen = 0;
      }
      int lastLineLen = expression.length() - (linesToPrint * maxLineLength);
      if (lastLineLen > lastLineMaxLen) {
        lastLineLen = lastLineMaxLen;
      }
      sb.append(expression.substring(linesToPrint * maxLineLength, linesToPrint * maxLineLength + lastLineLen));
      sb.append("...");
      // sb.append()
      for (int i = lastLineLen + 3; i < linePos; i++) {
        sb.append(' ');
      }
      sb.append('^');
    }
    return sb.toString();
  }
}
