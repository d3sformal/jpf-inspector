package gov.nasa.jpf.inspector.tests.acceptance.architecture;

import gov.nasa.jpf.JPFShell;
import gov.nasa.jpf.inspector.frontends.cmd.CommandLineShell;
import gov.nasa.jpf.inspector.utils.InspectorConfiguration;
import gov.nasa.jpf.shell.ShellManager;
import org.junit.After;
import org.junit.Assert;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Tests deriving from this class run the Inspector using the command line shell, and compare its console output
 * to an expected-output file.
 */
public abstract class CorrectOutputAbstractTest {
  public static final String BASEFOLDER = "jpf-inspector/src/tests/gov/nasa/jpf/inspector/tests/acceptance/";
  private CorrectOutputTestCase testCase;

  public CorrectOutputAbstractTest(CorrectOutputTestCase testCase) {
    this.testCase = testCase;
  }

  protected static CorrectOutputTestCase getCase(String folder, String name) {
    return new CorrectOutputTestCase(
            folder + "." + name,
            folder + "/" + name + ".in",
            folder + "/" + name + ".out",
            folder + "/" + "app.jpf"
    );
  }

  private static String readAllFile(String filename) {
    // http://stackoverflow.com/a/10176143/1580088
    File file = new File(filename);
    try {
      byte[] bytes = Files.readAllBytes(file.toPath());
      return new String(bytes,"UTF-8");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }


  @After
  public void cleanup() {
    ShellManager.destroy();
  }

  protected void baseTest()  {

    InspectorConfiguration.staticReset();
    String applicationPropertyFile = BASEFOLDER + testCase.applicationFile;
    String inputFile = BASEFOLDER + testCase.inputFile;
    String outputFile = BASEFOLDER + testCase.outputFile;
    String[] args = { applicationPropertyFile };

    InputStream inputStream = null;
    try {
      inputStream = new FileInputStream(inputFile);
    } catch (FileNotFoundException e) {
      Assert.fail("Input file (" + inputFile + ") was not found.");
    }
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(baos);
    String expectedOutput = readAllFile(outputFile);



    JPFShell shell = new CommandLineShell(applicationPropertyFile, inputStream, printStream);
    shell.start(args);


    String actualOutput = new String(baos.toByteArray(), StandardCharsets.UTF_8);
    Judge.judge(actualOutput, expectedOutput);


  }
}
