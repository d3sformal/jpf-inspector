package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import gov.nasa.jpf.inspector.frontends.swing.explorer.Attachment;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ExplorerNodeFactory;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ProgramStateTreeModel;
import gov.nasa.jpf.inspector.server.programstate.StateReadableValue;
import gov.nasa.jpf.inspector.server.programstate.StateWritableValue;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;

import java.util.ArrayList;

public class ExplorerJavaObjectNode extends ExplorerComplexNode {
  protected ElementInfo elementInfo;

  public ExplorerJavaObjectNode(Attachment attachment, ElementInfo elementInfo,
                                ProgramStateTreeModel model, ExplorerNode parent) {
    super(model, attachment, parent);

    this.elementInfo = elementInfo;
  }


  @Override
  protected ArrayList<ExplorerNode> populateChildren() {
    ArrayList<ExplorerNode> children = new ArrayList<>();
    if (elementInfo == null) {
      return children;
    }
    for (FieldInfo fieldInfo : elementInfo.getClassInfo().getInstanceFields()) {
       String fieldInfoName = fieldInfo.getName();
       ExplorerNode child = ExplorerNodeFactory.createFromField(fieldInfoName, fieldInfo, elementInfo.getFields(), model, this);
       children.add(child);
    }
    return children;
  }

  @Override
  public void updateComplexNodeFromJpf(ExplorerNode newVersion) {
    ElementInfo newElementInfo = ((ExplorerJavaObjectNode)newVersion).elementInfo;
    if (newElementInfo != elementInfo) {
      model.nodesChanged(parent, new int[]{parent.getIndex(this)});
      elementInfo = newElementInfo;
    }
  }

  @Override
  public String toString() {
    if (elementInfo == null) {
      return attachment.getName() + ": " + null;
    }
    String typeName = StateWritableValue.demangleTypeName(elementInfo.getType());
    if (model.isConnectedToVM()) {
      String shortFormValue = StateReadableValue.elementInfo2String(elementInfo);
      return attachment.getName() + " (" + typeName + ") = " + shortFormValue;
    } else {
      this.wronglyExpanded = true;
      return "(value forgotten)";
    }
  }

  @Override
  public boolean isRecognizableAs(ExplorerNode oldNode) {
    return oldNode instanceof ExplorerJavaObjectNode &&
            ((ExplorerJavaObjectNode)oldNode).attachment.equals(this.attachment);
  }
}

// TODO (elsewhere) Shell crashes when viewing explorer with active nodes while JVM has already powered down