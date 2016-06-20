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

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorParsingErrorException;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionStateValueConstChar.CharParsingState;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.programstate.StateNodeInterface;
import gov.nasa.jpf.inspector.server.programstate.StateReadableConstValue;
import gov.nasa.jpf.inspector.utils.parser.JPFInspectorRuntimeParsingException;
import gov.nasa.jpf.vm.*;

/**
 * Represents a string literal.
 */
public class ExpressionStateValueConstString extends ExpressionStateValueConst {
  /**
   * Constructs a string literal from a string.
   * @param value Represented string
   */
  public ExpressionStateValueConstString (String value) {
    this.value = value;
  }

  @Override
  public StateNodeInterface getResultExpression (JPFInspector inspector, InspectorState state) throws JPFInspectorException {

    // Create representation for the string in the JPF state.
    VM vm = state.getVM();
    Heap heap = vm.getHeap();
    ElementInfo ei = heap.newString(value, state.getVM().getCurrentThread());

    return new StateReadableConstValue(inspector, ClassLoaderInfo.getCurrentResolvedClassInfo("java.lang.String"), ei);
  }

  @Override
  public String getNormalizedExpression () {
    StringBuilder sb = new StringBuilder(10 + value.length());
    sb.append('\"');
    for (int i = 0; i < value.length(); i++) {
      sb.append(ExpressionStateValueConstChar.unparseChar(value.charAt(i)));
    }
    sb.append('\"');

    return sb.toString();
  }

  /// STATIC METHODS

  public static String convertToStringWrapped (String str) throws JPFInspectorRuntimeParsingException {
    try {
      return ExpressionStateValueConstString.convertToString(str);
    } catch (JPFInspectorParsingErrorException e) {
      throw new JPFInspectorRuntimeParsingException(e);
    }
  }
  /**
   * Converts string when characters can be represented in various forms (like in standard Java source file).
   * (like "___\u1234___" or "___\n___\032___")
   * 
   * Note: The conversion is like java source file string to runtime representation of that source code string.
   * 
   * @param str Parsed with characters in one of the following forms. ("a", "\n", "\032" "\u0020"). Note initial and terminating DOUBLE_QUOTE characters are
   *        required.
   * @return Java string.
   * 
   * @throws JPFInspectorParsingErrorException
   */
  public static String convertToString (String str) throws JPFInspectorParsingErrorException {
    assert (str != null);

    StringBuilder sb = new StringBuilder(str.length());

    CharParsingState sps = new CharParsingState(str, '"');

    // Check initial "
    if (sps.strArr.length < 1) {
      throw new JPFInspectorParsingErrorException("Empty string", str, 0);
    }

    if (sps.strArr[0] != '\"') {
      throw new JPFInspectorParsingErrorException("Error while parsing character at 0. The double quote (\") character is expected in the beginning.", str, 0);
    }
    sps.pos++;

    int bound = sps.strArr.length - 1; // The last character should be \"
    while (sps.pos < bound) {
      char nextChar = ExpressionStateValueConstChar.parseCharRepresentation(sps);
      sb.append(nextChar);
    }

    if (sps.pos >= sps.strArr.length) {
      throw new JPFInspectorParsingErrorException("Error while parsing string at " + (str.length())
          + ". The terminating double quote (\") character is expected.", str, str.length());
    }

    if (sps.strArr[sps.pos] != '\"') {
      throw new JPFInspectorParsingErrorException("Error while parsing string at " + (sps.pos + 1)
          + ". The terminating double quote (\") character is expected as a last character in string.", str, sps.pos + 1);
    }

    if (sps.pos < (sps.strArr.length - 1)) {
      throw new JPFInspectorParsingErrorException("Error while parsing string at " + (sps.pos + 1) + ". Garbage after terminating double quote (\").", str,
          sps.pos + 1);
    }

    return sb.toString();
  }

  private final String value; // Represented value



}
