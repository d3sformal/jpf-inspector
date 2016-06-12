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

package gov.nasa.jpf.inspector.server.breakpoints;

import gov.nasa.jpf.inspector.interfaces.InstructionPosition;
import gov.nasa.jpf.inspector.utils.expressions.FileName;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.Instruction;

/**
 * Represents position of the instruction.
 * 
 * <br>
 * It hits on any instruction that is in same file and and takes place at given line.
 * 
 */
public class InstructionPositionImpl implements InstructionPosition {

  private FileName fileName = null;

  private int line = InstructionPosition.IP_POS_LINE_NOT_DEFINED;

  private String sourceLine = null;
  private String className = null;
  private String methodName = null;
  private String instrName = null;
  private int instrOffset = -1;

  public InstructionPositionImpl (String fileName, int line) {
    assert fileName != null;
    assert line != InstructionPosition.IP_POS_LINE_NOT_DEFINED && line > 0;

    this.fileName = new FileName(fileName);
    this.line = line;
  }

  // In this case we match at the same line as the instruction (not at given instruction)
  private InstructionPositionImpl(Instruction instr) {
    assert instr != null;

    this.fileName = new FileName(getInstructionFileName(instr));

    this.line = instr.getLineNumber();

    boundWithInstruction(instr);
  }

  /**
   * Assign specialized fields of the class. (Note that they are null of not bind to any instruction)
   * 
   * @param instr Instruction which specify position mire precisely
   */
  private void boundWithInstruction (Instruction instr) {
    assert instr != null;
    this.sourceLine = instr.getSourceLine();
    this.className = instr.getMethodInfo().getClassName();
    this.methodName = instr.getMethodInfo().getName();
    this.instrOffset = instr.getInstructionIndex();
    this.instrName = instr.toString();
  }

  /**
   * Notification that new Class has been loaded.
   * We checks if this is not the represented class, and retrieve information in this case.
   * 
   * @param ci Loaded class
   */
  public void newClassLoaded (ClassInfo ci) {
    assert ci != null;

    if (!fileName.representsSingleFile()) {
      // Group file names are ignored, we require 1:1 mapping
      return;
    }

    if (fileName.isSameFile(ci.getSourceFileName())) {
      // Represented class
      for (MethodInfo mi : ci.getDeclaredMethodInfos()) {
        // found the first instruction on given line
        for (Instruction inst : mi.getInstructions()) {
          if (inst.getLineNumber() == line) {
            boundWithInstruction(inst);
            return;
          }
        }
      }
    }
  }

  @Override
  public int getLineNumber () {
    return line;
  }

  @Override
  public boolean hitPosition (Instruction instr) {
    if (instr == null) {
      return false;
    }

    final String instrFileName = getInstructionFileName(instr);
    final int instrLine = instr.getLineNumber();
    if (instrLine == line && instrFileName != null) {
      // In the same line -> check if same files
      return fileName.isSameFile(instrFileName);
    } else {
      return false;
    }
  }

  /**
   * For given instruction get file where instruction is defined.
   * 
   * @param instr Instruction to process
   * @return File name where instruction takes place.
   */
  private static String getInstructionFileName(Instruction instr) {
    assert instr != null;
    String result = null;
    if (instr.getMethodInfo() != null) {
      result = instr.getMethodInfo().getSourceFileName();
    }
    return result;
  }

  @Override
  public String getFileName () {
    return fileName.getFileName();
  }

  @Override
  public String toString () {
    return fileName.getFileName() + ":" + line;
  }

  /**
   * Creates instruction position that represents the same location (file and line)
   * as given instruction.
   * 
   * @param instr Position specification
   * @return position that represents the location (file and line) of given instruction
   */
  static public InstructionPosition getInstructionPosition (Instruction instr) {
    return new InstructionPositionImpl(instr);
  }

  @Override
  public int getInstructionOffset () {
    return instrOffset;
  }

  @Override
  public String getClassName () {
    return className;
  }

  @Override
  public String getMethodName () {
    return methodName;
  }

  @Override
  public String getSourceLine () {
    return sourceLine;
  }

  @Override
  public String getInstructionName () {
    return instrName;
  }
}
