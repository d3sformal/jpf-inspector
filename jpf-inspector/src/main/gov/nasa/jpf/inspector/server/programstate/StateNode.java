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
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;

/**
 * A node of the intermediate representation of an expression (hierarchy 2).
 * Hierarchy 2 class names start with "State".
 */
public abstract class StateNode implements StateNodeInterface {

  /**
   * An expression that, if evaluated in the same program state,
   * would result in an equal {@link ProgramStateEntry}.
   */
  private String stateExpr;

  /**
   * The Inspector server.
   */
  private final JPFInspector inspector;

  protected StateNode (JPFInspector inspector) {
    assert inspector != null;

    this.inspector = inspector;
  }

  @Override
  public JPFInspector getInspector () {
    return inspector;
  }

  /**
   * Sets the expression that, if evaluated using print, would give rise to this.
   */
  protected void setStateExpr (String stateExpr) {
    assert (stateExpr != null); // Illegal state expression
    assert (this.stateExpr == null); // State expression must be set only once.
    this.stateExpr = stateExpr;
  }

  @Override
  public String getStateExpr () {
    assert (stateExpr != null); // State expression has to be set exactly from the constructor
    return stateExpr;
  }
}
