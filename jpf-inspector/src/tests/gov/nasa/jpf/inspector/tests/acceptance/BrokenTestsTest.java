package gov.nasa.jpf.inspector.tests.acceptance;

import gov.nasa.jpf.inspector.tests.acceptance.architecture.CorrectOutputAbstractTest;
import gov.nasa.jpf.inspector.tests.acceptance.architecture.CorrectOutputTestCase;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

@RunWith(Parameterized.class)
public class BrokenTestsTest extends CorrectOutputAbstractTest {

  public BrokenTestsTest(CorrectOutputTestCase testCase) {
    super(testCase);
  }

  @Test
  public void test() {
    baseTest();
  }

  @Parameters(name = "{0}")
  public static Iterable<?> data() {
     return Arrays.asList(
             getCase("broken", "everstop")
             // We won't test infinitely running test cases because we don't have a good means of timeout.
     );
  }




}
