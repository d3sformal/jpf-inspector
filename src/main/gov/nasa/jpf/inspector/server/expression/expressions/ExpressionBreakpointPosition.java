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

package gov.nasa.jpf.inspector.server.expression.expressions;

import gov.nasa.jpf.inspector.interfaces.InstructionPosition;
import gov.nasa.jpf.inspector.server.expression.ExpressionBooleanLeaf;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.expression.InspectorState.ListenerMethod;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.VM;

/**
 * Represents the "position = [filename]:[linenumber]" hit condition that hits just before the first instruction on the specified line.
 */
// Previously, there were these to-do items:
// "the NEW instruction can stand in place of the call instruction (calls the constructor)"
// "problem is connected with the DirectCalls (cinits) and reexecution of the same instruction"
// However, I am pretty sure they are no longer relevant.
public class ExpressionBreakpointPosition extends ExpressionBooleanLeaf {
  private static final boolean DEBUG = false;
  @SuppressWarnings("FieldCanBeLocal") // IDEA bug
  private final JPFInspector inspector;

  /**
   * The file and line we are interested in.
   */
  private final InstructionPosition targetLocation;

  public ExpressionBreakpointPosition (JPFInspector inspector, InstructionPosition pos) {
    assert pos != null;
    assert inspector != null;
    this.inspector = inspector;

    if (DEBUG) {
      inspector.getDebugPrintStream().println(
          ExpressionBreakpointPosition.class.getSimpleName() + "." + ExpressionBreakpointPosition.class.getSimpleName() + "(pos=" + pos + ")");
    }

    this.targetLocation = pos;
  }

  @Override
  public boolean evaluateExpression (InspectorState state) {
    assert state != null;
    if (DEBUG) {
      inspector.getDebugPrintStream().println(this.getClass().getSimpleName() + ".evaluateExpression(...)");
    }
    if (state.getListenerMethod() != ListenerMethod.LM_EXECUTE_INSTRUCTION) {
      return false;
    }

    VM vm = state.getVM();
    assert vm != null;

    final Instruction thisInstruction = vm.getInstruction();

    // This represents whether we are the same file and line, but we still need to ensure that we are the FIRST
    // instruction on this line. By "first instruction", we mean "the first instruction on this line in the current method".
    if (!targetLocation.hitPosition(thisInstruction)) {
      return false;
    }

    int targetLine = targetLocation.getLineNumber();
    Instruction firstInstructionOnThisLine = thisInstruction.getMethodInfo().getInstructionsForLine(targetLine)[0];
    return thisInstruction.equals(firstInstructionOnThisLine);
  }

  @Override
  public String getDetails (InspectorState state) {
    if (state != null && evaluateExpression(state)) {
      return "SuT will now execute \"" + state.getVM().getInstruction().toString() + "\" at position " + targetLocation.toString() + ".";
    }
    return "";
  }

  @Override
  public String getNormalizedExpression () {
    String string = "position=" +
            targetLocation.getFileName() +
            ':' +
            targetLocation.getLineNumber();
    return string;
  }

}
