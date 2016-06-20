package gov.nasa.jpf.inspector.tests.acceptance;

import gov.nasa.jpf.inspector.tests.acceptance.architecture.CorrectOutputAbstractTest;
import gov.nasa.jpf.inspector.tests.acceptance.architecture.CorrectOutputTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

@RunWith(Parameterized.class)
public class CorrectOutputTest extends CorrectOutputAbstractTest {

  public CorrectOutputTest(CorrectOutputTestCase testCase) {
    super(testCase);
  }

  @Test
  public void test() {
    baseTest();
  }

  @Parameters(name = "{0}")
  public static Iterable<?> data() {
     return Arrays.asList(
          getCase("simple", "hello"),
          getCase("simple", "breakpoint-expressions"),
          getCase("hitcondition", "complex-breakpoint"),
          getCase("bugfix", "printnull"),
          getCase("bugfix", "b"),
          getCase("legacy", "AssertSimpleTest"),
          getCase("legacy", "BreakpointTest"),
          getCase("legacy/simpleassignments", "SimpleAssignments"),
          getCase("features/fixheap", "heap"),
          getCase("features/decimal", "decimal")
     );
  }




}
