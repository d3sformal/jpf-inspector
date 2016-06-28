package gov.nasa.jpf.inspector.client.commands;

import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.inspector.interfaces.ThreadEnablingResult;
import gov.nasa.jpf.inspector.interfaces.ThreadSuppressionStatus;

import java.io.PrintStream;

/**
 * Represents the "enable thread" and "disable thread" commands which determine whether a thread is eligible to
 * be scheduled.
 */
public final class CmdEnableThread extends ClientCommand {
  private int threadId;
  private final ThreadSuppressionStatus newOption;

  private CmdEnableThread(int threadId, ThreadSuppressionStatus newOption) {
    this.threadId = threadId;
    this.newOption = newOption;
  }

  public static CmdEnableThread createEnableThreadCommand(int id) {
    return new CmdEnableThread(id, ThreadSuppressionStatus.SCHEDULE_AS_NORMAL);
  }
  public static CmdEnableThread createDisableThreadCommand(int id) {
    return new CmdEnableThread(id, ThreadSuppressionStatus.DO_NOT_SCHEDULE);
  }
  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    ThreadEnablingResult threadEnablingResult = inspector.changeThreadSuppressionStatus(threadId, newOption);
    switch (threadEnablingResult){
      case THREAD_STATE_UNCHANGED:
        outStream.println("The thread " + threadId + " was already " + newOption.toString() + ".");
        break;
      case THREAD_SUCCESSFULLY_CHANGED_STATE:
        outStream.println("The thread " + threadId + " is now " + newOption.toString() + ".");
        break;
    }
  }

  @Override
  public String getNormalizedCommand() {
    if (newOption == ThreadSuppressionStatus.DO_NOT_SCHEDULE) {
      return "disable thread " + threadId;
    } else {
      return "enable thread " + threadId;
    }
  }

}
