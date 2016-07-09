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

package gov.nasa.jpf.inspector.server.jpf;

import gov.nasa.jpf.inspector.interfaces.*;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorParsingErrorException;
import gov.nasa.jpf.inspector.common.pse.ProgramStateEntry;
import gov.nasa.jpf.inspector.common.pse.PSEThread;
import gov.nasa.jpf.inspector.utils.ChoiceGeneratorWrapper;

import java.util.List;
import java.util.Map;

/**
 * Represents the concrete server part of the Inspector.
 * It is a mystery to me to why the server was separated into {@link JPFInspector} and {@link JPFInspectorParallel}.
 * Maybe it should be merged?
 */
public class JPFInspectorParallel extends JPFInspector {

  /**
   * Initializes a new instance of the Inspector server.
   * @param callbacks The client's callback handler.
   */
  public JPFInspectorParallel (InspectorCallbacks callbacks) {
    super(callbacks);
  }

  @Override
  public void start () throws JPFInspectorGenericErrorException {
    commandsManager.start();
  }

  @Override
  public void requestTermination() {
    commandsManager.requestTermination();
  }

  @Override
  public void waitUntilStopped() {
    commandsManager.waitUntilStopped();
  }

  @Override
  public boolean isPaused() {
    return commandsManager.isPaused();
  }

  @Override
  public void stop () throws JPFInspectorGenericErrorException {
    commandsManager.stop();
  }

  @Override
  public BreakpointStatus createBreakpoint(BreakpointCreationInformation newBP) throws JPFInspectorParsingErrorException, JPFInspectorGenericErrorException {
    return breakpointHandler.createBreakpoint(newBP);
  }

  @Override
  public AssertStatus createAssert (AssertCreationInformation newAssert) throws JPFInspectorParsingErrorException, JPFInspectorGenericErrorException {
    return breakpointHandler.createAssert(newAssert);
  }

  @Override
  public boolean deleteBreakpoint(int bpID) {
    return breakpointHandler.deleteBreakpoint(bpID);
  }

  @Override
  public boolean changeBreakpointState(int breakpointId, BreakpointState newState) {
    return breakpointHandler.changeBreakpointState(breakpointId, newState);
  }

  @Override
  public List<BreakpointStatus> getBreakpoints() {
    return breakpointHandler.getBreakpoints();
  }

  @Override
  public void backstep(StepType type) throws JPFInspectorException {
    commandsManager.backstep(type);
  }

  @Override
  public void backFieldAccessStep(String fieldNameExpression) throws JPFInspectorGenericErrorException {
    commandsManager.backFieldAccessStep(fieldNameExpression);
  }

  @Override
  public void forwardStep (StepType type) throws JPFInspectorException {
    commandsManager.forwardStep(type);

  }

  @Override
  public void backstepTransition(StepType transitionKind, int count) throws JPFInspectorGenericErrorException {
    commandsManager.backstepTransition(transitionKind, count);
  }

  @Override
  public List<ChoiceGeneratorWrapper> getUsedChoiceGenerators() throws JPFInspectorException {
    return choiceGeneratorsManager.getUsedChoiceGenerators();
  }

  @Override
  public Map<Integer, PSEThread> getThreads (Integer threadNum) throws JPFInspectorException {
    return stateManager.getThreads(threadNum);
  }

  @Override
  public Map<Integer, InstructionPosition> getThreadsPC (Integer threadNum) throws JPFInspectorException {
    return stateManager.getThreadsPC(threadNum);
  }

  @Override
  public ProgramStateEntry evaluateStateExpression (String expr) throws JPFInspectorException {
    return stateManager.evaluateStateExpression(expr);
  }

  @Override
  public ThreadEnablingResult changeThreadSuppressionStatus(int threadId, ThreadSuppressionStatus newStatus) {
    return choiceGeneratorsManager.changeThreadSuppressionStatus(threadId, newStatus);
  }

  @Override
  public Integer[] getSuppressedThreads() {
    return choiceGeneratorsManager.getSuppressedThreads();
  }

  @Override
  public void modifyCGNotifications (CGNotificationSpecification spec) {
    choiceGeneratorsManager.modifyCGNotifications(spec);
  }

  @Override
  public CGNotificationSpecification[] getCGNotificationStatus () {
    return choiceGeneratorsManager.getCGNotificationStatus();
  }

  @Override
  public void selectChoice (int selectedChoice) throws JPFInspectorException {
    choiceGeneratorsManager.selectChoice(selectedChoice);
  }

  @Override
  public void setValue (String expr) throws JPFInspectorException {
    stateManager.setValue(expr);
  }

  @Override
  public void setAttributeValue(String expression) throws JPFInspectorException {
    attributesManager.setAttributeValue(expression);
  }
}
