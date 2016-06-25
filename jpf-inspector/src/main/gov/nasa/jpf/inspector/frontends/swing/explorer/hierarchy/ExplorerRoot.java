package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import gov.nasa.jpf.inspector.frontends.swing.explorer.CustomTreeModelDeprecated;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ProgramStateTreeModel;

import java.util.ArrayList;

public class ExplorerRoot extends ExplorerComplexNode {

  public ExplorerRoot() {
    super(null);
  }


  public void initialize(ProgramStateTreeModel model) {
    children = new ArrayList<>();
    children.add(new ExplorerEntireHeapNode(model, this));
    children.add(new ExplorerEntireHeapNode(model, this));
    children.add(new ExplorerEntireHeapNode(model, this));
  }

  @Override
  public String toString() {
    return "root";
  }

  @Override
  public void updateFromJpf() {

  }

  @Override
  protected void ensureChildrenArePopulated() {
    // Automatically ensured.
  }
}
