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

package gov.nasa.jpf.inspector.tests.legacy;

import gov.nasa.jpf.inspector.client.commands.CmdBreakpointCreate;
import gov.nasa.jpf.inspector.interfaces.CommandsInterface.InspectorStates;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorArrayIndexOutOutRangeException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorAssignmentOutOfRangeException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorIncompatibleTypesException;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.common.pse.ProgramStateEntry;
import gov.nasa.jpf.inspector.common.pse.PSEVariable;
import gov.nasa.jpf.inspector.tests.legacy.infrastructure.Breakpoint;
import gov.nasa.jpf.inspector.tests.legacy.infrastructure.InspectorTest;
import gov.nasa.jpf.inspector.tests.legacy.infrastructure.InspectorTestCallbacks;
import gov.nasa.jpf.inspector.tests.legacy.infrastructure.InspectorTestDriver;
import gov.nasa.jpf.util.TypeRef;

import java.util.Map;

import org.junit.Test;

/**
 * 
 * @author Alf
 */
public class Assignments01Test extends InspectorTest {

  public boolean sa_field_boolean = true;
  char sa_field_char = 'a';
  protected byte sa_field_byte = 9;
  private final short sa_field_short = 257;

  public final static int sa_field_int = 1 * 1024 * 1024; // Static and final
  static long sa_field_long = 1 * 1024 * 1024 * 1024 * 1024L;

  protected static float sa_field_float = 3.1415f;
  private static double sa_field_double = Math.E;
  private static double sa_field_double2 = Math.PI;

  private static String sa_field_string1 = "Alf";
  private static String sa_field_string2 = "BlaBlaBla";
  private static String sa_field_string3 = "Bin Aladin";
  private static Object sa_field_Object = new Object();
  private static StringBuffer sa_field_StringBuffer = new StringBuffer();
  private static Map<Integer, Integer> sa_field_Map = null;

  // Boxed fields
  public boolean sa_field_box_test_boolean = true;
  char sa_field_box_test_char = 'a';
  protected byte sa_field_box_test_byte = 9;
  private final short sa_field_box_test_short = 257;

  public final static int sa_field_box_test_int = 1 * 1024 * 1024; // Static and final
  static long sa_field_box_test_long = 1 * 1024 * 1024 * 1024 * 1024L;

  protected static float sa_field_box_test_float = 3.1415f;
  private static double sa_field_box_test_double = Math.E;
  private static double sa_field_box_test_double2 = Math.PI;

  public Boolean sa_field_Boolean = false;
  Character sa_field_Char = 'l';
  protected Byte sa_field_Byte = -9;
  private final Short sa_field_Short = -257;

  public final static Integer sa_field_Int = 2 * 1024 * 1024; // Static and final
  static Long sa_field_Long = 2L * 1024L * 1024L * 1024L * 1024L;

  protected static Float sa_field_Float = 4f;
  private static Double sa_field_Double = 0.1;

  @Test
  public void simpleAssignments () {
    if (runInspectorTest(new TypeRef("gov.nasa.jpf.inspector.tests.legacy.infrastructure.InspectorTestCallbacks"), new TypeRef(
        "gov.nasa.jpf.inspector.tests.legacy.Assignments01Test$SimpleAssignmentsDriver"))) {

      boolean sa_slot_boolean = false;
      char sa_slot_char = 'f';
      byte sa_slot_byte = Byte.MIN_VALUE;
      short sa_slot_short = Short.MIN_VALUE;
      int sa_slot_int = Integer.MIN_VALUE;
      long sa_slot_long = Long.MIN_VALUE;

      float sa_slot_float = Float.NEGATIVE_INFINITY;
      double sa_slot_double = Double.NaN;

      String sa_slot_string = "Alf rulez";
      Object sa_slot_Object = new Object();

      boolean sa_array_boolean[] = {
        true,
        false };
      char sa_array_char[] = {
        '\u0000',
        '\uFFFF',
        'a',
        'l',
        'f' };
      byte sa_array_byte[] = {
        0,
        1,
        127 };
      short sa_array_short[] = {
        Short.MIN_VALUE,
        0,
        Short.MAX_VALUE };
      int sa_array_int[] = {
        Integer.MIN_VALUE,
        0,
        Integer.MAX_VALUE };
      long sa_array_long[] = {
        Long.MIN_VALUE,
        0,
        Long.MAX_VALUE };
      float sa_array_float[] = {
        Float.NEGATIVE_INFINITY,
        Float.NEGATIVE_INFINITY,
        Float.NEGATIVE_INFINITY,
        Float.MIN_VALUE,
        (float) (-Math.PI),
        0.0f,
        (float) (Math.PI),
        Float.MAX_VALUE,
        Float.POSITIVE_INFINITY,
        Float.POSITIVE_INFINITY,
        Float.POSITIVE_INFINITY,
        Float.NaN,
        Float.NaN };

      double sa_array_double[] = {
        Double.NEGATIVE_INFINITY,
        Double.NEGATIVE_INFINITY,
        Double.NEGATIVE_INFINITY,
        Double.MIN_VALUE,
        -Math.E,
        0.0,
        Math.E,
        Double.MAX_VALUE,
        Double.POSITIVE_INFINITY,
        Double.POSITIVE_INFINITY,
        Double.POSITIVE_INFINITY,
        Double.NaN,
        Double.NaN };

      Object sa_array_Object[] = { new Object() };
      Object sa_array2_String_String[][] = {
        {
          "Java",
          "PathFinder" },
        { "JPF-Shell" } };

      // Tries assignment new values to local variables, fields and arrays
      // a) fields
      System.out.println("sa_field_boolean=" + sa_field_boolean);
      System.out.println("sa_field_char=" + sa_field_char);
      System.out.println("sa_field_byte=" + sa_field_byte);
      System.out.println("sa_field_short=" + sa_field_short);
      System.out.println("sa_field_int=" + sa_field_int);
      System.out.println("sa_field_long=" + sa_field_long);
      System.out.println("sa_field_float=" + sa_field_float);
      System.out.println("sa_field_double=" + sa_field_double);
      System.out.println("sa_field_double2=" + sa_field_double2);
      System.out.println("sa_field_string1=" + sa_field_string1);
      System.out.println("sa_field_string2=" + sa_field_string2);
      System.out.println("sa_field_string3=" + sa_field_string3);
      System.out.println("sa_field_Object=" + sa_field_Object);
      System.out.println("sa_field_StringBuffer=" + sa_field_StringBuffer);
      System.out.println("sa_field_Map=" + sa_field_Map);

      System.out.println("sa_field_box_test_boolean=" + sa_field_box_test_boolean);
      System.out.println("sa_field_box_test_char=" + sa_field_box_test_char);
      System.out.println("sa_field_box_test_byte=" + sa_field_box_test_byte);
      System.out.println("sa_field_box_test_short=" + sa_field_box_test_short);
      System.out.println("sa_field_box_test_int=" + sa_field_box_test_int);
      System.out.println("sa_field_box_test_long=" + sa_field_box_test_long);
      System.out.println("sa_field_box_test_float=" + sa_field_box_test_float);
      System.out.println("sa_field_box_test_double=" + sa_field_box_test_double);

      System.out.println("sa_field_Long=" + sa_field_Long);

      System.out.println();
      System.out.println("SLOTS");
      System.out.println("sa_slot_boolean=" + sa_slot_boolean);
      System.out.println("sa_slot_char=" + sa_slot_char);
      System.out.println("sa_slot_byte=" + sa_slot_byte);
      System.out.println("sa_slot_short=" + sa_slot_short);
      System.out.println("sa_slot_int=" + sa_slot_int);
      System.out.println("sa_slot_long=" + sa_slot_long);
      System.out.println("sa_slot_float=" + sa_slot_float);
      System.out.println("sa_slot_double=" + sa_slot_double);
      System.out.println("sa_slot_string=" + sa_slot_string);
      System.out.println("sa_slot_Object=" + sa_slot_Object);

      Breakpoint.breakMe();

      System.out.println("sa_field_boolean=" + sa_field_boolean);
      System.out.println("sa_field_char=" + sa_field_char);
      System.out.println("sa_field_byte=" + sa_field_byte);
      System.out.println("sa_field_short=" + sa_field_short);
      System.out.println("sa_field_int=" + sa_field_int);
      System.out.println("sa_field_long=" + sa_field_long);
      System.out.println("sa_field_float=" + sa_field_float);
      System.out.println("sa_field_double=" + sa_field_double);
      System.out.println("sa_field_string1=" + sa_field_string1);
      System.out.println("sa_field_string2=" + sa_field_string2);
      System.out.println("sa_field_string3=" + sa_field_string3);
      System.out.println("sa_field_Object=" + sa_field_Object);
      System.out.println("sa_field_StringBuffer=" + sa_field_StringBuffer);
      System.out.println("sa_field_Map=" + sa_field_Map);

      System.out.println("sa_field_box_test_boolean=" + sa_field_box_test_boolean);
      System.out.println("sa_field_box_test_char=" + sa_field_box_test_char);
      System.out.println("sa_field_box_test_byte=" + sa_field_box_test_byte);
      System.out.println("sa_field_box_test_short=" + sa_field_box_test_short);
      System.out.println("sa_field_box_test_int=" + sa_field_box_test_int);
      System.out.println("sa_field_box_test_long=" + sa_field_box_test_long);
      System.out.println("sa_field_box_test_float=" + sa_field_box_test_float);
      System.out.println("sa_field_box_test_double=" + sa_field_box_test_double);
      System.out.println("sa_field_box_test_double2=" + sa_field_box_test_double2);

      System.out.println();
      System.out.println("SLOTS");
      System.out.println("sa_slot_boolean=" + sa_slot_boolean);
      System.out.println("sa_slot_char=" + sa_slot_char);
      System.out.println("sa_slot_byte=" + sa_slot_byte);
      System.out.println("sa_slot_short=" + sa_slot_short);
      System.out.println("sa_slot_int=" + sa_slot_int);
      System.out.println("sa_slot_long=" + sa_slot_long);
      System.out.println("sa_slot_float=" + sa_slot_float);
      System.out.println("sa_slot_double=" + sa_slot_double);
      System.out.println("sa_slot_string=" + sa_slot_string);
      System.out.println("sa_slot_Object=" + sa_slot_Object);

      Breakpoint.breakMe();

      return;
    }
  }

  static public class SimpleAssignmentsDriver extends InspectorTestDriver {

    public SimpleAssignmentsDriver (JPFInspector inspector, InspectorTestCallbacks callbacks, InspectorTest test) {
      super(inspector, callbacks, test);
    }

    @Override
    public void driveInspector () throws JPFInspectorException {
      System.out.println("InspectorDriver");

      CmdBreakpointCreate.ConsoleBreakpointCreationExpression bpC = Breakpoint.getBreakpoint();
      System.out.println("\tCreating breakpoint " + bpC);
      System.out.println("\tinspector.createBreakPoint(bpC) ... " + inspector.createBreakPoint(bpC));

      callbacks.waitForCB_BreakpointHit();
      System.out.println("\tBreakPoint hitted" + callbacks.getCbBreakpointHit_bp());

      callbacks.waitForCB_StateChange(InspectorStates.JPF_STOPPED);

      // ProgramStateEntry pse = inspector.evaluateStateExpression("#thread[0]");
      // CmdPrint.ValuePrinter vp = new CmdPrint.ValuePrinter();
      // pse.visit(vp);
      // System.out.println(vp.getResult());

      System.out.println("\tSetting values");
      for (int i = 0; i < testInputs.length; i++) {
        TestInput spec = testInputs[i];

        try {
          String setExpression = spec.varName + " = " + spec.varSetValue;
          System.out.println("\t\tExecution setValue command: " + setExpression);
          inspector.setValue(setExpression);

          if (spec.passing == false) {
            fail("Exception of the " + spec.exceptionType.getSimpleName() + " type is expected.");
          }
        } catch (JPFInspectorException e) {
          if (spec.passing == true) {
            fail(e);
          } else {
            if ((e.getClass().equals(spec.exceptionType)) == false) {
              fail("Wrong type of exception thrown. Expceted exception type " + spec.exceptionType.getSimpleName() + " but thrown exception type is "
                  + e.getClass().getSimpleName() + "\n" + e.getMessage());
            }
          }
        }
      }

      callbacks.nextCB_Resume_and_StateChange(InspectorStates.JPF_RUNNING);

      callbacks.waitForCB_BreakpointHit();
      System.out.println("\tBreakPoint hitted" + callbacks.getCbBreakpointHit_bp());

      callbacks.waitForCB_StateChange(InspectorStates.JPF_STOPPED);

      // Check if values set correctly
      System.out.println("\tChecking set values");
      for (int i = 0; i < testInputs.length; i++) {
        TestInput spec = testInputs[i];
        if (spec.passing == false) {
          continue;
        }

        try {
          System.out.print("\t\tExecution print command " + spec.varName);
          ProgramStateEntry pse1 = inspector.evaluateStateExpression(spec.varName);

          assertNotNull(pse1);
          assertTrue(pse1 instanceof PSEVariable);

          PSEVariable pseVar = (PSEVariable) pse1;

          // System.out.println();
          // System.out.println("Expected value: ->" + spec.exceptedValue + "<-");
          // System.out.print("Actual   value: ->" + pseVar.getVarValue() + "<-");
          // System.out.print("\t\tExecution print command " + spec.varName);

          System.out.println(", result: " + pseVar.getVarValue());

          if (spec.passing) {
            assertEquals(spec.exceptedValue, pseVar.getVarValue());
          }

        } catch (JPFInspectorException e) {
          fail(e);
        }
      }

      callbacks.nextCB_Resume_and_StateChange(InspectorStates.JPF_RUNNING);

      callbacks.waitForCB_StateChange(InspectorStates.JPF_TERMINATING);

      callbacks.finishCallback();
    }
  }

  static private class TestInput {
    public final boolean passing; // Test pass, if false=given method chould throw exception

    public final String varName; // "#stack[2].fiels - specification of the value to set (stateExpression)
    public final String varSetValue; // 15, "Alf" - text with value to set - as used in the set expression

    public final String exceptedValue; // Expected value after the set as returns "print" command

    public final Class<? extends JPFInspectorException> exceptionType; // Expected exception which is thrown in case of the not passing test

    private final static String PREFFIX_STATE_EXPR = "#stackFrame[1]."; // When checked we are stopped in the break method.

    public static TestInput createPassingTestPrefixVarName (String varName, String varValue) {
      return TestInput.createPassingTestPrefixVarName(varName, varValue, varValue);
    }

    public static TestInput createFailingTestPrefixVarName (String varName, String varValue, Class<? extends JPFInspectorException> exceptionType) {
      return new TestInput(false, PREFFIX_STATE_EXPR + varName, varValue, null, exceptionType);
    }

    public static TestInput createPassingTestPrefixVarName (String varName, String varSetValue, String exceptedValue) {
      return new TestInput(true, PREFFIX_STATE_EXPR + varName, varSetValue, exceptedValue, null);
    }

    public static TestInput createPassingTestCopyVarValuePrefixBothVarNames (String varName, String varNameWithValue2Set, String exceptedValue) {
      return new TestInput(true, PREFFIX_STATE_EXPR + varName, PREFFIX_STATE_EXPR + varNameWithValue2Set, exceptedValue, null);
    }

    public static TestInput createFailingTestPrefixBothVarName (String varName, String varNameWithValue2Set,
        Class<? extends JPFInspectorException> exceptionType) {
      return new TestInput(false, PREFFIX_STATE_EXPR + varName, PREFFIX_STATE_EXPR + varNameWithValue2Set, null, exceptionType);
    }

    public TestInput (boolean passing, String varName, String varSetValue, String exceptedValue, Class<? extends JPFInspectorException> exceptionType) {
      super();
      this.passing = passing;
      this.varName = varName;
      this.varSetValue = varSetValue;
      this.exceptedValue = exceptedValue;

      this.exceptionType = exceptionType;
    }

  }

  private static final TestInput[] testInputs = {
    TestInput.createPassingTestPrefixVarName("sa_array_double[4]", "pos_inf", "Infinity"),

    // Fields
    TestInput.createPassingTestPrefixVarName("sa_field_boolean", "false"),
    TestInput.createPassingTestPrefixVarName("sa_field_char", "'\n'", "\n"),
    TestInput.createPassingTestPrefixVarName("sa_field_byte", "10"),
    TestInput.createPassingTestPrefixVarName("sa_field_short", "-257"),
    TestInput.createPassingTestPrefixVarName("sa_field_int", "0"),
    TestInput.createPassingTestPrefixVarName("sa_field_boolean", "false"),
    TestInput.createPassingTestPrefixVarName("sa_field_long", "-1234567890123456789L", "-1234567890123456789"),
    TestInput.createPassingTestPrefixVarName("sa_field_float", "0.2f", "0.2"),
    TestInput.createPassingTestPrefixVarName("sa_field_double", "3.1415"),
    TestInput.createPassingTestPrefixVarName("sa_field_double2", "0.5f", "0.5"),

    // Unboxing tests
    TestInput.createPassingTestCopyVarValuePrefixBothVarNames("sa_field_box_test_boolean", "sa_field_Boolean", "false"),
    TestInput.createPassingTestCopyVarValuePrefixBothVarNames("sa_field_box_test_char", "sa_field_Char", "l"),
    TestInput.createPassingTestCopyVarValuePrefixBothVarNames("sa_field_box_test_byte", "sa_field_Byte", "-9"),
    TestInput.createPassingTestCopyVarValuePrefixBothVarNames("sa_field_box_test_short", "sa_field_Short", "-257"),
    TestInput.createPassingTestCopyVarValuePrefixBothVarNames("sa_field_box_test_int", "sa_field_Int", "2097152"),
    TestInput.createPassingTestCopyVarValuePrefixBothVarNames("sa_field_box_test_long", "sa_field_Long", "2199023255552"),
    TestInput.createPassingTestCopyVarValuePrefixBothVarNames("sa_field_box_test_float", "sa_field_Float", "4.0"),
    TestInput.createPassingTestCopyVarValuePrefixBothVarNames("sa_field_box_test_double", "sa_field_Double", "0.1"),
    TestInput.createPassingTestCopyVarValuePrefixBothVarNames("sa_field_box_test_double2", "sa_field_box_test_float", "4.0"),
    // String test
    TestInput.createPassingTestPrefixVarName("sa_field_string1", "\"is great\""),

    // Object assignments tests
    TestInput.createPassingTestCopyVarValuePrefixBothVarNames("sa_field_string2", "sa_field_string3", "\"" + sa_field_string3 + "\""),
    // Assigning to predecessor
    TestInput.createPassingTestCopyVarValuePrefixBothVarNames("sa_field_Object", "sa_field_string3", "\"" + sa_field_string3 + "\""),
    // Assigning null to anybody
    TestInput.createPassingTestPrefixVarName("sa_field_Short", "null", "null"),
    TestInput.createPassingTestCopyVarValuePrefixBothVarNames("sa_field_StringBuffer", "sa_field_Map", "null"),

    // Failing tests
    TestInput.createFailingTestPrefixVarName("sa_field_boolean", "'f'", JPFInspectorIncompatibleTypesException.class),
    TestInput.createFailingTestPrefixVarName("sa_field_boolean", "10", JPFInspectorIncompatibleTypesException.class),
    TestInput.createFailingTestPrefixVarName("sa_field_boolean", "10l", JPFInspectorIncompatibleTypesException.class),
    TestInput.createFailingTestPrefixVarName("sa_field_boolean", "10f", JPFInspectorIncompatibleTypesException.class),
    TestInput.createFailingTestPrefixVarName("sa_field_boolean", "null", JPFInspectorIncompatibleTypesException.class),
    TestInput.createFailingTestPrefixVarName("sa_field_boolean", "\"Alf\"", JPFInspectorIncompatibleTypesException.class),

    TestInput.createFailingTestPrefixVarName("sa_field_char", "false", JPFInspectorIncompatibleTypesException.class),
    TestInput.createFailingTestPrefixVarName("sa_field_char", "10", JPFInspectorIncompatibleTypesException.class),
    TestInput.createFailingTestPrefixVarName("sa_field_char", "10l", JPFInspectorIncompatibleTypesException.class),
    TestInput.createFailingTestPrefixVarName("sa_field_char", "10f", JPFInspectorIncompatibleTypesException.class),
    TestInput.createFailingTestPrefixVarName("sa_field_char", "null", JPFInspectorIncompatibleTypesException.class),
    TestInput.createFailingTestPrefixVarName("sa_field_char", "\"Alf\"", JPFInspectorIncompatibleTypesException.class),

    TestInput.createFailingTestPrefixVarName("sa_field_int", "false", JPFInspectorIncompatibleTypesException.class),
    TestInput.createFailingTestPrefixVarName("sa_field_int", "\'c\'", JPFInspectorIncompatibleTypesException.class),
    TestInput.createFailingTestPrefixVarName("sa_field_int", "10f", JPFInspectorIncompatibleTypesException.class),
    TestInput.createFailingTestPrefixVarName("sa_field_int", "null", JPFInspectorIncompatibleTypesException.class),
    TestInput.createFailingTestPrefixVarName("sa_field_int", "\"Alf\"", JPFInspectorIncompatibleTypesException.class),

    TestInput.createFailingTestPrefixVarName("sa_field_float", "false", JPFInspectorIncompatibleTypesException.class),
    TestInput.createFailingTestPrefixVarName("sa_field_float", "\'c\'", JPFInspectorIncompatibleTypesException.class),
    TestInput.createFailingTestPrefixVarName("sa_field_float", "10", JPFInspectorIncompatibleTypesException.class),
    TestInput.createFailingTestPrefixVarName("sa_field_float", "null", JPFInspectorIncompatibleTypesException.class),
    TestInput.createFailingTestPrefixVarName("sa_field_float", "10.1", JPFInspectorIncompatibleTypesException.class),
    TestInput.createFailingTestPrefixVarName("sa_field_float", "\"Alf\"", JPFInspectorIncompatibleTypesException.class),

    TestInput.createFailingTestPrefixVarName("sa_field_double", "false", JPFInspectorIncompatibleTypesException.class),
    TestInput.createFailingTestPrefixVarName("sa_field_double", "\'c\'", JPFInspectorIncompatibleTypesException.class),
    TestInput.createFailingTestPrefixVarName("sa_field_double", "10", JPFInspectorIncompatibleTypesException.class),
    TestInput.createFailingTestPrefixVarName("sa_field_double", "null", JPFInspectorIncompatibleTypesException.class),
    TestInput.createFailingTestPrefixVarName("sa_field_double", "\"Alf\"", JPFInspectorIncompatibleTypesException.class),

    TestInput.createFailingTestPrefixBothVarName("sa_field_Map", "sa_field_Object", JPFInspectorIncompatibleTypesException.class),
    TestInput.createFailingTestPrefixVarName("sa_field_Map", "\"Alf\"", JPFInspectorIncompatibleTypesException.class),
    TestInput.createFailingTestPrefixBothVarName("sa_field_Map", "sa_field_Double", JPFInspectorIncompatibleTypesException.class),

    // Range tests
    TestInput.createFailingTestPrefixVarName(
        "sa_field_byte",
        Integer.valueOf(Byte.MIN_VALUE - 1).toString() + "l",
        JPFInspectorAssignmentOutOfRangeException.class),
    TestInput.createFailingTestPrefixVarName(
        "sa_field_byte",
        Integer.valueOf(Byte.MAX_VALUE + 1).toString() + "l",
        JPFInspectorAssignmentOutOfRangeException.class),
    TestInput
        .createFailingTestPrefixVarName("sa_field_short", Integer.valueOf(Short.MIN_VALUE - 1).toString(), JPFInspectorAssignmentOutOfRangeException.class),
    TestInput
        .createFailingTestPrefixVarName("sa_field_short", Integer.valueOf(Short.MAX_VALUE + 1).toString(), JPFInspectorAssignmentOutOfRangeException.class),
    TestInput.createFailingTestPrefixVarName(
        "sa_field_int",
        Long.valueOf(Long.valueOf(Integer.MIN_VALUE) - 1L).toString() + "l",
        JPFInspectorAssignmentOutOfRangeException.class),
    TestInput.createFailingTestPrefixVarName(
        "sa_field_int",
        Long.valueOf(Long.valueOf(Integer.MAX_VALUE) + 1L).toString() + "l",
        JPFInspectorAssignmentOutOfRangeException.class),

    // Slot assignments tests
    TestInput.createPassingTestPrefixVarName("sa_slot_boolean", "true"),
    TestInput.createPassingTestPrefixVarName("sa_slot_char", "'č'", "č"),
    TestInput.createPassingTestPrefixVarName("sa_slot_byte", Byte.valueOf(Byte.MAX_VALUE).toString()),
    TestInput.createPassingTestPrefixVarName("sa_slot_short", Short.valueOf(Short.MAX_VALUE).toString()),
    TestInput.createPassingTestPrefixVarName("sa_slot_int", Integer.valueOf(Integer.MAX_VALUE).toString()),
    TestInput.createPassingTestPrefixVarName("sa_slot_long", Long.valueOf(Long.MAX_VALUE).toString() + "L", Long.valueOf(Long.MAX_VALUE).toString()),
    // TODO - Bad parsing floats *E21 - in scientific format - exponents
    // TestInput.createPassingTestPrefixVarName("sa_slot_float", Float.valueOf(Float.MAX_VALUE).toString() + "f", Float.valueOf(Float.MAX_VALUE).toString()),
    // TestInput.createPassingTestPrefixVarName("sa_slot_double", Double.valueOf(Double.MAX_VALUE).toString(), Double.valueOf(Double.MAX_VALUE).toString()),
    TestInput.createPassingTestPrefixVarName("sa_slot_double", "0.3"),
    TestInput.createPassingTestPrefixVarName("sa_slot_Object", "\"Overcasting test\""),

    // Array assigning tests
    TestInput.createPassingTestPrefixVarName("sa_array_boolean[0]", "false"),
    TestInput.createPassingTestPrefixVarName("sa_array_boolean[1]", "true"),
    TestInput.createPassingTestPrefixVarName("sa_array_char[0]", "'♥'", "♥"),
    TestInput.createPassingTestPrefixVarName("sa_array_byte[0]", Byte.valueOf(Byte.MAX_VALUE).toString()),
    TestInput.createPassingTestPrefixVarName("sa_array_byte[1]", Byte.valueOf(Byte.MIN_VALUE).toString()),
    TestInput.createPassingTestPrefixVarName("sa_array_short[0]", Short.valueOf(Short.MAX_VALUE).toString()),
    TestInput.createPassingTestPrefixVarName("sa_array_short[1]", Short.valueOf(Short.MIN_VALUE).toString()),
    TestInput.createPassingTestPrefixVarName("sa_array_int[0]", Integer.valueOf(Integer.MAX_VALUE).toString()),
    TestInput.createPassingTestPrefixVarName("sa_array_int[1]", Integer.valueOf(Integer.MIN_VALUE).toString()),
    TestInput.createPassingTestPrefixVarName("sa_array_long[0]", Long.valueOf(Long.MAX_VALUE).toString() + "L", Long.valueOf(Long.MAX_VALUE).toString()),
    TestInput.createPassingTestPrefixVarName("sa_array_long[1]", Long.valueOf(Long.MIN_VALUE).toString() + "L", Long.valueOf(Long.MIN_VALUE).toString()),
    // TODO - Bad parsing floats *E21 - in scientific format - exponents
    // Float.NEGATIVE_INFINITY,
    // Float.NEGATIVE_INFINITY,
    // Float.NEGATIVE_INFINITY,
    // Float.MIN_VALUE,
    // (float) (-Math.PI),
    // 0.0f,
    // (float) (Math.PI),
    // Float.MAX_VALUE,
    // Float.POSITIVE_INFINITY,
    // Float.POSITIVE_INFINITY,
    // Float.POSITIVE_INFINITY,
    // Float.NaN,
    // Float.NaN };

    // TODO - not parsing
    // TestInput.createPassingTestPrefixVarName("sa_array_float[0]", "NaN"),
    // TODO - no conversion
    // TestInput.createPassingTestPrefixVarName("sa_array_float[1]", "not-a-number"),
    // TestInput.createPassingTestPrefixVarName("sa_array_float[2]", "+inf"),
    // TestInput.createPassingTestPrefixVarName("sa_array_float[3]", "positive_infinity"),
    // TestInput.createPassingTestPrefixVarName("sa_array_float[4]", "pos_inf"),
    // ...
    // TestInput.createPassingTestPrefixVarName("sa_array_float[4]", "+inf"),

    TestInput.createPassingTestPrefixVarName("sa_array_double[0]", "NaN"),
    TestInput.createPassingTestPrefixVarName("sa_array_double[1]", "not-a-number", "NaN"),

    TestInput.createPassingTestPrefixVarName("sa_array_double[2]", "+inf", "Infinity"),
    TestInput.createPassingTestPrefixVarName("sa_array_double[3]", "positive_infinity", "Infinity"),
    TestInput.createPassingTestPrefixVarName("sa_array_double[4]", "pos_inf", "Infinity"),

    // TestInput.createPassingTestPrefixVarName("sa_slot_float", Float.valueOf(Float.MAX_VALUE).toString() + "f", Float.valueOf(Float.MAX_VALUE).toString()),
    // TestInput.createPassingTestPrefixVarName("sa_slot_double", Double.valueOf(Double.MAX_VALUE).toString(), Double.valueOf(Double.MAX_VALUE).toString()),
    TestInput.createPassingTestCopyVarValuePrefixBothVarNames("sa_array_Object", "sa_field_Map", "null"),
    // TODO - Not parsing (F) in PathFinder
    // TestInput.createPassingTestPrefixVarName("sa_array2_String_String[0][0]", "\"Great Java PathFinder\""),
    // TestInput.createPassingTestPrefixVarName("sa_array_String[0][0]", "\"Great Java PathFinder\""),
    TestInput.createPassingTestPrefixVarName("sa_array2_String_String[0][0]", "\"Alf\""),
    TestInput.createPassingTestPrefixVarName("sa_array2_String_String[0][1]", "\"\""),
    TestInput.createPassingTestPrefixVarName("sa_array2_String_String[1][0]", "null"),

    // Failing tests - access out of bound exception
    TestInput.createFailingTestPrefixVarName("sa_array_boolean[2]", "true", JPFInspectorArrayIndexOutOutRangeException.class)

  };

  public static void main (String[] testMethods) {
    runTestsOfThisClass(testMethods);
  }

}
