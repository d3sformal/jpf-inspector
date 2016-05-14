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

package gov.nasa.jpf.inspector.interfaces;

import gov.nasa.jpf.jvm.bytecode.Instruction;

public interface InstructionPosition {

  public static final int IP_POS_LINE_NOT_DEFINED = -1; /// Unspecified line

  /**
   * Can be obtained by calling Instruction.getLineNumber()
   */
  public int getLineNumber();

  
  /**
   * File name where the class is defined. (If the class file is stored in the jar file, then jar file name is ignored) 
   */
  public String getFileName();
  
  /**
   * Test whether given instruction take place at represented position. (File && Line)
   * 
   * @param instr Instruction which position is tested.
   * @return true if position of given instruction is the represented one
   */
  public boolean hitPosition(Instruction instr);
  

  //Note following entries may not be valid all the time
  /**
   * @return Line from source file with represented position
   */
  public String getSourceLine();
  
  /**
   * @return Class name in which represented position takes place
   */
  public String getClassName();

  /**
   * @return Method name in which represented position takes place
   */
  public String getMethodName();
  
  /**
   * @return Get index of represented instruction in method
   */
  public int getInstructionOffset();
  
  /**
   * @return Name of the represented instruction
   */
  public String getInstructionName();
}