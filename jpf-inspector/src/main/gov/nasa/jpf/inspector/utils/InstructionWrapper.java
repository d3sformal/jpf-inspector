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
    super();
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

  public String getClassName () {
    return className;
  }

  public String getMethodName () {
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
   * 
   * @param instw
   * @param sb
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
