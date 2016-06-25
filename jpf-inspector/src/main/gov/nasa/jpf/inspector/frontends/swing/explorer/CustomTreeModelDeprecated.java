package gov.nasa.jpf.inspector.frontends.swing.explorer;

import gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy.ExplorerComplexNode;
import gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy.ExplorerNode;
import gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy.ExplorerRoot;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.jvm.bytecode.LDC_W;
import javafx.scene.Parent;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

public class CustomTreeModelDeprecated implements TreeModel {
  private ExplorerRoot root;
  private JPFInspectorBackEndInterface server;

  public CustomTreeModelDeprecated(JPFInspectorBackEndInterface server) {
    this.server = server;
  }

  @Override
  public Object getRoot() {
    return root;
  }

  @Override
  public Object getChild(Object parent, int index) {
    ExplorerNode node = (ExplorerNode)parent;
    if (index < 0 || index >= node.getChildCount()) {
      return null;
    }
    ExplorerComplexNode complexNode = (ExplorerComplexNode)node;
    return complexNode.getChildAt(index);
  }

  @Override
  public int getChildCount(Object parent) {
    ExplorerNode node = (ExplorerNode)parent;
    if (node.getAllowsChildren()) {
      return node.getChildCount();
    } else {
      return 0;
    }
  }

  @Override
  public boolean isLeaf(Object node) {
    return !((ExplorerNode)node).getAllowsChildren();
  }



  @Override
  public int getIndexOfChild(Object parent, Object child) {
    if (parent == null || child == null) {
      return -1;
    }
    ExplorerComplexNode parentNode = (ExplorerComplexNode) parent;
    ExplorerNode childNode = (ExplorerNode)child;
    return parentNode.getIndex(childNode);
  }


  // No listeners for now.
  @Override
  public void valueForPathChanged(TreePath path, Object newValue) {
    // We don't permit changing values for now.
  }

  private List<TreeModelListener> listeners = new ArrayList<>();

  public void fireDrasticChange(ExplorerNode changedNode) {

  }

  @Override
  public void addTreeModelListener(TreeModelListener l) {
    listeners.add(l);
  }
  @Override
  public void removeTreeModelListener(TreeModelListener l) {
    listeners.remove(l);
  }

  public void update() {
    root.updateFromJpf();
  }

  public JPFInspectorBackEndInterface getServer() {
    return server;
  }
}
