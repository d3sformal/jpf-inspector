package gov.nasa.jpf.inspector.common;

/**
 * Contains static information about the current Inspector client console.
 */
public class ConsoleInformation {
  /**
   * Represents the maximum length that a one-line message can have.
   *
   * If a server components wants to display a one-line message or other one-line expression and wants
   * to ensure that it will fit on a single line, it should only display the first MAX_LINE_LENGTH
   * characters and then display an ellipsis (three dots).
   */
  public static int MAX_LINE_LENGTH = 80;
  /**
   * Represents the maximum length that a user-input text can have before it's broken on another line.
   *
   * It works like this:
   * If the user's command or expression within the command ends with a parse error, we want to report the position
   * at which the error occurred. For example, if the user writes "create breakpoint invalidcontinuation", something
   * like this will be displayed:
   *
   * ERR: Unexpected 'invalidcontinuation'
   * create breakpoint invalidcontinuation"
   *                   ^
   *
   * Notice the little arrow pointing at the position of the error. If the line the arrow is pointing to is greater
   * than MAX_ERROR_LINE_LENGTH, then it should be broken up into several lines.
   */
  public static int MAX_ERROR_LINE_LENGTH = 50;
}
