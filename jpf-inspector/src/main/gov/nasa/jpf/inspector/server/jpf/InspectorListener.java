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

package gov.nasa.jpf.inspector.server.jpf;

import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.inspector.server.breakpoints.BreakPointHandler;
import gov.nasa.jpf.inspector.server.breakpoints.CommandsManager;
import gov.nasa.jpf.inspector.server.breakpoints.DefaultForwardTraceManager;
import gov.nasa.jpf.inspector.server.choicegenerators.ChoiceGeneratorNotifications;
import gov.nasa.jpf.jvm.ClassFile;
import gov.nasa.jpf.vm.*;
import gov.nasa.jpf.report.Publisher;
import gov.nasa.jpf.search.Search;

import java.util.Stack;

/*
 * Single entry from JPF inspector.
 *  Listener dynamically forwards all methods to specified listener instance.
 *  Class can dynamically change listener to which methods are forwarded and thus 
 *   implement different modes of work.
 *  Modes are stacked, used mode is in the top of the modes stacks (others are not notified)
 *
 * Currently modes of works are supported
 *  {@link InspectorListenerModeNotifications} - Default mode when whole Inspector is notified about all interesting events 
 *  {@link InspectorListenerModeSilent} - Silent mode used in backward steps, when only command listener is notified.
 *
 * This listener is added automatically on JPF Inspector startup, without using configuration. It is essential.
 */
public class InspectorListener extends ListenerAdapter {
  final private JPFInspector inspector;
  private ListenerAdapter mode;

  // Stack with disabled modes
  private final Stack<ListenerAdapter> modeStack = new Stack<ListenerAdapter>();

  private boolean finished = false; // Holds true if searchFinished was called

  /**
   * Inspector we serve for.
   * 
   * @param inspector
   * @param dftMgr
   */
  public InspectorListener (JPFInspector inspector, CommandsManager cmdMgr, BreakPointHandler bpMgr, ChoiceGeneratorNotifications cgNotify,
      DefaultForwardTraceManager dftMgr, boolean searchMultipleError) {
    this.inspector = inspector;
    mode = new InspectorListenerModeNotifications(inspector, cmdMgr, bpMgr, cgNotify, dftMgr, searchMultipleError);
  }

  /**
   * @return Gets true if {@link #searchFinished(Search)} was called.
   */
  public boolean getFinished () {
    return finished;
  }

  public void pushMode (ListenerAdapter newMode) {
    assert newMode != null : "Internal error - bad usage";

    modeStack.push(mode);
    mode = newMode;
  }

  public void popMode () {
    mode = modeStack.pop();
  }

  public ListenerAdapter getCurrentMode () {
    return mode;
  }

  @Override
  public void stateAdvanced (Search search) {
    mode.stateAdvanced(search);
  }

  @Override
  public void instructionExecuted(VM vm, ThreadInfo currentThread, Instruction nextInstruction, Instruction executedInstruction) {
    mode.instructionExecuted(vm, currentThread, nextInstruction, executedInstruction);
  }

  
  

  @Override
  public void gcBegin (VM vm) {
    mode.gcBegin(vm);
  }

  @Override
  public void gcEnd (VM vm) {
    mode.gcEnd(vm);
  }
  
  
  
  
  
  @Override
  public void stateProcessed (Search search) {
    mode.stateProcessed(search);
  }

  @Override
  public void stateBacktracked (Search search) {
    mode.stateBacktracked(search);
  }

  @Override
  public void statePurged (Search search) {
    mode.statePurged(search);
  }

  @Override
  public void stateStored (Search search) {
    mode.stateStored(search);
  }

  @Override
  public void stateRestored (Search search) {
    mode.stateRestored(search);
  }

  @Override
  public void propertyViolated (Search search) {
    mode.propertyViolated(search);
  }

  @Override
  public void searchStarted (Search search) {
    mode.searchStarted(search);
  }

  @Override
  public void searchConstraintHit (Search search) {
    mode.searchConstraintHit(search);
  }

  @Override
  public void searchFinished (Search search) {
    finished = true;
    inspector.notifyJPFFinished();
    mode.searchFinished(search);
  }

  @Override
  public void publishStart (Publisher publisher) {
    mode.publishStart(publisher);
  }

  @Override
  public void publishTransition (Publisher publisher) {
    mode.publishTransition(publisher);
  }

  @Override
  public void publishPropertyViolation (Publisher publisher) {
    mode.publishPropertyViolation(publisher);
  }

  @Override
  public void publishConstraintHit (Publisher publisher) {
    mode.publishConstraintHit(publisher);
  }

  @Override
  public void publishFinished (Publisher publisher) {
    mode.publishFinished(publisher);
  }
  
  
  //********************** METHODS OVERRIDEN DURING MIGRATION *********************/

  @Override
  public void vmInitialized(VM vm) {
    mode.vmInitialized(vm);
  }

  @Override
  public void executeInstruction(VM vm, ThreadInfo currentThread, Instruction instructionToExecute) {
    mode.executeInstruction(vm, currentThread, instructionToExecute);
  }

  @Override
  public void threadStarted(VM vm, ThreadInfo startedThread) {
    mode.threadStarted(vm, startedThread);
  }

  @Override
  public void threadWaiting(VM vm, ThreadInfo waitingThread) {
    mode.threadWaiting(vm, waitingThread);
  }

  @Override
  public void threadNotified(VM vm, ThreadInfo notifiedThread) {
    mode.threadNotified(vm, notifiedThread);
  }

  @Override
  public void threadInterrupted(VM vm, ThreadInfo interruptedThread) {
    mode.threadInterrupted(vm, interruptedThread);
  }

  @Override
  public void threadScheduled(VM vm, ThreadInfo scheduledThread) {
    mode.threadScheduled(vm, scheduledThread);
  }

  @Override
  public void threadBlocked(VM vm, ThreadInfo blockedThread, ElementInfo lock) {
    mode.threadBlocked(vm, blockedThread, lock);
  }

  @Override
  public void threadTerminated(VM vm, ThreadInfo terminatedThread) {
    mode.threadTerminated(vm, terminatedThread);
  }

  @Override
  public void loadClass(VM vm, ClassFile cf) {
    mode.loadClass(vm, cf);
  }

  @Override
  public void classLoaded(VM vm, ClassInfo loadedClass) {
    mode.classLoaded(vm, loadedClass);
  }

  @Override
  public void objectCreated(VM vm, ThreadInfo currentThread, ElementInfo newObject) {
    mode.objectCreated(vm, currentThread, newObject);
  }

  @Override
  public void objectReleased(VM vm, ThreadInfo currentThread, ElementInfo releasedObject) {
    mode.objectReleased(vm, currentThread, releasedObject);
  }

  @Override
  public void objectLocked(VM vm, ThreadInfo currentThread, ElementInfo lockedObject) {
    mode.objectLocked(vm, currentThread, lockedObject);
  }

  @Override
  public void objectUnlocked(VM vm, ThreadInfo currentThread, ElementInfo unlockedObject) {
    mode.objectUnlocked(vm, currentThread, unlockedObject);
  }

  @Override
  public void objectWait(VM vm, ThreadInfo currentThread, ElementInfo waitingObject) {
    mode.objectWait(vm, currentThread, waitingObject);
  }

  @Override
  public void objectNotify(VM vm, ThreadInfo currentThread, ElementInfo notifyingObject) {
    mode.objectNotify(vm, currentThread, notifyingObject);
  }

  @Override
  public void objectNotifyAll(VM vm, ThreadInfo currentThread, ElementInfo notifyingObject) {
    mode.objectNotifyAll(vm, currentThread, notifyingObject);
  }

  @Override
  public void objectExposed(VM vm, ThreadInfo currentThread, ElementInfo fieldOwnerObject, ElementInfo exposedObject) {
    mode.objectExposed(vm, currentThread, fieldOwnerObject, exposedObject);
  }

  @Override
  public void objectShared(VM vm, ThreadInfo currentThread, ElementInfo sharedObject) {
    mode.objectShared(vm, currentThread, sharedObject);
  }

  @Override
  public void exceptionThrown(VM vm, ThreadInfo currentThread, ElementInfo thrownException) {
    mode.exceptionThrown(vm, currentThread, thrownException);
  }

  @Override
  public void exceptionBailout(VM vm, ThreadInfo currentThread) {
    mode.exceptionBailout(vm, currentThread);
  }

  @Override
  public void exceptionHandled(VM vm, ThreadInfo currentThread) {
    mode.exceptionHandled(vm, currentThread);
  }

  @Override
  public void choiceGeneratorRegistered(VM vm, ChoiceGenerator<?> nextCG, ThreadInfo currentThread, Instruction executedInstruction) {
    mode.choiceGeneratorRegistered(vm, nextCG, currentThread, executedInstruction);
  }

  @Override
  public void choiceGeneratorSet(VM vm, ChoiceGenerator<?> newCG) {
    mode.choiceGeneratorSet(vm, newCG);
  }

  @Override
  public void choiceGeneratorAdvanced(VM vm, ChoiceGenerator<?> currentCG) {
    mode.choiceGeneratorAdvanced(vm, currentCG);
  }

  @Override
  public void choiceGeneratorProcessed(VM vm, ChoiceGenerator<?> processedCG) {
    mode.choiceGeneratorProcessed(vm, processedCG);
  }

  @Override
  public void methodEntered(VM vm, ThreadInfo currentThread, MethodInfo enteredMethod) {
    mode.methodEntered(vm, currentThread, enteredMethod);
  }

  @Override
  public void methodExited(VM vm, ThreadInfo currentThread, MethodInfo exitedMethod) {
    mode.methodExited(vm, currentThread, exitedMethod);
  }

  @Override
  public void searchProbed(Search search) {
    mode.searchProbed(search);
  }

  @Override
  public void publishProbe(Publisher publisher) {
    mode.publishProbe(publisher);
  }
}
