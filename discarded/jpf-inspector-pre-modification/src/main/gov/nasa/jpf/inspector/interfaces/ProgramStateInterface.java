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

import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.server.programstate.ProgramStateEntry;
import gov.nasa.jpf.inspector.server.programstate.client.PSEThread;

import java.util.Map;

/**
 * Is intended for inspection of state of the SuT.
 * 
 * Stateless (all important information always provides client)
 * 
 * - there will be 3 views - threads+stack, heap, variables
 */
public interface ProgramStateInterface {

  /**
   * Gets selected/all threads and their status.
   * 
   * @param threadNum The thread we select. Or all thread if null.
   * @throws JPFInspectorGenericErrorException
   */
  public Map<Integer, PSEThread> getThreads (Integer threadNum) throws JPFInspectorException;

  /**
   * Gets selected/all threads and their PC.
   * 
   * @param threadNum The thread we select. Or all thread if null.
   * @throws JPFInspectorGenericErrorException
   */
  public Map<Integer, InstructionPosition> getThreadsPC (Integer threadNum) throws JPFInspectorException;

  /**
   * Evaluates given expression and gets final representation (thread/method/variable) value
   * 
   * @param expr Expression to process
   * @return Representation of requested program state
   * @throws JPFInspectorGenericErrorException
   */
  public ProgramStateEntry evaluateStateExpression (String expr) throws JPFInspectorException;

  /**
   * Sets value of the variable. If setting is not successful throws exception.
   * 
   * @param expr String with expression in the following form "lvalue state expression = rvalue state expression"
   * @throws JPFInspectorException
   */
  public void setValue (String expr) throws JPFInspectorException;

  /**
   * Sets value of the variable. If setting is not successful throws exception.
   * 
   * @param lValue String with state expression (as can used in {@link #evaluateStateExpression(String)} which represents value of variable (not a constant, a
   *        stack frame, etc.)
   * @param rValue String with state expression (as can used in {@link #evaluateStateExpression(String)} which represents value of the "same"/assignable type
   *        lValue.
   * @throws JPFInspectorException
   */
  public void setValue (String lValue, String rValue) throws JPFInspectorException;
}
