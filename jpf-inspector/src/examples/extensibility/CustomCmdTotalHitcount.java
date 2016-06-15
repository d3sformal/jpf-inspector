package extensibility;

import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.BreakpointStatus;
import gov.nasa.jpf.inspector.interfaces.CustomCommand;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;

import java.io.PrintStream;
import java.util.List;

/**
 * This is an example custom command class that demonstrates how you can add your own commands to JPF Inspector.
 *
 * This class represents a new "hitcount" command that counts the total number of times all breakpoint were hit,
 * in total.
 */
public class CustomCmdTotalHitcount implements CustomCommand {
  /**
   * Any of the strings returned from this method can be used as the command name.
   * Note that all the returned strings must be valid identifiers, i.e. only use underscores, ASCII letters and digits
   * and must not start with a digit.
   */
  @Override
  public String[] getNames() {
    return new String[] {
      "hitcount",
      // "hc",
      // Note that using "hc" here would not permit us to use "hc" as a command because "hc" is a keyword of JPF Inspector
      // (may be used instead of hit_count in the "create breakpoint" command).
      "total_hit_count",
      "thc"
    };
  }

  @Override
  public String getHelpText() {
    return "Sums the hit counts of all existing breakpoints and prints the result.";
  }

  @Override
  public boolean isSafeToExecuteWhenNotPaused() {
    return true;
  }

  @Override
  public void execute(String arguments, JPFInspectorClient client, JPFInspectorBackEndInterface server, PrintStream outStream) {
    List<BreakpointStatus> breakpoints = server.getBreakPoints();
    int sum = 0;
    for (BreakpointStatus breakpoint : breakpoints) {
      sum += breakpoint.getHitCounterTotal();
    }
    outStream.println("Breakpoints have been hit a total of " + sum + " times.");
  }
}
