package gov.nasa.jpf.inspector.interfaces;

/**
 * Represents the result of an "enable thread" or "disable thread" operation.
 */
public enum ThreadEnablingResult {
  /**
   * A thread's suppression status was flipped.
   */
  THREAD_SUCCESSFULLY_CHANGED_STATE,
  /**
   * If enabling, the thread was already enabled. If disabling, the thread was already disabled.
   */
  THREAD_STATE_UNCHANGED
}
