package gov.nasa.jpf.inspector.tests.infrastructure;

import gov.nasa.jpf.inspector.client.commands.CmdBreakpointCreate;
import gov.nasa.jpf.inspector.interfaces.BreakPointCreationInformation;
import gov.nasa.jpf.inspector.interfaces.BreakPointStates;

public class Breakpoint {

  public static final int BREAKME_LINE = 14;
  public static final String FILE_NAME = Breakpoint.class.getName().replace('.', '/') + ".java";

  public static void breakMe () {
    // Following line has to be at the line BREAKME (14)
    System.out.println("Break me");
  }

  public static CmdBreakpointCreate.ConsoleBreakpointCreationExpression getBreakpoint () {
    CmdBreakpointCreate.ConsoleBreakpointCreationExpression result = new CmdBreakpointCreate.ConsoleBreakpointCreationExpression();
    result.setName(Breakpoint.class.getSimpleName() + ".breakMe:" + BREAKME_LINE);
    result.setState(BreakPointStates.BP_STATE_ENABLED);
    result.setBounds(BreakPointCreationInformation.DEFAULT_LOWER_BOUND, BreakPointCreationInformation.DEFAULT_UPPER_BOUND);
    result.setBPExpression("position = " + FILE_NAME + ":" + BREAKME_LINE);
    return result;
  }

}
