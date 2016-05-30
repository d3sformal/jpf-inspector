package gov.nasa.jpf.inspector.tests.infrastructure;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;

abstract public class InspectorTestDriver {
  protected static final boolean DEBUG = false;

  protected final JPFInspector inspector; // Inspector to test
  protected final InspectorTestCallbacks callbacks; // Callbacks from inspector
  protected final InspectorTest test; // Test where report errors
  private Thread testDriverThread;

  public InspectorTestDriver (JPFInspector inspector, InspectorTestCallbacks callbacks, InspectorTest test) {
    super();
    this.inspector = inspector;
    this.callbacks = callbacks;
    this.test = test;
  }

  public void init () {
    testDriverThread = new Thread() {

      @Override
      public void run () {
        synchronized (callbacks) {
          callbacks.notifyAll();

          try {
            driveInspector();
          } catch (JPFInspectorException e) {
            fail(e);
          } catch (AssertionError ae) {
            // Pass through assertion error (fail() calls, etc.) detected in the user code
            throw ae;
          } catch (Throwable t) {
            fail(t);
          }
        }
      }
    };
    testDriverThread.setDaemon(true);

    synchronized (callbacks) {
      testDriverThread.start();
      try {
        callbacks.wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Waits until test driver terminate (whole test is done).
   */
  public void joinTestDriver () {
    if (DEBUG) {
      System.out.println(this.getClass().getSimpleName() + ".joinTestDriver()");
    }
    try {
      testDriverThread.join();
    } catch (InterruptedException e) {
    }
    if (DEBUG) {
      System.out.println(this.getClass().getSimpleName() + ".joinTestDriver() - end");
    }
  }

  // In this method you should simulate using of inspector and check whether results are as expected
  // Thrown exception is caught and considered as error - if the exception is expected result of test, then has to be caught in the test
  abstract public void driveInspector () throws JPFInspectorException;

  /**
   * Fails with given cause -> fails because given exception is thrown
   * Never returns, always throws an exception.
   * 
   * @param cause Exception which caused the failure.
   * 
   */
  public static void fail (Throwable cause) {
    // This try catch block if here to initialize cause of the fail exception properly
    try {
      InspectorTest.fail("Unexpected exception - " + cause.getMessage());
    } catch (AssertionError ae) {
      ae.initCause(cause);
      throw ae;
    } catch (Throwable t) {
      throw new RuntimeException("UnexpectedException thworn from TestJPF.fail() method - implementation has been changed??", t);
    }

    throw new RuntimeException("TestJPF.fail() method does not throw an exception - implementation has been changed??");
  }

  public static void fail (String errorMsg) {
    InspectorTest.fail(errorMsg);
  }

}
