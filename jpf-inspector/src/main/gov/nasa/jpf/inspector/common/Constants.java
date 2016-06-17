package gov.nasa.jpf.inspector.common;

import gov.nasa.jpf.inspector.common.pse.PSEThread;

/**
 * Contains constants used throughout JPF Inspector.
 */
public final class Constants {
  /**
   * A {@link PSEThread} has this value in its {@link PSEThread#getThreadTypeName()} getter if the corresponding
   * ThreadInfo object does not have an associated class info.
   */
  public static final String UNKNOWN_THREAD_TYPE_NAME = "(unspecified class)";
  /**
   * The prompt is shown to the user in both graphical shells and is echoed to the output in batch mode
   * if the echo_input option is set.
   */
  public static final String PROMPT = "cmd>";

}
