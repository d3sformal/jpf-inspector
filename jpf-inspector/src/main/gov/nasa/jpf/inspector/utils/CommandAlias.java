package gov.nasa.jpf.inspector.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a user-defined command alias, such as "c" for "continue".
 */
public class CommandAlias {
  private String key;
  private String value;
  private int numberOfRequiredParameters;
  private boolean usesFullArgumentLine;
  public static final String FULL_ARGUMENT_PATTERN = "{*}";

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }

  public int getNumberOfRequiredParameters() {
    return numberOfRequiredParameters;
  }

  private static Pattern patternNumericArgument = Pattern.compile("\\{[0-9]\\}");

  public CommandAlias(String key, String value) {
    this.key = key;
    this.value = value;
    Matcher patternMatcher =  patternNumericArgument.matcher(value);
    int count = 0;
    while (patternMatcher.find()) {
      count++;
    }
    this.numberOfRequiredParameters = count;
    this.usesFullArgumentLine = value.contains(FULL_ARGUMENT_PATTERN);
  }

  public boolean usesFullArgumentLine() {
    return usesFullArgumentLine;
  }
}
