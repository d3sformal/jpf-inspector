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
 * Represents a literal "java.lang.Long" expression.
 */
public class ExpressionStateValueConstLong extends ExpressionStateValueConst {

  private static final int HEXADECIMAL_MODE = 16;

  /**
   * The value has to be string in one of the following formats
   * 10l, +10L, -10l, 0xFFL, +0xFFl, -0xFFl
   * 
   * @param value String representation of the number.
   * @return Return value converted to integer or exception if value is out of the integer range or input is malformed.
   */
  public static long convertToLongWrapped (String value) {
    try {
      return ExpressionStateValueConstLong.convertToLong(value);
    } catch (JPFInspectorParsingErrorException e) {
      throw new JPFInspectorRuntimeParsingException(e);
    }
  }

  private static long convertToLong(String value) throws JPFInspectorParsingErrorException {
    assert value != null;
    assert !value.isEmpty();

    String val = value;
    // Check if the last character is 'l' or 'L'
    char clast = val.charAt(val.length() - 1);
    if (clast != 'l' && clast != 'L') {
      throw new JPFInspectorParsingErrorException("Invalid long value. Does not terminate with \'l\' character.", value, value.length() - 1);
    }
    val = val.substring(0, val.length() - 1);

    if (val.isEmpty()) {
      throw new JPFInspectorParsingErrorException("Invalid long value. Does not contain a number value", value, 0);
    }

    char c0 = val.charAt(0);
    if (c0 == '+') {
      val = val.substring(1); // Ignore initial '+' character
    }
    boolean startWithMinus = c0 == '-';

    // Decide type of the input HEX or INT
    if (ExpressionStateValueConstInteger.isHEX(val)) {
      if (startWithMinus) {
        val = "-" + val.substring(3);
      } else {
        val = val.substring(2);
      }
      try {
        return Long.valueOf(val, HEXADECIMAL_MODE);
      } catch (NumberFormatException nfe) {
        throw new JPFInspectorParsingErrorException("Invalid hex integer value. Value is probably out of the integer range.", value, 0);
      }
    } else {
      try {
        // Decimal number
        return Long.valueOf(val, 10);
      } catch (NumberFormatException nfe) {
        throw new JPFInspectorParsingErrorException("Invalid integer value. Value is probably out of the integer range.", value, 0);
      }
    }
  }

  private final long value;

  public ExpressionStateValueConstLong (long value) {
    this.value = value;
  }

  /*
   * @see gov.nasa.jpf.inspector.server.expression.ExpressionStateRootNode#getResultExpression(gov.nasa.jpf.inspector.server.jpf.JPFInspector,
   * gov.nasa.jpf.inspector.server.expression.InspectorState)
   */
  @Override
  public StateNodeInterface getResultExpression (JPFInspector inspector, InspectorState state) throws JPFInspectorException {
    return new StateReadableConstValue(inspector, ClassLoaderInfo.getCurrentResolvedClassInfo("long"), value);
  }

  /* @see gov.nasa.jpf.inspector.server.expression.ExpressionNodeInterface#getNormalizedExpression() */
  @Override
  public String getNormalizedExpression () {
    return Long.toString(value) + "l";
  }

}
