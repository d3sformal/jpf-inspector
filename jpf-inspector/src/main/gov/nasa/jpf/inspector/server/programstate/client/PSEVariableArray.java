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

package gov.nasa.jpf.inspector.server.programstate.client;

import gov.nasa.jpf.inspector.interfaces.JPFInspectorException;
import gov.nasa.jpf.inspector.server.programstate.ProgramStateEntry;
import gov.nasa.jpf.inspector.server.programstate.StateNodeInterface;

/**
 * @author Alf
 * 
 */
public class PSEVariableArray extends PSEVariable {

  private static final long serialVersionUID = -7445210497451473309L;

  private final int len; // Length of the represented array
  private PSEVariable[] refArrayItems; // Content of the array

  private boolean referencesCreated;

  /**
   * Creates full representation of the array with references to array entries
   */
  public PSEVariableArray (String name, int clientID, StateNodeInterface sni, String varName, String varTypeName, String varValue, boolean isStatic,
      String definedIn, int index, int len, PSEVariable[] refArrayItems) {
    super(name, clientID, sni, varName, varTypeName, varValue, isStatic, definedIn, index);

    this.referencesCreated = true;

    this.len = len;
    this.refArrayItems = refArrayItems;
  }

  /**
   * Creates basic representation of the array without references to array entries
   */
  public PSEVariableArray (String name, int clientID, StateNodeInterface sni, String varName, String varTypeName, boolean isStatic, String definedIn, int index) {
    super(name, clientID, sni, varName, varTypeName, "null", isStatic, definedIn, index);

    this.referencesCreated = false;

    this.len = 0;
    this.refArrayItems = null;
  }

  public int getLen () {
    return len;
  }

  public PSEVariable[] getArrayItems () throws JPFInspectorException {
    loadReferences();
    return refArrayItems;
  }

  /**
   * Lazy load of references
   */
  protected void loadReferences () throws JPFInspectorException {
    if (!referencesCreated) {
      if (DEBUG) {
        System.out.println(this.getClass().getSimpleName() + ".loadReferences() - lazy reference load");
      }
      // Create a copy of this PSE with filled references
      ProgramStateEntry pse = getInspector().evaluateStateExpression(getStateExpr());
      assert (pse instanceof PSEVariableArray);
      PSEVariableArray myCopy = (PSEVariableArray) pse;

      assert (myCopy.referencesCreated == true);

      this.refArrayItems = myCopy.refArrayItems;

      referencesCreated = true;
    }
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.ProgramStateEntry#visit(gov.nasa.jpf.inspector.server.programstate.client.PSEVisitor) */
  @Override
  public <T> T visit (PSEVisitor<T> visitor) throws JPFInspectorException {
    return visitor.visitPSEVariableArray(this);
  }

}
