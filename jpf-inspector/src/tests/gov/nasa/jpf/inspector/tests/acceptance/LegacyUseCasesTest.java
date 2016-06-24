package gov.nasa.jpf.inspector.tests.acceptance;

import gov.nasa.jpf.inspector.tests.acceptance.architecture.CorrectOutputAbstractTest;
import gov.nasa.jpf.inspector.tests.acceptance.architecture.CorrectOutputTestCase;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

//@Ignore
@RunWith(Parameterized.class)
public class LegacyUseCasesTest extends CorrectOutputAbstractTest {

  public LegacyUseCasesTest(CorrectOutputTestCase testCase) {
    super(testCase);
  }

  @Test
  public void test() {
    baseTest();
  }

  @Parameters(name = "{0}")
  public static Iterable<?> data() {
     return Arrays.asList(
             getCase("legacy/usecases/uc1", "part1")
           //  getCase("legacy/usecases/uc1", "usecase")
           //  getCase("legacy/usecases/uc2", "usecase"),
           //  getCase("legacy/usecases/uc3", "usecase"),
           //  getCase("legacy/usecases/uc4", "usecase"),
           //  getCase("legacy/usecases/ucjpfw", "usecase")
     );
  }




}
