package gov.nasa.jpf.inspector.common;

public class AntlrParseException extends RuntimeException {
  private final int column;

  public AntlrParseException(String msg, int column) {
    super(msg);
    this.column = column;
  }

  public int getColumn() {
    return column;
  }
}
