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
import gov.nasa.jpf.inspector.client.commands.CmdChoiceGeneratorsTracking.CGTypeSpec;
import gov.nasa.jpf.inspector.interfaces.CommandsInterface.StepType;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.utils.InspectorConfiguration;

import java.io.PrintStream;

/**
 * Represents a single-stepping command such as "step_in" or "back_step_over".
 * All such commands, including back-stepping commands, are handled by this class.
 */
public class CmdSingleStepping extends ClientCommand {

  /**
   * Whether this is a forward or backward step.
   */
  private final boolean forward;
  private final StepType stepType;
  private String fieldNameExpression = null;
  /**
   * Number of times to execute this command.
   */
  private final int stepCount;

  public static CmdSingleStepping createBackBreakpointHit() {
    return new CmdSingleStepping(false, StepType.BACK_BREAKPOINT_HIT, 1);
  }
  public static CmdSingleStepping createBackFieldAccess(String fieldNameExpression) {
    return new CmdSingleStepping(false, StepType.BACK_FIELD_ACCESS, 1, fieldNameExpression);
  }

  public static CmdSingleStepping createCmdSingleSteppingTransition(boolean forward, CGTypeSpec typeOrNull, Integer repeatCnt) {
    assert repeatCnt == 1;
    CGTypeSpec type = typeOrNull;
    // Set default value
    if (type == null) {
      type = CGTypeSpec.CGS_ALL;
    }

    switch (type) {
      case CGS_ALL:
        return new CmdSingleStepping(forward, StepType.ST_TRANSITION_ALL, repeatCnt);
      case CGS_DATA:
        return new CmdSingleStepping(forward, StepType.ST_TRANSITION_DATA, repeatCnt);
      case CGS_SCHEDULING:
        return new CmdSingleStepping(forward, StepType.ST_TRANSITION_SCHED, repeatCnt);
      default:
        throw new RuntimeException(
                "Internal error - Unsupported " + type.getClass().getSimpleName() + " value - " + type);
    }
  }

  public CmdSingleStepping (boolean forward, StepType stepType, Integer repeatCnt) {
    this(forward, stepType, repeatCnt, null);
  }
  public CmdSingleStepping (boolean forward, StepType stepType, Integer repeatCnt, String fieldNameExpression) {
    assert stepType != null;
    assert repeatCnt != null;

    this.forward = forward;
    this.stepType = stepType;
    this.stepCount = repeatCnt;
    this.fieldNameExpression = fieldNameExpression;
  }

  @Override
  public boolean isSafeToExecuteWhenNotPaused() {
    return false;
  }

  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    assert inspector != null;

    for (int i = 0; i < stepCount; i++) {
      try {
        if (forward) {
          inspector.forwardStep(stepType);
        } else {
          if (fieldNameExpression != null) {
            inspector.backFieldAccessStep(fieldNameExpression);
          } else {
            inspector.backstep(stepType);
          }
        }
      } catch (JPFInspectorException e) {
        outStream.println(e.getMessage());
        client.recordComment(e.getMessage());
      }
      boolean isFinalStep = (i == (stepCount - 1));
      if (!isFinalStep) {
        inspector.waitUntilStopped();
      }
    }
    if (InspectorConfiguration.getInstance().shouldWaitAfterRun()) {
      inspector.waitUntilStopped();
    }
  }

  @Override
  public String getNormalizedCommand () {
    String optionalDirection = "";
    if (forward == false) {
      optionalDirection = "back_";
    }
    String optionalCount = "";
    if (stepCount != 1) {
      optionalCount = " " + stepCount;
    }

    switch (stepType) {
      case BACK_BREAKPOINT_HIT:
        return "back_breakpoint_hit";
      case BACK_FIELD_ACCESS:
        return "back_field_access " + fieldNameExpression;
    case ST_INSTRUCTION:
      return optionalDirection + "step_instruction" + optionalCount;
    case ST_LINE:
      return optionalDirection + "step_over" + optionalCount;
    case ST_STEP_IN:
      return optionalDirection + "step_in" + optionalCount;
    case ST_STEP_OUT:
      return optionalDirection + "step_out" + optionalCount;
    case ST_TRANSITION_ALL:
      return optionalDirection + "step_transition all" + optionalCount;
    case ST_TRANSITION_DATA:
      return optionalDirection + "step_transition data" + optionalCount;
    case ST_TRANSITION_SCHED:
      return optionalDirection + "step_transition scheduling" + optionalCount;
    default:
      throw new RuntimeException("Internal error: Unknown " + stepType.getClass().getName() + " entry: " + stepType);
    }
  }
}
