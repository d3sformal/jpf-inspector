package gov.nasa.jpf.inspector.tests.acceptance.architecture;

import org.junit.Assert;

/**
 * The Judge compares outputs for similarity and "judges" whether the actual output matches the expected output.
 */
public final class Judge {
  /**
   * Removes whitespace and special characters from a line. This is useful because these small changes in English
   * grammar or command output might happen sometimes and we don't want to modify the expected output files all the time.
   */
  private static String simplify(String line) {
    return line.replaceAll("[\\[\\]\t+\\-.,*/:^ $()]", "");
  }
  public static void judge(String actual, String expected) {
    actual = actual.trim().replace("\r", "");
    expected = expected.trim().replace("\r", "");
    String[] actualLines = actual.split("\n");
    String[] expectedLines = expected.split("\n");
    if (actualLines.length != expectedLines.length) {
      Assert.assertEquals(expected, actual);
      // We compare the whole thing so that it's easier to debug the result.
    }
    else {
      for (int i = 0; i < actualLines.length; i++) {
        String actualLine = simplify(actualLines[i]);
        String expectedLine = simplify(expectedLines[i]);
        if (!actualLine.equals(expectedLine)) {
          Assert.assertEquals(expected, actual);
        }
      }
    }
  }
}
