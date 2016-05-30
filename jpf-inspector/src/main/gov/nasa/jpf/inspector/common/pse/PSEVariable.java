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

package gov.nasa.jpf.inspector.common.pse;

import gov.nasa.jpf.inspector.common.pse.ProgramStateEntry;
import gov.nasa.jpf.inspector.server.programstate.StateNodeInterface;

/**
 * Common predecessor for all "value" representing types
 * Represents variable. Either primitive type (can be local variable, field), heap instance (this, field) or "Class" (this for static method)
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

  private final String varName; // Name of represented variable
  private final String varTypeName; // Type of represented variable
  private final String varValue; // Text representation of the value of the variable

  private final boolean isStatic; // true if represents static field
  private final String definedIn; // ClassName (or ClassName.methodName) where the represented variable is defined (in case of field or localVariable) null
                                  // otherwise

  private final int index;

  protected PSEVariable (String name, int clientID, StateNodeInterface sni, String varName, String varTypeName, String varValue, boolean isStatic,
      String definedIn, int index) {
    super(name, clientID, sni);

    this.varName = varName;
    this.varValue = varValue;
    this.varTypeName = varTypeName;
    this.isStatic = isStatic;
    this.definedIn = definedIn;
    this.index = index;
    // if (sei.isReference()) {
    // this.varValue = PSEVariable.elementInfo2String(sei.getReferenceValue());
    // } else {
    // this.varValue = sei.getValue().toString();
    // }

  }

  /**
   * @return Gets name of represented variable, field or parameter. Can get null if name is not known (no debug information or stack slot)
   */
  public String getVarName () {
    return varName;
  }

  /**
   * @return Gets type of represented entry.
   */
  public String getVarTypeName () {
    return varTypeName;
  }

  /**
   * @return Gets value of variable in textual form (meaning full only for primitive types and strings)
   */
  public String getVarValue () {
    return varValue;
  }

  public boolean isStatic () {
    return isStatic;
  }

  public String varDefined () {
    return definedIn;
  }

  /**
   * Gets index (stack slot index in the stack, field index in ElementInfo, array index of the element, heap index if represents whole ElementIfno) where the
   * variable is defined
   * 
   * @return
   */
  public int getIndex () {
    return index;
  }

}
