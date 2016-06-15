package gov.nasa.jpf.inspector.tests.acceptance.features.extensibility;

import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.BreakpointStatus;
import gov.nasa.jpf.inspector.interfaces.CustomCommand;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;

import java.io.PrintStream;
import java.util.List;

/**
 * Only used for testing.
 */
public class CustomPrintCommand implements CustomCommand {

  @Override
  public String[] getNames() {
    return new String[] {
            "print1",
            "print2"
    };
  }

  @Override
  public String getHelpText() {
    return "Testing.";
  }

  @Override
  public boolean isSafeToExecuteWhenNotPaused() {
    return true;
  }

  @Override
  public void execute(String arguments, JPFInspectorClient client, JPFInspectorBackEndInterface server, PrintStream outStream) {
    outStream.println("The custom command has been executed.");
  }
}
