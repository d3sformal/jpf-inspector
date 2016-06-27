package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import gov.nasa.jpf.inspector.frontends.swing.explorer.Attachment;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ProgramStateTreeModel;
import gov.nasa.jpf.vm.StackFrame;
import gov.nasa.jpf.vm.ThreadInfo;

import java.util.ArrayList;

public class ExplorerThreadInfoNode extends ExplorerComplexNode {
  private final ThreadInfo threadInfo;

  public ExplorerThreadInfoNode(ThreadInfo threadInfo, ProgramStateTreeModel model, ExplorerAllThreadsNode parent) {
    super(model, parent);
    this.threadInfo = threadInfo;
  }

  @Override
  protected ArrayList<ExplorerNode> populateChildren() {
    ArrayList<ExplorerNode> frames = new ArrayList<>();
    frames.add(new ExplorerStackFrameNode(Attachment.topmostStackFrame(), threadInfo,
                                          threadInfo.getCallerStackFrame(0),
                                          model, this));
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
