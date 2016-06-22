package gov.nasa.jpf.inspector.server.pathanalysis;

import gov.nasa.jpf.inspector.utils.Debugging;
import gov.nasa.jpf.vm.Path;
import gov.nasa.jpf.vm.Transition;

import java.util.Iterator;

/**
 * An interator that gives transitions starting at the current transition and then backtracking to the past, and the
 * iterator ignores transitions executed by threads other than the one we are interested in.
 */
class TransitionThreadBacktracker {

  /**
   * Iterates over transitions in the current path, from the latest to the root transition.
   * The first transition from this iterator is the transition that was current when the backtracker
   * was initialized.
   */
  private final Iterator<Transition> reversePathIterator;

  /**
   * The thread we are interested in.
   */
  private final int threadId;

  /**
   * Number of transitions we backtracked through.
   * "0" means we haven't begun yet.
   * "1" means we backtracked through the topmost transition only.
   */
  private int backsteppedTransitions = 0;
  /**
   * The transition that the backtracker currently examines. At start, this is null.
   * After the first getPreviousTransition call, it will be the current transition. Then,
   * it will be the previous one etc.
   */
  private Transition currentTransition = null;

  /**
   * Initializes the backtracker.
   *
   * Starts uninitialized and "backtrackToPreviousTransition" must be called once to get to the current transition first.
   *
   * @param path The current JPF transition path.
   * @param threadId The thread we are interested in.
   */
  public TransitionThreadBacktracker(Path path, int threadId) {
    assert (path != null);

    this.threadId = threadId;
    reversePathIterator = path.descendingIterator();
  }

  /**
   * Backtracks to the previous transition in given thread. Can be used iteratively to go back in program trace.
   * When this method is first called, it returns the current transition.
   * 
   * @return The transition that was backtracked to, or null if the backtracking faileed because no such transition exists.
   */
  public Transition backtrackToPreviousTransition() {
    Debugging.getLogger().info("Backtracking: Transition backtracking commences.");

    // Iterate until we find a transition executed by the thread we are interested in.
    while (reversePathIterator.hasNext()) {

      // Update current state
      currentTransition = reversePathIterator.next();
      Debugging.getLogger().info("Backtracking: Backtracked to transition labeled '" + currentTransition.getLabel() + "'");
      backsteppedTransitions++;

      if (currentTransition.getThreadIndex() == threadId) {
        return currentTransition;
      }
    }
    // No such Transition exists
    return null;
  }

  /**
   * Gets result of the last {@link #backtrackToPreviousTransition()} call, or null if no such call was yet made.
   * 
   * @return Current transition of the backtracker.
   */
  public Transition getCurrentTransition() {
    return currentTransition;
  }

  /**
   * Number of transitions we backtracked through.
   * "0" means we haven't begun yet.
   * "1" means we backtracked through the topmost transition only.
   *
   * This includes transitions executed by threads other than the one we are interested in.
   */
  public int getBacksteppedTransitions() {
    return backsteppedTransitions;
  }
}
