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

import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGTypes;

import java.io.Serializable;

/**
 * Representation of the ChoiceGenerator for clients
 * 
 * @author Alf
 * 
 */
public final class ChoiceGeneratorWrapper implements Serializable {

  private static final long serialVersionUID = -1840182791083790707L;

  private final CGTypes cgType; // Represents type of ChoiceGenerator

  private final String cgString; // Holds toString() of represented CG

  private final int threadNum; // Thread which executes the instruction which caused the CG

  private final String id;
  private final InstructionWrapper inst;

  public ChoiceGeneratorWrapper (CGTypes cgType, String cgString, int threadNum, InstructionWrapper inst, String id) {
    super();
    this.cgType = cgType;
    this.cgString = cgString;
    this.threadNum = threadNum;
    this.inst = inst;
    this.id = id;
  }

  public CGTypes getCgType () {
    return cgType;
  }

  public String getCgString () {
    return cgString;
  }

  public int getThreadNum () {
    return threadNum;
  }

  public InstructionWrapper getInstruction () {
    return inst;
  }

  public String getId () {
    return id;
  }

}
