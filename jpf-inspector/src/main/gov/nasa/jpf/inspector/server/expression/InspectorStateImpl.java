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

package gov.nasa.jpf.inspector.server.expression;

import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.search.Search;

import java.util.HashMap;
import java.util.Map;

public class InspectorStateImpl implements InspectorState {

  private final Map<Integer, Instruction> lastExecutedInstructions = new HashMap<Integer, Instruction>();
  private JVM jvm;
  private Search search; // / Holds search class from last executed step

  private Instruction currentInstruction = null; //
  private int currentThread = 0;

  private InspectorState.ListenerMethod listenerMethod = ListenerMethod.LM_NOT_IN_LIST;

  @Override
  public JVM getJVM () {
    return jvm;
  }

  @Override
  public Map<Integer, Instruction> getPreviousSteps () {
    return lastExecutedInstructions;
  }

  @Override
  public Search getSearch () {
    return search;
  }

  @Override
  public ListenerMethod getListenerMethod () {
    return listenerMethod;
  }

  // Has to be called after each executed instruction
  public void instructionExecuted (JVM newJVM) {
    this.jvm = newJVM;

    lastExecutedInstructions.put(currentThread, currentInstruction);
    currentInstruction = newJVM.getLastInstruction();
    currentThread = newJVM.getLastThreadInfo().getId();
    this.listenerMethod = ListenerMethod.LM_INSTRUCTION_EXECUTED;

  }

  public void stateChanged (Search search, ListenerMethod listenerMethod) {
    this.jvm = search.getVM();
    this.search = search;
    this.listenerMethod = listenerMethod;
  }

  public void notifyListenerMethodCall (ListenerMethod listenerMethod, JVM jvm) {
    this.listenerMethod = listenerMethod;
    this.jvm = jvm;
  }

}
