//
// Copyright (C) 2010-2011 Pavel Jančík
// Copyright (C) 2016 Petr Hudeček
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
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
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
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
