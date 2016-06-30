package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import gov.nasa.jpf.inspector.frontends.swing.explorer.Attachment;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ProgramStateTreeModel;
import gov.nasa.jpf.inspector.server.programstate.StateWritableValue;
import gov.nasa.jpf.vm.ClassLoaderInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.Statics;

import java.util.ArrayList;

/**
 * Represents the single "Static Area" node that contains static representations of all classes loaded by the
 * current class loader.
 */
public class ExplorerStaticAreaNode extends ExplorerComplexNode {
  /**
   * Initializes the single "Static Area" node.
   * @param model The tree model.
   * @param explorerRoot The parent of this node.
   */
  public ExplorerStaticAreaNode(ProgramStateTreeModel model, ExplorerRoot explorerRoot) {
    super(model, Attachment.irrelevant(), explorerRoot);
  }

  @Override
  protected ArrayList<ExplorerNode> populateChildren() {
    ArrayList<ExplorerNode> newChildren = new ArrayList<>();
    Statics statics = ClassLoaderInfo.getCurrentClassLoader().getStatics();
    for (ElementInfo elementInfo : statics) {
      String typeName = StateWritableValue.demangleTypeName(elementInfo.getType());
      newChildren.add(new ExplorerJavaObjectNode(Attachment.staticAreaEntry(typeName),
                                              elementInfo, model, this));
    }
    return newChildren;
  }

  @Override
  public void updateComplexNodeFromJpf(ExplorerNode newVersion) {
    // Nothing is needed.
  }

  @Override
  public String toString() {
    return "Statics";
  }

  @Override
  public boolean isRecognizableAs(ExplorerNode oldNode) {
    return oldNode instanceof ExplorerStaticAreaNode;
  }
}
