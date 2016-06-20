package gov.nasa.jpf.inspector.tests.acceptance;

import gov.nasa.jpf.JPFShell;
import gov.nasa.jpf.inspector.frontends.cmd.CommandLineShell;
import gov.nasa.jpf.inspector.tests.acceptance.architecture.Judge;
import gov.nasa.jpf.inspector.utils.InspectorConfiguration;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static gov.nasa.jpf.inspector.tests.acceptance.architecture.CorrectOutputAbstractTest.BASEFOLDER;

@Ignore
public class LegacySimpleAssignmentsTest {
  @Test
  public void testAssignments() {
    InspectorConfiguration.staticReset();
    String applicationPropertyFile = BASEFOLDER + "legacy/simpleassignments/app.jpf";
    String[] args = { applicationPropertyFile };


    InputStream inputStream = null; // TODO
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(baos);


    JPFShell shell = new CommandLineShell(applicationPropertyFile, inputStream, printStream);
    shell.start(args);

  }
}
