package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import gov.nasa.jpf.inspector.frontends.swing.explorer.Attachment;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ProgramStateTreeModel;

import javax.swing.tree.DefaultTreeModel;
import java.util.ArrayList;

/**
 * The root node of the Explorer tree view. It never changes, remains constant throughout application lifetime and
 * is never shown to the user.
 */
public class ExplorerRoot extends ExplorerComplexNode {

  /**
   * Creates the root, but does not initialize it yet. That happens in {@link #initialize(ProgramStateTreeModel)}.
   */
  public ExplorerRoot() {
    super(null, Attachment.irrelevant(), null);
  }


  /**
   * Creates the root node's children and associates a model with all of them. This cannot be run in the constructor
   * because the model is not yet available by that point because of a stupid restriction by {@link DefaultTreeModel}.
   * @param model The Explorer's only model.
   */
  public void initialize(ProgramStateTreeModel model) {
    children = new ArrayList<>();
    children.add(new ExplorerEntireHeapNode(model, this));
    children.add(new ExplorerAllThreadsNode(model, this));
    children.add(new ExplorerStaticAreaNode(model, this));
  }

  @Override
  public String toString() {
    return "Program State Explorer Root Node";
  }

  @Override
  protected ArrayList<ExplorerNode> populateChildren() {
    return children; // We will never change.
  }

  @Override
  public void updateComplexNodeFromJpf(ExplorerNode newVersion) {
    // Nothing.
  }

  @Override
  public boolean isRecognizableAs(ExplorerNode oldNode) {
    return oldNode instanceof ExplorerRoot;
  }
}
