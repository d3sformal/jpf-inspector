package gov.nasa.jpf.inspector.tests.acceptance.architecture;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public abstract class CorrectOutputAbstractTest {
  public static final String BASEFOLDER = "jpf-inspector/src/tests/gov/nasa/jpf/inspector/tests/acceptance/";

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
}
