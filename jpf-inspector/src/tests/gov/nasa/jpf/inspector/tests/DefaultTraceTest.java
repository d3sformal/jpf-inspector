package gov.nasa.jpf.inspector.tests;

import gov.nasa.jpf.inspector.client.commands.CmdBreakpointCreate;
import gov.nasa.jpf.inspector.interfaces.BreakPointStatus;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGMode;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGNotificationSpecification;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGTypes;
import gov.nasa.jpf.inspector.interfaces.CommandsInterface.InspectorStates;
import gov.nasa.jpf.inspector.interfaces.CommandsInterface.StepType;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorException;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorParsingErrorException;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.tests.infrastructure.Breakpoint;
import gov.nasa.jpf.inspector.tests.infrastructure.InspectorTest;
import gov.nasa.jpf.inspector.tests.infrastructure.InspectorTestCallbacks;
import gov.nasa.jpf.inspector.tests.infrastructure.InspectorTestDriver;
import gov.nasa.jpf.vm.Verify;
import gov.nasa.jpf.util.TypeRef;

import org.junit.Test;

public class DefaultTraceTest extends InspectorTest {

  @Test
  public void passTest () {

  }

  // @Test
  public void defaultTraceTest01 () {
    if (runInspectorTest(
        new TypeRef("gov.nasa.jpf.inspector.tests.infrastructure.InspectorTestCallbacks"),
        new TypeRef("gov.nasa.jpf.inspector.tests.DefaultTraceTest$DefaultTraceTest01InspectorDriver"),
        "+report.console.property_violation=error,snapshot,trace,error",
        "+report.console.show_code=true")) {
      // Tries backward and forward steps through IntegerChoiceGenerator
      int i1 = Verify.getInt(0, 4);
      System.out.println("i1=" + i1);
      Breakpoint.breakMe();
      int i2 = Verify.getInt(0, 4);
      System.out.println("i2=" + i2);
      Breakpoint.breakMe();
      int i3 = Verify.getInt(0, 4);
      System.out.println("i3=" + i3);
      Breakpoint.breakMe();
      return;
    }
  }

  static public class DefaultTraceTest01InspectorDriver extends InspectorTestDriver {

    public DefaultTraceTest01InspectorDriver (JPFInspector inspector, InspectorTestCallbacks callbacks, InspectorTest test) {
      super(inspector, callbacks, test);
    }

    @Override
    public void driveInspector () throws JPFInspectorException {
      synchronized (callbacks) {
        System.out.println("InspectorDriver");

        // Specify your own path i1=3 i2=2 i4=1
        initializePath();

        System.out.println("\tExecuting backward transitionstep");
        // Go back for 2 transitions
        inspector.backwardStep(StepType.ST_TRANSITION_DATA);
        callbacks.nextCB_StateChange(InspectorStates.JPF_RUNNING);
        callbacks.nextCB_StateChange(InspectorStates.JPF_STOPPED);

        // inspector.backwardStep(StepType.ST_TRANSITION_DATA);
        // callbacks.nextCB_StateChange();
        // callbacks.nextCB_StateChange();

        inspector.modifyCGNotifications(new CGNotificationSpecification(CGTypes.CG_TYPE_DATA, CGMode.CG_MODE_ASK, true));
        inspector.start();

        callbacks.finishCallback();
        // Let JPF continue
      }

      // InspectorTest.assertEquals(callbacks.getCbStateChange_newState(),InspectorStates.JPF_TERMINATING);
      //
      // callbacks.finishCallback();
    }

    /**
     * Initialize path to i1=3 i2=2 i4=1 and returns in stopped state in the stateChanged callbacks.
     */
    private void initializePath () throws JPFInspectorException {
      // Breakpoint which breaks after i3 at breakMe
      CmdBreakpointCreate.ConsoleBreakpointCreate bpC = Breakpoint.getBreakpoint();
      bpC.setBounds(3, BreakPointStatus.DEFAULT_UPPER_BOUND); // Break after i3
      System.out.println("\tCreating breakpoint " + bpC);
      try {
        System.out.println("\tinspector.createBreakPoint(bpC) ... " + inspector.createBreakPoint(bpC));
      } catch (JPFInspectorParsingErrorException e) {
        fail(e);
      }

      // Force first CG to uses third choice -> i1=3
      System.out.println("\tModifying notifications - modifyCGNotifications - DATA ASK");
      inspector.modifyCGNotifications(new CGNotificationSpecification(CGTypes.CG_TYPE_DATA, CGMode.CG_MODE_ASK, true));
      System.out.println("\tWaiting notification stateChange JPF_STARTED");
      callbacks.nextCB_StateChange(InspectorStates.JPF_STARTED);
      System.out.println("\tWaiting notification new ChoiceGeneratorNewChoice");
      callbacks.nextCB_ChoiceGeneratorNewChoice();
      System.out.println("\tWaiting notification specifyChoiceToUse");
      callbacks.nextCB_specifyChoiceToUse();
      inspector.modifyCGNotifications(new CGNotificationSpecification(CGTypes.CG_TYPE_DATA, CGMode.CG_MODE_ASK, false));
      // Select value
      synchronized (callbacks) {
        new Thread() {
          @Override
          public void run () {
            synchronized (callbacks) {
              System.out.println("\tSelecting choice - 4");
              try {
                inspector.selectChoice(4);
              } catch (JPFInspectorException e) {
                // TODO - Why in separate thread?
                fail(e);
              }
            }
          }
        }.start();

        System.out.println("\tWaiting notification breakpointHit");
        callbacks.nextCB_BreakpointHit();
        System.out.println("\tBreakPoint hitted" + callbacks.getCbBreakpointHit_bp());
      }
      callbacks.waitForCB_StateChange(InspectorStates.JPF_STOPPED);
      callbacks.nextCB_Resume_and_StateChange(InspectorStates.JPF_RUNNING);

      // We should reach end of the program -> repeat until i2=2 i3=1
      for (int i = 0; i < 10; i++) {
        callbacks.nextCB_BreakpointHit();
        System.out.println("\tBreakPoint hitted" + callbacks.getCbBreakpointHit_bp());
        callbacks.waitForCB_StateChange(InspectorStates.JPF_STOPPED);
        callbacks.nextCB_Resume_and_StateChange(InspectorStates.JPF_RUNNING);
      }

      callbacks.waitForCB_StateChange(InspectorStates.JPF_STOPPED);
    }
  }

  public static void main (String[] testMethods) {
    runTestsOfThisClass(testMethods);
  }

}
