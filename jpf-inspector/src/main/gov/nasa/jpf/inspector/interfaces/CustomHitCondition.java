package gov.nasa.jpf.inspector.interfaces;

import gov.nasa.jpf.inspector.server.expression.InspectorState;

/**
 * Implement this interface to create a custom hit condition that you can use with JPF Inspector.
 * See the wiki page "Custom Hit Conditions" for documentation on how to use this class, or see the example
 * "OnLineHitCondition" for an example implementation.
 *
 * Remember that a class that implements {@link CustomHitCondition} must also have a parameterless constructor.
 */
public interface CustomHitCondition {
  /**
   * Returns the keyword that must be used as the "method name" to call this hit condition in a hit condition expression.
   * For example, if this method returns "helloCondition", then the command "create breakpoint helloCondition()" would
   * create this hit condition.
   *
   * This method MUST always returns the same constant value.
   */
  String getName();

  /**
   * Evaluates whether the current state of the system under test causes this hit condition to trigger and returns true
   * if it does. This method should usually first check whether it was triggered by the appropriate listener method, and
   * then use {@link InspectorState#getVM()} to get the information it needs.
   * @param state Current state of the system under test and the Inspector.
   * @param arguments Arguments passed to this hit condition. An argument is usually a string or a number but it could also be part of the program state.
   * @return True if the breakpoint should trigger; false otherwise.
   */
  boolean isHit(InspectorState state, Object[] arguments);

  /**
   * Returns additional information whenever a breakpoint with this hit condition is hit.
   * It may return null if no useful additional information is available.
   * @param state Current state of the system under test and the Inspector.
   * @param arguments Arguments passed to this hit condition. An argument is usually a string or a number but it could also be part of the program state.
   * @return Details to be shown to the user, or null if no details are provided.
   */
  String getDetails(InspectorState state, Object[] arguments);
}
