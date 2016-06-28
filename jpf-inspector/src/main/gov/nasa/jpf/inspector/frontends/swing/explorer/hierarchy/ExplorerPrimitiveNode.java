package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import gov.nasa.jpf.inspector.frontends.swing.explorer.Attachment;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ProgramStateTreeModel;

public class ExplorerPrimitiveNode extends ExplorerLeafNode {
  private Object value;

  public ExplorerPrimitiveNode(Object value, Attachment attachment, ProgramStateTreeModel model, ExplorerNode parent) {
    super(attachment, model, parent);
    this.value = value;
    this.attachment = attachment;
  }



  @Override
  public String toString() {
    return attachment.getName() + ": " + value.toString();
  }

  @Override
  public void updateFromJpf(ExplorerNode newVersion) {
     this.value = ((ExplorerPrimitiveNode)newVersion).value;
  }

  @Override
  public boolean isRecognizableAs(ExplorerNode oldNode) {
    return oldNode.attachment.equals(this.attachment) && oldNode instanceof ExplorerPrimitiveNode;
  }
}
