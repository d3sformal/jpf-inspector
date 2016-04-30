//
// Copyright (C) 2011 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
// 
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
// 
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//  

package gov.nasa.jpf.inspector.server.programstate;

import gov.nasa.jpf.inspector.interfaces.JPFInspectorException;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorInvalidStackFrame;
import gov.nasa.jpf.inspector.server.choicegenerators.ChoiceGeneratorsManager;
import gov.nasa.jpf.inspector.server.programstate.client.PSEMethod;
import gov.nasa.jpf.inspector.server.programstate.client.PSEVariable;
import gov.nasa.jpf.inspector.server.programstate.client.PSEVariableObject;
import gov.nasa.jpf.inspector.utils.InstructionWrapper;
import gov.nasa.jpf.jvm.ClassInfo;
import gov.nasa.jpf.jvm.LocalVarInfo;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.StackFrame;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.bytecode.Instruction;

/**
 * @author Alf
 * 
 */
public class StateStackFrame extends StateNode {

  protected final StackFrame sf;
  protected final MethodInfo mi;

  public StateStackFrame (StateThreadInfo sti, Integer stackFrameNum) throws JPFInspectorException {
    this(sti, stackFrameNum, 1);
  }

  public StateStackFrame (StateThreadInfo sti, Integer stackFrameNum, int referenceDepth) throws JPFInspectorException {
    super(sti, referenceDepth);

    ThreadInfo ti = sti.getThreadInfo();

    int stackFrameIdx = 0;
    if (stackFrameNum != null) {
      stackFrameIdx = stackFrameNum;
    }

    StackFrame sf = ti.getCallerStackFrame(stackFrameIdx);
    if (sf == null) {
      throw new JPFInspectorInvalidStackFrame(ti.getId(), stackFrameIdx);
    }

    this.sf = sf;
    this.mi = sf.getMethodInfo();
    assert (mi != null);

    setStateExpr(createStateExpression(sti, stackFrameIdx));
  }

  /*
   * @see gov.nasa.jpf.inspector.server.programstate.StateNode#getResultExpression(gov.nasa.jpf.inspector.server.jpf.JPFInspector,
   * gov.nasa.jpf.inspector.server.expression.InspectorState)
   */
  @Override
  public PSEMethod getResultExpression (String name, int clientID) throws JPFInspectorException {

    Instruction inst = sf.getPC();
    InstructionWrapper instw = ChoiceGeneratorsManager.createInstructionWrapper(inst);

    PSEVariable refLocals[] = null;
    PSEVariableObject refThis = null;
    if (referenceDepth > 0) {

      if (mi.isStatic() == false) {
        try {
          StateValueStackSlot svss = StateValueStackSlot.createHiddenThisSlotValue(this);
          PSEVariable refThisGeneric = svss.getResultExpression(name, clientID);

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
        StateValueStackSlot svss = StateValueStackSlot.createSlotFromIndex(this, i, getReferenceDepth() - 1);
        refLocals[i] = svss.getResultExpression(name, clientID);
      }
    }

    return new PSEMethod(name, clientID, this, instw, refLocals, refThis);
  }

  /**
   * String which can be used to obtain specified StackFrame.
   * 
   * @param sti Specification of Thread which stack frame to get.
   * @param stackFrameDepth How depth (top is 0) is required stack frame
   * @return Requested expression
   */
  public static String createStateExpression (StateThreadInfo sti, int stackFrameDepth) {
    assert (sti != null);
    return sti.getStateExpr() + '.' + PSEMethod.EXPRESSION_METHOD_KEY_WORD + '[' + stackFrameDepth + ']';
  }

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
   * @return The of stack frame represents static method.
   */
  public boolean isStaticMethod () {
    return mi.isStatic();
  }

  /**
   * @return ClassInfo of called method.
   */
  public ClassInfo getClassInfo () {
    return mi.getClassInfo();
  }
}
