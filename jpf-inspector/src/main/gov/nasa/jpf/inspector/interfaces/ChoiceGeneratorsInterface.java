package gov.nasa.jpf.inspector.interfaces;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.utils.ChoiceGeneratorWrapper;
import gov.nasa.jpf.vm.ChoiceGenerator;

import java.io.Serializable;
import java.util.List;

/**
 * Interfaces related to choice generators and managing behavior of CG
 * 
 */
public interface ChoiceGeneratorsInterface {
  /**
   * Represents types of {@link ChoiceGenerator} in JPF.
   */
  public enum CGTypes {
    CG_TYPE_SCHEDULING,
    CG_TYPE_DATA
  }

  /**
   * Mode of work with {@link ChoiceGenerator}.
   */
  public enum CGMode {
    CG_MODE_PRINT, // / User (client) is notified about used choices
    CG_MODE_ASK
    // / User (client) is notified about possible choices and may select the choice to use
  }

  /**
   * Class used to specify/obtain current state of notifications.
   */
  static class CGNotificationSpecification implements Serializable {

    private static final long serialVersionUID = 5858782042646431620L;

    private final CGTypes type;
    private final CGMode mode;
    private final boolean enabled;

    public CGNotificationSpecification (CGTypes type, CGMode mode, boolean enabled) {
      super();
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
    public boolean nofiticationEnabled () {
      return enabled;
    }
  }

  /**
   * Constant that marks that there is no forward default choice from previous backward steps.
   * 
   * Can be used in {@link InspectorCallBacks#notifyChoiceGeneratorNewChoice(CGTypes, String, int, String[], int, int)} as value of defaultChoice parameter.
   */
  public final static int NO_DEFAULT_CHOICE = -1;

  /**
   * Constant that marks that for the current value for the CoiceGenerator should by taken from the default forward choice.
   * 
   * Can be used in {@link #selectChoice(int) method call.
   */
  public final static int USE_DEFAULT_CHOICE = -1;

  /**
   * Updates the behavior (user is notified or prompted) of the JPF if ChoiceGenetator event occurs.
   * 
   * <br>Note: For any combination of type and mode parameter there is stored state that represents expected behavior if such event occur.
   * 
   * @param nofitySpec Specifies how to change notification
   */
  public void modifyCGNotifications (CGNotificationSpecification nofitySpec);

  /**
   * Get status of CG notifications. Returned array holds entry for any combination of {@link CGMode} and {@link CGTypes}.
   * 
   * @return Get status of CG notifications.
   */
  public CGNotificationSpecification[] getCGNotificationStatus ();

  /**
   * Specifies which choice should the current {@link ChoiceGenerator} use.
   * 
   * <br>Useful only immediately after {@link InspectorCallBacks#notifyChoiceGeneratorNewChoice(CGTypes, String, int, String[], int, int)} is called.
   * 
   * @param selectedChoice Choice which the {@link ChoiceGenerator} will return.
   *        Value {@link #USE_DEFAULT_CHOICE} can be specify to uses value from previous execution.
   *        If no default value exists, no value is selected and error is reported to the user.
   */
  public void selectChoice (int selectedChoice) throws JPFInspectorException;

  /**
   * @param wait Specify behavior if the SuT is running when the command is invoked. If false error is reported, if try to stop the SuT is get result and resume
   *        the SuT
   */
  public List<ChoiceGeneratorWrapper> getUsedChoiceGenerators (boolean wait) throws JPFInspectorException;

}
