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
package gov.nasa.jpf.inspector.server.choicegenerators;

import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorState;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGTypes;

public class ChoiceGeneratorStateImpl implements ChoiceGeneratorState {

  private final ChoiceGeneratorsInterface.CGTypes cgType;
  private final String cgName;
  private final String[] choices;
  private final int nextChoice;
  private final int defaultChoice;
  
  
  public ChoiceGeneratorStateImpl(CGTypes cgType, String cgName, String[] choices, int nextChoice, int defaultChoice) {
    this.cgType = cgType;
    this.cgName = cgName;
    this.choices = choices;
    this.nextChoice = nextChoice;
    this.defaultChoice = defaultChoice;
  }

  @Override
  public CGTypes getChoiceGeneratorType() {
    return cgType;
  }

  @Override
  public String getChoiceGeneratorID() {
    return cgName;
  }

  @Override
  public String[] getListOfChoices() {
    return choices;
  }

  @Override
  public int getNextChoicePlannedToUse() {
    return nextChoice;
  }

  @Override
  public int getDefaultForwardThreadChoice() {
    return defaultChoice;
  }

}
