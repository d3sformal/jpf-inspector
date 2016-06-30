//
// Copyright (C) 2011 United States Government as represented by the
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

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.utils.ChoiceGeneratorWrapper;
import gov.nasa.jpf.vm.ChoiceGenerator;

import java.io.Serializable;
import java.util.List;

/**
 * Interfaces related to choice generators and managing behavior of CG
 */
public interface ChoiceGeneratorsInterface {

  /**
   * Changes a thread's suppression status.
   *
   * Note: This is called from the command execution thread.
   *
   * @param threadId The ID to which the suppression status should be linked.
   * @param newStatus The new suppression status.
   * @return Whether the status was changed or not.
   */
  ThreadEnablingResult changeThreadSuppressionStatus(int threadId, ThreadSuppressionStatus newStatus);

  /**
   * Represents types of {@link ChoiceGenerator} in JPF.
   */
  enum CGTypes {
    /**
     * The choice generator determines which thread will become active.
     */
    CG_TYPE_SCHEDULING,
    /**
     * The choice generator determines values of program state, such as when using the Verify class.
     */
    CG_TYPE_DATA
  }

  /**
   * Mode of work with {@link ChoiceGenerator}.
   */
  enum CGMode {
    /**
     * User (client) is merely notified about used choices
     */
    CG_MODE_PRINT,
    /**
     * User (client) is notified about possible choices and must select the choice to use
     */
    CG_MODE_ASK
  }

  /**
   * Class used to specify/obtain current state of notifications.
   */
  class CGNotificationSpecification implements Serializable {

    private static final long serialVersionUID = 5858782042646431620L;

    private final CGTypes type;
    private final CGMode mode;
    private final boolean enabled;

    public CGNotificationSpecification (CGTypes type, CGMode mode, boolean enabled) {
      this.type = type;
      this.mode = mode;
      this.enabled = enabled;
    }

    /**
     * @return Specified notification type to change. Null means all Choice generator types (in specify mode)
     */
    public CGTypes getNotificationType () {
      return type;
    }

    /**
     * @return Specified mode of notification. (In specify mode) Null means all modes (printing and asking)
     */
    public CGMode getNotificationMode () {
      return mode;
    }

    /**
     * @return Specifies if notification is enabled or disabled.
     */
    public boolean isNotificationEnabled() {
      return enabled;
    }
  }

  /**
   * Constant that marks that there is no forward default choice from previous backward steps.
   * 
   * Can be used in {@link InspectorCallbacks#notifyChoiceGeneratorNewChoice(CGTypes, String, int, String[], int, int)} as value of defaultChoice parameter.
   */
  int NO_DEFAULT_CHOICE = -1;

  /**
   * Constant that marks that for the current value for the ChoiceGenerator should by taken from the default forward choice.
   * 
   * Can be used in {@link #selectChoice(int) method call.
   */
  int USE_DEFAULT_CHOICE = -1;

  /**
   * Updates the behavior (user is notified or prompted) of the JPF if ChoiceGenetator event occurs.
   * 
   * <br>Note: For any combination of type and mode parameter there is stored state that represents expected behavior if such event occur.
   * 
   * @param nofitySpec Specifies how to change notification
   */
  void modifyCGNotifications(CGNotificationSpecification nofitySpec);

  /**
   * Get status of CG notifications. Returned array holds entry for any combination of {@link CGMode} and {@link CGTypes}.
   * 
   * @return Get status of CG notifications.
   */
  CGNotificationSpecification[] getCGNotificationStatus();

  /**
   * Specifies which choice should the current {@link ChoiceGenerator} use.
   * 
   * <br>Useful only immediately after {@link InspectorCallbacks#notifyChoiceGeneratorNewChoice(CGTypes, String, int, String[], int, int)} is called.
   * 
   * @param selectedChoice Choice which the {@link ChoiceGenerator} will return.
   *        Value {@link #USE_DEFAULT_CHOICE} can be specify to uses value from previous execution.
   *        If no default value exists, no value is selected and error is reported to the user.
   */
  void selectChoice(int selectedChoice) throws JPFInspectorException;

  /**
   * @param wait Specify behavior if the SuT is running when the command is invoked. If false error is reported, if try to stop the SuT is get result and resume
   *        the SuT
   */
  List<ChoiceGeneratorWrapper> getUsedChoiceGenerators(boolean wait) throws JPFInspectorException;

}
