/**
 * Contains classes of the JPF Inspector server.
 *
 * JPF Inspector consists of the client and of the server.
 *
 * Methods of the server classes are called either from the client (whenever the user executes a command) or from the
 * JPF thread (when called by the {@link gov.nasa.jpf.inspector.server.jpf.InspectorListener}). Methods that are called
 * from the JPF thread must be annotated as such.
 */
package gov.nasa.jpf.inspector.server;