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

package gov.nasa.jpf.inspector.server.choicegenerators;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNoVMConnected;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface;
import gov.nasa.jpf.inspector.interfaces.InspectorCallbacks;
import gov.nasa.jpf.inspector.interfaces.ThreadEnablingResult;
import gov.nasa.jpf.inspector.interfaces.ThreadSuppressionStatus;
import gov.nasa.jpf.inspector.server.breakpoints.CommandsManager;
import gov.nasa.jpf.inspector.server.breakpoints.DefaultForwardTraceManager;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.jpf.StopHolder;
import gov.nasa.jpf.inspector.server.programstate.StateWritableValue;
import gov.nasa.jpf.inspector.utils.ChoiceGeneratorWrapper;
import gov.nasa.jpf.inspector.utils.InstructionWrapper;
import gov.nasa.jpf.vm.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages choice generators and used choices.
 */
public class ChoiceGeneratorsManager implements ChoiceGeneratorsInterface, ChoiceGeneratorNotifications {
  private static final boolean DEBUG = false;
  @SuppressWarnings("FieldCanBeLocal") // IDEA bug
  private final PrintStream out;

  private final CommandsManager commandsManager;
  private final StopHolder stopHolder;
  @SuppressWarnings("FieldCanBeLocal") // IDEA bug
  private final JPFInspector inspector;
  private final InspectorCallbacks serverCallbacks;
  private final DefaultForwardTraceManager forwardTrace;
  /**
   * Flag which specifies whether the JPF is stopped because we need the user to provide a choice for a choice generator.
   * While JPF is stopped this way, we don't know what the next instruction will be and thus, stepping commands and thpc
   * may not work correctly.
   */
  private Boolean waitForChoice = false;
  private boolean resumeWasDueToStepping = false;

  private final CGNotificationSpecification[] cgNotifications;
  /**
   * Maintains a list of disabled and enabled threads. This list is manipulated using the commands "enable thread"
   * and "disable thread". If a thread index it not in this map, it means the thread is enabled by default.
   *
   * This map also functions as a lock for itself, because it may be accessed from multiple threads.
   */
  private final Map<Integer, ThreadSuppressionStatus> suppressionStatusMap;

  public ChoiceGeneratorsManager (JPFInspector inspector, InspectorCallbacks serverCallbacks,
                                  CommandsManager commandsManager, StopHolder stopHolder,
                                  DefaultForwardTraceManager forwardTrace) {
    this.inspector = inspector;
    this.serverCallbacks = serverCallbacks;
    this.commandsManager = commandsManager;
    this.stopHolder = stopHolder;
    this.forwardTrace = forwardTrace;

    this.out = this.inspector.getDebugPrintStream();

    // At the beginning, all notifications are disabled.
    cgNotifications = new CGNotificationSpecification[CGMode.values().length * CGTypes.values().length];
    for (CGTypes type : CGTypes.values()) {
      for (CGMode mode : CGMode.values()) {
        CGNotificationSpecification spec = new CGNotificationSpecification(type, mode, false);
        cgNotifications[getIndexCGNotificationSpecification(spec)] = spec;
      }
    }
    suppressionStatusMap = new HashMap<>();
  }

  @Override
  public synchronized List<ChoiceGeneratorWrapper> getUsedChoiceGenerators (boolean wait) throws JPFInspectorException {
    commandsManager.initialStopTest(wait, "can't get choice generators");

    VM vm = stopHolder.getVM();
    JPFInspectorNoVMConnected.checkVM(vm);

    vm.updatePath();
    List<ChoiceGeneratorWrapper> result = new ArrayList<>(vm.getPathLength());
    Path path = vm.getPath();
    for (Transition tr : path) {
      ChoiceGenerator<?> cg = tr.getChoiceGenerator();
      result.add(createCGWrapper(cg));
    }

    // TODO it appears to me that we are forgotting to resume the VM here
    return result;
  }

  @Override
  public ThreadEnablingResult changeThreadSuppressionStatus(int threadId, ThreadSuppressionStatus newStatus) {
    synchronized (suppressionStatusMap) {
      // If not yet present, add it.
      if (!suppressionStatusMap.containsKey(threadId)) {
        suppressionStatusMap.put(threadId, ThreadSuppressionStatus.SCHEDULE_AS_NORMAL);
      }

      // Old status
      ThreadSuppressionStatus oldStatus = suppressionStatusMap.get(threadId);

      // Change
      suppressionStatusMap.put(threadId, newStatus);

      // Notify the caller about the result
      if (oldStatus == newStatus) {
        return ThreadEnablingResult.THREAD_STATE_UNCHANGED;
      } else {
        return ThreadEnablingResult.THREAD_SUCCESSFULLY_CHANGED_STATE;
      }
    }
  }

  @Override
  public Integer[] getSuppressedThreads() {
    List<Integer> suppressedThreads = new ArrayList<>();
    for (Map.Entry<Integer, ThreadSuppressionStatus> entry : suppressionStatusMap.entrySet()) {
      if (entry.getValue() == ThreadSuppressionStatus.DO_NOT_SCHEDULE) {
        suppressedThreads.add(entry.getKey());
      }
    }
    return suppressedThreads.toArray(new Integer[suppressedThreads.size()]);
  }

  @Override
  public void modifyCGNotifications (CGNotificationSpecification spec) {
    if (spec == null) {
      throw new IllegalArgumentException("Invalid parameter - " + this.getClass().getSimpleName()
          + ".modifyCGMotifications - CGNotificationSpecification. Cannot be null.");
    }

    // Replace null with all values
    for (CGTypes type : CGTypes.values()) {
      CGNotificationSpecification spec1 = spec;
      if (spec1.getNotificationType() == null) {
        spec1 = new CGNotificationSpecification(type, spec1.getNotificationMode(), spec1.isNotificationEnabled());
      }
      for (CGMode mode : CGMode.values()) {
        CGNotificationSpecification spec2 = spec1;
        if (spec2.getNotificationMode() == null) {
          spec2 = new CGNotificationSpecification(type, mode, spec2.isNotificationEnabled());
        }
        if (type.equals(spec2.getNotificationType()) && mode.equals(spec2.getNotificationMode())) {
          cgNotifications[getIndexCGNotificationSpecification(spec2)] = spec2;
        }
      }
    }
  }

  @Override
  public CGNotificationSpecification[] getCGNotificationStatus () {
    return cgNotifications;
  }

  @Override
  public void notifyChoiceGeneratorAdvance(ChoiceGenerator<?> cg, VM vm, InspectorState inspState)  {
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".notifyChoiceGeneratorAdvance( cg=" + cg + ", inspState=" + inspState + ")");
    }

    CGTypes cgType;
    if (cg instanceof ThreadChoiceGenerator) {
      cgType = CGTypes.CG_TYPE_SCHEDULING;
    } else {
      cgType = CGTypes.CG_TYPE_DATA;
    }

    // Following default trace if exists
    int dftChoice = forwardTrace.getDefaultChoice(cg); // Default forward trace choice
    if (dftChoice != ChoiceGeneratorsInterface.NO_DEFAULT_CHOICE) {
      cg.reset();
      cg.advance(dftChoice);
      if (DEBUG) {
        out.println(this.getClass().getSimpleName() + ".notifyChoiceGeneratorAdvance - using default choice ... dftChoice=" + dftChoice + ", cg=" + cg);
      }
    }
    // Remember the current choice
    int currentChoice = cg.getProcessedNumberOfChoices() - 1;

    // Obtain all possible choices
    cg.reset();
    int totalChoices = cg.getTotalNumberOfChoices();
    String[] choices = new String[totalChoices];
    Object[] choiceObjects = new Object[totalChoices];
    for (int i = 0; i < totalChoices; i++) {
      cg.advance();
      Object genChoice = cg.getNextChoice();
      choiceObjects[i] = genChoice;
      choices[i] = (genChoice != null ? genChoice.toString() : "null");
    }

    // Restore state of the CG
    cg.reset();
    for (int i = 0; i <= currentChoice; i++) {
      cg.advance();
    }

    //out.println("Before: " + cg.getProcessedNumberOfChoices() + ", total: " + cg.getTotalNumberOfChoices());
    if (cgType == CGTypes.CG_TYPE_SCHEDULING) {
      // Skip disabled threads
      for (int i = currentChoice; i < cg.getTotalNumberOfChoices(); i++) {
        ThreadInfo threadInfo = (ThreadInfo)choiceObjects[i];
        int targetId = threadInfo.getId();
        if (isThreadDisabled(targetId)) {
          out.println("Ignoring disabled thread " + targetId + ".");
          cg.advance();
          currentChoice++;
        } else {
          break;
        }
      }
      //out.println("After: " + cg.getProcessedNumberOfChoices() + ", total: " + cg.getTotalNumberOfChoices());

      if (currentChoice >= choices.length) {
        // The fact that the thread was disabled exhausted this choice generator.
        vm.ignoreState();
      }
    }

    // Check whether send notification to client
    boolean printChoice = cgNotifications[getIndexCGNotificationSpecification(cgType, CGMode.CG_MODE_PRINT)].isNotificationEnabled();
    boolean askChoice = cgNotifications[getIndexCGNotificationSpecification(cgType, CGMode.CG_MODE_ASK)].isNotificationEnabled();
    if (printChoice == false && askChoice == false) {
      return; // No notification are selected
    }



    int hashCode = cg.hashCode();

    // TODO Gather default choice if exists
    serverCallbacks.notifyChoiceGeneratorNewChoice(cgType, cg.getId(), hashCode, choices, currentChoice, ChoiceGeneratorsInterface.NO_DEFAULT_CHOICE);

    if (askChoice) {
      serverCallbacks.specifyChoiceToUse(totalChoices - 1);

      waitForChoice = true;
      resumeWasDueToStepping = false;
      stopHolder.stopExecution(inspState);
      waitForChoice = false;
      if (resumeWasDueToStepping) {
        serverCallbacks.genericInfo("You used a backstepping command while execution was broken due to a choice generator prompt." +
                                            " Such use is not supported and will probably not work. It is recommended that you use breakpoints for breaking execution.");
      }

      Object genChoice = cg.getNextChoice();
      serverCallbacks.notifyUsedChoice(cgType, cg.getId(), hashCode, cg.getProcessedNumberOfChoices(), (genChoice != null ? genChoice.toString() : "null"));
    }
  }

  private boolean isThreadDisabled(int targetId) {
    return suppressionStatusMap.containsKey(targetId) &&
            suppressionStatusMap.get(targetId) == ThreadSuppressionStatus.DO_NOT_SCHEDULE;
  }

  @Override
  public void selectChoice (int selectedChoice) throws JPFInspectorGenericErrorException {
    commandsManager.initialStopTest(false, "'cg select' command can be called only if execution is stopped");
    VM vm = stopHolder.getVM();
    assert vm != null;

    if (!waitForChoice) {
      throw new JPFInspectorGenericErrorException("JPF has not been stopped by CG.Advanced call");
    }

    ChoiceGenerator<?> cg = vm.getChoiceGenerator();

    if (cg == null) {
      return;
    }
    // Processing default forward trace
    int dftChoice = forwardTrace.getDefaultChoice(cg); // Default forward trace choice
    if (selectedChoice == ChoiceGeneratorsInterface.USE_DEFAULT_CHOICE) {
      if (dftChoice == ChoiceGeneratorsInterface.NO_DEFAULT_CHOICE) {
        throw new JPFInspectorGenericErrorException("No forward trace to follow.");
      }
      //noinspection AssignmentToMethodParameter
      selectedChoice = dftChoice;
    }

    int totalChoices = cg.getTotalNumberOfChoices();

    if (selectedChoice < 0 || selectedChoice >= totalChoices) {
      throw new JPFInspectorGenericErrorException("The index '" + selectedChoice + "' is not a valid choice.");
    } else {
      if (selectedChoice != dftChoice) {
        // Choice different path then default forward one -> destroy DFT is exists
        forwardTrace.destroyTrace(true);
      }
      cg.reset();
      selectedChoice++; // Own index is zero based however (ThreadInfo CG) needs to advance one to get first thread

      cg.advance(selectedChoice);
    }

    stopHolder.resumeExecution();
  }

  private static int getIndexCGNotificationSpecification (CGNotificationSpecification spec) {
    assert (spec != null);
    return getIndexCGNotificationSpecification(spec.getNotificationType(), spec.getNotificationMode());
  }

  private static int getIndexCGNotificationSpecification (CGTypes type, CGMode mode) {
    assert (type != null);
    assert (mode != null);
    return mode.ordinal() * CGTypes.values().length + type.ordinal();
  }

  private static ChoiceGeneratorWrapper createCGWrapper(ChoiceGenerator<?> cg) {
    if (cg == null) {
      return null;
    }

    Instruction inst = cg.getInsn();

    CGTypes cgType = (cg.isSchedulingPoint() ? CGTypes.CG_TYPE_SCHEDULING : CGTypes.CG_TYPE_DATA);

    return new ChoiceGeneratorWrapper(cgType, cg.toString(), createInstructionWrapper(inst));
  }

  public static InstructionWrapper createInstructionWrapper (Instruction inst) {
    if (inst == null) {
      return null;
    }
    MethodInfo mi = inst.getMethodInfo();
    ClassInfo ci = mi.getClassInfo();

    return new InstructionWrapper(inst.toString(), StateWritableValue.getFullClassName(ci), mi.getName(), inst.getPosition(), mi.getSourceFileName(), inst
        .getLineNumber(), inst.getSourceLine());
  }

  public void resumeWasDueToStepping() {
    resumeWasDueToStepping = true;
  }
}
