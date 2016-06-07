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

/**
 * 
 */
package gov.nasa.jpf.inspector.server.expression;

import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.search.Search;

import java.util.Map;

/**
 * Represents state of the SuT and the inspector. Holds all necessary
 * information needed to evaluate expressions {@link ExpressionNodeInterface}.
 */
public interface InspectorState {

  /**
   * Represents called method on the listener interface
   */
  enum ListenerMethod {
    LM_INSTRUCTION_EXECUTED,
    LM_STATE_ADVANCED,
    LM_STATE_BACKTRACKED,
    LM_SEARCH_CONSTRAINT_HIT,
    LM_PROPERTY_VIOLATED,
    LM_GC_BEGIN,
    LM_GC_END,
    LM_OBJECT_CREATED,
    LM_OBJECT_RELEASED,
    LM_EXCEPTION_THROWN,
    LM_CHOICE_GENERATOR_ADVANCED,
    LM_THREAD_SCHEDULED,
    LM_EXECUTE_INSTRUCTION,
    LM_NOT_IN_LIST;

    /**
     * @param lm Method to check. Cannot be null.
     * @return Gets true if method is defined in {@link gov.nasa.jpf.search.SearchListener} interface
     */
    static public boolean isSearchListenerMethod (ListenerMethod lm) {
      assert (lm != null);

      switch (lm) {
      case LM_STATE_ADVANCED:
      case LM_STATE_BACKTRACKED:
      case LM_SEARCH_CONSTRAINT_HIT:
      case LM_CHOICE_GENERATOR_ADVANCED:
      case LM_PROPERTY_VIOLATED:
        return true;
      case LM_INSTRUCTION_EXECUTED:
      case LM_GC_BEGIN:
      case LM_GC_END:
      case LM_OBJECT_CREATED:
      case LM_OBJECT_RELEASED:
      case LM_EXCEPTION_THROWN:
      case LM_THREAD_SCHEDULED:
      case LM_NOT_IN_LIST:
        return false;
      default:
        throw new RuntimeException("Unknown enum entry " + lm);
      }
    }
  }

  // map read only, should not be modified
  // / For all threads gets previously executed instruction
  Map<Integer, Instruction> getPreviousSteps();

  VM getJVM();

  Instruction getLastExecutedInstruction(int thread);

  // Used search listener
  Search getSearch();

  ListenerMethod getListenerMethod();

}
