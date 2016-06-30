//
// Copyright (C) 2010-2011 Pavel Jančík
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

package gov.nasa.jpf.inspector.server.programstate;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorInvalidStackFrame;
import gov.nasa.jpf.inspector.server.choicegenerators.ChoiceGeneratorsManager;
import gov.nasa.jpf.inspector.common.pse.PSEMethod;
import gov.nasa.jpf.inspector.common.pse.PSEVariable;
import gov.nasa.jpf.inspector.common.pse.PSEVariableObject;
import gov.nasa.jpf.inspector.utils.InstructionWrapper;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.LocalVarInfo;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.StackFrame;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.Instruction;

/**
 * Represent a stack frame using a {@link StackFrame} object.
 */
public final class StateStackFrame extends StateNode {

  /**
   * The JPF representation of this StackFrame.
   */
  private final StackFrame sf;

  /**
   * The method associated with the stack frame.
   */
  protected final MethodInfo mi;

  /**
   * Initializes a new instance of a hierarchy-2 stack frame.
   * @param sti Hierarchy-2 representation of the parent thread.
   * @param stackFrameNum Index of the stack frame in the parent thread (0 is top).
   */
  public StateStackFrame (StateThreadInfo sti, Integer stackFrameNum) throws JPFInspectorException {
    super(sti.getInspector());

    ThreadInfo ti = sti.getThreadInfo();

    int stackFrameIdx = 0;
    if (stackFrameNum != null) {
      stackFrameIdx = stackFrameNum;
    }

    StackFrame stackFrame = ti.getCallerStackFrame(stackFrameIdx);
    if (stackFrame == null) {
      throw new JPFInspectorInvalidStackFrame(ti.getId(), stackFrameIdx);
    }

    this.sf = stackFrame;
    this.mi = stackFrame.getMethodInfo();
    assert (mi != null);

    setStateExpr(createStateExpression(sti, stackFrameIdx));
  }

  @Override
  public PSEMethod toHierarchy3() throws JPFInspectorException {

    Instruction inst = sf.getPC();
    InstructionWrapper instw = ChoiceGeneratorsManager.createInstructionWrapper(inst);

    PSEVariable[] refLocals;
    PSEVariableObject refThis = null;

    if (mi.isStatic() == false) {
      try {
        // TODO there is some commented code here... check that it's really okay
        StateValueStackSlot svss = StateValueStackSlot.createHiddenThisSlotValue(this);
        PSEVariable refThisGeneric = svss.toHierarchy3();

        // can return also PSEVariablePrimitive ...
        // in case that there is no type information for the the fields (or the this field)
        // assert (refThisGeneric instanceof PSEVariableObject);
        if (refThisGeneric != null && refThisGeneric instanceof PSEVariableObject) {
          refThis = (PSEVariableObject) refThisGeneric;
        }
      } catch (JPFInspectorException e) {
        // The JPFInspectorInvalidSlotIndexException is thrown if stopped in native method such as join.

        // getInspector().getDebugPrintStream().println(e);
        // e.printStackTrace(getInspector().getDebugPrintStream());
      }
    }
    int stackSlots = sf.getTopPos() + 1; // Only valid slots
    refLocals = new PSEVariable[stackSlots];
    for (int i = 0; i < stackSlots; i++) {
      StateValueStackSlot svss = StateValueStackSlot.createSlotFromIndex(this, i);
      refLocals[i] = svss.toHierarchy3();
    }

    return new PSEMethod(instw, refLocals, refThis);
  }

  /**
   * String which can be used to obtain specified StackFrame.
   * 
   * @param sti Specification of Thread which stack frame to get.
   * @param stackFrameDepth How depth (top is 0) is required stack frame
   * @return Requested expression
   */
  private static String createStateExpression(StateThreadInfo sti, int stackFrameDepth) {
    assert (sti != null);
    return sti.getStateExpr() + '.' + PSEMethod.EXPRESSION_METHOD_KEY_WORD + '[' + stackFrameDepth + ']';
  }

  /**
   * Gets the JPF representation of the stack frame.
   */
  public StackFrame getStackFrame () {
    return sf;
  }

  /**
   * @param varName Name of the variable or parameter
   * @return Gets index of the slot with given name, or {@link StateValueStackSlot#INVALID_SLOT_INDEX} if no slot with given name exists}
   */
  public int namedSlotIndex (String varName) {
    LocalVarInfo lvi = mi.getLocalVar(varName, sf.getPC().getPosition());
    if (lvi == null) {
      return StateValueStackSlot.INVALID_SLOT_INDEX;
    }

    return lvi.getSlotIndex();

  }

  /**
   * Returns true if the method associated with this stack frame is static; false otherwise.
   */
  public boolean isStaticMethod () {
    return mi.isStatic();
  }

  /**
   * Returns the class of the method associated with this stack frame.
   */
  public ClassInfo getClassInfo () {
    return mi.getClassInfo();
  }
}
