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
             // Use Case 1 -- fully operational
             getCase("legacy/usecases/uc1", "part1"),
             getCase("legacy/usecases/uc1", "part2"),

             // Use Case 2
             getCase("legacy/usecases/uc2", "part1"),

             // Use Case 3
             getCase("legacy/usecases/uc3", "part2"),

             // Use case 4 -- requires back_step_transition
           //  getCase("legacy/usecases/uc4", "usecase"),
             // Use case JPF W -- fully operational
             getCase("legacy/usecases/ucjpfw", "usecase")
           //  getCase("legacy/usecases/uc2", "usecase"),
           //  getCase("legacy/usecases/uc3", "usecase"),
           //  getCase("legacy/usecases/uc4", "usecase"),
     );
  }




}
