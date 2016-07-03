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

package gov.nasa.jpf.inspector.common.pse;

/**
 * Common abstract predecessor for all types representing a value.
 * Represents a variable. Either primitive type (can be local variable, field), heap instance (this, field) or "Class"
 * (this for static methods)
 */
public abstract class PSEVariable extends ProgramStateEntry {

  private static final long serialVersionUID = -8656032045220241273L;

  public static final String EXPRESSION_VARIABLE_THIS = "#this"; // Reference to this in the program state expression
  public static final String EXPRESSION_VARIABLE_LOCAL_VAR = "#stackSlot"; // Reference to local variable in the program state expression (by index)
  public static final String EXPRESSION_VARIABLE_FIELD = "#field"; // Reference to field in the program state expression (by index)
  public static final String EXPRESSION_VARIABLE_HEAP = "#heap"; // Reference to heap entry in the program state expression (by index)
  public static final String EXPRESSION_OUTER_CLASS = "#outerClass"; // Get representation of the outer/enclosing class
  public static final String EXPRESSION_STATIC = "#static"; // Get access to static classes #static[*ClassName*] or static representation of current class
                                                            // #static or static field with given index #static[3]

  public static final String EXPRESSION_SUPER = "#super";


  /**
   * Name of represented variable
   */
  private final String varName;
  /**
   * Class name of the type of represented variable
   */
  private final String varTypeName;
  /**
   * Short-form ext representation of the value of the variable
   */
  private final String varValue;

  private final int index;

  protected PSEVariable(String varName, String varTypeName, String varValue,
                        int index) {

    this.varName = varName;
    this.varValue = varValue;
    this.varTypeName = varTypeName;
    this.index = index;
  }

  /**
   * Gets the name of the represented variable, field or parameter. Returns null if the name is not known
   * (no debug information or stack slot, for example).
   */
  public String getVarName () {
    return varName;
  }

  /**
   * Gets the Java type of the represented object.
   */
  public String getVarTypeName () {
    return varTypeName;
  }

  /**
   * Gets the short-form value of the variable in text form.
   * See the "Commands" -> "print" -> "Output interpretation" for details.
   */
  public String getVarValue () {
    return varValue;
  }

  /**
   * Gets the index of this entry in its parent entry.
   * This might be:
   *  - stack slot index
   *  - field index
   *  - array index
   *  - heap index
   */
  public int getIndex () {
    return index;
  }

}
