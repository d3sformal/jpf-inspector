package gov.nasa.jpf.inspector.tests.infrastructure;

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.jpf.JPFInspectorParallel;
import gov.nasa.jpf.util.Reflection;
import gov.nasa.jpf.util.TypeRef;
import gov.nasa.jpf.util.test.TestJPF;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class InspectorTest extends TestJPF {
  public static final Class<?>[] NO_ARGTYPES = new Class<?>[0];
  public static final Object[] NO_ARGS = new Object[0];

  public static final Class<?>[] DEFAULT_TEST_DRIVER_CONSTRUCTOR_ARGTYPE = new Class<?>[] {
    JPFInspector.class,
    InspectorTestCallbacks.class,
    InspectorTest.class };

  // Currently running JPFInspector - only single JPF test can be run at a time (no parallel execution of tests is supported)
  private JPFInspector inspector = null;

  /**
   * 
   * @param inspectorCallbacksClsSpec InstpectorTessCallback class to uses. Non-parametric constructor is required.
   * @param inspectorTestDriverClsSpec Class which uses JPF Inspector and checks if behaves as expected. Has to have 3 parametric constructor like {@see
   *        InspectorTestDriver#InspectorTestDriver(JPFInspector, InspectorCallBacks, InspectorTest)}
   * @param jpfArgs Arguments for JPF
   * @return true in JPF test run, false if test has been executed
   */
  protected boolean runInspectorTest (TypeRef inspectorCallbacksClsSpec, TypeRef inspectorTestDriverClsSpec, String... jpfArgs) {
    return runInspectorTest(inspectorCallbacksClsSpec, inspectorTestDriverClsSpec, null, null, 2, jpfArgs);
  }

  protected boolean runInspectorTest (TypeRef inspectorCallbacksClsSpec, TypeRef inspectorTestDriverClsSpec, Class<?>[] testDriverParamsClasses,
      Object[] testDriverParams, String... jpfArgs) {
    return runInspectorTest(inspectorCallbacksClsSpec, inspectorTestDriverClsSpec, testDriverParamsClasses, testDriverParams, 2, jpfArgs);
  }

  /**
   * 
   * @param inspectorCallbacksClsSpec InstpectorTessCallback class to uses. Non-parametric constructor is required.
   * @param inspectorTestDriverClsSpec Class which uses JPF Inspector and checks if behaves as expected. Has to have first 3parametric constructor like {@see
   *        InspectorTestDriver#InspectorTestDriver(JPFInspector, InspectorCallBacks, InspectorTest, ...)} Additional parameters are passed
   * @param testDriverParamsClasses Types of additional parameters for test driver
   * @param testDriverParams Additional parameters for testDriver
   * @param testMethodDepth Which method on the call stack is the test method to execute (count from the top, from the caller of this method)
   * @param jpfArgs
   * @return
   */
  protected boolean runInspectorTest (TypeRef inspectorCallbacksClsSpec, TypeRef inspectorTestDriverClsSpec, Class<?>[] testDriverParamsClasses,
      Object[] testDriverParams, int testMethodDepth, String... jpfArgs) {

    if (runDirectly) {
      return true;
    } else {
      System.out.println("InspectorTest.runInspectorTest()");
      // Create Callbacks
      Class<? extends InspectorTestCallbacks> inspectorCallbacksCls = null;
      try {
        inspectorCallbacksCls = inspectorCallbacksClsSpec.asNativeSubclass(InspectorTestCallbacks.class);
      } catch (ClassCastException e) {
        fail("not a InspectorClassBacks type: " + inspectorCallbacksClsSpec);
      } catch (ClassNotFoundException cnfx) {
        fail("InspectorClassBacks class not found: " + inspectorCallbacksClsSpec);
      }

      InspectorTestCallbacks inspectorCallbacks = getInstance(inspectorCallbacksCls, NO_ARGTYPES, NO_ARGS);

      inspector = new JPFInspectorParallel(inspectorCallbacks);
      inspectorCallbacks.setInspector(inspector);

      // Create test driver
      Class<? extends InspectorTestDriver> testDriverCls = null;
      try {
        testDriverCls = inspectorTestDriverClsSpec.asNativeSubclass(InspectorTestDriver.class);
      } catch (ClassCastException e) {
        fail("not a InspectorTestDriver type: " + inspectorTestDriverClsSpec);
      } catch (ClassNotFoundException e) {
        fail("InspectorTestDriver class not found: " + inspectorTestDriverClsSpec);
      }

      Class<?>[] testDriverConstructorArgsType = concatArrays(DEFAULT_TEST_DRIVER_CONSTRUCTOR_ARGTYPE, testDriverParamsClasses);

      assert (DEFAULT_TEST_DRIVER_CONSTRUCTOR_ARGTYPE.length == 3);
      Object[] defaultTestDriverConstructorArgs = new Object[] {
        inspector,
        inspectorCallbacks,
        this };
      Object[] testDriverConstructorArgs = concatArrays(defaultTestDriverConstructorArgs, testDriverParams);

      InspectorTestDriver testDriver = getInstance(testDriverCls, testDriverConstructorArgsType, testDriverConstructorArgs);
      if (testDriver == null) {
        fail("InspectorTestDriver - failed to instanciate");
      }

      testDriver.init();

      // Create and set JPF to inspector
      /*
      TODO clean up this thing before migration:
      StackTraceElement caller = Reflection.getCallerElement(testMethodDepth);
      jpfArgs = appendTestClass(jpfArgs, caller);
      createAndRunJPF()
      */
      StackTraceElement caller = Reflection.getCallerElement(testMethodDepth);
      report(jpfArgs);

      JPF jpf = createJPF(caller, jpfArgs);
      jpf.getVM().recordSteps(true);
      try {
        inspector.bindWithJPF(jpf);
      } catch (JPFInspectorGenericErrorException e) {
        fail("Fail to initialize test infrastructure: " + e.getMessage());
      }

      try {
        jpf.run(); // runJPF(jpf);
      } catch (Throwable t) {
        t.printStackTrace();
        fail("JPF internal exception executing: ", jpfArgs, t.toString());
      }
      if (jpf.foundErrors()) {
        fail("JPF found errors: " + jpf.getSearchErrors().get(0));
      }
      return false;
    }
  }

  public static <T> T getInstance (Class<T> type, Class<?>[] argTypes, Object[] args) {
    if (type == null) {
      return null;
    }

    try {
      Constructor<?> ctor = type.getConstructor(argTypes);
      return type.cast(ctor.newInstance(args));
    } catch (Exception ex) {
      System.out.println(ex);
    }

    // Try non parametric constructor
    try {
      Constructor<?> ctor = type.getConstructor(NO_ARGTYPES);
      return type.cast(ctor.newInstance(NO_ARGS));
    } catch (Exception ex) {
    }

    return null;
  }

  static public <T> T[] concatArrays (T[] arr1, T[] arr2) {
    if (arr1 == null) {
      return arr2;
    }
    if (arr2 == null) {
      return arr1;
    }

    int totalLen = arr1.length + arr2.length;
    T[] result = Arrays.copyOf(arr1, totalLen);
    // Append arr2
    for (int i = 0; i < arr2.length; i++) {
      result[arr1.length + i] = arr2[i];
    }
    return result;
  }

}
