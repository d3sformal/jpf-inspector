package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import gov.nasa.jpf.inspector.frontends.swing.explorer.AttachmentInformation;
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
    frames.add(new ExplorerStackFrameNode(AttachmentInformation.topmostStackFrame(), threadInfo.getCallerStackFrame(0),
                                          model, this));
    for (int i = 0; i < threadInfo.getStackDepth(); i++) {
      StackFrame callerStackFrame = threadInfo.getCallerStackFrame(i);
      frames.add(new ExplorerStackFrameNode(AttachmentInformation.stackFrame(i), callerStackFrame,
                                            model, this));
    }
    return frames;
  }

  @Override
  public void updateComplexNodeFromJpf() {

  }

  @Override
  public String toString() {
    return threadInfoToShortFormValue(this.threadInfo);
  }

  @Override
  public boolean isRecognizableAs(ExplorerNode oldNode) {
    return oldNode instanceof ExplorerThreadInfoNode && ((ExplorerThreadInfoNode)oldNode).threadInfo == this.threadInfo;
  }

  public static String threadInfoToShortFormValue(ThreadInfo threadInfo) {
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
