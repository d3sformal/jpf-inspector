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

package gov.nasa.jpf.inspector.server.programstate;

import gov.nasa.jpf.inspector.common.pse.ProgramStateEntry;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;

/**
 * Represents an evaluated program state expression.
 */
public interface StateNodeInterface {

  /**
   * Gets the Inspector server.
   */
  JPFInspector getInspector();

  /**
   * Returns an expression that, if evaluated at in the same program state, would result in an equal {@link ProgramStateEntry}.
   */
  String getStateExpr();

  /**
   * Evaluates the evaluated expression again, this time resulting in a {@link ProgramStateEntry} which is mostly
   * just a container of strings.
   *
   * The name "toHierarchy3" is temporary (I promise). Previously, this method was called "getResultExpression" which
   * was confusing because the method that transformed Expression classes (hierarchy 1) to state expressions (hierarchy 2)
   * was also called "getResultExpression".
   */
  ProgramStateEntry toHierarchy3() throws JPFInspectorException;
}
