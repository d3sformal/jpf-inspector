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

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.interfaces.InspectorCallbacks;
import gov.nasa.jpf.inspector.interfaces.InspectorStatusChange;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.inspector.server.breakpoints.BreakpointHandler;
import gov.nasa.jpf.inspector.server.breakpoints.CommandsManager;
import gov.nasa.jpf.inspector.server.breakpoints.DefaultForwardTraceManager;
import gov.nasa.jpf.inspector.server.callbacks.CallbacksSender;
import gov.nasa.jpf.inspector.server.callbacks.InspectorServerCallbacks;
import gov.nasa.jpf.inspector.server.choicegenerators.ChoiceGeneratorsManager;
import gov.nasa.jpf.inspector.server.programstate.ProgramStateManager;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.VM;

import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * Represents the server part of JPF Inspector.
 *
 * This class uses object composition: different commands are handled by different subcomponents, but all of these
 * components are instantiated and held by a {@link JPFInspector} object.
 *
 */
@SuppressWarnings("WeakerAccess")
public abstract class JPFInspector implements JPFInspectorBackEndInterface {
  private static final boolean DEBUG = false;

  /**
   * File where print debug outputs, if not file exists, or name is empty then standard output is used.
   */
  private static final String DEBUG_OUTPUT_FILE = "/tmp/Inspector.log";

  private PrintStream debugOutStream;
  /**
   * Whether JPF was connected to the Inspector since the last time we used the "run" command to send
   * the Verify command
   */
  private boolean jpfHasLaunched = false;
  /**
   * Currently used JPF instance. Is null when no JPF is bound or if currently bound JPF terminates.
   */
  private JPF jpf = null;
  private InspectorListener listener = null;

  /**
   * This is the server-side serializer that can be called from the JPF thread and which sends callbacks to the user.
   */
  private final InspectorServerCallbacks serverCallbacks;
  private final StopHolder stopHolder;

  /**
   * The commands managers component.
   */
  protected final CommandsManager commandsManager;
  /**
   * The breakpoint handler component.
   */
  protected final BreakpointHandler breakpointHandler;
  /**
   * The state manager component. This component is responsible for handling user commands related to printing and modifying
   * current program state.
   */
  protected final ProgramStateManager stateManager;
  /**
   * The choice generator manager component. This component also handles thread suppression.
   */
  protected final ChoiceGeneratorsManager choiceGeneratorsManager;

  private final DefaultForwardTraceManager defaultForwardTraceManager;

  /**
   * Creates and initializes an instance of the Inspector's server part.
   * 
   * @param clientCallbacks Interface where callback events should take place. Can't be null.
   */
  protected JPFInspector (InspectorCallbacks clientCallbacks) {

    // Set up debug output stream
    //noinspection ConstantConditions
    if ((DEBUG_OUTPUT_FILE != null) && !DEBUG_OUTPUT_FILE.isEmpty()) {
      try {
        debugOutStream = new PrintStream("/tmp/alf/Inspector.log");
      } catch (FileNotFoundException ignored) {
        debugOutStream = System.out;
      }
    } else {
      debugOutStream = System.out;
    }



    // Create the callback thread.
    assert clientCallbacks != null;
    CallbacksSender callbacksSender = new CallbacksSender(this, clientCallbacks);
    this.serverCallbacks = callbacksSender.getCallbackSerializer();

    if (DEBUG) {
      getDebugPrintStream().println(JPFInspector.class.getSimpleName() + "." + JPFInspector.class.getSimpleName() + "callBacks=" + serverCallbacks + " )");
    }

    // Construct components of the Inspector
    this.stopHolder = new StopHolder(this, serverCallbacks);

    this.defaultForwardTraceManager = new DefaultForwardTraceManager(this);
    this.breakpointHandler = new BreakpointHandler(this, serverCallbacks, stopHolder);
    this.commandsManager = new CommandsManager(this, stopHolder, breakpointHandler, serverCallbacks,
                                               defaultForwardTraceManager);
    this.stateManager = new ProgramStateManager(this, stopHolder);
    this.choiceGeneratorsManager = new ChoiceGeneratorsManager(this, serverCallbacks, commandsManager, stopHolder,
                                                               defaultForwardTraceManager);

    // Run the callback thread
    callbacksSender.enableSender(stopHolder);
  }

  @Override
  public InspectorServerCallbacks getServerCallbacks() {
    if (DEBUG) {
      getDebugPrintStream().println("  " + JPFInspector.class.getSimpleName() + ".getCallBack()");
    }
    return serverCallbacks;
  }

  @Override
  public synchronized void waitUntilJpfBecomesConnected() {
    while (!jpfHasLaunched) {
      try {
        wait();
      } catch (InterruptedException ignored) {
        Thread.currentThread().interrupt();
      }
    }
    jpfHasLaunched = false;
  }

  @Override
  public Instruction getCurrentInstruction() {
    if (jpf == null) {
      return null;
    }
    if (jpf.getVM() == null) {
      return null;
    }
    return (jpf.getVM().getInstruction());
  }

  @Override
  public VM getVM() {

    return stopHolder.getVM();
  }

  /**
   * @return Class where JPF thread waits if breakpoint is reached (when execution is stopped)
   */
  public StopHolder getStopHolder () {
    if (DEBUG) {
      getDebugPrintStream().println("  " + JPFInspector.class.getSimpleName() + ".getStopHolder()");
    }
    return stopHolder;
  }

  /**
   * @return Gets currently bounded JPF. May change during time
   */
  public synchronized JPF getJPF () {
    if (DEBUG) {
      getDebugPrintStream().println("  " + JPFInspector.class.getSimpleName() + ".getJPF()");
    }
    return jpf;
  }

  @Override
  public synchronized void bindWithJPF (JPF jpf) throws JPFInspectorGenericErrorException {
    if (DEBUG) {
      getDebugPrintStream().println("  " + JPFInspector.class.getSimpleName() + ".boundWithJPF(jpf=" + jpf + " )");
    }
    if (jpf == null) {
      return;
    }

    if ((this.jpf != null) && (listener.getFinished() == false)) {
      stopHolder.terminating();
      // Currently there is running older instance ... we should terminate it
      // TODO: Is not better to force previous instance to stop.
      try {
        wait();
      } catch (InterruptedException e) {
        throw new JPFInspectorGenericErrorException("Previous instance of the JPF is running");
      }
    }



    // Modify JPF Configuration
    jpf.getVM().recordSteps(true);
    Config jpfCfg = jpf.getConfig();

    // Previously, there was a complex block of code here that I did not understand.
    // It felt like it was trying to force a multiple-error search regardless of the "search.multiple_error"
    // setting, but it also referenced a change "to be propagated to the trunk" which never made it to the trunk.
    // I changed it to respect the property, hopefully that won't break it.
    boolean originalSearchMultipleErrors = jpfCfg.getBoolean("search.multiple_errors");
    stopHolder.clearFlags();

    this.jpf = jpf;
    this.jpfHasLaunched = true;
      notifyAll(); // JPF is now connected and the "run" command should be notified.
                   // This also requires synchronization.

    listener = new InspectorListener(this, commandsManager, breakpointHandler, choiceGeneratorsManager,
                                     defaultForwardTraceManager, originalSearchMultipleErrors);
    jpf.addListener(listener);

    // Initialize instance of the JPF
    Search search = jpf.getSearch();
    if (!(search instanceof SearchInspectorExtension)) {
      // There is no way how to swap search objects
      getServerCallbacks().genericInfo("Unsupported search class ('" + search.getClass().toString() + "', not all features will be available).\nUse 'gov.nasa.jpf.inspector.server.jpf.DFSearchInspector' or have your class implement the 'gov.nasa.jpf.inspector.server.jpf.SearchInspectorExtension' interface.");
    } else {
      final SearchInspectorExtension searchInspector = (SearchInspectorExtension) search;
      searchInspector.setInspector(this);
    }

    // Notify all managers that there is new JPF instance (to be able to update its states)
    commandsManager.newJPF();
    stopHolder.newJPF();
    breakpointHandler.newJPF();
    serverCallbacks.notifyStateChange(InspectorStatusChange.JPF_STARTED, null); // It has to be here because some code is excecuted BEFORE search started is called!!
  }

  public synchronized void notifyJPFFinished () {
    jpf = null;
    listener = null;
    stopHolder.notifyClientTerminating();
    notifyAll();
    // If not terminating notify state change
  }

  public PrintStream getDebugPrintStream () {
    return debugOutStream;
  }

  /**
   * @return Get currently used {@link InspectorListener} or null if no JPF is connected/associated to Inspector.
   */
  public final InspectorListener getInspectorListener () {
    return listener;
  }


  @Override
  public boolean preventJpfFromResuming() {
    return stopHolder.preventJpfFromResuming();
  }


  @Override
  public void permitJpfToResumeAgain() {
    stopHolder.permitJpfToResumeAgain();
  }
}
