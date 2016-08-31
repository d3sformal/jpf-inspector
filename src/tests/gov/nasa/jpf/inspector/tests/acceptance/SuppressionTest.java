package gov.nasa.jpf.inspector.tests.acceptance;

import gov.nasa.jpf.inspector.tests.acceptance.architecture.CorrectOutputAbstractTest;
import gov.nasa.jpf.inspector.tests.acceptance.architecture.CorrectOutputTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

/**
 * This test tests new features added during the 2016 update.
 */
@RunWith(Parameterized.class)
public class SuppressionTest extends CorrectOutputAbstractTest {

  public SuppressionTest(CorrectOutputTestCase testCase) {
    super(testCase);
  }

  @Test
  public void test() {
    baseTest();
  }

  @Parameters(name = "{0}")
  public static Iterable<?> data() {
     return Arrays.asList(
          getCase("features/suppression", "nonsuppressed"),
          getCase("features/suppression", "suppressed1"),
          getCase("features/suppression", "suppressed2"),
          getCase("features/suppression", "suppressedAll")
     );
  }
}
