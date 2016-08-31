package gov.nasa.jpf.inspector.tests.acceptance.hitcondition.custom;

import gov.nasa.jpf.inspector.interfaces.CustomHitCondition;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.inspector.server.expression.InspectorState;

/**
 * This is an example implementation of a custom hit condition.
 *
 * To create a breakpoint with this hit condition, use a command such as "create breakpoint position(20)".
 * The condition name is "position" because that's the string return by its {#link getName} method.
 *
 * Note that the class has a parameterless constructor (the default one) - that is important because that's the constructor
 * that is used by the Inspector to create an instance of it.
 *
 * This hit condition hits just before an instruction on the specified code line is executed. However, compared to
 * the built-in hit condition "pos", it will hit before <b>each</b> instruction on the line, not just the first.
 *
 * The details returned by the {#link getDetails} method are displayed to the user when the condition hits.
 *
 * To be able to use this hit condition, it must be loaded via properties. By default, this example condition is loaded
 * in the Inspector's jpf.properties file (look in there).
 */
public class OnLineHitCondition implements CustomHitCondition {
  @Override
  public String[] getNames() {
    return new String[]{ "line", "l"};
  }

  int whichLine;

  @Override
  public void initialize(String[] arguments) {
    if (arguments.length != 1) {
      throw new RuntimeException("There must be exactly one argument.");
    }
    try {
      whichLine = Integer.parseInt(arguments[0]);
    } catch (NumberFormatException e) {
      throw new RuntimeException("The only argument must be a line number.");
    }
  }

  @Override
  public boolean isHit(JPFInspectorBackEndInterface inspector, InspectorState state) {
    if (state.getListenerMethod() != InspectorState.ListenerMethod.LM_EXECUTE_INSTRUCTION) {
      return false;
    }

    // Hits whenever the next instruction to be executed has the line number at the first parameter.
    return state.getVM().getInstruction().getLineNumber() == whichLine;
  }

  @Override
  public String getDetails(JPFInspectorBackEndInterface inspector, InspectorState state) {
    return "These are details of the example custom hit condition.";
  }
}
