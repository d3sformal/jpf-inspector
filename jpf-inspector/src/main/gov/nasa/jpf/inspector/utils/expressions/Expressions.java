package gov.nasa.jpf.inspector.utils.expressions;

import gov.nasa.jpf.inspector.server.expression.ExpressionNodeInterface;
import gov.nasa.jpf.inspector.server.expression.ExpressionStateInterface;
import gov.nasa.jpf.inspector.server.expression.ExpressionStateRootNode;
import gov.nasa.jpf.listener.OverlappingMethodAnalyzer;
import jdk.nashorn.internal.runtime.ListAdapter;
import sun.nio.cs.ext.IBM037;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Expressions implements Iterable<ExpressionStateRootNode> {
  private List<ExpressionStateRootNode> expressions;

  public Expressions() {
    expressions = new ArrayList<>();
  }
  public void insert(ExpressionStateRootNode o) {
    expressions.add(0, o);
  }

  @Override
  public Iterator<ExpressionStateRootNode> iterator() {
    return expressions.iterator();
  }

  @Override
  public String toString() {
    String s = "";
    for (int i = 0; i < expressions.size(); i++) {
      s += expressions.get(i).getNormalizedExpression();
      if (i < expressions.size() - 1) {
        s += ", ";
      }
    }
    return s;
  }

  public int size() {
    return expressions.size();
  }

  public ExpressionStateRootNode get(int index) {
    return expressions.get(index);
  }
}
