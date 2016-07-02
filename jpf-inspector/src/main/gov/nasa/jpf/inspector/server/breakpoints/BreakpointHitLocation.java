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

package gov.nasa.jpf.inspector.server.breakpoints;

import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.Transition;

/**
 * Represents a location in the transition path at which a breakpoint was hit.
 * The instruction is the instruction that was about to execute when the breakpoint interrupted the JPF thread.
 */
public class BreakpointHitLocation {
  /**
   * We store the index of the transition in path (i.e. the number of transitions before this transition, plus 1) instead
   * of the actual transition, because backwards stepping would interfere with back_breakpoint_hit otherwise.
   */
  private final int indexOfTransitionInPath;
  private final Instruction instruction;
  private int numberOfSkippedInstructions;

  public BreakpointHitLocation(int indexOfTransitionInPath, Instruction instruction, int numberOfSkippedInstructions) {

    this.indexOfTransitionInPath = indexOfTransitionInPath;
    this.instruction = instruction;
    this.numberOfSkippedInstructions = numberOfSkippedInstructions;
  }

  public Instruction getInstruction() {
    return instruction;
  }

  public int getIndexOfTransitionInPath() {
    return indexOfTransitionInPath;
  }

  public int getNumberOfSkippedInstructions() {
    return numberOfSkippedInstructions;
  }
}
