package gov.nasa.jpf.inspector.client;

public abstract class ClientCommand implements ClientCommandInterface {

  protected CommandRecorder rec; // The last recorder used to record the command

  @Override
  public void recordCommand (CommandRecorder rec) {
    this.rec = rec;
    rec.recordCommand(this);
  }

  @Override
  public boolean isHiddenCommand () {
    return false;
  }

}
