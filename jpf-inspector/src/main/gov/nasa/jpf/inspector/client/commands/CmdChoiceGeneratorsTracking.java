//
// Copyright (C) 2010-2011 Pavel Jančík
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
package gov.nasa.jpf.inspector.client.commands;

import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGMode;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGNotificationSpecification;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGTypes;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;

import java.io.PrintStream;

/**
 * Represents the "enable cg" command and its variants. These commands specify whether and how should user interact
 * with choice generators.
 */
public class CmdChoiceGeneratorsTracking extends ClientCommand {

  /**
   * Specifies the kind of the choice generator.
   */
  public enum CGTypeSpec {
    /**
     * Applicable to scheduling choice generators that schedule threads.
     */
    CGS_SCHEDULING,
    /**
     * Applicable to data choice generators, usually inside Verify methods.
     */
    CGS_DATA,
    /**
     * Applicable to all choice generators.
     */
    CGS_ALL
  }

  private final CGTypeSpec cgsType;
  private final CGMode cgMode;
  private final boolean enabled;

  public CmdChoiceGeneratorsTracking (CGTypeSpec cgsType, CGMode cgMode, boolean enabled) {
    assert (cgsType != null);
    assert (cgMode != null);

    this.cgsType = cgsType;
    this.cgMode = cgMode;
    this.enabled = enabled;
  }

  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    if (CGTypeSpec.CGS_SCHEDULING.equals(cgsType) || CGTypeSpec.CGS_ALL.equals(cgsType)) {
      inspector.modifyCGNotifications(new CGNotificationSpecification(CGTypes.CG_TYPE_SCHEDULING, cgMode, enabled));
    }
    if (CGTypeSpec.CGS_DATA.equals(cgsType) || CGTypeSpec.CGS_ALL.equals(cgsType)) {
      inspector.modifyCGNotifications(new CGNotificationSpecification(CGTypes.CG_TYPE_DATA, cgMode, enabled));
    }
  }

  @Override
  public String getNormalizedCommand () {
    StringBuilder sb = new StringBuilder();
    sb.append(choiceGeneratorTrackingMode2NormalizedString(enabled));
    sb.append(' ');
    sb.append(choiceGenetatorMode2NormalizedString(cgMode));

    sb.append(' ');
    sb.append(choiceGenetatorSpecType2NormalizedString(cgsType));
    sb.append(" choice_generators");
    return sb.toString();
  }

  private static String choiceGeneratorTrackingMode2NormalizedString(boolean mode) {
    return mode ? "enable" : "disable";
  }

  private static String choiceGenetatorMode2NormalizedString(CGMode cgMode) {
    assert (cgMode != null);

    switch (cgMode) {
    case CG_MODE_ASK:
      return "ask";
    case CG_MODE_PRINT:
      return "print";
    default:
      throw new RuntimeException("Internal error: Unknown " + cgMode.getClass().getName() + " entry: " + cgMode);
    }
  }

  private static String choiceGenetatorSpecType2NormalizedString(CGTypeSpec cgsType) {
    assert (cgsType != null);

    switch (cgsType) {
    case CGS_ALL:
      return "all";
    case CGS_DATA:
      return "data";
    case CGS_SCHEDULING:
      return "scheduling"; // sched
    default:
      throw new RuntimeException("Internal error: Unknown " + cgsType.getClass().getName() + " entry: " + cgsType);
    }
  }
}
