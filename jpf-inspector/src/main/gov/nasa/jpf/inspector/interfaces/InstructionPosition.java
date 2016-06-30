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

package gov.nasa.jpf.inspector.interfaces;

import gov.nasa.jpf.vm.Instruction;

/**
 * Represents the coordinates of an instruction, i.e. its location in the source code and in its method's bytecode.
 */
public interface InstructionPosition {

  /**
   * The source code line is unspecified.
   */
  int IP_POS_LINE_NOT_DEFINED = -1;

  /**
   * Gets the line number of this location, or -1 if the line could not be found.
   *
   * Can be obtained by calling Instruction.getLineNumber()
   */
  int getLineNumber();

  
  /**
   * Gets the name of the file where the instruction's class is defined. (If the class file is stored in the jar file, then jar file name is ignored.)
   *
   * If the filename could not be found, this returs null.
   */
  String getFileName();
  
  /**
   * Test whether given instruction take place at represented position. (File && Line)
   * 
   * @param instr Instruction which position is tested.
   * @return true if position of given instruction is the represented one
   */
  boolean hitPosition(Instruction instr);
  

  //Note following entries may not be valid all the time

  /**
   * @return Line from source file with represented position
   */
  String getSourceLine();
  
  /**
   * @return Class name in which represented position takes place
   */
  String getClassName();

  /**
   * @return Method name in which represented position takes place
   */
  String getMethodName();
  
  /**
   * @return Get index of represented instruction in method
   */
  int getInstructionOffset();
  
  /**
   * @return Name of the represented instruction
   */
  String getInstructionName();
}