package gov.nasa.jpf.inspector.tests.acceptance;

import gov.nasa.jpf.inspector.tests.acceptance.architecture.CorrectOutputAbstractTest;
import gov.nasa.jpf.inspector.tests.acceptance.architecture.CorrectOutputTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

@RunWith(Parameterized.class)
public class FeaturesTest extends CorrectOutputAbstractTest {

  public FeaturesTest(CorrectOutputTestCase testCase) {
    super(testCase);
  }

  @Test
  public void test() {
    baseTest();
  }

  @Parameters(name = "{0}")
  public static Iterable<?> data() {
     return Arrays.asList(
          getCase("features/termination", "terminate_command"),
          getCase("features/extensibility", "alias"),
          getCase("features/extensibility", "custom"),
          getCase("features/extensibility", "then"),
          getCase("features/extensibility", "then_advanced"),
          getCase("features/extensibility", "cycle"),
          getCase("features/extensibility", "args"),
          getCase("features/extensibility", "argument_mismatch"),
          getCase("features/extensibility", "invalid_alias"),
          getCase("features/ignorejava", "ignore")
     );
  }




}
