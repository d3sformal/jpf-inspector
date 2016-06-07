package gov.nasa.jpf.inspector.utils.expressions;

import gov.nasa.jpf.inspector.server.expression.ExpressionNodeInterface;
import jdk.nashorn.internal.runtime.ListAdapter;

import java.util.ArrayList;
import java.util.List;

public class Expressions {
  private List<Object> expressions;

  public Expressions() {
    expressions = new ArrayList<>();
  }
  public void insert(Object o) {
    expressions.add(0, o);
  }
}
