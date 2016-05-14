package gov.nasa.jpf.inspector.client.commands;

import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorException;

import java.io.PrintStream;

public class CmdChoiceSelect extends ClientCommand {

  public static final int USE_CURENT_CHOICE = -2;

  {
    assert (USE_CURENT_CHOICE != ChoiceGeneratorsInterface.USE_DEFAULT_CHOICE);
  }

  private final int choice;

  public CmdChoiceSelect (int choice) {
    this.choice = choice;
  }

  @Override
  public void executeCommands (JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    try {
      if (choice == USE_CURENT_CHOICE) {
        inspector.start(); // Continue until next CG
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
    if (choice == USE_CURENT_CHOICE) {
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
