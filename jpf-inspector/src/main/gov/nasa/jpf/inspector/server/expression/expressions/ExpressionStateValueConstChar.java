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

package gov.nasa.jpf.inspector.server.expression.expressions;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorParsingErrorException;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.programstate.StateNodeInterface;
import gov.nasa.jpf.inspector.server.programstate.StateReadableConstValue;
import gov.nasa.jpf.inspector.utils.parser.JPFInspectorRuntimeParsingException;
import gov.nasa.jpf.vm.ClassLoaderInfo;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a character literal as a hierarchy-1 expression.
 */
@SuppressWarnings("MagicNumber") // I'm not touching this...
public class ExpressionStateValueConstChar extends ExpressionStateValueConst {

  public static char convertToCharWrapped (String str) throws JPFInspectorRuntimeParsingException {
    try {
      return ExpressionStateValueConstChar.convertToChar(str);
    } catch (JPFInspectorParsingErrorException e) {
      throw new JPFInspectorRuntimeParsingException(e);
    }
  }

  /**
   * Converts string representing character (from the parser) to real character.
   * (like \u1234 or \032)
   * 
   * @param str Parsed string in one of the following forms. ('a', '\n', '\032' '\u0020'). Note initial and terminating QUOTE characters are required.
   * @return Character from the input string.
   * 
   * @throws JPFInspectorParsingErrorException
   */
  public static char convertToChar (String str) throws JPFInspectorParsingErrorException {
    assert (str != null);

    char result;
    CharParsingState cps = new CharParsingState(str, '\'');

    // Check initial '
    if (cps.strArr.length < 1) {
      throw new JPFInspectorParsingErrorException("Empty character", str, 0);
    }

    if (cps.strArr[0] != '\'') {
      throw new JPFInspectorParsingErrorException("Error while parsing character at 0. The quote (\') character is expected in the beginning.", str, 0);
    }
    cps.pos++;

    result = parseCharRepresentation(cps);

    if (cps.pos >= cps.strArr.length) {
      throw new JPFInspectorParsingErrorException("Error while parsing character at " + (str.length()) + ". The terminating quote (\') character is expected.",
          str, str.length());
    }

    if (cps.strArr[cps.pos] != '\'') {
      throw new JPFInspectorParsingErrorException("Error while parsing character at " + (cps.pos) + ". The terminating quote (\') character is expected.", str,
          cps.pos);
    }

    if (cps.pos < cps.strArr.length - 1) {
      throw new JPFInspectorParsingErrorException("Error while parsing character at " + (cps.pos + 1) + ". Garbage after terminating quote (\').", str,
          cps.pos + 1);
    }

    return result;
  }

  /**
   * Auxiliary class - we are using our own parser to parse escape characters inside the literal.
   */
  @SuppressWarnings("PublicField") // This is just a data holder object.
  public static final class CharParsingState {
    /**
     * String to parse
     */
    public final char[] strArr;
    /**
     * Position where starts unparsed input (note modified by parsers)
     */
    public int pos;

    /**
     * List of chars which could not be used without escaping in plain text (for chars - ', for strings - ")
     */
    public final Set<Character> bannedUnescapedCharacters;

    public CharParsingState (String str, char bannedUnsescapedChar) {
      this.strArr = str.toCharArray();
      this.pos = 0;
      this.bannedUnescapedCharacters = new HashSet<>();
      bannedUnescapedCharacters.add(bannedUnsescapedChar);
    }
  }

  /**
   * Gets next char from input string and updates parser state
   * 
   * @param cps Process first unprocessed character. Specification of character can be in one of the following forms. (a, \n, \032 (octal) (hexa).
   * 
   * @return Parsed character
   * @throws JPFInspectorParsingErrorException if error is found
   */
  public static char parseCharRepresentation (CharParsingState cps) throws JPFInspectorParsingErrorException {
    assert (cps != null);

    assert (cps.pos < cps.strArr.length);

    char c = cps.strArr[cps.pos];
    cps.pos++;

    if (c != '\\') {
      if (cps.bannedUnescapedCharacters.contains(c)) {
        throw new JPFInspectorParsingErrorException("Error while parsing character at " + cps.pos + ". This character has to be escaped.", new String(
            cps.strArr), cps.pos);
      }
      // Ordinary character
      return c;
    } else {
      // Escaped chars \b \t \n \f \r \" \' \\
      // or \377 -> octal code
      // or \uFFFF

      if (cps.pos >= cps.strArr.length) {
        // Invalid escape sequence ... \ char as last character in the input (no escaped character)
        throw new JPFInspectorParsingErrorException("Error while parsing character at " + cps.pos
            + ". No escape character specified. Escaped character expected.", new String(cps.strArr), cps.pos);
      }
      c = cps.strArr[cps.pos];
      cps.pos++;

      if (c == 'b') {
        return '\b';
      } else if (c == 't') {
        return '\t';
      } else if (c == 'n') {
        return '\n';
      } else if (c == 'f') {
        return '\f';
      } else if (c == 'r') {
        return '\r';
      } else if (c == '"') {
        return '\"';
      } else if (c == '\'') {
        return '\'';
      } else if (c == '\\') {
        return '\\';
      } else if (c == 'u') {
        // \uFFFF -> HEXA
        if (cps.strArr.length < cps.pos + 4) {
          // Invalid input
          throw new JPFInspectorParsingErrorException("Error while parsing character at " + cps.strArr.length
              + ". Hexa representation of char malformed (\\uFFFF) like character is expected.", new String(cps.strArr), cps.strArr.length);
        }
        int hexVal = 0;
        for (int i = 0; i < 4; i++) {
          char cx = cps.strArr[cps.pos];
          cx = Character.toLowerCase(cx);

          if (cx >= '0' && cx <= '9') {
            hexVal = (hexVal * 16) + (cx - '0');
          } else if (cx >= 'a' && cx <= 'f') {
            hexVal = (hexVal * 16) + (cx - 'a' + 10);
          } else {
            // Not e hexa char
            throw new JPFInspectorParsingErrorException("Error while parsing character at " + (cps.pos)
                + " - non hexa character found. The \\uFFFF like character is expected.", new String(cps.strArr), cps.pos);
          }
          cps.pos++;

        }
        return (char) hexVal;
      } else if (c >= '0' && c <= '7') {
        // Octal num 0 .. 377 // parsed eagerly
        int numsLeft = 2; // Maximal number of octal numbers to be processed
        if (c >= '0' && c <= '3') {
          numsLeft++;
        }
        if (cps.pos + numsLeft > cps.strArr.length) {
          numsLeft = cps.strArr.length - cps.pos;
        }

        assert (numsLeft > 0);

        int octVal = 0;
        cps.pos--; // to process actual character in c variable
        for (int i = 0; i < numsLeft; i++) {
          char cx = cps.strArr[cps.pos];

          if (cx >= '0' && cx <= '7') {
            octVal = (octVal * 8) + (cx - '0');
          } else {
            break;
          }

          cps.pos++;
        }
        return (char) octVal;
      } else {
        // invalid escape sequence
        throw new JPFInspectorParsingErrorException("Error while parsing character at " + (cps.pos - 1) + ". Invalid escape sequence.", new String(cps.strArr),
            cps.pos - 1);
      }
    }
  }

  /**
   * Converts char to "normalized grammar representations".
   * 
   * Chars like \t \n are converted to escaped version "\\n" string, other chars lower then 32 are converted to unicode \u1234 representation, and same for
   * chars greater than 1024.
   * 
   * @param c Character to convert.
   */
  public static String unparseChar (char c) {
    // 1 step - escaping characters
    // Escaped chars \b \t \n \f \r \" \' \\
    if (c == '\b') {
      return "\\b";
    }
    if (c == '\t') {
      return "\\t";
    }
    if (c == '\n') {
      return "\\n";
    }
    if (c == '\f') {
      return "\\f";
    }
    if (c == '\"') {
      return "\\\"";
    }
    if (c == '\'') {
      return "\\\'";
    }
    if (c == '\\') {
      return "\\\\";
    }
    // 2 step - convert to unicode represenation

    if (c < 16) {
      return "\\u000" + Integer.toHexString(c);
    }
    if (c < 32) {
      return "\\u00" + Integer.toHexString(c);
    }

    if (c > 0x0FFF) {
      return "\\u" + Integer.toHexString(c);
    }
    if (c >= 1024) {
      return "\\u0" + Integer.toHexString(c);
    }

    return String.valueOf(c);
  }

  private final char c; // Represented value

  /**
   * @param c Represented character
   */
  public ExpressionStateValueConstChar (char c) {
    this.c = c;
  }

  /*
   * @see gov.nasa.jpf.inspector.server.expression.ExpressionStateRootNode#getResultExpression(gov.nasa.jpf.inspector.server.jpf.JPFInspector,
   * gov.nasa.jpf.inspector.server.expression.InspectorState)
   */
  @Override
  public StateNodeInterface getResultExpression (JPFInspector inspector, InspectorState state) throws JPFInspectorException {
    return new StateReadableConstValue(inspector, ClassLoaderInfo.getCurrentResolvedClassInfo("char"), c);
  }

  /* @see gov.nasa.jpf.inspector.server.expression.ExpressionNodeInterface#getNormalizedExpression() */
  @Override
  public String getNormalizedExpression () {
    return '\'' + unparseChar(c) + '\'';
  }

}
