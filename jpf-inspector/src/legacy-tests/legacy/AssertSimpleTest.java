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

import gov.nasa.jpf.inspector.client.commands.CmdAssert;
import gov.nasa.jpf.inspector.interfaces.AssertCreationInformation;
import gov.nasa.jpf.inspector.interfaces.CommandsInterface.InspectorStates;
import gov.nasa.jpf.inspector.interfaces.InstructionPosition;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.common.pse.ProgramStateEntry;
import gov.nasa.jpf.inspector.common.pse.PSEVariable;
import gov.nasa.jpf.inspector.tests.legacy.infrastructure.Breakpoint;
import gov.nasa.jpf.inspector.tests.legacy.infrastructure.InspectorTest;
import gov.nasa.jpf.inspector.tests.legacy.infrastructure.InspectorTestCallbacks;
import gov.nasa.jpf.inspector.tests.legacy.infrastructure.InspectorTestDriver;
import gov.nasa.jpf.vm.Verify;
import gov.nasa.jpf.util.TypeRef;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Alf
 * 
 */
public class AssertSimpleTest extends InspectorTest  {
  private final static int ASSERT_TEST_01_RANDOM_MAX = 5;

  @Test
  public void junitIsWorkingTest() {
    Assert.assertTrue(true);
  }

  @Test
  public void test01 () {
    if (runInspectorTest(new TypeRef("gov.nasa.jpf.inspector.tests.legacy.infrastructure.InspectorTestCallbacks"), new TypeRef(
        "gov.nasa.jpf.inspector.tests.legacy.AssertSimpleTest$Test01Driver"))) {

      // Tries backward and forward steps through IntegerChoiceGenerator
      int i = Verify.getInt(0, ASSERT_TEST_01_RANDOM_MAX - 1);
      System.out.println(i);
      Breakpoint.breakMe();
      return;
    }
  }

  static public class Test01Driver extends InspectorTestDriver {

    public Test01Driver (JPFInspector inspector, InspectorTestCallbacks callbacks, InspectorTest test) {
      super(inspector, callbacks, test);
    }

    @Override
    public void driveInspector () throws JPFInspectorException {
      System.out.println("InspectorDriver");

      AssertCreationInformation ac = new CmdAssert.ConsoleAssertionCreationExpression(Breakpoint.FILE_NAME + ":" + Breakpoint.BREAKME_LINE, "#stackFrame[1].i < "
          + (ASSERT_TEST_01_RANDOM_MAX - 2));
      System.out.println("\tCreating assertion " + ac);
      System.out.println("\tinspector.createAssertion(ac) ... " + inspector.createAssert(ac));

      callbacks.nextCB_StateChange(InspectorStates.JPF_STARTED);

      for (int i = 0; i < 5; i++) {
        callbacks.waitForCB_BreakpointHit();
        callbacks.waitForCB_StateChange(InspectorStates.JPF_STOPPED);

        // Check if stoppoed at right position
        Map<Integer, InstructionPosition> threadsPC = inspector.getThreadsPC(0);
        if (!threadsPC.containsKey(0)) {
          fail("Unknown position");
        }

        InstructionPosition inPos = threadsPC.get(0);
        if (!Breakpoint.FILE_NAME.equals(inPos.getFileName())) {
          fail("Bad file name: " + inPos.getFileName());
        }

        if (Breakpoint.BREAKME_LINE != inPos.getLineNumber()) {
          fail("Bad file line num: " + inPos.getLineNumber());
        }

        System.out.println("\t#pos=" + inPos.getFileName() + ":" + inPos.getLineNumber());

        ProgramStateEntry pse = inspector.evaluateStateExpression("#stackFrame[1].i");
        assert (pse != null);
        assert (pse instanceof PSEVariable);
        PSEVariable pseVar = (PSEVariable) pse;
        int value = Integer.parseInt(pseVar.getVarValue());

        if (value < (ASSERT_TEST_01_RANDOM_MAX - 2)) {
          fail("Wrong variable value: " + pseVar.getVarValue());
        }

        System.out.println("\t#stackFrame[1].i=" + pseVar.getVarValue());

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
