package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import gov.nasa.jpf.inspector.frontends.swing.explorer.AttachmentInformation;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ProgramStateTreeModel;
import gov.nasa.jpf.vm.StackFrame;

import java.util.ArrayList;

public class ExplorerStackFrameNode extends ExplorerComplexNode {
  private final AttachmentInformation attachmentInformation;
  private final StackFrame stackFrame;

  protected ExplorerStackFrameNode(AttachmentInformation attachmentInformation, StackFrame stackFrame,
                                   ProgramStateTreeModel model, ExplorerNode parent) {
    super(model, parent);
    this.attachmentInformation = attachmentInformation;
    this.stackFrame = stackFrame;
  }

  @Override
  protected ArrayList<ExplorerNode> populateChildren() {
    return null;
  }

  @Override
  public void updateComplexNodeFromJpf() {
  }

  @Override
  public String toString() {
    return attachmentInformation.getName();
  }

  @Override
  public boolean isRecognizableAs(ExplorerNode oldNode) {
    if (!(oldNode instanceof ExplorerStackFrameNode)) {
      return false;
    }
    ExplorerStackFrameNode other = (ExplorerStackFrameNode)oldNode;
    if (other.attachmentInformation.getKind() == AttachmentInformation.AttachmentKind.TOPMOST_STACK_FRAME &&
            this.attachmentInformation.getKind() == AttachmentInformation.AttachmentKind.TOPMOST_STACK_FRAME) {
      return true;
    }
    return this.stackFrame == other.stackFrame;
  }
}
