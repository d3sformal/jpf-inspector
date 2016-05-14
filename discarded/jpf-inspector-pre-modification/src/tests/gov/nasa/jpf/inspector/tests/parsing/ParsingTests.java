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

package gov.nasa.jpf.inspector.tests.parsing;

import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorParsingErrorException;
import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionStateValueConstChar;
import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionStateValueConstString;
import gov.nasa.jpf.inspector.tests.infrastructure.InspectorTest;

import java.io.PrintStream;
import java.util.Arrays;

import org.junit.Test;

/**
 * @author Alf
 * 
 */
public class ParsingTests extends InspectorTest {
  private static final boolean DEBUG = true;
  private final PrintStream out = System.out;

  // *************************************************************************
  // ** Infrastructure
  // *************************************************************************
  private static class TestSpec<TEST_INPUT, EXPECTED_RESULT> {
    private final TEST_INPUT testInput;
    private final EXPECTED_RESULT expectedResult;

    protected TestSpec (TEST_INPUT testInput, EXPECTED_RESULT expectedResult) {
      super();
      this.testInput = testInput;
      this.expectedResult = expectedResult;
    }

    public TEST_INPUT getTestInput () {
      return testInput;
    }

    public EXPECTED_RESULT getExpectedResult () {
      return expectedResult;
    }
  }

  // *************************************************************************
  // ** Character tests
  // *************************************************************************

  private static class TestSpecChar extends TestSpec<String, Character> {

    public TestSpecChar (String testInput, Character expectedResult) {
      super(testInput, expectedResult);
    }

  }

  private static final TestSpecChar characterParsingPassingTests[] = {
    new TestSpecChar("'a'", 'a'),
    new TestSpecChar("'Á'", 'Á'),
    new TestSpecChar("'\n'", '\n'),
    new TestSpecChar("'\t'", '\t'),
    new TestSpecChar("'\\\''", '\''), // \'
    new TestSpecChar("'\\\"'", '\"'), // \"
    new TestSpecChar("'\\\\'", '\\'), // \
    new TestSpecChar("'\\000'", '\u0000'),
    new TestSpecChar("'\\00'", '\u0000'),
    new TestSpecChar("'\\0'", '\u0000'),
    new TestSpecChar("'\\377'", '\u00FF'),
    new TestSpecChar("'\\77'", '\u003F'),
    new TestSpecChar("'\\7'", '\u0007'),
    new TestSpecChar("'\\123'", '\u0053'),
    new TestSpecChar("'\\000'", '\u0000'),
    new TestSpecChar("'\\00'", '\u0000'),
    new TestSpecChar("'\\0'", '\u0000'),
    new TestSpecChar("'\\u0000'", '\u0000'),
    new TestSpecChar("'\\uFFFF'", '\uFFFF'),
    new TestSpecChar("'\\uABBA'", '\uABBA'),
    new TestSpecChar("'\\uBabA'", '\uBABA'),
    new TestSpecChar("'\\u0AF9'", '\u0AF9'),
    new TestSpecChar("'\\u0af9'", '\u0AF9') };

  @Test
  public void characterParsingTest () {
    for (TestSpecChar tsc : characterParsingPassingTests) {
      try {
        if (DEBUG) {
          out.println("Trying to parse: ->" + tsc.getTestInput() + "<-");
        }
        char result = ExpressionStateValueConstChar.convertToChar(tsc.getTestInput());
        if (DEBUG) {
          out.println("\ttest     result=" + result + ", test      result as int=" + (int) result + ".\n" + "\texpected result=" + tsc.getExpectedResult()
              + ", excpected result as int=" + (int) (tsc.getExpectedResult().charValue()) + '.');
        }
        if (tsc.getExpectedResult().equals(result) == false) {
          fail("Error while converting  ->" + tsc.getTestInput() + "<- to character. \n" + "\ttest     result=" + result + ", test      result as int="
              + (int) result + ".\n" + "\texpected result=" + tsc.getExpectedResult() + ", excpected result as int="
              + (int) (tsc.getExpectedResult().charValue()) + '.');

        }
      } catch (JPFInspectorParsingErrorException e) {
        fail("Error while converting  ->" + tsc.getTestInput() + "<- to character. \n" + "Unexpecte exception: " + e.getMessage());
      }
    }
  }

  private static class TestSpecCharFail extends TestSpec<String, Integer> {

    public TestSpecCharFail (String testInput, Integer errrReportedAtPosition) {
      super(testInput, errrReportedAtPosition);
    }

  }

  // Invalid inputs which should result into exception
  private static final TestSpecCharFail characterParsingFailingTests[] = {
    new TestSpecCharFail("", 0), // Empty input
    new TestSpecCharFail("a", 0), // Missing initial '
    new TestSpecCharFail("'a", 2), // Missing terminating '
    new TestSpecCharFail("'\\", 2), // Invalid escape sequence (no escaped character specified)
    new TestSpecCharFail("'\\x'", 2), // Invalid escape sequence
    new TestSpecCharFail("'\\t'_", 4), // Garbage after terminating '
    new TestSpecCharFail("'\\uFf", 5), // Malformed HEXA - too short
    new TestSpecCharFail("'\\uFed", 6), // Malformed HEXA - too short
    new TestSpecCharFail("'\\u00X0'", 5), // Malformed HEXA - invalid character
    new TestSpecCharFail("'\\uFf00", 7), // Missing terminating '
    new TestSpecCharFail("'\\3777'", 5), // Too long octal (-> missing terminating ')
    new TestSpecCharFail("'\\777'", 4), // Too long octal (-> missing terminating ')
  };

  @Test
  public void characterParsingTestInvalidInputs () {
    // In this tests invalid inputs are pushed into conversion routine and check if exception with proper error location is thrown.
    // Note: If some of tests fail, analyse the error and if reported position looks reasonable, just update the test

    for (TestSpecCharFail tsc : characterParsingFailingTests) {
      try {
        if (DEBUG) {
          out.println("Trying to parse: ->" + tsc.getTestInput() + "<-");
        }
        char result = ExpressionStateValueConstChar.convertToChar(tsc.getTestInput());

        fail("Error while converting  ->" + tsc.getTestInput() + "<- to character. \n" + "\ttest     result=" + result + ", test      result as int="
            + (int) result + ".\n" + " While exception with parsing error at position " + tsc.getExpectedResult() + " is expected.\n");

      } catch (JPFInspectorParsingErrorException e) {
        if (DEBUG) {
          out.println("\t Expected exception: " + tsc.getExpectedResult() + "\n\t Exception position: " + e.getPosition() + "\n\tThe exception: "
              + e.getMessage() + "\n" + e.expressError(60));
        }

        if (e.getPosition() != tsc.getExpectedResult()) {
          fail("Error while converting  ->" + tsc.getTestInput() + "<- to character. \n Exception thrown but at invalid location. Expected location= "
              + tsc.getExpectedResult() + "\n" + e.getMessage());
        }
      }
    }
  }

  // *************************************************************************
  // ** String tests
  // *************************************************************************

  private static class TestSpecString extends TestSpec<String, String> {

    public TestSpecString (String testInput, String expectedResult) {
      super(testInput, expectedResult);
    }
  }

  private static final TestSpecString stringParsingPassingTests[] = {
    // Single character strings
    new TestSpecString("\"a\"", "a"),
    new TestSpecString("\"Á\"", "Á"),
    new TestSpecString("\"\n\"", "\n"),
    new TestSpecString("\"\t\"", "\t"),
    new TestSpecString("\"\\\'\"", "\'"), // \'
    new TestSpecString("\"\\\"\"", "\""), // \"
    new TestSpecString("\"\\\\\"", "\\"), // \
    new TestSpecString("\"\\000\"", "\u0000"),
    new TestSpecString("\"\\00\"", "\u0000"),
    new TestSpecString("\"\\0\"", "\u0000"),
    new TestSpecString("\"\\377\"", "\u00FF"),
    new TestSpecString("\"\\77\"", "\u003F"),
    new TestSpecString("\"\\7\"", "\u0007"),
    new TestSpecString("\"\\123\"", "\u0053"),
    new TestSpecString("\"\\000\"", "\u0000"),
    new TestSpecString("\"\\00\"", "\u0000"),
    new TestSpecString("\"\\0\"", "\u0000"),
    new TestSpecString("\"\\u0000\"", "\u0000"),
    new TestSpecString("\"\\uFFFF\"", "\uFFFF"),
    new TestSpecString("\"\\uABBA\"", "\uABBA"),
    new TestSpecString("\"\\uBabA\"", "\uBABA"),
    new TestSpecString("\"\\u0AF9\"", "\u0AF9"),
    new TestSpecString("\"\\u0af9\"", "\u0AF9"),
    // Examples from Javadoc
    new TestSpecString("\"___\\u1234___\"", "___\u1234___"),
    new TestSpecString("\"___\\n___\\032___\"", "___\n___\032___"),
    // More complex example when different types of character follows one after another
    new TestSpecString("\"Alfíček\"", "Alfíček"),
    new TestSpecString("\"\\n\\t\\000\\3777\"", "\n\t\u0000\u00ff7"),
    new TestSpecString("\"\\n\\t\\000\\773\"", "\n\t\u0000\u003f3"),
    new TestSpecString("\"\\\\\\t\"", "\\\t"),
    new TestSpecString("\"\\\"Alf\\\"\"", "\"Alf\""),
    new TestSpecString("\"\\123\u78900\"", "\u0053\u78900"), };

  @Test
  public void stringParsingTest () {
    for (TestSpecString tsc : stringParsingPassingTests) {
      try {
        if (DEBUG) {
          out.println("Trying to parse: ->" + tsc.getTestInput() + "<-");
        }
        String result = ExpressionStateValueConstString.convertToString(tsc.getTestInput());

        if (DEBUG) {
          out.println("\ttest     result=" + result + ", test      result as int array=" + Arrays.toString(string2intArray(result)) + ".");
          out.println("\texpected result=" + tsc.getExpectedResult() + ", excpected result as int array="
              + Arrays.toString(string2intArray(tsc.getExpectedResult())) + '.');
        }

        if (tsc.getExpectedResult().equals(result) == false) {
          String errMsg = "";
          errMsg += "Error while converting  ->" + tsc.getTestInput() + "<- to string.\n";
          errMsg += "\ttest     result=" + result + ", test      result as int=result as int array=" + Arrays.toString(string2intArray(result)) + ".)\n";
          errMsg += "\texpected result=" + tsc.getExpectedResult() + ", excpected result as int array="
              + Arrays.toString(string2intArray(tsc.getExpectedResult())) + ".\n";

          fail(errMsg);
        }
      } catch (JPFInspectorParsingErrorException e) {
        fail("Error while converting  ->" + tsc.getTestInput() + "<- to String. \n" + "Unexpecte exception: " + e.getMessage() + "\n" + e.expressError(60));
      }
    }
  }

  static private int[] string2intArray (String str) {
    final char[] strArr = str.toCharArray();
    final int[] result = new int[strArr.length];
    for (int i = 0; i < strArr.length; i++) {
      result[i] = strArr[i];
    }
    return result;
  }

  private static class TestSpecStringFail extends TestSpec<String, Integer> {

    public TestSpecStringFail (String testInput, Integer errrReportedAtPosition) {
      super(testInput, errrReportedAtPosition);
    }
  }

  private static final TestSpecStringFail stringParsingFailingTests[] = {
    new TestSpecStringFail("", 0), // Empty input
    new TestSpecStringFail("ohloh", 0), // Missing initial "
    new TestSpecStringFail("\"a", 2), // Missing terminating "
    new TestSpecStringFail("\"a\\\"", 4), // Faked termination "
    new TestSpecStringFail("\"__\\x\"", 4), // Invalid escape sequence
    new TestSpecStringFail("\"\\uFf", 5), // Malformed HEXA - too short
    new TestSpecStringFail("\"\\uFf\\u0000", 5), // Malformed HEXA - invalid character in HEXA
    new TestSpecStringFail("\"\\uFed", 6), // Malformed HEXA - too short
    new TestSpecStringFail("\"\\\\u1234\\u00X0Submarine\"", 12), // Malformed HEXA - invalid character
    new TestSpecStringFail("\"\\uFf00", 7), // Missing terminating '
    new TestSpecStringFail("\"\\t\"_\"next string\"", 4), // Garbage after terminating "
  };

  @Test
  public void stringParsingTestInvalidInputs () {
    for (TestSpecStringFail tsc : stringParsingFailingTests) {
      try {
        if (DEBUG) {
          out.println("Trying to parse: ->" + tsc.getTestInput() + "<-");
        }
        String result = ExpressionStateValueConstString.convertToString(tsc.getTestInput());

        String errMsg = "";
        errMsg += "While converting invalid input ->" + tsc.getTestInput() + "<- no exception thrown.\n";
        errMsg += "\ttest     result=" + result + ", test      result as int=result as int array=" + Arrays.toString(string2intArray(result)) + ".)\n";
        errMsg += "\texpected error at position " + tsc.getExpectedResult() + ".\n";

        fail(errMsg);
      } catch (JPFInspectorParsingErrorException e) {
        if (DEBUG) {
          out.println("\t Expected exception: " + tsc.getExpectedResult());
          out.println("\t Exception position: " + e.getPosition());
          out.println("\t The exception: " + e.getMessage() + "\n" + e.expressError(60));
        }

        if (e.getPosition() != tsc.getExpectedResult()) {
          String errMsg = "";
          errMsg += "\t Expected exception: " + tsc.getExpectedResult() + "\n";
          errMsg += "\t Exception position: " + e.getPosition() + "\n";
          errMsg += "\t The exception: " + e.getMessage() + "\n" + e.expressError(60) + "\n";
          fail(errMsg);
        }
      }
    }
  }

  public static void main (String[] testMethods) {
    runTestsOfThisClass(testMethods);
  }

}
