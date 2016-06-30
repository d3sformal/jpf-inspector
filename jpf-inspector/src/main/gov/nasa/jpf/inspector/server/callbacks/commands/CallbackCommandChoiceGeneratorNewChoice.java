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
package gov.nasa.jpf.inspector.server.callbacks.commands;

import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGTypes;
import gov.nasa.jpf.inspector.interfaces.InspectorCallbacks;
import gov.nasa.jpf.inspector.server.callbacks.CallbackCommand;

public class CallbackCommandChoiceGeneratorNewChoice implements CallbackCommand {

  private final CGTypes cgType;
  private final String cgName;
  private final int cgId;
  private final String[] choices;
  private final int nextChoice;
  private final int defaultChoice;

  public CallbackCommandChoiceGeneratorNewChoice (CGTypes cgType, String cgName, int cgId, String[] choices, int nextChoice, int defaultChoice) {
    this.cgType = cgType;
    this.cgName = cgName;
    this.cgId = cgId;
    this.choices = choices;
    this.nextChoice = nextChoice;
    this.defaultChoice = defaultChoice;
  }

  @Override
  public void sendCallback (InspectorCallbacks clientCallbacks) {
    clientCallbacks.notifyChoiceGeneratorNewChoice(cgType, cgName, cgId, choices, nextChoice, defaultChoice);
  }

  @Override
  public boolean waitJPF2stop () {
    return false;
  }

}
