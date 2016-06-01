/**
 * Contains classes of the JPF Inspector client.
 *
 * JPF Inspector consists of the client and of the server.
 *
 * The client is created by the frontend (a graphical shell panel or the command-line shell). The frontend takes
 * commands from the user and passes them to the client. The client parses them and sends them to the server. The
 * server executes them and returns the result to the client and the client sends the result to the output stream.
 */
package gov.nasa.jpf.inspector.client;