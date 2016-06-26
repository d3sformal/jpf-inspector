package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import gov.nasa.jpf.inspector.frontends.swing.explorer.AttachmentInformation;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ProgramStateTreeModel;
import gov.nasa.jpf.inspector.server.choicegenerators.ChoiceGeneratorsManager;
import gov.nasa.jpf.inspector.utils.InstructionWrapper;
import gov.nasa.jpf.vm.StackFrame;
import gov.nasa.jpf.vm.ThreadInfo;

import java.util.ArrayList;
import java.util.Objects;

public class ExplorerStackFrameNode extends ExplorerComplexNode {
  private AttachmentInformation attachmentInformation;
  private final ThreadInfo parentThread;
  private StackFrame stackFrame;

  private String shortFormDescription;

  protected ExplorerStackFrameNode(AttachmentInformation attachmentInformation,
                                   ThreadInfo parentThread,
                                   StackFrame stackFrame,
                                   ProgramStateTreeModel model, ExplorerNode parent) {
    super(model, parent);
    this.attachmentInformation = attachmentInformation;
    this.parentThread = parentThread;
    this.stackFrame = stackFrame;
  }

  @Override
  protected ArrayList<ExplorerNode> populateChildren() {
    return new ArrayList<>();
  }

  @Override
  public void updateComplexNodeFromJpf(ExplorerNode newVersion) {
    shortFormDescription = toString();

    if (attachmentInformation.getKind() == AttachmentInformation.AttachmentKind.TOPMOST_STACK_FRAME) {
      if (parentThread.getCallerStackFrame(0) != stackFrame) {
        stackFrame = parentThread.getCallerStackFrame(0);
        model.nodesChanged(parent, new int[] { parent.getIndex(this)});
      }
    }

    AttachmentInformation newAttachmentInformation =  ((ExplorerStackFrameNode)newVersion).attachmentInformation;
    if (!newAttachmentInformation.equals(this.attachmentInformation)) {
      this.attachmentInformation = newAttachmentInformation;
    }
    model.nodesChanged(parent, new int[] { parent.getIndex(this)});
  }

  @Override
  public String toString() {
    InstructionWrapper instructionWrapper = ChoiceGeneratorsManager.createInstructionWrapper(stackFrame.getPC());
    return attachmentInformation.getName() + ": " + instructionWrapper.toString();
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
