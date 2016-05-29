package gov.nasa.jpf.inspector.common;

/**
 * Contains static information about the current Inspector client console.
 */
public class ConsoleInformation {
  /**
   * Represents the maximum length that a one-line message can have.
   *
   * If a server components wants to display a one-line error message or other one-line expression and wants
   * to ensure that it will fit on a single line, it should only display the first {@link #MAX_MESSAGE_LENGTH}
   * characters and then display an ellipsis (three dots).
   *
   * The value of this number should already take into account that there may be something on the line before the
   * message, such as a tab or the text "Error: ", for example.
   */
  public static int MAX_MESSAGE_LENGTH = 50;
}
