//
// Copyright (C) 2016 Petr Hudeček
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

package gov.nasa.jpf.inspector.interfaces;

import gov.nasa.jpf.inspector.server.expression.InspectorState;

/**
 * Implement this interface to create a custom hit condition that you can use with JPF Inspector.
 * See the wiki page "Custom Hit Conditions" for documentation on how to use this interface, or see the example
 * "extensibility.OnLineHitCondition" for an example implementation.
 *
 * Remember that a class that implements {@link CustomHitCondition} must also have a parameterless constructor.
 */
public interface CustomHitCondition {
  /**
   * Returns the keywords that may be used as the "method name" to call this hit condition in a hit condition expression.
   * For example, if this method returns a "helloCondition", then the command "create breakpoint helloCondition()" would
   * create this hit condition.
   *
   * Each element of the array is one way to name the condition. For example, if the built-in condition
   * "position" were implemented as a custom hit condition, then this method would return an array consisting
   * of "position" and "pos".
   * This method MUST always returns the same constant value.
   */
  String[] getNames();


  /**
   * This method is called once per breakpoint, when the user executes the "create breakpoint" command that
   * instantiates this hit condition. In this method, the custom hit condition should process the arguments and
   * optionally throw an exception (of any type - only the message will be shown to the user) if there is a wrong
   * number of arguments or they are otherwise illegal.
   *
   * @param arguments Arguments passed to the hit condition during the "create breakpoint" command.
   */
  void initialize(String[] arguments);

  /**
   * Evaluates whether the current state of the system under test causes this hit condition to trigger and returns true
   * if it does. This method should usually first check whether it was triggered by the appropriate listener method, and
   * then use {@link InspectorState#getVM()} to get the information it needs.
   *
   * @param inspector The Inspector backend interface provides access to low-level features. It may not be fully documented.
   * @param state Current state of the system under test and the Inspector.
   * @return True if the breakpoint should trigger; false otherwise.
   */
  boolean isHit(JPFInspectorBackEndInterface inspector, InspectorState state);

  /**
   * Returns additional information whenever a breakpoint with this hit condition is hit.
   * It may return null if no useful additional information is available.
   *
   * @param state Current state of the system under test and the Inspector.
   * @param inspector The Inspector backend interface provides access to low-level features. It may not be fully documented.
   * @return Details to be shown to the user, or null if no details are provided.
   */
  String getDetails(JPFInspectorBackEndInterface inspector,InspectorState state);
}
