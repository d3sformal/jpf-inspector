package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import gov.nasa.jpf.inspector.frontends.swing.explorer.Attachment;

public class ExplorerPrimitiveNode extends ExplorerLeafNode {
  protected ExplorerPrimitiveNode(Attachment attachment, ExplorerNode parent) {
    super(parent);
  }



  @Override
  public String toString() {
    return null;
  }

  @Override
  public void updateFromJpf(ExplorerNode newVersion) {

  }

  @Override
  public boolean isRecognizableAs(ExplorerNode oldNode) {
    return false;
  }
}
