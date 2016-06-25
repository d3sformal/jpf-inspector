package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

public class ExplorerDebugLeafNode extends ExplorerLeafNode {
  private final String name;

  public ExplorerDebugLeafNode(String name, ExplorerNode parent) {
    super(parent);

    this.name = name;
  }
  @Override
  public String toString() {
    return name;
  }

  @Override
  public void updateFromJpf() {

  }
}
