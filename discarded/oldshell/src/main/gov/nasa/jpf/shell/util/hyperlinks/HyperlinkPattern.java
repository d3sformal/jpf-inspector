package gov.nasa.jpf.shell.util.hyperlinks;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Used by JavaOutputPane to capture patterns that can translated into
 * hyperlinks.
 * The {@link Pattern#matcher(java.lang.CharSequence)} method will be used to
 * get a {@link Matcher}. If that {@code Matcher}'s {@link Matcher#find() }
 * returns true then
 * {@link #getResult(java.lang.String, java.util.regex.Matcher)}. is called with
 * the given matcher and the string that passed.
 * @author Sandro Badame <a href="mailto:s.badame@gmail.com">s.badame&amp;gmail.com</a>
 */
public interface  HyperlinkPattern {

  /**
   * @return the pattern that is used to see if a string should be converted into
   *         a hyperlink.
   */
  Pattern getPattern();

  /**
   * If the pattern from {@link #getPattern() } finds a match then this method
   * is called to retrieve a result. The result returned from here is then used
   * for the {@link #onClick(java.lang.Object) } and
   * {@link #getTooltip(java.lang.Object)} to identify which hyperlink is being
   * referenced.
   * @param str the line that matched the pattern
   * @param m the result of getting a matcher from {@link #getPattern() }
   * @return null if this should not be a hyperlink or an object to associate
   *              with this hyperlink.
   */
  Object getResult(String str, MatchResult m);

  void onClick(Object result);
  String getTooltip(Object result);

  int getModifiers();
}
