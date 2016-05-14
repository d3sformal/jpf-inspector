//
// Copyright (C) 2011 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
// 
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
// 
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//  

package gov.nasa.jpf.inspector.server.expression.expressions;

import gov.nasa.jpf.inspector.interfaces.JPFInspectorException;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorParsingErrorException;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.programstate.StateNodeInterface;
import gov.nasa.jpf.inspector.server.programstate.StateReadableConstValue;
import gov.nasa.jpf.inspector.utils.parser.JPFInspectorRuntimeParsingException;
import gov.nasa.jpf.vm.ClassInfo;

/**
 * @author Alf
 */
public class ExpressionStateValueConstLong extends ExpressionStateValueConst {
  /**
   * The value has to be string in one of the following formats
   * 10l, +10L, -10l, 0xFFL, +0xFFl, -0xFFl
   * 
   * @param value
   * @return Return value converted to integer or exception if value is out of the integer range or input is malformed.
   */
  public static long convertToLongWrapped (String value) {
    try {
      return ExpressionStateValueConstLong.convertToLong(value);
    } catch (JPFInspectorParsingErrorException e) {
      throw new JPFInspectorRuntimeParsingException(e);
    }
  }

  public static long convertToLong (String value) throws JPFInspectorParsingErrorException {
    assert value != null;
    assert value.length() > 0;

    String val = value;
    // Check if the last character is 'l' or 'L'
    char clast = val.charAt(val.length() - 1);
    if (clast != 'l' && clast != 'L') {
      throw new JPFInspectorParsingErrorException("Invalid long value. Does not terminate with \'l\' character.", value, value.length() - 1);
    }
    val = val.substring(0, val.length() - 1);

    if (val.length() == 0) {
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
        return Long.valueOf(val, 16);
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
    super();
    this.value = value;
  }

  /*
   * @see gov.nasa.jpf.inspector.server.expression.ExpressionStateRootNode#getResultExpression(gov.nasa.jpf.inspector.server.jpf.JPFInspector,
   * gov.nasa.jpf.inspector.server.expression.InspectorState)
   */
  @Override
  public StateNodeInterface getResultExpression (JPFInspector inspector, InspectorState state) throws JPFInspectorException {
    return new StateReadableConstValue(inspector, 1, ClassInfo.getResolvedClassInfo("long"), Long.valueOf(value));
  }

  /* @see gov.nasa.jpf.inspector.server.expression.ExpressionNodeInterface#getNormalizedExpression() */
  @Override
  public String getNormalizedExpression () {
    return Long.toString(value) + "l";
  }

}
