package gov.nasa.jpf.inspector.client.commands;

import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;

import java.io.PrintStream;

/**
 * Represents the "cg select" command that forces the current choice generator to use the user-specified choice.
 */
public class CmdChoiceSelect extends ClientCommand {

  public static final int USE_CURRENT_CHOICE = -2;

  static {
    assert (USE_CURRENT_CHOICE != ChoiceGeneratorsInterface.USE_DEFAULT_CHOICE);
  }

  private final int choice;

  public CmdChoiceSelect (int choice) {
    this.choice = choice;
  }

  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    try {
      if (choice == USE_CURRENT_CHOICE) {
        throw new JPFInspectorGenericErrorException("You must specify a choice.");
        // inspector.start(); // Continue until next CG
      }
      inspector.selectChoice(choice);
    } catch (JPFInspectorException e) {
      outStream.print(e.getMessage());
      client.recordComment(e.getMessage());
    }

  }

  @Override
  public String getNormalizedCommand () {
    String result = "choice_generators select";
    // Use current choice
    if (choice == USE_CURRENT_CHOICE) {
      return result;
    }
    // Default forward trace
    if (choice == ChoiceGeneratorsInterface.USE_DEFAULT_CHOICE) {
      return result + " *";
    }
    // Use specified choice
    return "choice_generators select " + choice;
  }

}
