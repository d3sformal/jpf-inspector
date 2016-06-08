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

package gov.nasa.jpf.inspector.server.expression;

import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.VM;

/**
 * Represents state of the SuT and the inspector. Holds all necessary
 * information needed to evaluate expressions ({@link ExpressionNodeInterface}).
 *
 * This is a public-facing interface that is passed to custom breakpoint hit conditions.
 */
public interface InspectorState {

  /**
   * Represents a method of the Listener interface.
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
     * TODO remove this
     * @param lm Method to check. Cannot be null.
     * @return Gets true if method is defined in {@link gov.nasa.jpf.search.SearchListener} interface
     */
    @SuppressWarnings("unused")
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

  /**
   * Returns the current JPF virtual machine.
   */
  VM getVM();

  /**
   * Gets the last instruction that was already executed by the virtual machine on the specified thread.
   * Specifically, it is NOT the instruction that is about to be executed.
   * @param thread Index of a thread of the system under test.
   * @return The last executed instruction of the htread.
   */
  Instruction getLastExecutedInstruction(int thread);

  /**
   * Returns the used Search object.
   */
  Search getSearch();

  /**
   * Returns the name of the listener method that caused this state to be passed to a hit condition.
   * Each hit condition should check the return value of this method as one of the first things it does to tell
   * if it wants to apply.
   */
  ListenerMethod getListenerMethod();

}
