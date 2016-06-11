package gov.nasa.jpf.inspector.tests.acceptance;

import org.junit.Assert;

public class Judge {
  public static void judge(String actual, String expected) {
    actual = actual.trim();
    expected = expected.trim();
    Assert.assertEquals(expected, actual);
  }
}
