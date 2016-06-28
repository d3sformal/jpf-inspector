package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import gov.nasa.jpf.inspector.frontends.swing.explorer.Attachment;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ExplorerNodeFactory;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ProgramStateTreeModel;
import gov.nasa.jpf.inspector.server.choicegenerators.ChoiceGeneratorsManager;
import gov.nasa.jpf.inspector.utils.InstructionWrapper;
import gov.nasa.jpf.vm.LocalVarInfo;
import gov.nasa.jpf.vm.StackFrame;
import gov.nasa.jpf.vm.ThreadInfo;

import java.util.ArrayList;

public class ExplorerStackFrameNode extends ExplorerComplexNode {
  private final ThreadInfo parentThread;
  private StackFrame stackFrame;

  protected ExplorerStackFrameNode(Attachment attachment,
                                   ThreadInfo parentThread,
                                   StackFrame stackFrame,
                                   ProgramStateTreeModel model, ExplorerNode parent) {
    super(model, attachment, parent);
    this.parentThread = parentThread;
    this.stackFrame = stackFrame;
  }

  @Override
  protected ArrayList<ExplorerNode> populateChildren() {
    int stackSlotCount = stackFrame.getTopPos() + 1; // Only valid slots -- copied from StateStackFrame
    ArrayList<ExplorerNode> children = new ArrayList<>();
    for (int i = 0; i < stackSlotCount; i++) {
      LocalVarInfo localVarInfo = stackFrame.getLocalVarInfo(i);
      if (localVarInfo == null) {
        localVarInfo = new LocalVarInfo("???", "I", "I", 0, stackFrame.getMethodInfo().getLastInsn().getPosition(), i);
      }
      children.add(ExplorerNodeFactory.createFromStackSlot(localVarInfo.getName(), stackFrame, localVarInfo, model, this));
    }
    return children;
  }

  @Override
  public void updateComplexNodeFromJpf(ExplorerNode newVersion) {
    if (attachment.getKind() == Attachment.AttachmentKind.TOPMOST_STACK_FRAME) {
      if (parentThread.getCallerStackFrame(0) != stackFrame) {
        stackFrame = parentThread.getCallerStackFrame(0);
        model.nodesChanged(parent, new int[] { parent.getIndex(this)});
      }
    }

    Attachment newAttachment =  ((ExplorerStackFrameNode)newVersion).attachment;
    if (!newAttachment.equals(this.attachment)) {
      this.attachment = newAttachment;
    }
    model.nodesChanged(parent, new int[] { parent.getIndex(this)});
  }

  @Override
  public String toString() {
    InstructionWrapper instructionWrapper = ChoiceGeneratorsManager.createInstructionWrapper(stackFrame.getPC());
    return attachment.getName() + ": " + instructionWrapper.toString();
  }

  @Override
  public boolean isRecognizableAs(ExplorerNode oldNode) {
    if (!(oldNode instanceof ExplorerStackFrameNode)) {
      return false;
    }
    ExplorerStackFrameNode other = (ExplorerStackFrameNode)oldNode;
    if (other.attachment.getKind() == Attachment.AttachmentKind.TOPMOST_STACK_FRAME &&
            this.attachment.getKind() == Attachment.AttachmentKind.TOPMOST_STACK_FRAME) {
      return true;
    }
    return this.stackFrame == other.stackFrame;
  }
}
// TODO (elsewhere) allow arrays that are direct heap entries to be browsable