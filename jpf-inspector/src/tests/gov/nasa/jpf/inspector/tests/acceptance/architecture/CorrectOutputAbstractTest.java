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

public abstract class CorrectOutputAbstractTest {
  public static final String BASEFOLDER = "jpf-inspector/src/tests/gov/nasa/jpf/inspector/tests/acceptance/";
  protected CorrectOutputTestCase testCase;

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

  protected static String readAllFile(String filename) {
    // http://stackoverflow.com/a/10176143/1580088
    File f = new File(filename);
    try {
      byte[] bytes = Files.readAllBytes(f.toPath());
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
      Assert.fail("Input file was not found.");
    }
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(baos);


    JPFShell shell = new CommandLineShell(applicationPropertyFile, inputStream, printStream);
    shell.start(args);

    String actualOutput = new String(baos.toByteArray(), StandardCharsets.UTF_8);
    String expectedOutput = readAllFile(outputFile);
    Judge.judge(actualOutput, expectedOutput);
  }
}
