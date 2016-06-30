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
package gov.nasa.jpf.inspector.interfaces;

import gov.nasa.jpf.vm.ChoiceGenerator;

public interface ChoiceGeneratorState {
  
  /**
   * @return Gets type or represented {@link ChoiceGenerator}
   */
  ChoiceGeneratorsInterface.CGTypes getChoiceGeneratorType();
  
  /**
   * @return Get {@link ChoiceGenerator#getId()} of represented CG
   */
  String getChoiceGeneratorID();
  
  /**
   * @return Gets list of all possible choices of given {@link ChoiceGenerator}
   */
  String[] getListOfChoices();
  
  /**
   * @return Gets index in the list {@link #getListOfChoices()} of choices which JPF schedules for next execution
   */
  int getNextChoicePlannedToUse();
  /**
   * @return Gets index in the list {@link #getListOfChoices()} of choice which were used the trace which were backward stepped by user. 
   */
  int getDefaultForwardThreadChoice();
}
