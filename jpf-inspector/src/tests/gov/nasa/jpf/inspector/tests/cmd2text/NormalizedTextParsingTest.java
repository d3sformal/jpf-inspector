package gov.nasa.jpf.inspector.tests.cmd2text;

import gov.nasa.jpf.inspector.client.ClientCommandInterface;
import gov.nasa.jpf.inspector.client.parser.CommandParserFactory;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorParsingErrorException;
import gov.nasa.jpf.inspector.tests.infrastructure.InspectorTest;

import java.io.PrintStream;

import org.junit.Test;

/**
 * Test which checks if {@link ClientCommandInterface#getNormalizedCommand()} is parsable text.
 * 
 * Test parse selected inputs one by one.
 * Each input has to be successfully parsed into {@link ClientCommandInterface}, the normalized version of command is obtained and parsed again.
 * 
 * @author Alf
 * 
 */
public class NormalizedTextParsingTest extends InspectorTest {
  private static final boolean DEBUG = true;
  private final PrintStream out = System.out;

  private static final String createBPcmds[] = {
    "cr bp name=log state =    log  10<=hit_count<15 this_text_is_not_test_because_it_is_parsed_by_server_part",
    "create bp name=enable99 property_violated",
    "create breakpoint name=enable state=en hc<16 property_violated",
    "create breakpoint name=hit_count state=dis 3<=hit_count property_violated hit_count áeíóúý+-.", };

  @Test
  public void cmdCreateBP () {
    testCommands(createBPcmds);
  }

  private static final String deleteBPcmds[] = { "del bp 10", };

  @Test
  public void cmdDeleteBP () {
    testCommands(deleteBPcmds);
  }

  private static final String showBPcmds[] = { "sw bp", };

  @Test
  public void cmdShowBP () {
    testCommands(showBPcmds);
  }

  private static final String cgTrackingsCmds[] = {
    "en ask  sched cg",
    "dis print  all cg",
    "dis ask  data cg", };

  @Test
  public void cmdCGTracking () {
    testCommands(cgTrackingsCmds);
  }

  private static final String selectChoiceCmds[] = {
    "cg sel 3",
    "cg sel *", // Default choice
    "cg sel", // Use current choice
  };

  @Test
  public void cmdSelectChoice () {
    testCommands(selectChoiceCmds);
  }

  private static final String printCmds[] = {
    "print Alf is great",
    "print print enabled", };

  @Test
  public void cmdPrint () {
    testCommands(printCmds);
  }

  private static final String runCmds[] = {
    "run",
    "continue",
    "break", };

  @Test
  public void cmdRun () {
    testCommands(runCmds);
  }

  private static final String singleSteppingCmds[] = {
    "sins",
    "bsins",
    "so",
    "bso",
    "si",
    "bsi",
    "sout",
    "bsout",
    "st",
    "bst",
    "st all",
    "bst all",
    "st sched",
    "bst sched",
    "st data",
    "bst data", };

  @Test
  public void cmdSingleStepping () {
    testCommands(singleSteppingCmds);
  }

  private static final String statusThreadsCmds[] = {
    "thread 3",
    "ti" // thread info for all
  };

  @Test
  public void cmdStatusThreads () {
    testCommands(statusThreadsCmds);
  }

  private static final String threadsPCcmds[] = {
    "thread_pc 10",
    "thpc" // all threads
  };

  @Test
  public void cmdThreadsPC () {
    testCommands(threadsPCcmds);
  }

  private static final String usedCGcmds[] = { "used cg", };

  @Test
  public void cmdUsedCG () {
    testCommands(usedCGcmds);
  }

  protected void testCommands (String[] cmds) {

    for (int i = 0; i < cmds.length; i++) {
      String origCmd = cmds[i];
      if (DEBUG) {
        out.println("Parsing \"" + origCmd + "\"");
      }

      ClientCommandInterface cmd = null;
      try {
        cmd = CommandParserFactory.getClientCommandParser().parseCommands(origCmd);
      } catch (JPFInspectorParsingErrorException e) {
        fail("Error while parsing initial command: " + origCmd + " error" + e.getMessage());
      }
      if (cmd == null) {
        fail("Initial command has not been parsed successfully: " + origCmd);
      }

      if (DEBUG) {
        out.println("\t\t ... OK");
      }

      String normalizedText = cmd.getNormalizedCommand();
      if (DEBUG) {
        out.println("\tNormalized command version \"" + normalizedText + "\"");
      }
      try {
        cmd = CommandParserFactory.getClientCommandParser().parseCommands(normalizedText);
      } catch (JPFInspectorParsingErrorException e) {
        fail("Error while parsing normalized command: " + origCmd + " error" + e.getMessage());
      }
      if (DEBUG) {
        out.println("\t\t ... OK");
      }
    }
  }

  public static void main (String[] testMethods) {
    runTestsOfThisClass(testMethods);
  }

}
