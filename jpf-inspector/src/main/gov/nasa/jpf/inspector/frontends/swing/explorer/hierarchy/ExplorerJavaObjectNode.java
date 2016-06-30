//
// Copyright (C) 2016 Petr Hudeček
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;

import gov.nasa.jpf.inspector.frontends.swing.explorer.Attachment;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ExplorerNodeFactory;
import gov.nasa.jpf.inspector.frontends.swing.explorer.ProgramStateTreeModel;
import gov.nasa.jpf.inspector.server.programstate.StateReadableValue;
import gov.nasa.jpf.inspector.server.programstate.StateWritableValue;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.StaticElementInfo;

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
    ArrayList<ExplorerNode> localChildren = new ArrayList<>();
    if (elementInfo == null) {
      return localChildren;
    }
    ClassInfo myClass = elementInfo.getClassInfo();

    // Instance fields
    for (FieldInfo fieldInfo : myClass.getInstanceFields()) {
       String fieldInfoName = fieldInfo.getName();
       ExplorerNode child = ExplorerNodeFactory.createFromField(fieldInfoName, fieldInfo, elementInfo.getFields(), model, this);
       localChildren.add(child);
    }

    // Static fields
    int staticFieldCount = myClass.getNumberOfStaticFields();
    for (int i = 0; i < staticFieldCount; i++) {
      FieldInfo staticField = myClass.getStaticField(i);
      String fieldInfoName = staticField.getName();
      StaticElementInfo staticElementInfo = myClass.getStaticElementInfo();
      ExplorerNode child = ExplorerNodeFactory.createFromStaticField(fieldInfoName, staticField, staticElementInfo.getFields(), model, this);
      localChildren.add(child);
    }

    return localChildren;
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