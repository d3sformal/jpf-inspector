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

package gov.nasa.jpf.inspector.client;

import gov.nasa.jpf.inspector.client.commands.CmdBreakpointShow;
import gov.nasa.jpf.inspector.frontends.swing.AuxiliaryInspectorPanel;
import gov.nasa.jpf.inspector.interfaces.*;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGTypes;

import java.io.PrintStream;
import java.util.List;

/**
 * Handles notifications from the JPFInspector server.
 *
 * This class is created by the client.
 */
public class JPFClientCallbackHandler implements InspectorCallbacks {
  private final List<AuxiliaryInspectorPanel> listeners;
  /**
   * Console where server notifications should be printed out.
   */
  private final PrintStream out;
  /**
   * Previous callback issued by the server. Each method of this class is required
   * to set a new value to this field.
   */
  private CallbackKind lastCallback;
  /**
   * Indicates whether the user should be given the syntax of the "cg select" command when a choice generator
   * requests user interaction. This begins true and is set to false after the first time this hint is given.
   * This could maybe be refactored into a more global tutorial system.
   */
  private boolean showHintCgSelect = true;

  /**
   * Initializes a new instance of this class. This class should never be used directly, but should be instead
   * decorated in {@link CallbackRecordingDecorator}.
   * @param listeners Listeners that should be notified when a callback arrives.
   * @param out Where server notifications should be printed out.
   */
  public JPFClientCallbackHandler(List<AuxiliaryInspectorPanel> listeners, PrintStream out) {
    this.listeners = listeners;
    this.out = out;
  }

  private void callbackReceived() {
    for(AuxiliaryInspectorPanel panel : listeners) {
      panel.fireCommandExecutedOrCallbackReceived();
    }
  }

  @Override
  public void genericError (String msg) {
    out.println("ERR: " + msg);
    lastCallback = CallbackKind.CB_GENERIC_ERROR;
    callbackReceived();
  }

  @Override
  public void genericInfo (String msg) {
    out.println("INFO: " + msg);

    // Previously, this line was commented out. The lastCallback field is used to distinguish whether information
    // about state change should be printed out. This may therefore cause some state changes to be printed
    // or, conversely, to be omitted. I don't know what's better so I'll just leave this in for now and
    // see if something breaks.

    // On second thought, let's leave it commented out. Weird things are happening and until we fully understand
    // the code, we shouldn't be making uneasy changes.

    // lastCallback = CB_METHODS.CB_GENERIC_INFO;
    callbackReceived();
  }

  private static boolean shouldStateChangeNotificationBeShown(CallbackKind prevCB,
                                                              InspectorStatusChange newState) {
    if (prevCB == null) {
      return true;
      // First state change is always shown to the user.
      // In practice, this should always be JPF_STARTING, because the lastCallback field is reset upon termination.
    }

    if (InspectorStatusChange.JPF_TERMINATING.equals(newState) || InspectorStatusChange.JPF_STARTED.equals(newState)) {
      return true; // Always show JPF started and terminating
    }

    if (CallbackKind.CB_BREAKPOINT_HIT.equals(prevCB) || CallbackKind.CB_CG_CHOICE_TO_USE.equals(prevCB)) {
      return false; // Don't show stop notification after breakpoint hit
      // Normally, when a breakpoint is hit, it first issues the "breakpoint hit" callback and then follows
      // by issuing "state change". Because the first one already displays information, we don't need to show
      // a state change as well.
    }

    //noinspection RedundantIfStatement
    if (InspectorStatusChange.JPF_RUNNING.equals(newState) && CallbackKind.CB_STATE_CHANGE.equals(prevCB)) {
      // Suppose previous SB was JPF Stopped ad next command started JPF execution (run, (back_)step_over, )
      // --> hide obvious JPF started notification
      return false;
    }

    return true;
  }

  @Override
  public void notifyStateChange (InspectorStatusChange newState, String details) {

    if (shouldStateChangeNotificationBeShown(lastCallback, newState)) {
      if (newState == InspectorStatusChange.JPF_STARTED) {
        out.println("INFO: JPF created and connected, SuT is started");
      } else if (newState == InspectorStatusChange.JPF_RUNNING) {
        out.println("INFO: SuT is running");
      } else if (newState == InspectorStatusChange.JPF_STOPPED) {
        out.println("INFO: SuT is stopped");
        if (details != null && !details.trim().isEmpty()) {
          out.println("\t" + details);
        }
      } else if (newState == InspectorStatusChange.JPF_TERMINATING) {
        out.println("INFO: JPF Terminating");
      } else {
        throw new RuntimeException("Unknown " + InspectorStatusChange.class.getName() + " enum entry " + newState);
      }
    }
    lastCallback = CallbackKind.CB_STATE_CHANGE;
    if (newState == InspectorStatusChange.JPF_TERMINATING) {
      lastCallback = null;
    }
    callbackReceived();
  }

  @Override
  public void notifyBreakpointHit (BreakpointStatus bp) {
    assert bp != null;

    if (bp instanceof AssertStatus) {
      AssertStatus as = (AssertStatus) bp;

      String message = "Assertion violated:";
      if (bp.getState() == BreakpointState.LOGGING) {
        message = "Logging assertion violated:";
      }
      out.println("INFO: " + message + " [assert " + as.getNormalizedPosition() + " " + as.getNormalizedCondition() + "]");

    } else {
      // Standard breakpoint
      String message = "Breakpoint hit:";
      if (bp.getState() == BreakpointState.LOGGING) {
        message = "Logging breakpoint hit:";
      }
      out.println("INFO: " + message + " [" + CmdBreakpointShow.breakpointToString(bp) + "]");
      String details = bp.getDetails();
      if (details != null && !details.trim().isEmpty()) {
        out.println("\t" + details);
      }
    }
    lastCallback = CallbackKind.CB_BREAKPOINT_HIT;
    callbackReceived();
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
    if (nextChoice == choices.length) {
      result.append("\t > [exhausted]\n");
    }
    out.print(result.toString());
    lastCallback = CallbackKind.CB_CG_NEW_CHOICE;
    callbackReceived();
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
    lastCallback = CallbackKind.CB_CG_USED_CHOICE;
    callbackReceived();
  }


  @Override
  public void specifyChoiceToUse (int maxChoiceIndex) {
    String userText = "Execution is halted. Specify which choice to use (0-" + (maxChoiceIndex) + "):";
    if (showHintCgSelect) {
      showHintCgSelect = false;
      userText += "\n\tHint: Use the command 'cg select CHOICE_INDEX'.";
      // TODO create command which will print list of possible choices
    }
    out.println(userText);
    lastCallback = CallbackKind.CB_CG_CHOICE_TO_USE;
    callbackReceived();
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
    sb.append(" (0x");
    sb.append(Integer.toHexString(cgId));
    sb.append(") : ");
  }
}
