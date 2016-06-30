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

package gov.nasa.jpf.inspector.server.breakpoints;

import gov.nasa.jpf.inspector.interfaces.InstructionPosition;
import gov.nasa.jpf.inspector.utils.expressions.FileName;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.Instruction;

/**
 * Represents the source code location of an instruction.
 *
 * It is used as a filter or a tester that "hits" on any instruction that is in same file
 * and takes place at given line.
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
  public InstructionPositionImpl(Instruction instr) {
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
   * Gets the filename of the source file where the instruction is used, or null if the source file cannot be found.
   * 
   * @param instr Instruction whose file we wish to discover.
   * @return Filename where the instruction takes place, or null if it could not be found.
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
  public static InstructionPosition getInstructionPosition (Instruction instr) {
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
