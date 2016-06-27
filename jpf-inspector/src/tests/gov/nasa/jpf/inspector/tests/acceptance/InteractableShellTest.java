package gov.nasa.jpf.inspector.tests.acceptance;

import gov.nasa.jpf.inspector.tests.acceptance.architecture.InteractableTestShell;
import gov.nasa.jpf.inspector.utils.InspectorConfiguration;
import gov.nasa.jpf.shell.ShellManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static gov.nasa.jpf.inspector.tests.acceptance.architecture.CorrectOutputAbstractTest.BASEFOLDER;

public class InteractableShellTest {
  @Test
  public void testHello() {
    InspectorConfiguration.staticReset();

    InteractableTestShell interactableTestShell = new InteractableTestShell(BASEFOLDER + "simple/app.jpf",
                                                                            new ByteArrayOutputStream(),
                                                                            new PrintStream(new ByteArrayOutputStream()));
    interactableTestShell.start(new String[] { BASEFOLDER + "simple/app.jpf"});
    Assert.assertEquals("Hello to you, too, dear user! The JPF Inspector command loop is working!", interactableTestShell.executeCommand("hello"));
    Assert.assertEquals("Hello to you, too, dear user! The JPF Inspector command loop is working!", interactableTestShell.executeCommand("hello"));
    Assert.assertEquals("Hello to you, too, dear user! The JPF Inspector command loop is working!", interactableTestShell.executeCommand("hello"));

  }

  @After
  public void cleanup() {
    ShellManager.destroy();
  }
}
