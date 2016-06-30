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

import gov.nasa.jpf.inspector.client.commands.CmdCallback;
import gov.nasa.jpf.inspector.interfaces.BreakpointStatus;
import gov.nasa.jpf.inspector.interfaces.CallbackKind;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGTypes;
import gov.nasa.jpf.inspector.interfaces.InspectorStatusChange;
import gov.nasa.jpf.inspector.interfaces.InspectorCallbacks;

/**
 * Monitors and records callbacks into list of executed commands.
 * These recorded callbacks are "hidden commands" that are not displayed to the user.
 *
 * All methods of this class are synchronized on the lock of the {@link CommandRecorder} passed into this class.
 * Thus, all server methods that send a callback require the recorder's lock.
 */
public class CallbackRecordingDecorator implements InspectorCallbacks {

  /**
   * The wrapped callbacks handler.
   */
  private final InspectorCallbacks cb;

  private final CommandRecorder cmdRecorder;

  public CallbackRecordingDecorator (InspectorCallbacks cb, CommandRecorder recorder) {
    this.cb = cb;
    this.cmdRecorder = recorder;
  }

  @Override
  public void notifyStateChange (InspectorStatusChange newState, String details) {
    CmdCallback cmdCB = new CmdCallback(newState);
    cmdRecorder.recordCallback(cmdCB);

    cb.notifyStateChange(newState, details);
  }

  @Override
  public void genericError (String msg) {
    CmdCallback cmdCB = new CmdCallback(CallbackKind.CB_GENERIC_ERROR);
    cmdRecorder.recordCallback(cmdCB);

    cb.genericError(msg);
  }

  @Override
  public void genericInfo (String msg) {
    CmdCallback cmdCB = new CmdCallback(CallbackKind.CB_GENERIC_INFO);
    cmdRecorder.recordCallback(cmdCB);

    cb.genericInfo(msg);
  }

  @Override
  public void notifyBreakpointHit (BreakpointStatus bp) {
    CmdCallback cmdCB = new CmdCallback(CallbackKind.CB_BREAKPOINT_HIT);
    cmdRecorder.recordCallback(cmdCB);

    cb.notifyBreakpointHit(bp);
  }

  @Override
  public void notifyChoiceGeneratorNewChoice (CGTypes cgType, String cgName, int cgId, String[] choices, int nextChoice, int defaultChoice) {
    CmdCallback cmdCB = new CmdCallback(CallbackKind.CB_CG_NEW_CHOICE);
    cmdRecorder.recordCallback(cmdCB);

    cb.notifyChoiceGeneratorNewChoice(cgType, cgName, cgId, choices, nextChoice, defaultChoice);
  }

  @Override
  public void specifyChoiceToUse (int maxChoiceIndex) {
    CmdCallback cmdCB = new CmdCallback(CallbackKind.CB_CG_CHOICE_TO_USE);
    cmdRecorder.recordCallback(cmdCB);

    cb.specifyChoiceToUse(maxChoiceIndex);
  }

  @Override
  public void notifyUsedChoice (CGTypes cgType, String cgName, int cgId, int usedChoiceIndex, String usedChoice) {
    CmdCallback cmdCB = new CmdCallback(CallbackKind.CB_CG_USED_CHOICE);
    cmdRecorder.recordCallback(cmdCB);

    cb.notifyUsedChoice(cgType, cgName, cgId, usedChoiceIndex, usedChoice);
  }
}
