package gov.nasa.jpf.inspector.frontends.swing.explorer;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class ProgramStateTreeModel implements TreeModel {
  @Override
  public Object getRoot() {
    return null;
  }

  @Override
  public Object getChild(Object parent, int index) {
    return null;
  }

  @Override
  public int getChildCount(Object parent) {
    return 0;
  }

  @Override
  public boolean isLeaf(Object node) {
    return false;
  }

  @Override
  public void valueForPathChanged(TreePath path, Object newValue) {

  }

  @Override
  public int getIndexOfChild(Object parent, Object child) {
    return 0;
  }

  @Override
  public void addTreeModelListener(TreeModelListener l) {

  }

  @Override
  public void removeTreeModelListener(TreeModelListener l) {

  }
}
