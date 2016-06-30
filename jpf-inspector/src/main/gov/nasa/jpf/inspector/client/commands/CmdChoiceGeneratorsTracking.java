//
// Copyright (C) 2011 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
//
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
//
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
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

  public static String cgNotificationSpec2NormalizedString (CGNotificationSpecification spec) {
    assert spec != null;

    StringBuilder sb = new StringBuilder();
    sb.append(choiceGeneratorTrackingMode2NormalizedString(spec.isNotificationEnabled()));
    CGMode cgMode = spec.getNotificationMode();
    if (cgMode != null) {
      sb.append(' ');
      sb.append(choiceGenetatorMode2NormalizedString(cgMode));
    }
    CGTypes cgType = spec.getNotificationType();
    if (cgType != null) {
      sb.append(' ');
      sb.append(choiceGenetatorType2NormalizedString(cgType));
    }
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

  private static String choiceGenetatorType2NormalizedString(CGTypes cgType) {
    assert (cgType != null);

    switch (cgType) {
    case CG_TYPE_DATA:
      return "data";
    case CG_TYPE_SCHEDULING:
      return "scheduling"; // sched
    default:
      throw new RuntimeException("Internal error: Unknown " + cgType.getClass().getName() + " entry: " + cgType);
    }
  }

}
