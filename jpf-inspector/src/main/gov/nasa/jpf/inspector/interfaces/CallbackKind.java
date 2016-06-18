package gov.nasa.jpf.inspector.interfaces;

/**
 * Represents a method of the {@link InspectorCallBacks} interface.
 */
public enum CallbackKind {
  CB_ANY,
  CB_STATE_CHANGE,
  CB_GENERIC_ERROR,
  CB_GENERIC_INFO,
  CB_BREAKPOINT_HIT,
  CB_CG_NEW_CHOICE,
  CB_CG_CHOICE_TO_USE,
  CB_CG_USED_CHOICE
}
