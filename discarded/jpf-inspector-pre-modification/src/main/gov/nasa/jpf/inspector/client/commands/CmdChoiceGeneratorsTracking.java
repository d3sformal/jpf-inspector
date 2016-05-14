package gov.nasa.jpf.inspector.client.commands;

import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGMode;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGNotificationSpecification;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGTypes;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;

import java.io.PrintStream;

public class CmdChoiceGeneratorsTracking extends ClientCommand {

  public enum CGTypeSpec {
    CGS_SCHEDULING, CGS_DATA, CGS_ALL
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
  public void executeCommands (JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
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
    if (cgMode != null) {
      sb.append(' ');
      sb.append(choiceGenetatorMode2NormalizedString(cgMode));
    }

    if (cgsType != null) {
      sb.append(' ');
      sb.append(choiceGenetatorSpecType2NormalizedString(cgsType));
    }
    sb.append(" choice_generators");
    return sb.toString();
  }

  public static String cgNotificationSpec2NormalizedString (CGNotificationSpecification spec) {
    assert spec != null;

    StringBuilder sb = new StringBuilder();
    sb.append(choiceGeneratorTrackingMode2NormalizedString(spec.nofiticationEnabled()));
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

  public static String choiceGeneratorTrackingMode2NormalizedString (boolean mode) {
    return mode ? "enable" : "disable";
  }

  public static String choiceGenetatorMode2NormalizedString (CGMode cgMode) {
    assert (cgMode != null);

    switch (cgMode) {
    case CG_MODE_ASK:
      return "ask";
    case CG_MODE_PRINT:
      return "print";
    default:
      throw new RuntimeException("Internal error: Unkwnow " + cgMode.getClass().getName() + " entry: " + cgMode);
    }
  }

  public static String choiceGenetatorSpecType2NormalizedString (CGTypeSpec cgsType) {
    assert (cgsType != null);

    switch (cgsType) {
    case CGS_ALL:
      return "all";
    case CGS_DATA:
      return "data";
    case CGS_SCHEDULING:
      return "scheduling"; // sched
    default:
      throw new RuntimeException("Internal error: Unkwnow " + cgsType.getClass().getName() + " entry: " + cgsType);
    }
  }

  public static String choiceGenetatorType2NormalizedString (CGTypes cgType) {
    assert (cgType != null);

    switch (cgType) {
    case CG_TYPE_DATA:
      return "data";
    case CG_TYPE_SCHEDULING:
      return "scheduling"; // sched
    default:
      throw new RuntimeException("Internal error: Unkwnow " + cgType.getClass().getName() + " entry: " + cgType);
    }
  }

}
