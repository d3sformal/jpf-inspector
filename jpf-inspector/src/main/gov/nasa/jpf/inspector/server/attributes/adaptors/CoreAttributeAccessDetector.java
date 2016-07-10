///
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
///

package gov.nasa.jpf.inspector.server.attributes.adaptors;

import gov.nasa.jpf.inspector.client.JPFInspectorClientInterface;
import gov.nasa.jpf.inspector.interfaces.attributes.AttributeAccessDetector;
import gov.nasa.jpf.inspector.utils.expressions.FieldName;
import gov.nasa.jpf.jvm.bytecode.*;
import gov.nasa.jpf.vm.*;
import gov.nasa.jpf.vm.bytecode.*;

import java.util.Objects;

public class CoreAttributeAccessDetector implements AttributeAccessDetector {

  @Override
  public boolean detectRead(Instruction impendingInstruction, FieldName fieldName) {
    if (!(impendingInstruction instanceof GETFIELD) &&
        !(impendingInstruction instanceof GETSTATIC)) {
        return false;
    }
    FieldInstruction fieldInstruction = (FieldInstruction)impendingInstruction;
    FieldInfo fieldInfo = fieldInstruction.getFieldInfo();
    if (!fieldName.isSameField(fieldInfo)) {
      return false;
    }
    ElementInfo owner = fieldInstruction.peekElementInfo(ThreadInfo.getCurrentThread());
    Object fieldAttr = owner.getFieldAttr(fieldInfo);
    return fieldAttr != null;
  }


  @Override
  public boolean detectWrite(Instruction impendingInstruction, FieldName fieldName) {
    if (!(impendingInstruction instanceof PUTFIELD) &&
            !(impendingInstruction instanceof PUTSTATIC)) {
      return false;
    }
    FieldInstruction fieldInstruction = (FieldInstruction)impendingInstruction;
    FieldInfo fieldInfo = fieldInstruction.getFieldInfo();
    if (!fieldName.isSameField(fieldInfo)) {
      return false;
    }
    ElementInfo owner = fieldInstruction.peekElementInfo(ThreadInfo.getCurrentThread());
    Object fieldAttr = owner.getFieldAttr(fieldInfo);
    int sourceSlot = ((WriteInstruction)impendingInstruction).getValueSlot(ThreadInfo.getCurrentThread().getTopFrame());
    Object slotAttr = ThreadInfo.getCurrentThread().getTopFrame().getSlotAttr(sourceSlot);
    return fieldAttr != null || slotAttr != null;
  }

  @Override
  public boolean detectRead(Instruction impendingInstruction, String localVariable) {
    if (!(impendingInstruction instanceof LocalVariableInstruction) ||
          impendingInstruction instanceof StoreInstruction) {
      return false;
    }
    LocalVariableInstruction localVariableInstruction = (LocalVariableInstruction)impendingInstruction;
    LocalVarInfo localVarInfo = localVariableInstruction.getLocalVarInfo();
    if (localVarInfo == null) {
      return false;
    }
    if (!Objects.equals(localVarInfo.getName(), localVariable))  {
      return false;
    }
    int slot = localVariableInstruction.getLocalVariableSlot();
    StackFrame topFrame = ThreadInfo.getCurrentThread().getTopFrame();
    Object slotAttr = topFrame.getSlotAttr(slot);
    return slotAttr != null;
  }

  @Override
  public boolean detectWrite(Instruction impendingInstruction, String localVariable) {
    if (!(impendingInstruction instanceof LocalVariableInstruction) ||
        !(impendingInstruction instanceof StoreInstruction)) {
      return false;
    }
    LocalVariableInstruction localVariableInstruction = (LocalVariableInstruction)impendingInstruction;
    LocalVarInfo localVarInfo = localVariableInstruction.getLocalVarInfo();
    if (localVarInfo == null) {
      return false;
    }
    if (!Objects.equals(localVarInfo.getName(), localVariable))  {
      return false;
    }
    int targetSlot = localVariableInstruction.getLocalVariableSlot();
    StackFrame topFrame = ThreadInfo.getCurrentThread().getTopFrame();
    Object targetSlotAttr = topFrame.getSlotAttr(targetSlot);
    Object sourceAttr = topFrame.getOperandAttr();
    return targetSlotAttr != null || sourceAttr != null;
  }

  @Override
  public void initialize(JPFInspectorClientInterface inspector) {

  }
}
