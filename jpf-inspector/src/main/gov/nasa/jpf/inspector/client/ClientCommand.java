package gov.nasa.jpf.inspector.client;

import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;

import java.io.PrintStream;

/**
 * Represents a single instance of a command sent by the user.
 *
 * Whenever the user executes a command by typing in the Inspector console and pressing Enter, an instance of ClientCommand is created, and then executed using its {@link ClientCommand#execute(JPFInspectorClient, JPFInspectorBackEndInterface, PrintStream)} method.
 *
 * Each command (such as "help" or "print") has its own subclass.
 */
public abstract class ClientCommand implements ClientCommandInterface {

  /**
   * The last recorder used to record the command
   */
  protected CommandRecorder rec;

  @Override
  public void recordCommand (CommandRecorder rec) {
    this.rec = rec;
    rec.recordCommand(this);
  }

  @Override
  public boolean isHiddenCommand () {
    return false;
  }

  @Override
  public boolean isSafeToExecuteWhenNotPaused() {
    return true;
  }
}
