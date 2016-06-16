package gov.nasa.jpf.inspector.common;

import gov.nasa.jpf.inspector.common.pse.PSEThread;

public class Constants {
  // TODO (elsewhere): the help command should show syntax of hit conditions... I know I could use it...
  /**
   * A {@link PSEThread} has this value in its {@link PSEThread#getThreadTypeName()} getter if the corresponding
   * ThreadInfo object does not have an associated class info.
   */
  public static final String UNKNOWN_THREAD_TYPE_NAME = "(unspecified class)";
  public static final String PROMPT = "cmd>";

}
