//
// Copyright (C) 2010 United States Government as represented by the
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

package gov.nasa.jpf.inspector.client.commands;

import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorException;
import gov.nasa.jpf.inspector.server.programstate.client.PSEThread;

import java.io.PrintStream;
import java.util.Map;

public class CmdStatusThreads extends ClientCommand {

  private final Integer tn;

  /**
   * @param tn
   *        Specify the thread we get the information. Null means all threads.
   */
  public CmdStatusThreads (Integer tn) {
    this.tn = tn;
  }

  @Override
  public String getNormalizedCommand () {
    String result = "thread";
    if (tn == null) {
      return result;
    }
    return result + ' ' + tn;
  }

  @Override
  public void executeCommands (JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    assert inspector != null;
    try {
      Map<Integer, PSEThread> threads = inspector.getThreads(tn);

      if (threads == null) {
        return;
      }
      if (threads.size() == 0) {
        outStream.println("No thread");
        return;
      }

      for (PSEThread te : threads.values()) {
        outStream.println(thread2String(te));
      }
    } catch (JPFInspectorException e) {
      outStream.println(e.getMessage());
      client.recordComment(e.getMessage());
    }

  }

  /**
   * Converts the thread representation into string.
   * 
   * @param thread
   *        The thread description to convert.
   * @return String with short description of the thread properties (doesn't show the call stack)
   */
  static public String thread2String (PSEThread thread) {
    assert thread != null;
    StringBuffer sb = new StringBuffer(120);
    sb.append(thread.getThreadNum());
    sb.append(" :");
    String tn = thread.getThreadName();
    if (tn != null && !tn.trim().isEmpty() && !tn.equals("Thread-" + Integer.toString(thread.getThreadNum()))) {
      sb.append(' ');
      sb.append(tn);
    }

    sb.append(" state=");
    sb.append(thread.getState());

    if (thread.isDaemon()) {
      sb.append(" daemon thread");
    }

    sb.append(" priority=");
    sb.append(thread.getPriority());
    return sb.toString();
  }

}
