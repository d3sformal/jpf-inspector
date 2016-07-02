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
import gov.nasa.jpf.inspector.frontends.swing.explorer.ProgramStateTreeModel;
import gov.nasa.jpf.vm.StackFrame;
import gov.nasa.jpf.vm.ThreadInfo;

import java.util.ArrayList;

/**
 * Represents a thread in the visual Explorer.
 */
public class ExplorerThreadInfoNode extends ExplorerComplexNode {
  private final ThreadInfo threadInfo;

  public ExplorerThreadInfoNode(ThreadInfo threadInfo, ProgramStateTreeModel model, ExplorerAllThreadsNode parent) {
    super(model, Attachment.irrelevant(), parent);
    this.threadInfo = threadInfo;
  }

  @Override
  protected ArrayList<ExplorerNode> populateChildren() {
    ArrayList<ExplorerNode> frames = new ArrayList<>();
    StackFrame topStackFrame = threadInfo.getCallerStackFrame(0);
    if (topStackFrame != null) {
      frames.add(new ExplorerStackFrameNode(Attachment.topmostStackFrame(), threadInfo,
                                            topStackFrame,
                                            model, this));
    }
    for (int i = 0; i < threadInfo.getStackDepth(); i++) {
      StackFrame callerStackFrame = threadInfo.getCallerStackFrame(i);
      frames.add(new ExplorerStackFrameNode(Attachment.stackFrame(i), threadInfo,
                                            callerStackFrame,
                                            model, this));
    }
    return frames;
  }

  @Override
  public void updateComplexNodeFromJpf(ExplorerNode newVersion) {
    // Do nothing.
  }

  @Override
  public String toString() {
    return threadInfoToShortFormValue(this.threadInfo);
  }

  @Override
  public boolean isRecognizableAs(ExplorerNode oldNode) {
    return oldNode instanceof ExplorerThreadInfoNode && ((ExplorerThreadInfoNode)oldNode).threadInfo == this.threadInfo;
  }

  private static String threadInfoToShortFormValue(ThreadInfo threadInfo) {
    String s =
            threadInfo.getId() + " : " +
                    threadInfo.getName() + " state=" +
                    threadInfo.getState();
    if (threadInfo.isDaemon()) {
      s += " daemon thread";
    }
    s += " priority=" + threadInfo.getPriority();
    return s;
  }
}
