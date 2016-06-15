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

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.common.pse.ProgramStateEntry;
import gov.nasa.jpf.inspector.common.pse.PSEThread;

import java.util.Map;

/**
 * This interface is part of the server's contract to the client. It contains methods used by commands that read
 * and manipulate the program state of the system under test.
 *
 * Stateless (all important information is always provided by the client).
 * 
 * There will be 3 views - threads+stack, heap, variables.
 */
public interface ProgramStateInterface {

  /**
   * Gets selected/all threads and their status.
   * 
   * @param threadNum The thread we select. Or all thread if null.
   * @throws JPFInspectorGenericErrorException
   */
  Map<Integer, PSEThread> getThreads(Integer threadNum) throws JPFInspectorException;

  /**
   * Gets selected/all threads and their PC.
   * 
   * @param threadNum The thread we select. Or all thread if null.
   * @throws JPFInspectorGenericErrorException
   */
  Map<Integer, InstructionPosition> getThreadsPC(Integer threadNum) throws JPFInspectorException;

  /**
   * Evaluates given expression and gets final representation (thread/method/variable) value
   * 
   * @param expr Expression to process
   * @return Representation of requested program state
   * @throws JPFInspectorGenericErrorException
   */
  ProgramStateEntry evaluateStateExpression(String expr) throws JPFInspectorException;

  /**
   * Changes the value of a variable or a field.
   * 
   * @param expr String with an expression in the following form "lvalue-state-expression = rvalue-state-expression"
   * @throws JPFInspectorException If it fails to change the value.
   */
  void setValue(String expr) throws JPFInspectorException;
}
