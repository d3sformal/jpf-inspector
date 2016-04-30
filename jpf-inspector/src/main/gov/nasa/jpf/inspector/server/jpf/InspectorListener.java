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
import gov.nasa.jpf.jvm.JVM;
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
  public void instructionExecuted (JVM vm) {
    mode.instructionExecuted(vm);
  }

  @Override
  public void executeInstruction (JVM vm) {
    mode.executeInstruction(vm);
  }

  @Override
  public void threadStarted (JVM vm) {
    mode.threadStarted(vm);
  }

  @Override
  public void threadWaiting (JVM vm) {
    mode.threadWaiting(vm);
  }

  @Override
  public void threadNotified (JVM vm) {
    mode.threadNotified(vm);
  }

  @Override
  public void threadInterrupted (JVM vm) {
    mode.threadInterrupted(vm);
  }

  @Override
  public void threadScheduled (JVM vm) {
    mode.threadScheduled(vm);
  }

  @Override
  public void threadBlocked (JVM vm) {
    mode.threadBlocked(vm);
  }

  @Override
  public void threadTerminated (JVM vm) {
    mode.threadTerminated(vm);
  }

  @Override
  public void classLoaded (JVM vm) {
    mode.classLoaded(vm);
  }

  @Override
  public void objectCreated (JVM vm) {
    mode.objectCreated(vm);
  }

  @Override
  public void objectReleased (JVM vm) {
    mode.objectReleased(vm);
  }

  @Override
  public void objectLocked (JVM vm) {
    mode.objectLocked(vm);
  }

  @Override
  public void objectUnlocked (JVM vm) {
    mode.objectUnlocked(vm);
  }

  @Override
  public void objectWait (JVM vm) {
    mode.objectWait(vm);
  }

  @Override
  public void objectNotify (JVM vm) {
    mode.objectNotify(vm);
  }

  @Override
  public void objectNotifyAll (JVM vm) {
    mode.objectNotifyAll(vm);
  }

  @Override
  public void gcBegin (JVM vm) {
    mode.gcBegin(vm);
  }

  @Override
  public void gcEnd (JVM vm) {
    mode.gcEnd(vm);
  }

  @Override
  public void exceptionThrown (JVM vm) {
    mode.exceptionThrown(vm);
  }

  @Override
  public void exceptionBailout (JVM vm) {
    mode.exceptionBailout(vm);
  }

  @Override
  public void exceptionHandled (JVM vm) {
    mode.exceptionHandled(vm);
  }

  @Override
  public void choiceGeneratorRegistered (JVM vm) {
    mode.choiceGeneratorRegistered(vm);
  }

  @Override
  public void choiceGeneratorSet (JVM vm) {
    mode.choiceGeneratorSet(vm);
  }

  @Override
  public void choiceGeneratorAdvanced (JVM vm) {
    mode.choiceGeneratorAdvanced(vm);
  }

  @Override
  public void choiceGeneratorProcessed (JVM vm) {
    mode.choiceGeneratorProcessed(vm);
  }

  @Override
  public void methodEntered (JVM vm) {
    mode.methodEntered(vm);
  }

  @Override
  public void methodExited (JVM vm) {
    mode.methodExited(vm);
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
}
