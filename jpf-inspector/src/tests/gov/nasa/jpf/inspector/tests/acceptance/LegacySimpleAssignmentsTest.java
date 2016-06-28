package gov.nasa.jpf.inspector.tests.acceptance;

import gov.nasa.jpf.inspector.common.pse.PSEVariable;
import gov.nasa.jpf.inspector.common.pse.ProgramStateEntry;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorArrayIndexOutOutRangeException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorAssignmentOutOfRangeException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorIncompatibleTypesException;
import gov.nasa.jpf.inspector.tests.acceptance.architecture.InteractableTestShell;
import gov.nasa.jpf.inspector.utils.InspectorConfiguration;
import gov.nasa.jpf.shell.ShellManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

import static gov.nasa.jpf.inspector.tests.acceptance.architecture.CorrectOutputAbstractTest.BASEFOLDER;

/**
 * This legacy test verifies that program state modification works correctly.
 */
public class LegacySimpleAssignmentsTest {
  @Test
  public void testAssignments() {
    InspectorConfiguration.staticReset();
    String applicationPropertyFile = BASEFOLDER + "legacy/simpleassignments/app.jpf";
    String[] args = { applicationPropertyFile };

    ByteArrayOutputStream normalOutput = new ByteArrayOutputStream();
    PrintStream callbackPrinter =System.out;

    InteractableTestShell shell = new InteractableTestShell(applicationPropertyFile, normalOutput, callbackPrinter);
    shell.start(args);

    System.out.println("\tCreating breakpoint..." + shell.executeCommand("create breakpoint method_invoke=*:breakpoint"));
    System.out.println("Showing breakpoints..." + shell.executeCommand("show bp"));
    System.out.println("\tRunning..." + shell.executeCommand("run"));
    System.out.println("\tWaiting..." + shell.executeCommand("wait"));

    // Set values
    System.out.println("\tSetting values...\n");
    setValues(shell);

    System.out.println("\tResuming...");
    System.out.println(shell.executeCommand("run && wait"));

    // Check values
    System.out.println("\tChecking set values...\n");
    checkValues(shell);

    System.out.println("\tFinishing...");
    System.out.println(shell.executeCommand("run && wait"));
    System.out.println("\tDone.");
  }

  private static void checkValues(InteractableTestShell shell) {
    for (TestInput spec : testInputs) {
      if (spec.passing == false) {
        continue;
      }

      try {
        System.out.print("\t\tPrinting " + spec.varName);
        ProgramStateEntry pse1 = shell.getServer().evaluateStateExpression(spec.varName);

        Assert.assertNotNull(pse1);
        Assert.assertTrue(pse1 instanceof PSEVariable);

        PSEVariable pseVar = (PSEVariable) pse1;

        System.out.println(", result: " + pseVar.getVarValue());

        assert spec.passing;

        Assert.assertEquals(spec.expectedValue, pseVar.getVarValue());

      } catch (JPFInspectorException e) {
        Assert.fail(e.getMessage());
      }
    }
  }

  private static void setValues(InteractableTestShell shell) {
    for (TestInput spec : testInputs) {
      try {
        String setExpression = spec.varName + " = " + spec.varSetValue;
        System.out.println("\t\tExecuting the 'set' command with the following argument: " + setExpression);
        shell.getServer().setValue(setExpression);

        if (spec.passing == false) {
          Assert.fail("Exception of the " + spec.getExceptionType().getSimpleName() + " type is expected.");
        }
      } catch (JPFInspectorException e) {
        if (spec.passing == true) {
          Assert.fail(e.toString());
        } else {
          if ((e.getClass().equals(spec.getExceptionType())) == false) {
            Assert.fail(
                    "Wrong type of exception thrown. Expected exception of type " + spec.getExceptionType().getSimpleName() + " but thrown exception type is "
                            + e.getClass().getSimpleName() + "\n" + e.getMessage());
          }
        }
      }
    }
  }

  @After
  public void cleanup() {
    ShellManager.destroy();
  }

  /**
   * Represents an "input/expected output" combination for the legacy Simple Assignments test.
   */
  private static class TestInput {
    public final boolean passing; // Test pass, if false=given method chould throw exception

    public final String varName; // "#stack[2].fiels - specification of the value to set (stateExpression)
    public final String varSetValue; // 15, "Alf" - text with value to set - as used in the set expression

    public final String expectedValue; // Expected value after the set as returns "print" command

    private final Class<? extends JPFInspectorException> exceptionType; // Expected exception which is thrown in case of the not passing test

    private static final String PREFFIX_STATE_EXPR = "#stackFrame[0]."; // When checked we are stopped in the main testing method.

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

    public TestInput (boolean passing, String varName, String varSetValue, String expectedValue, Class<? extends JPFInspectorException> exceptionType) {
      this.passing = passing;
      this.varName = varName;
      this.varSetValue = varSetValue;
      this.expectedValue = expectedValue;

      this.exceptionType = exceptionType;
    }

    public Class<? extends JPFInspectorException> getExceptionType() {
      return exceptionType;
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
          TestInput.createPassingTestCopyVarValuePrefixBothVarNames("sa_field_string2", "sa_field_string3", "\"" + "Bin Aladin" + "\""),
          // Assigning to predecessor
          TestInput.createPassingTestCopyVarValuePrefixBothVarNames("sa_field_Object", "sa_field_string3", "\"" + "Bin Aladin" + "\""),
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
                  Long.valueOf((long) Integer.MIN_VALUE - 1L).toString() + "l",
                  JPFInspectorAssignmentOutOfRangeException.class),
          TestInput.createFailingTestPrefixVarName(
                  "sa_field_int",
                  Long.valueOf((long) Integer.MAX_VALUE + 1L).toString() + "l",
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
}
