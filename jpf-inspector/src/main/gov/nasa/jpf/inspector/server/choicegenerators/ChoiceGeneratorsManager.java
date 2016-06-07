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

package gov.nasa.jpf.inspector.server.choicegenerators;

import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface;
import gov.nasa.jpf.inspector.interfaces.InspectorCallBacks;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNoVMConnected;
import gov.nasa.jpf.inspector.migration.MigrationUtilities;
import gov.nasa.jpf.inspector.server.breakpoints.CommandsManager;
import gov.nasa.jpf.inspector.server.breakpoints.DefaultForwardTraceManager;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.jpf.StopHolder;
import gov.nasa.jpf.inspector.server.programstate.StateValue;
import gov.nasa.jpf.inspector.utils.ChoiceGeneratorWrapper;
import gov.nasa.jpf.inspector.utils.InstructionWrapper;
import gov.nasa.jpf.vm.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages choice generators and used choices.
 */
public class ChoiceGeneratorsManager implements ChoiceGeneratorsInterface, ChoiceGeneratorNotifications {
  private final static boolean DEBUG = false;
  private final PrintStream out;

  private final CommandsManager cmdMgr;
  private final StopHolder stopHolder;
  private final JPFInspector inspector;
  private final InspectorCallBacks callBacks;
  private final DefaultForwardTraceManager forwardTrace;
  private Boolean waitForChoice = false; // / Flag which specifies whether the JPF is stopped due to prompt with CG (to client)

  private final CGNotificationSpecification[] cgNotifications;

  public ChoiceGeneratorsManager (JPFInspector inspector, InspectorCallBacks callBacks, CommandsManager cmdMgr, StopHolder stopHolder,
      DefaultForwardTraceManager forwardTrace) {
    // Internal tests

    this.inspector = inspector;
    this.callBacks = callBacks;
    this.cmdMgr = cmdMgr;
    this.stopHolder = stopHolder;
    this.forwardTrace = forwardTrace;

    this.out = this.inspector.getDebugPrintStream();

    cgNotifications = new CGNotificationSpecification[CGMode.values().length * CGTypes.values().length];
    disableAllNotifications();
  }

  private void disableAllNotifications () {
    for (CGTypes type : CGTypes.values()) {
      for (CGMode mode : CGMode.values()) {
        CGNotificationSpecification spec = new CGNotificationSpecification(type, mode, false);
        cgNotifications[getIndexCGNotificationSpecification(spec)] = spec;
      }
    }
  }

  @Override
  public synchronized List<ChoiceGeneratorWrapper> getUsedChoiceGenerators (boolean wait) throws JPFInspectorException {
    cmdMgr.initialStopTest(wait, "can't get choice generators");

    VM vm = stopHolder.getJVM();
    JPFInspectorNoVMConnected.checkVM(vm);

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
  public void modifyCGNotifications (CGNotificationSpecification spec) {
    if (spec == null) {
      throw new IllegalArgumentException("Invalid parameter - " + this.getClass().getSimpleName()
          + ".modifyCGMotifications - CGNotificationSpecification. Cannot be null.");
    }

    // Replace null with all values
    for (CGTypes type : CGTypes.values()) {
      CGNotificationSpecification spec1 = spec;
      if (spec1.getNotificationType() == null) {
        spec1 = new CGNotificationSpecification(type, spec1.getNotificationMode(), spec1.nofiticationEnabled());
      }
      for (CGMode mode : CGMode.values()) {
        CGNotificationSpecification spec2 = spec1;
        if (spec2.getNotificationMode() == null) {
          spec2 = new CGNotificationSpecification(type, mode, spec2.nofiticationEnabled());
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
  public void notifyChoiceGeneratorAdvance (ChoiceGenerator<?> cg, InspectorState inspState) {
    if (DEBUG) {
      out.println(this.getClass().getSimpleName() + ".notifyChoiceGeneratorAdvance( cg=" + cg + ", inspState=" + inspState + ")");
    }

    // inspector.getDebugPrintStream().println("CG call occured - " + cg.toString() + " ... cg.className=" + cg.getClass().getName() + "#" + cg.hashCode());
    CGTypes cgType = null;
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

    // Check whether send notification to client
    boolean printChoice = cgNotifications[getIndexCGNotificationSpecification(cgType, CGMode.CG_MODE_PRINT)].nofiticationEnabled();
    boolean askChoice = cgNotifications[getIndexCGNotificationSpecification(cgType, CGMode.CG_MODE_ASK)].nofiticationEnabled();
    if (printChoice == false && askChoice == false) {
      return; // No notification are selected
    }

    // Obtain all possible choices
    int currentChoice = cg.getProcessedNumberOfChoices() - 1;
    cg.reset();
    int totalChoices = cg.getTotalNumberOfChoices();
    String[] choices = new String[totalChoices];
    for (int i = 0; i < totalChoices; i++) {
      cg.advance();
      Object genChoice = cg.getNextChoice();
      choices[i] = (genChoice != null ? genChoice.toString() : "null");
    }

    // Restore state of the CG
    cg.reset();
    for (int i = 0; i <= currentChoice; i++) {
      cg.advance();
    }

    int hashCode = cg.hashCode();

    // TODO Gather default choice if exists
    callBacks.notifyChoiceGeneratorNewChoice(cgType, cg.getId(), hashCode, choices, currentChoice, ChoiceGeneratorsInterface.NO_DEFAULT_CHOICE);

    if (askChoice) {
      callBacks.specifyChoiceToUse(totalChoices - 1);

      waitForChoice = true;
      stopHolder.stopExecution(inspState);
      waitForChoice = false;

      Object genChoice = cg.getNextChoice();
      callBacks.notifyUsedChoice(cgType, cg.getId(), hashCode, cg.getProcessedNumberOfChoices(), (genChoice != null ? genChoice.toString() : "null"));
    }
  }

  @Override
  public void selectChoice (int selectedChoice) throws JPFInspectorGenericErrorException {
    cmdMgr.initialStopTest(false, "'cg select' command can be called only if execution is stopped");
    VM vm = stopHolder.getJVM();
    assert vm != null;

    if (!waitForChoice) {
      throw new JPFInspectorGenericErrorException("JPF has not been stopped by CG.Advanced call");
    }

    ChoiceGenerator<?> cg = MigrationUtilities.getLastChoiceGenerator(vm);

    if (cg == null) {
      return;
    }
    // Processing default forward trace
    int dftChoice = forwardTrace.getDefaultChoice(cg); // Default forward trace choice
    if (selectedChoice == ChoiceGeneratorsInterface.USE_DEFAULT_CHOICE) {
      if (dftChoice == ChoiceGeneratorsInterface.NO_DEFAULT_CHOICE) {
        throw new JPFInspectorGenericErrorException("no forward trace to follow");
      }
      selectedChoice = dftChoice;
    }

    int totalChoices = cg.getTotalNumberOfChoices();

    // TODO Removed =
    if (selectedChoice < 0 || selectedChoice >= totalChoices) {
      // if (selectedChoice < 0 || selectedChoice > totalChoices) {
      throw new JPFInspectorGenericErrorException("invalid index of the choices ( " + selectedChoice + ").");
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

  public static ChoiceGeneratorWrapper createCGWrapper (ChoiceGenerator<?> cg) {
    if (cg == null) {
      return null;
    }

    ThreadInfo ti = cg.getThreadInfo();
    Instruction inst = cg.getInsn();

    CGTypes cgType = (cg.isSchedulingPoint() ? CGTypes.CG_TYPE_SCHEDULING : CGTypes.CG_TYPE_DATA);

    return new ChoiceGeneratorWrapper(cgType, cg.toString(), ti.getId(), createInstructionWrapper(inst), cg.getId());
  }

  public static InstructionWrapper createInstructionWrapper (Instruction inst) {
    if (inst == null) {
      return null;
    }
    MethodInfo mi = inst.getMethodInfo();
    ClassInfo ci = mi.getClassInfo();

    return new InstructionWrapper(inst.toString(), StateValue.getFullClassName(ci), mi.getName(), inst.getPosition(), mi.getSourceFileName(), inst
        .getLineNumber(), inst.getSourceLine());
  }
}
