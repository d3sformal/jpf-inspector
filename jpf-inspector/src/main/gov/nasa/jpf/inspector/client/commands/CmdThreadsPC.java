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
import gov.nasa.jpf.inspector.interfaces.InstructionPosition;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;

import java.io.PrintStream;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Represents the "thread_pc" command that shows the current instruction of a thread (or all threads).
 */
public class CmdThreadsPC extends ClientCommand {

  private final Integer threadNum; // / Specify thread to print. Null means all threads.

  public CmdThreadsPC (Integer threadNum) {
    this.threadNum = threadNum;
  }

  @Override
  public void executeCommands (JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    assert inspector != null;
    try {
      Map<Integer, InstructionPosition> places = inspector.getThreadsPC(threadNum);

      if (places == null) {
        // Error
        return;
      }
      if (places.size() == 0) {
        outStream.println("No thread with a valid program counter.");
        return;
      }

      for (Entry<Integer, InstructionPosition> entry : places.entrySet()) {
        outStream.println(Integer.toString(entry.getKey()) + " : " + instructionPosition2String(entry.getValue()));
      }
    } catch (JPFInspectorException e) {
      outStream.println(e.getMessage());
      client.recordComment(e.getMessage());
    }

  }

  @Override
  public boolean isSafeToExecuteWhenNotPaused() {
    return false;
  }

  @Override
  public String getNormalizedCommand () {
    String result = "thread_pc";
    if (threadNum == null) {
      return result;
    }
    return result + ' ' + threadNum;
  }

  /**
   * Converts representation of the instruction position into string
   * 
   * @param ip
   *        Representation of the instruction position
   * @return String representation of the position
   */
  private static String instructionPosition2String(InstructionPosition ip) {
    StringBuilder sb = new StringBuilder(200);
    sb.append(ip.getFileName());
    sb.append(':');
    sb.append(ip.getLineNumber());
    String sourceLine = ip.getSourceLine();
    if (sourceLine != null && !sourceLine.trim().isEmpty()) {
      sb.append(':');
      sb.append(sourceLine);
    }
    String className = ip.getClassName();
    if (className != null && !className.trim().isEmpty()) {
      sb.append("\n\t");
      sb.append(className);
      sb.append(':');
      sb.append(ip.getMethodName());
      sb.append(':');
      sb.append(ip.getInstructionOffset());
      sb.append(':');
      sb.append(ip.getInstructionName());
    }
    return sb.toString();
  }

}
