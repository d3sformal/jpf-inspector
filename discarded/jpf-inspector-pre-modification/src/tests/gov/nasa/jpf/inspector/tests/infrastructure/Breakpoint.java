package gov.nasa.jpf.inspector.tests.infrastructure;

import gov.nasa.jpf.inspector.client.commands.CmdBreakpointCreate;
import gov.nasa.jpf.inspector.interfaces.BreakPointCreate;
import gov.nasa.jpf.inspector.interfaces.BreakPointStates;

public class Breakpoint {

  public static final int BREAKME_LINE = 14;
  public static final String FILE_NAME = Breakpoint.class.getName().replace('.', '/') + ".java";

  public static void breakMe () {
    // Following line has to be at the line BREAKME (14)
    System.out.println("Break me");
  }

  public static CmdBreakpointCreate.ConsoleBreakpointCreate getBreakpoint () {
    CmdBreakpointCreate.ConsoleBreakpointCreate result = new CmdBreakpointCreate.ConsoleBreakpointCreate();
    result.setName(Breakpoint.class.getSimpleName() + ".breakMe:" + BREAKME_LINE);
    result.setState(BreakPointStates.BP_STATE_ENABLED);
    result.setBounds(BreakPointCreate.DEFAULT_LOWER_BOUND, BreakPointCreate.DEFAULT_UPPER_BOUND);
    result.setBPExpression("position = " + FILE_NAME + ":" + BREAKME_LINE);
    return result;
  }

}
