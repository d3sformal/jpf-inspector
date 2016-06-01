package gov.nasa.jpf.inspector.client;

/**
 * Indicates from where a command was executed.
 */
public enum ExecutionContext {
  /**
   * The command was executed from the Swing terminal.
   */
  FROM_SWING_TERMINAL,
  /**
   * The command was executed from the commnad-line terminal.
   */
  FROM_COMMAND_LINE_TERMINAL
}
