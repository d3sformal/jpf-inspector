package gov.nasa.jpf.inspector.utils;

/**
 * Represents a user-defined command alias, such as "c" for "continue".
 */
public class CommandAlias {
  private String key;
  private String value;
  private int numberOfRequiredParameters;

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }

  public int getNumberOfRequiredParameters() {
    return numberOfRequiredParameters;
  }

  public CommandAlias(String key, String value, int numberOfRequiredParameters) {
    this.key = key;
    this.value = value;
    this.numberOfRequiredParameters = numberOfRequiredParameters;
  }
}
