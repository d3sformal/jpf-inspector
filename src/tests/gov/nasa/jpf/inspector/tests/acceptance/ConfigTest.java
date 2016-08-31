package gov.nasa.jpf.inspector.tests.acceptance;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.inspector.tests.acceptance.architecture.CorrectOutputAbstractTest;
import org.junit.Assert;
import org.junit.Test;

public class ConfigTest {
  @Test
  public void testLoadedInspector() {
    Config config = new Config(new String[] { CorrectOutputAbstractTest.BASEFOLDER + "/simple/app.jpf" });
    Assert.assertEquals("gov.nasa.jpf.inspector.server.jpf.DFSearchInspector", config.get("search.class"));
  }
}
