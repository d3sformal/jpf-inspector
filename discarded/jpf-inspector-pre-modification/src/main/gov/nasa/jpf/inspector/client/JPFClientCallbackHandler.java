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

package gov.nasa.jpf.inspector.client;

import gov.nasa.jpf.inspector.client.commands.CmdBreakpointShow;
import gov.nasa.jpf.inspector.interfaces.AssertStatus;
import gov.nasa.jpf.inspector.interfaces.BreakPointStates;
import gov.nasa.jpf.inspector.interfaces.BreakPointStatus;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGTypes;
import gov.nasa.jpf.inspector.interfaces.CommandsInterface.InspectorStates;
import gov.nasa.jpf.inspector.interfaces.InspectorCallBacks;

import java.io.PrintStream;

/**
 * Handles notifications from the JPFInspector server part.
 */
public class JPFClientCallbackHandler implements InspectorCallBacks {
  private final PrintStream out; // Console where print server notifications
  private CB_METHODS prevCB; // Previous callback

  public JPFClientCallbackHandler (PrintStream out) {
    this.out = out;
  }

  @Override
  public void genericError (String msg) {
    out.println("ERR: " + msg);
    prevCB = CB_METHODS.CB_GENERIC_ERROR;
  }

  @Override
  public void genericInfo (String msg) {
    out.println("INFO: " + msg);
    // prevCB = CB_METHODS.CB_GENERIC_INFO;
  }

  private static boolean checkIfShowStateChangeNofitication (CB_METHODS prevCB, InspectorStates newState) {
    if (prevCB == null) {
      return true; // First CB
    }

    if (InspectorStates.JPF_TERMINATING.equals(newState) || InspectorStates.JPF_STARTED.equals(newState)) {
      return true; // Always show JPF started and terminating
    }

    if (CB_METHODS.CB_BREAKPOINT_HIT.equals(prevCB) || CB_METHODS.CB_CG_CHOICE_TO_USE.equals(prevCB)) {
      return false; // Don't show stop notification after breakpoint hit
    }

    if (InspectorStates.JPF_RUNNING.equals(newState) && CB_METHODS.CB_STATE_CHANGE.equals(prevCB)) {
      // Suppose previous SB was JPF Stopped ad next command started JPF execution (run, (back_)step_over, )
      // --> hide obvious JPF started notification
      return false;
    }

    return true; // Fail safe
  }

  @Override
  public void notifyStateChange (InspectorStates newState, String details) {
    // Check if notification can be hidden
    if (checkIfShowStateChangeNofitication(prevCB, newState)) {

      if (newState == InspectorStates.JPF_STARTED) {
        out.println("INFO: JPF created and connected, SuT is started");
      } else if (newState == InspectorStates.JPF_RUNNING) {
        out.println("INFO: SuT is running");
      } else if (newState == InspectorStates.JPF_STOPPED) {
        out.println("INFO: SuT is stopped");
        if (details != null && !details.trim().isEmpty()) {
          out.println("\t" + details);
        }
      } else if (newState == InspectorStates.JPF_TERMINATING) {
        out.println("INFO: JPF Terminating");
      } else {
        throw new RuntimeException("Unknown " + InspectorStates.class.getName() + " enum entry " + newState);
      }
    }
    prevCB = CB_METHODS.CB_STATE_CHANGE;
    if (newState == InspectorStates.JPF_TERMINATING) {
      prevCB = null;
    }

  }

  @Override
  public void notifyBreakpointHit (BreakPointStatus bp) {
    assert bp != null;

    if (bp instanceof AssertStatus) {
      AssertStatus as = (AssertStatus) bp;

      String message = "Assertion violated";
      if (bp.getState() == BreakPointStates.BP_STATE_LOGGING) {
        message = "Logging assertion violation";
      }
      out.println("INFO: " + message + " [ assert " + as.getNormalizedPosition() + " " + as.getNormalizedCondition() + " ]");

    } else {
      // Standard breakpoint
      String message = "Breakpoint hit";
      if (bp.getState() == BreakPointStates.BP_STATE_LOGGING) {
        message = "Logging breakpoint hit";
      }
      out.println("INFO: " + message + " [ " + CmdBreakpointShow.breakpointToString(bp) + "]");
      String details = bp.getDetails();
      if (details != null && !details.trim().isEmpty()) {
        out.println("\t" + details);
      }
    }
    prevCB = CB_METHODS.CB_BREAKPOINT_HIT;
  }

  @Override
  public void notifyChoiceGeneratorNewChoice (CGTypes cgType, String cgName, int cgId, String[] choices, int nextChoice, int defaultChoice) {
    StringBuilder result = new StringBuilder();

    result.append("ChoiceGeneratorAdvance - ");
    generateCGHeader(result, cgType, cgName, cgId);
    result.append('\n');

    for (int i = 0; i < choices.length; i++) {
      result.append('\t');

      if (i == defaultChoice) {
        result.append('*');
      } else {
        result.append(' ');
      }
      if (i == nextChoice) {
        result.append('>');
      } else {
        result.append(' ');
      }
      result.append(i);
      result.append('-');
      result.append(choices[i]);
      result.append('\n');
    }
    out.print(result.toString());
    prevCB = CB_METHODS.CB_CG_NEW_CHOICE;
  }

  @Override
  public void notifyUsedChoice (CGTypes cgType, String cgName, int cgId, int usedChoiceIndex, String usedChoice) {
    StringBuilder result = new StringBuilder();

    result.append("ChoiceGeneratorAdvance used values - ");
    generateCGHeader(result, cgType, cgName, cgId);
    result.append('>');
    result.append(usedChoiceIndex);
    result.append('-');
    result.append(usedChoice);
    result.append('\n');
    out.print(result.toString());
    prevCB = CB_METHODS.CB_CG_USED_CHOICE;
  }

  private boolean showHintCgSelect = true;

  @Override
  public void specifyChoiceToUse (int maxChoiceIndex) {
    String userText = "Execution is halted. Specify which choice to use (0-" + (maxChoiceIndex) + ")";
    if (showHintCgSelect) {
      showHintCgSelect = false;
      userText += "\n\tHint: Use 'cg select CHOICE_INDEX' command";
      // TODO create command which will print list of possible choices
      // userText += "\n\tHint: Use 'cg select CHOICE_INDEX' command\n";
    }
    out.println(userText);
    prevCB = CB_METHODS.CB_CG_CHOICE_TO_USE;
  }

  private static void generateCGHeader (StringBuilder sb, ChoiceGeneratorsInterface.CGTypes cgType, String cgName, int cgId) {
    if (CGTypes.CG_TYPE_DATA.equals(cgType)) {
      sb.append("data CG");
    } else if (CGTypes.CG_TYPE_SCHEDULING.equals(cgType)) {
      sb.append("scheduling CG");
    } else {
      sb.append("unknown CG type");
    }
    sb.append(" - ");
    sb.append(cgName);
    sb.append(" (");
    sb.append(Integer.toHexString(cgId));
    sb.append(") : ");
  }
}
