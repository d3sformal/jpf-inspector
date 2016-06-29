package gov.nasa.jpf.inspector.server.breakpoints;

import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.Transition;

/**
 * Represents a location in the transition path at which a breakpoint was hit.
 * The instruction is the instruction that was about to execute when the breakpoint interrupted the JPF thread.
 */
public class BreakpointHitLocation {
  private final Transition transition;
  private final Instruction instruction;
  private int numberOfSkippedInstructions;

  public BreakpointHitLocation(Transition transition, Instruction instruction, int numberOfSkippedInstructions) {

    this.transition = transition;
    this.instruction = instruction;
    this.numberOfSkippedInstructions = numberOfSkippedInstructions;
  }

  public Instruction getInstruction() {
    return instruction;
  }

  public Transition getTransition() {
    return transition;
  }

  public int getNumberOfSkippedInstructions() {
    return numberOfSkippedInstructions;
  }
}
