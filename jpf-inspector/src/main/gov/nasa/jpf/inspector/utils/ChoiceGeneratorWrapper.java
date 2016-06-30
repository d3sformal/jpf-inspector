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
