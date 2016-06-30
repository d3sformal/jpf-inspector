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
package gov.nasa.jpf.inspector.server.callbacks;

import gov.nasa.jpf.inspector.interfaces.InspectorCallbacks;

/**
 * A callback command is a server-side class that sends a specific callback to the client. Each class corresponds
 * to a type of callback.
 */
public interface CallbackCommand {
  /**
   * Sends this callback to the client. Only the callback sending thread calls this method.
   * 
   * @param clientCallbacks Client-side class that displays callbacks to the user.
   */
  void sendCallback(InspectorCallbacks clientCallbacks);
  
  /**
   * Indicates whether the callback sending thread should wait for JPF to be paused.
   *
   *  If false, the callback should be sent as soon as all previous callbacks are processed.
   *  If true, the callbacks should only be sent once JPF is stopped. Until then, the callback sending thread
   *  will be blocked.
   */
  boolean waitJPF2stop();
}
