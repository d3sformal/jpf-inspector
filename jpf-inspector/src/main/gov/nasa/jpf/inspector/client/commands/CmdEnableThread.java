package gov.nasa.jpf.inspector.client.commands;

import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.inspector.interfaces.ThreadSchedulingOption;

import java.io.PrintStream;

/**
 * Represents the "enable thread" and "disable thread" commands which determine whether a thread is eligible to
 * be scheduled.
 */
public class CmdEnableThread extends ClientCommand {
  private int threadId;
  private final ThreadSchedulingOption newOption;
  private ThreadSchedulingOption newSchedulingOption;

  private CmdEnableThread(int threadId, ThreadSchedulingOption newOption) {
    this.threadId = threadId;
    this.newOption = newOption;
  }

  public static CmdEnableThread createEnableThreadCommnad(int id) {
    return new CmdEnableThread(id, ThreadSchedulingOption.SCHEDULE_AS_NORMAL);
  }
  public static CmdEnableThread createDisableThreadCommand(int id) {
    return new CmdEnableThread(id, ThreadSchedulingOption.DO_NOT_SCHEDULE);
  }
  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    // TODO
    outStream.println("Not yet implemented.");
  }

  @Override
  public String getNormalizedCommand() {
    if (newOption == ThreadSchedulingOption.DO_NOT_SCHEDULE) {
      return "disable thread " + threadId;
    } else {
      return "enable thread " + threadId;
    }
  }
}
