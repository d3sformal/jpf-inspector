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

package gov.nasa.jpf.inspector.interfaces;

import gov.nasa.jpf.vm.ChoiceGenerator;

/**
 * Call backs to the client part. Used to notify client about interesting events.
 * Client has to implement this interface and provide them to the server.
 * 
 * Note: Only JPF thread is permitted to sent callbacks (except {@link #genericInfo(String) method which can be called by any thread})
 */
public interface InspectorCallBacks {

  // List of possible CB types
  public enum CB_METHODS {
    CB_ANY, CB_STATE_CHANGE, CB_GENERIC_ERROR, CB_GENERIC_INFO, CB_BREAKPOINT_HIT, CB_CG_NEW_CHOICE, CB_CG_CHOICE_TO_USE, CB_CG_USED_CHOICE
  }

  /**
   * Called when state of the inspector is modified - paused, resumed, property
   * violated
   */
  public void notifyStateChange (CommandsInterface.InspectorStates newState, String details);

  /**
   * Inspector detected unrecoverable error that should be reported to user.
   * 
   * @param msg Message to print.
   */
  public void genericError (String msg);

  /**
   * Channel used for sending various messages for the user.
   * 
   * 
   * @param msg Message to print.
   */
  public void genericInfo (String msg);

  /**
   * Called when breakpoint is hitted.
   * 
   * @param bp Breakpoint that is reached.
   */
  public void notifyBreakpointHit (BreakPointStatus bp);

  /**
   * Notification from the server that ChoiceGenerator is asked for new value. {@link ChoiceGenerator#advance()} is called.
   * 
   * @param cgType Specifies type of the ChoiceGenerator
   * @param cgName Name of the {@link ChoiceGenerator}
   * @param cgId Identification of the {@link ChoiceGenerator} - the hashCode
   * @param choices List with possible choices of {@link ChoiceGenerator}
   * @param nextChoice Index of selected choice. (Choice which will be selected after the {@link ChoiceGenerator#advance()} call
   * @param defaultChoice Value of default choice, if any. ({@link ChoiceGeneratorsInterface#NO_DEFAULT_CHOICE} is used if no default choice exists)
   */
  public void notifyChoiceGeneratorNewChoice (ChoiceGeneratorsInterface.CGTypes cgType, String cgName, int cgId, String[] choices, int nextChoice,
      int defaultChoice);

  /**
   * Callback used to print prompt(notification) to user that execution is stopped and you should specify which choice to use.
   * 
   * <br>Note: This method is called after
   * {@link #notifyChoiceGeneratorNewChoice(gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGTypes, String, int, String[], int)} which shows
   * possible choices
   * 
   * @param maxChoiceIndex - specifies maximum allowed index of the choice
   */
  public void specifyChoiceToUse (int maxChoiceIndex);

  /**
   * Notification from the server with used choice.
   * 
   * @param cgType Specifies type of the ChoiceGenerator
   * @param cgName Name of the {@link ChoiceGenerator}
   * @param cgId Identification of the {@link ChoiceGenerator} - the hashCode
   * @param usedChoiceIndex Index of used choice in {@link ChoiceGenerator}
   * @param usedChoice Used choice - text representation of used choice
   */
  public void notifyUsedChoice (ChoiceGeneratorsInterface.CGTypes cgType, String cgName, int cgId, int usedChoiceIndex, String usedChoice);

}
