package gov.nasa.jpf.inspector.tests.acceptance;

import gov.nasa.jpf.JPFShell;
import gov.nasa.jpf.inspector.frontends.cmd.CommandLineShell;
import gov.nasa.jpf.inspector.tests.acceptance.architecture.CorrectOutputAbstractTest;
import gov.nasa.jpf.inspector.tests.acceptance.architecture.CorrectOutputTestCase;
import gov.nasa.jpf.inspector.utils.InspectorConfiguration;
import gov.nasa.jpf.shell.ShellManager;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Main class that runs tests. Will be refactored later.
 */
@RunWith(Parameterized.class)
public class CorrectOutputTest extends CorrectOutputAbstractTest {

  @Parameters(name = "{0}")
  public static Iterable<?> data() {
     return Arrays.asList(
          getCase("simple", "hello"),
          getCase("simple", "breakpoint-expressions"),
          getCase("features/extensibility", "alias"),
          getCase("features/extensibility", "custom"),
          getCase("features/extensibility", "then"),
          getCase("features/extensibility", "cycle"),
          getCase("features/extensibility", "args"),
          getCase("features/extensibility", "argument_mismatch"),
          getCase("features/extensibility", "invalid_alias"),
          getCase("bugfix", "printnull"),
          getCase("legacy", "AssertSimpleTest"),
          getCase("legacy", "BreakpointTest"),
          getCase("legacy/simpleassignments", "SimpleAssignments"),
          getCase("features/fixheap", "heap"),
          getCase("features/decimal", "decimal")
     );
  }
  private CorrectOutputTestCase testCase;

  public CorrectOutputTest(CorrectOutputTestCase testCase) {
    this.testCase = testCase;
  }

  @Test
  public void test() throws FileNotFoundException {
    InspectorConfiguration.staticReset();
    String applicationPropertyFile = BASEFOLDER + testCase.applicationFile;
    String inputFile = BASEFOLDER + testCase.inputFile;
    String outputFile = BASEFOLDER + testCase.outputFile;
    String[] args = new String[] { applicationPropertyFile };

    InputStream inputStream = new FileInputStream(inputFile);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(baos);


    JPFShell shell = new CommandLineShell(applicationPropertyFile, inputStream, printStream);
    //ShellManager.getManager().getConfig().printEntries();
    shell.start(args);


    String actualOutput = new String(baos.toByteArray(), StandardCharsets.UTF_8);
    String expectedOutput = readAllFile(outputFile);
    Judge.judge(actualOutput, expectedOutput);
  }

  @After
  public void cleanup() {
    ShellManager.destroy();
  }

}
