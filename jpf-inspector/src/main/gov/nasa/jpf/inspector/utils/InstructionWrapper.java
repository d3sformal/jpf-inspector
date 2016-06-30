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

package gov.nasa.jpf.inspector.utils;

import java.io.Serializable;

/**
 * Used to transfer information about instruction from backend to clients
 * 
 * @author Alf
 * 
 */
public final class InstructionWrapper implements Serializable {

  private static final long serialVersionUID = 4575281022863840595L;

  private final String instructionName; // Intruction.toString()ins

  // Bytecode representation
  private final String className;
  private final String methodName;
  private final int instructionPosition;

  // Source representation
  private final String fileName;
  private final int fileLineNum;
  private final String sourceCodeLine;

  /**
   * Maximal printed length of source line or value
   */
  private static final int MAX_LEN = 50;

  public InstructionWrapper (String instructionName, String className, String methodName, int instructionPosition, String fileName, int currLine,
      String currLineSource) {
    this.instructionName = instructionName;
    this.className = className;
    this.methodName = methodName;
    this.instructionPosition = instructionPosition;
    this.fileName = fileName;
    this.fileLineNum = currLine;
    this.sourceCodeLine = currLineSource;
  }

  public String getInstructionName () {
    return instructionName;
  }

  private String getClassName () {
    return className;
  }

  private String getMethodName() {
    return methodName;
  }

  public int getInstructionPosition () {
    return instructionPosition;
  }

  public String getSourceFileName () {
    return fileName;
  }

  public int getSourceFileLineNum () {
    return fileLineNum;
  }

  public String getSouceCodeLine () {
    return sourceCodeLine;
  }

  /**
   * Appends representation of the InstructionWrapper to given string buffer.
   */
  public static void toStringBuffer (InstructionWrapper instw, StringBuilder sb) {
    sb.append(instw.getClassName());
    sb.append(".");
    sb.append(instw.getMethodName());
    sb.append(" - (in file ");
    sb.append(instw.getSourceFileName());
    sb.append(":");
    sb.append(instw.getSourceFileLineNum());
    sb.append(")");
    String srcLine = instw.getSouceCodeLine();
    if (srcLine != null) {
      srcLine = srcLine.trim();
      if (srcLine.length() > MAX_LEN) {
        srcLine = srcLine.substring(0, MAX_LEN - 3) + "...";
      }
      sb.append(" - ");
      sb.append(srcLine);
    }
  }

  @Override
  public String toString () {
    StringBuilder sb = new StringBuilder();
    toStringBuffer(this, sb);
    return sb.toString();
  }

}
