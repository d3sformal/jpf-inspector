//
// Copyright (C) 2010 United States Government as represented by the
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
import gov.nasa.jpf.inspector.client.commands.CmdChoiceGeneratorsTracking.CGTypeSpec;
import gov.nasa.jpf.inspector.interfaces.CommandsInterface.StepType;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;

import java.io.PrintStream;

//TODO implement step count (there are 2 possibilities)
// a) add cycle into execute command + (wait until sJPF is stoped) + comment (with original repetition count)
// b) update normalized expression + pass stepCnt to the JPFInspectorBackEnd + implement there

/**
 * Represents a single-stepping command such as "step_in" or "step_over". All such commands, including back-stepping
 * commands, are handled by this class.
 */
public class CmdSingleStepping extends ClientCommand {

  final private boolean forward; // forward/backward step
  final private StepType stepType;
  final private int stepCnt; // How many times repeat the command

  public static CmdSingleStepping createCmdSingleSteppingTransition (boolean forward, CGTypeSpec type, Integer repeatCnt) {
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
      throw new RuntimeException("Internal error - Unsupported " + type.getClass().getSimpleName() + " value - " + type);
    }
  }

  public CmdSingleStepping (boolean forward, StepType stepType, Integer repeatCnt) {
    assert stepType != null;
    assert repeatCnt != null;

    this.forward = forward;
    this.stepType = stepType;
    this.stepCnt = repeatCnt;
  }

  @Override
  public boolean isSafeToExecuteWhenNotPaused() {
    return false;
  }

  @Override
  public void executeCommands (JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    assert inspector != null;

    if (stepCnt != 1) {
      outStream.println("ERR: StepCount not implemented yet. Repeat command manually.");
    }

    try {
      if (forward) {
        inspector.forwardStep(stepType);
      } else {
        inspector.backwardStep(stepType);
      }
    } catch (JPFInspectorException e) {
      outStream.println(e.getMessage());
      client.recordComment(e.getMessage());
    }

  }

  @Override
  public String getNormalizedCommand () {
    String result = "";
    if (forward == false) {
      result = "back_";
    }

    switch (stepType) {
    case ST_INSTRUCTION:
      return result + "step_instruction";
    case ST_LINE:
      return result + "step_over";
    case ST_STEP_IN:
      return result + "step_in";
    case ST_STEP_OUT:
      return result + "step_out";
    case ST_TRANSITION_ALL:
      return result + "step_transition all";
    case ST_TRANSITION_DATA:
      return result + "step_transition data";
    case ST_TRANSITION_SCHED:
      return result + "step_transition scheduling";
    default:
      throw new RuntimeException("Internal error: Unknown " + stepType.getClass().getName() + " entry: " + stepType);
    }
  }
}
