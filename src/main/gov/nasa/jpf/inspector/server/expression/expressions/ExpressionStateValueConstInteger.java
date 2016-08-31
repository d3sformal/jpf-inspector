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

/**
 * @author Alf
 * 
 */
public class ExpressionStateValueConstInteger extends ExpressionStateValueConst {

  private static final int HEXADECIMAL = 16;

  /**
   * The value has to be string in one of the following formats
   * 10, +10, -10, 0xFF, +0xFF, -0xFF
   *
   * @param value An integer value written as text.
   * @return Return value converted to integer or exception if value is out of the integer range or input is malformed.
   */
  public static int convertToIntegerWrapped (String value) {
    try {
      return ExpressionStateValueConstInteger.convertToInteger(value);
    } catch (JPFInspectorParsingErrorException e) {
      throw new JPFInspectorRuntimeParsingException(e);
    }
  }

  private static int convertToInteger(String value) throws JPFInspectorParsingErrorException {
    assert value != null;
    assert !value.isEmpty();

    String val = value;
    char c0 = val.charAt(0);
    if (c0 == '+') {
      val = val.substring(1); // Ignore initial '+' character
    }
    boolean startWithMinus = c0 == '-';

    // Decide type of the input HEX or INT
    if (isHEX(val)) {
      if (startWithMinus) {
        val = "-" + val.substring(3);
      } else {
        val = val.substring(2);
      }
      try {
        return Integer.valueOf(val, HEXADECIMAL);
      } catch (NumberFormatException nfe) {
        throw new JPFInspectorParsingErrorException("Invalid hex integer value. Value is probably out of the integer range.", value, 0);
      }
    } else {
      try {
        // Decimal number
        return Integer.valueOf(val, 10);
      } catch (NumberFormatException nfe) {
        throw new JPFInspectorParsingErrorException("Invalid integer value. Value is probably out of the integer range.", value, 0);
      }
    }
  }

  /**
   * Checks if represented number starts with [+|-]?0x
   * @param value A string that might look like a hexadecimal integer.
   * @return True if the string starts with 0x, +0x or -0x.
   */
  public static boolean isHEX (String value) {
    assert value != null;
    assert !value.isEmpty();

    char c0 = value.charAt(0);
    if (c0 == '0') {
      if (value.length() == 1) {
        // 0
        return false;
      }

      char c1 = value.charAt(1);
      // 0x
      return (c1 == 'x') || (c1 == 'X');
    }

    if ((c0 == '+') || (c0 == '-')) {
      assert (value.length() > 1);
      char c1 = value.charAt(1);

      if (c1 == '0') {
        if (value.length() == 2) {
          // [+|-]0
          return false;
        }

        char c2 = value.charAt(2);
        // [+|-]0x
        return (c2 == 'x') || (c2 == 'X');
      }

      return false;
    }

    return false;
  }

  private final int value;

  /**
   * Initializes a new instance of {@link ExpressionStateValueConstInteger}.
   * @param value The integer constant that this expression represents.
   */
  public ExpressionStateValueConstInteger (int value) {
    this.value = value;
  }

  /*
   * @see gov.nasa.jpf.inspector.server.expression.ExpressionStateRootNode#getResultExpression(gov.nasa.jpf.inspector.server.jpf.JPFInspector,
   * gov.nasa.jpf.inspector.server.expression.InspectorState)
   */
  @Override
  public StateNodeInterface getResultExpression (JPFInspector inspector, InspectorState state) throws JPFInspectorException {
    return new StateReadableConstValue(inspector, ClassLoaderInfo.getCurrentResolvedClassInfo("int"), value);
  }

  /* @see gov.nasa.jpf.inspector.server.expression.ExpressionNodeInterface#getNormalizedExpression() */
  @Override
  public String getNormalizedExpression () {
    return Integer.toString(value);
  }

}
