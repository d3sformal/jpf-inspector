package gov.nasa.jpf.inspector.tests;

import gov.nasa.jpf.inspector.client.commands.CmdBreakpointCreate;
import gov.nasa.jpf.inspector.interfaces.CommandsInterface.InspectorStates;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.tests.infrastructure.Breakpoint;
import gov.nasa.jpf.inspector.tests.infrastructure.InspectorTest;
import gov.nasa.jpf.inspector.tests.infrastructure.InspectorTestCallbacks;
import gov.nasa.jpf.inspector.tests.infrastructure.InspectorTestDriver;
import gov.nasa.jpf.vm.Verify;
import gov.nasa.jpf.util.TypeRef;

import org.junit.Test;

public class BreakpointTest extends InspectorTest {
  private final static int BP_TEST_01_BP_HITS = 5;

  @Test
  public void breakpointTest01 () {
    if (runInspectorTest(new TypeRef("gov.nasa.jpf.inspector.tests.infrastructure.InspectorTestCallbacks"), new TypeRef(
        "gov.nasa.jpf.inspector.tests.BreakpointTest$BreakpointTest01Driver"))) {
      // Tries backward and forward steps through IntegerChoiceGenerator
      int i = Verify.getInt(0, BP_TEST_01_BP_HITS - 1);
      System.out.println(i);
      Breakpoint.breakMe();
      return;
    }
  }

  static public class BreakpointTest01Driver extends InspectorTestDriver {

    public BreakpointTest01Driver (JPFInspector inspector, InspectorTestCallbacks callbacks, InspectorTest test) {
      super(inspector, callbacks, test);
    }

    @Override
    public void driveInspector () throws JPFInspectorException {
      System.out.println("InspectorDriver");

      CmdBreakpointCreate.ConsoleBreakpointCreate bpC = Breakpoint.getBreakpoint();
      System.out.println("\tCreating breakpoint " + bpC);
      System.out.println("\tinspector.createBreakPoint(bpC) ... " + inspector.createBreakPoint(bpC));

      for (int i = 0; i < BP_TEST_01_BP_HITS; i++) {
        callbacks.waitForCB_BreakpointHit();
        System.out.println("\tBreakPoint hitted" + callbacks.getCbBreakpointHit_bp());

        callbacks.waitForCB_StateChange(InspectorStates.JPF_STOPPED);
        callbacks.nextCB_Resume_and_StateChange(InspectorStates.JPF_RUNNING);
      }
      callbacks.waitForCB_StateChange(InspectorStates.JPF_TERMINATING);

      callbacks.finishCallback();
    }
  }

  public static void main (String[] testMethods) {
    runTestsOfThisClass(testMethods);
  }

}
