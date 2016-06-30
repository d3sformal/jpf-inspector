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
