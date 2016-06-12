package gov.nasa.jpf.inspector.tests.acceptance;

import gov.nasa.jpf.Config;
import org.junit.Assert;
import org.junit.Test;

public class ConfigTest {
  @Test
  public void testLoadedInspector() {
    Config config = new Config(new String[] { CorrectOutputTest.BASEFOLDER + "/simple/app.jpf" });
    Assert.assertEquals("gov.nasa.jpf.inspector.server.jpf.DFSearchInspector", config.get("search.class"));
  }
}
