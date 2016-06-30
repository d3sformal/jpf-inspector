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
