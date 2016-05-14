package gov.nasa.jpf.inspector.server.pathanalysis;

import gov.nasa.jpf.vm.Path;
import gov.nasa.jpf.vm.Transition;

import java.util.Iterator;

/**
 * Creates single thread backward (transition) trace from {@link Path} by hiding transitions from different threads.
 * 
 * @author Alf
 * 
 */
class TransitionThreadBacktracker {

  private final Iterator<Transition> reversePathIterator;

  private final int threadId; // Interesting thread

  private int backsteppedTransitions = 0;
  private Transition currentTransition = null;;

  private Transition prevReturnedTransition = null; // Previous (not last, but one before the last) result of {@link #getPreviousTransition()} call.
  private int prevReturnedBacksteppedTransitions = 0; // Number of transition to backtrack to obtain {@link #prevReturnedTransition}.

  public TransitionThreadBacktracker(Path path, int threadId) {
    super();
    assert (path != null);

    this.threadId = threadId;

    reversePathIterator = path.descendingIterator();
  }

  /**
   * Get previous transition in given thread. Can be used iteratively to go back in program trace.
   * 
   * @return Get previous transition in given thread or null if no such transition exists.
   */
  public Transition getPreviousTransition() {
    // Remember previous state
    prevReturnedBacksteppedTransitions = backsteppedTransitions;
    prevReturnedTransition = currentTransition;

    while (reversePathIterator.hasNext()) {

      // Update current state
      currentTransition = reversePathIterator.next();
      backsteppedTransitions++;

      if (currentTransition.getThreadIndex() == threadId) {
        return currentTransition;
      }
    }
    // No such Transition exists
    return null;
  }

  /**
   * Gets result of the last {@link #getPreviousTransition()} call.
   * 
   * @return Gets current transition.
   */
  public Transition getCurrentTransition() {
    return currentTransition;
  }

  /**
   * @return Gets number of transitions (including the skipped transition in different threads) which were "backstepped" by calls of
   *         {@link #getPreviousTransition()}
   */
  public int getBacksteppedTransitions() {
    return backsteppedTransitions;
  }

  /**
   * Gets previous (not last, but one before) result of {@link #getPreviousTransition()} call.
   * 
   * @return Gets previous transition.
   */
  public Transition getPrevReturnedTransition() {
    return prevReturnedTransition;
  }

  /**
   * @return Gets number of transitions (including the skipped transition in different threads) which were "backstepped" to obtain {@link #getPrevReturnedTransition()}
   */
  public int getPrevReturnedBackSteppedTransitions() {
    return prevReturnedBacksteppedTransitions;
  }

}
