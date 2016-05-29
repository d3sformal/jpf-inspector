package gov.nasa.jpf.inspector.common;

import gov.nasa.jpf.inspector.common.pse.PSEThread;

public class Constants {
  /**
   * A {@link PSEThread} has this value in its {@link PSEThread#getThreadTypeName()} getter if the corresponding
   * ThreadInfo object does not have an associated class info.
   */
  public static final String UNKNOWN_THREAD_TYPE_NAME = "(unspecified class)";
}
