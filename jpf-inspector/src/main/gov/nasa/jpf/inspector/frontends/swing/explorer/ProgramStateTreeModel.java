package gov.nasa.jpf.inspector.frontends.swing.explorer;

import gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy.ExplorerRoot;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.vm.VM;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class ProgramStateTreeModel extends DefaultTreeModel {
  private ExplorerRoot explorerRoot;
  private JPFInspectorBackEndInterface server;

  public ProgramStateTreeModel(JPFInspectorBackEndInterface server) {
    super(new ExplorerRoot(), true);
    this.server = server;
    this.explorerRoot = (ExplorerRoot)this.root;
    ((ExplorerRoot)this.root).initialize(this);
  }

  public VM getVM() {
    return server.getVM();
  }
  public void update() {
    this.explorerRoot.updateFromJpf();
  }

  public JPFInspectorBackEndInterface getServer() {
    return server;
  }
}
