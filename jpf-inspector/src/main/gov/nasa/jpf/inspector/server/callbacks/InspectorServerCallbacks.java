package gov.nasa.jpf.inspector.server.callbacks;

import gov.nasa.jpf.inspector.interfaces.InspectorCallbacks;

/**
 * This is the callbacks interface implemented by the server.
 *
 * {@inheritDoc}
 *
 * In addition to the methods inherited from {@link InspectorCallbacks}, it also
 * allows the client to block until callback queue is empty.
 */
public interface InspectorServerCallbacks extends InspectorCallbacks{
  /**
   * Blocks until the queue of the callbacks to send becomes empty, then returns.
   */
  void waitUntilCallbackQueueIsEmpty();
}
