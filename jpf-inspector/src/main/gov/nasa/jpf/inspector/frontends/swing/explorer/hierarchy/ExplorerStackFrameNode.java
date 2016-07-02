//
// Copyright (C) 2016 Petr Hudeček
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

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

/**
 * Represents a stack frame node in the Explorer.
 *
 * A stack frame node can only be a child of a thread. The node uses the parent thread _and_ the stack frame object
 * to get its children (stack slots) and its string representation.
 */
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
    ArrayList<ExplorerNode> newChildren = new ArrayList<>();
    for (int i = 0; i < stackSlotCount; i++) {
      LocalVarInfo localVarInfo = stackFrame.getLocalVarInfo(i);
      if (localVarInfo == null) {
        localVarInfo = new LocalVarInfo("???", "I", "I", 0, stackFrame.getMethodInfo().getLastInsn().getPosition(), i);
      }
      newChildren.add(ExplorerNodeFactory.createFromStackSlot(localVarInfo.getName(), stackFrame, localVarInfo, model, this));
    }
    return newChildren;
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
    if (instructionWrapper == null) {
      return attachment.getName() + ": (no program counter available)";
    }
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