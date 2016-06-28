package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import gov.nasa.jpf.inspector.frontends.swing.explorer.Attachment;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ProgramStateTreeModel;
import gov.nasa.jpf.inspector.server.programstate.StateWritableValue;
import gov.nasa.jpf.vm.ClassLoaderInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.Statics;

import java.util.ArrayList;

public class ExplorerStaticAreaNode extends ExplorerComplexNode {
  public ExplorerStaticAreaNode(ProgramStateTreeModel model, ExplorerRoot explorerRoot) {
    super(model, Attachment.irrelevant(), explorerRoot);
  }

  @Override
  protected ArrayList<ExplorerNode> populateChildren() {
    ArrayList<ExplorerNode> children = new ArrayList<>();
    Statics statics = ClassLoaderInfo.getCurrentClassLoader().getStatics();
    for (ElementInfo elementInfo : statics) {
      String typeName = StateWritableValue.demangleTypeName(elementInfo.getType());
      children.add(new ExplorerJavaObjectNode(Attachment.staticAreaEntry(typeName),
                                              elementInfo, model, this));
    }
    return children;
  }

  @Override
  public void updateComplexNodeFromJpf(ExplorerNode newVersion) {
    // Nothing is needed.
  }

  @Override
  public String toString() {
    return "Static Area [NOT YET WORKING]";
  }

  @Override
  public boolean isRecognizableAs(ExplorerNode oldNode) {
    return oldNode instanceof ExplorerStaticAreaNode;
  }
}
