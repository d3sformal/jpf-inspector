package gov.nasa.jpf.inspector.tests.acceptance;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPFShell;
import gov.nasa.jpf.inspector.frontends.cmd.CommandLineShell;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;

@RunWith(Parameterized.class)
public class CorrectOutputTest {

  @Parameters(name = "{0}")
  public static Iterable<? extends Object> data() {
     return Arrays.asList(
    //      getCase("simple", "hello"),
      //    getCase("simple", "breakpoint-expressions"),
          getCase("legacy", "AssertSimpleTest")
     );
  }
  private CorrectOutputTestCase testCase;

  private static CorrectOutputTestCase getCase(String folder, String name) {
    return new CorrectOutputTestCase(
            folder + "." + name,
            folder + "/" + name + ".in",
            folder + "/" + name + ".out",
            folder + "/" + "app.jpf"
    );
  }

  public CorrectOutputTest(CorrectOutputTestCase testCase) {
    this.testCase = testCase;
  }

  private static final String BASEFOLDER = "jpf-inspector/src/tests/gov/nasa/jpf/inspector/tests/acceptance/";

  @Test
  public void test() throws FileNotFoundException {
    String applicationPropertyFile = BASEFOLDER + testCase.applicationFile;
    String inputFile = BASEFOLDER + testCase.inputFile;
    String outputFile = BASEFOLDER + testCase.outputFile;
    String[] args = new String[] { applicationPropertyFile };

    InputStream inputStream = new FileInputStream(inputFile);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(baos);

    JPFShell shell = new CommandLineShell(applicationPropertyFile, inputStream, printStream);
    shell.start(args);


    String actualOutput = new String(baos.toByteArray(), StandardCharsets.UTF_8);
    String expectedOutput = readAllFile(outputFile);
    Judge.judge(actualOutput, expectedOutput);
  }

  private static String readAllFile(String filename) {
    // http://stackoverflow.com/a/10176143/1580088
    File f = new File(filename);
    try {
      byte[] bytes = Files.readAllBytes(f.toPath());
      return new String(bytes,"UTF-8");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }

  private static class CorrectOutputTestCase {
    private String name;
    public String inputFile;
    public String outputFile;
    public String applicationFile;

    @Override
    public String toString() {
      return name;
    }

    public CorrectOutputTestCase(String name, String inputFile, String outputFile, String applicationFile) {
      this.name = name;
      this.inputFile = inputFile;
      this.outputFile = outputFile;
      this.applicationFile = applicationFile;

    }
  }
}
