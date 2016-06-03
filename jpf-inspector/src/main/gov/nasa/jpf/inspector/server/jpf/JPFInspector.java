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

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.inspector.interfaces.InspectorCallBacks;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.server.breakpoints.BreakpointHandler;
import gov.nasa.jpf.inspector.server.breakpoints.CommandsManager;
import gov.nasa.jpf.inspector.server.breakpoints.DefaultForwardTraceManager;
import gov.nasa.jpf.inspector.server.callbacks.CallbacksSender;
import gov.nasa.jpf.inspector.server.choicegenerators.ChoiceGeneratorsManager;
import gov.nasa.jpf.inspector.server.programstate.ProgramStateManager;
import gov.nasa.jpf.search.Search;

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
  protected static final boolean DEBUG = false;

  /**
   * File where print debug outputs, if not file exists, or name is empty then standard output is used
   */
  private static final String DEBUG_OUTPUT_FILE = "/tmp/Inspector.log";

  private PrintStream debugOutStream;

  /**
   * Currently used JPF instance. Is null when no JPF is bound or if currently bound JPF terminates.
   */
  private JPF jpf = null;

  protected final InspectorCallBacks callBacks;
  protected final CallbacksSender callBacksSender;
  protected final StopHolder stopHolder;
  protected final CommandsManager cmdMgr;
  protected final BreakpointHandler breakpointMgr;
  protected final ProgramStateManager stateMgr;
  protected final ChoiceGeneratorsManager cgMgr;
  protected final DefaultForwardTraceManager dftMgr;
  private InspectorListener listener = null;

  /**
   * Creates and initialize instance of inspector's server part.
   * 
   * @param userCallBacks Interface where callback events should take place. Cann't be null.
   */
  protected JPFInspector (InspectorCallBacks userCallBacks) {
    debugOutStream = System.out; // Fail safe

    if ((DEBUG_OUTPUT_FILE != null) && !DEBUG_OUTPUT_FILE.isEmpty()) {
      try {
        debugOutStream = new PrintStream("/tmp/alf/Inspector.log");
      } catch (FileNotFoundException ignored) {
      }
    }

    if (DEBUG) {
      getDebugPrintStream().println(JPFInspector.class.getSimpleName() + "." + JPFInspector.class.getSimpleName() + "callBacks=" + callBacks + " )");
    }
    assert userCallBacks != null;

    this.callBacksSender = new CallbacksSender(this, userCallBacks);
    this.callBacks = callBacksSender.getCallbackSerializer();

    this.stopHolder = new StopHolder(this, callBacks);
    callBacksSender.enableSender(stopHolder);

    this.dftMgr = new DefaultForwardTraceManager(this);
    this.breakpointMgr = new BreakpointHandler(this, callBacks, stopHolder);
    this.cmdMgr = new CommandsManager(this, stopHolder, breakpointMgr, callBacks, dftMgr);
    this.stateMgr = new ProgramStateManager(this, stopHolder);
    this.cgMgr = new ChoiceGeneratorsManager(this, callBacks, cmdMgr, stopHolder, dftMgr);
  }

  @Override
  public InspectorCallBacks getCallBack () {
    if (DEBUG) {
      getDebugPrintStream().println("  " + JPFInspector.class.getSimpleName() + ".getCallBack()");
    }
    return callBacks;
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

    this.jpf = jpf;
    listener = new InspectorListener(this, cmdMgr, breakpointMgr, cgMgr, dftMgr, originalSearchMultipleErrors);
    jpf.addListener(listener);

    // Initialize instance of the JPF
    Search search = jpf.getSearch();
    if (!(search instanceof SearchInspectorExtension)) {
      // There is no way how to swap search objects
      getCallBack().genericInfo("Unsupported search class (not all features will be available).\nUse 'gov.nasa.jpf.inspector.server.jpf.DFSearchInspector' or have your class implement the 'gov.nasa.jpf.inspector.server.jpf.SearchInspectorExtension' interface.");
    } else {
      final SearchInspectorExtension searchInspector = (SearchInspectorExtension) search;
      searchInspector.setInspector(this);
    }

    // Notify all managers that there is new JPF instance (to be able to update its states)
    cmdMgr.newJPF();
    stopHolder.newJPF();
    breakpointMgr.newJPF();
    callBacks.notifyStateChange(InspectorStates.JPF_STARTED, null); // It has to be here because some code is excecuted BEFORE search started is called!!
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
  final public InspectorListener getInspectorListener () {
    return listener;
  }

}
