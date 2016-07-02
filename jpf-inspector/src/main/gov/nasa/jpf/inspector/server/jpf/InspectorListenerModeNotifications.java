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

import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.inspector.server.breakpoints.BreakpointHandler;
import gov.nasa.jpf.inspector.server.breakpoints.CommandsManager;
import gov.nasa.jpf.inspector.server.breakpoints.DefaultForwardTraceManager;
import gov.nasa.jpf.inspector.server.choicegenerators.ChoiceGeneratorNotifications;
import gov.nasa.jpf.inspector.server.expression.InspectorState.ListenerMethod;
import gov.nasa.jpf.inspector.server.expression.InspectorStateImpl;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.vm.*;

/**
 * Standard listener used in JPF Inspector when all parts of the Inspector should be notified about incoming events.
 *
 * This is contrary to {@link InspectorListenerModeSilent} which is active during backtracking and ignores breakpoints.
 */
public class InspectorListenerModeNotifications extends ListenerAdapter {
  private static final boolean DEBUG = false;

  private final JPFInspector inspector;
  private final CommandsManager commandsManager;
  private final BreakpointHandler breakpointHandler;
  private final ChoiceGeneratorNotifications cgNotify;
  private final DefaultForwardTraceManager defaultForwardTraceManager;

  private final InspectorStateImpl inspectorState = new InspectorStateImpl();

  /**
   * Whether continue or not after a property is violated
   */
  private final boolean searchMultipleError;

  public InspectorListenerModeNotifications (JPFInspector inspector, CommandsManager commandsManager,
                                             BreakpointHandler breakpointHandler, ChoiceGeneratorNotifications cgNotify,
                                             DefaultForwardTraceManager defaultForwardTraceManager, boolean searchMultipleError) {
    this.inspector = inspector;
    this.commandsManager = commandsManager;
    this.breakpointHandler = breakpointHandler;
    this.cgNotify = cgNotify;
    this.defaultForwardTraceManager = defaultForwardTraceManager;
    this.searchMultipleError = searchMultipleError;

  }

  @Override
  public void stateAdvanced (Search search) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".stateAdvanced()");
    }
    inspectorState.stateChanged(search, ListenerMethod.LM_STATE_ADVANCED);
    breakpointHandler.forwardJPFStep();
    breakpointHandler.checkBreakpoints(inspectorState);
    defaultForwardTraceManager.forwardStep(search);
    commandsManager.tryStop(inspectorState);
  }

  @Override
  public void stateProcessed (Search search) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".stateProcessed()");
    }
    inspectorState.stateChanged(search, ListenerMethod.LM_NOT_IN_LIST);
    commandsManager.tryStop(inspectorState);
  }

  @Override
  public void stateBacktracked (Search search) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".stateBacktracked()");
    }
    inspectorState.stateChanged(search, ListenerMethod.LM_STATE_BACKTRACKED);
    breakpointHandler.backwardJPFStep(inspectorState);
    // defaultForwardTraceManager.extendTrace(search.getTransition());
    commandsManager.tryStop(inspectorState);
  }

  @Override
  public void searchStarted (Search search) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".searchStarted()");
    }
    inspectorState.stateChanged(search, ListenerMethod.LM_NOT_IN_LIST);
    commandsManager.tryStop(inspectorState);
  }

  @Override
  public void searchConstraintHit (Search search) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".searchConstraintHit()");
    }
    inspectorState.stateChanged(search, ListenerMethod.LM_SEARCH_CONSTRAINT_HIT);
    commandsManager.tryStop(inspectorState);
  }

  @Override
  public void searchFinished (Search search) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".searchFinished()");
    }
  }

  @Override
  public void executeInstruction(VM vm, ThreadInfo currentThread, Instruction instructionToExecute) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".executeInstruction(" + instructionToExecute + ")");
    }
    inspectorState.notifyListenerMethodCall(ListenerMethod.LM_EXECUTE_INSTRUCTION, vm);
    breakpointHandler.checkBreakpoints(inspectorState);
    breakpointHandler.breakIfBreakScheduled(inspectorState);
  }



  @Override
  public void instructionExecuted(VM vm, ThreadInfo currentThread, Instruction nextInstruction, Instruction executedInstruction) {

    if (DEBUG) {
      inspector.getDebugPrintStream().println(
          this.getClass().getSimpleName() + ".instructionExecuted(" + executedInstruction + ", loc=" + executedInstruction.getFileLocation()
              + ")");
    }
    inspectorState.instructionExecuted(currentThread.getId(), executedInstruction, vm);
    breakpointHandler.checkBreakpoints(inspectorState);
    commandsManager.tryTerminate(vm.getSearch());
  }

  @Override
  public void classLoaded(VM vm, ClassInfo loadedClass) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".classLoaded()");
    }
  }

  @Override
  public void gcBegin (VM vm) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".gcBegin()");
    }
    inspectorState.notifyListenerMethodCall(ListenerMethod.LM_GC_BEGIN, vm);
    breakpointHandler.checkBreakpoints(inspectorState);
  }

  @Override
  public void gcEnd (VM vm) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".gcEnd()");
    }
    inspectorState.notifyListenerMethodCall(ListenerMethod.LM_GC_END, vm);
    breakpointHandler.checkBreakpoints(inspectorState);
  }

  @Override
  public void objectCreated(VM vm, ThreadInfo currentThread, ElementInfo newObject) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".objectCreated()");
    }
    inspectorState.notifyListenerElementInfoMethodCall(vm, ListenerMethod.LM_OBJECT_CREATED, newObject);
    breakpointHandler.checkBreakpoints(inspectorState);
  }

  @Override
  public void objectReleased(VM vm, ThreadInfo currentThread, ElementInfo releasedObject) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".objectReleased()");
    }
    inspectorState.notifyListenerElementInfoMethodCall(vm, ListenerMethod.LM_OBJECT_RELEASED, releasedObject);
    breakpointHandler.checkBreakpoints(inspectorState);
  }


  @Override
  public void exceptionThrown(VM vm, ThreadInfo currentThread, ElementInfo thrownException) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".exceptionThrown()");
    }
    inspectorState.notifyListenerElementInfoMethodCall(vm, ListenerMethod.LM_EXCEPTION_THROWN, thrownException);
    breakpointHandler.checkBreakpoints(inspectorState);
  }

  @Override
  public void choiceGeneratorSet(VM vm, ChoiceGenerator<?> newCG) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(
          this.getClass().getSimpleName() + ".choiceGeneratorSet() - cg=" + newCG + " processedChoices="
              + newCG.getProcessedNumberOfChoices());
    }
  }

  @Override
  public void choiceGeneratorAdvanced(VM vm, ChoiceGenerator<?> currentCG) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(
          this.getClass().getSimpleName() + ".choiceGeneratorAdvanced() - cg=" + currentCG + " processedChoices="
              + currentCG.getProcessedNumberOfChoices());
    }
    inspectorState.notifyListenerMethodCall(ListenerMethod.LM_CHOICE_GENERATOR_ADVANCED, vm);
    cgNotify.notifyChoiceGeneratorAdvance(currentCG, vm, inspectorState);
    breakpointHandler.checkBreakpoints(inspectorState);

  }

  @Override
  public void choiceGeneratorRegistered(VM vm, ChoiceGenerator<?> nextCG, ThreadInfo currentThread, Instruction executedInstruction) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(
          this.getClass().getSimpleName() + ".choiceGeneratorRegistered() - cg=" + nextCG + " processedChoices="
              + nextCG.getProcessedNumberOfChoices());
    }
  }

  @Override
  public void choiceGeneratorProcessed(VM vm, ChoiceGenerator<?> processedCG) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(
          this.getClass().getSimpleName() + ".choiceGeneratorProcessed() - cg=" + processedCG + " processedChoices="
              + processedCG.getProcessedNumberOfChoices());
    }
  }

  @Override
  public void threadScheduled(VM vm, ThreadInfo scheduledThread) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".threadScheduled()");
    }
    inspectorState.notifyListenerMethodCall(ListenerMethod.LM_THREAD_SCHEDULED, vm);
    breakpointHandler.checkBreakpoints(inspectorState);
  }

  @Override
  public void propertyViolated (Search search) {
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".propertyViolated()");
    }
    inspectorState.stateChanged(search, ListenerMethod.LM_PROPERTY_VIOLATED);

    // Simulate behavior of the JPF with original settings
    if (!searchMultipleError) {
      inspector.getStopHolder().terminateAfterResume();
    }

    breakpointHandler.checkBreakpoints(inspectorState);
    commandsManager.tryStop(inspectorState);

    super.propertyViolated(search);
  }

}
