package gov.nasa.jpf.inspector.utils.expressions;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a comma-separated list of strings.
 * This is used as arguments to custom hit conditions.
 */
public class Expressions  {
  private List<String> expressions;

  public Expressions() {
    expressions = new ArrayList<>();
  }
  public void insert(String argument) {
    expressions.add(0, argument.trim());
  }

  @Override
  public String toString() {
    String s = "";
    for (int i = 0; i < expressions.size(); i++) {
      String arg = expressions.get(i);
      s += arg;
      if (i < expressions.size() - 1) {
        s += ", ";
      }
    }
    return s;
  }

  public int size() {
    return expressions.size();
  }

  public String  get(int index) {
    return expressions.get(index);
  }

  public String[] toStringArray() {
    return expressions.toArray(new String[expressions.size()]);
  }
}
