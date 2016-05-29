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

package gov.nasa.jpf.inspector.common.pse;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.server.programstate.StateNodeInterface;

/**
 * Represents Object (a heap element different from array)
 */
public class PSEVariableObject extends PSEVariable {

  private static final long serialVersionUID = 5368791941110343439L;

  private boolean referencesCreated;

  private PSEVariable[] refFields; // Sorted by field/slot index (ensured by creator)
  private PSEVariable[] refStaticFields; // Sorted by field/slot index static element fields

  // refThis == null ... static or no references are created
  // refFields == null ... no references are created (if class does not contain field any field, empty array should by set)
  public PSEVariableObject (String name, int clientID, StateNodeInterface sni, String varName, String varTypeName, String varValue, boolean isStatic,
      String definedIn, int index, PSEVariable[] refFields, PSEVariable[] refStaticFields) {
    super(name, clientID, sni, varName, varTypeName, varValue, isStatic, definedIn, index);

    this.referencesCreated = refFields != null;
    this.refFields = refFields;
    this.refStaticFields = refStaticFields;
  }

  public PSEVariable[] getFields () throws JPFInspectorException {
    loadReferences();
    return refFields;
  }

  public PSEVariable[] getStaticFields () throws JPFInspectorException {
    loadReferences();
    return refStaticFields;
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
      assert (pse instanceof PSEVariableObject);
      PSEVariableObject myCopy = (PSEVariableObject) pse;

      assert (myCopy.referencesCreated == true);

      this.refFields = myCopy.refFields;
      this.refStaticFields = myCopy.refStaticFields;

      referencesCreated = true;
    }
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.ProgramStateEntry#visit(gov.nasa.jpf.inspector.server.programstate.client.PSEVisitor) */
  @Override
  public <T> T visit (PSEVisitor<T> visitor) throws JPFInspectorException {
    return visitor.visitPSEVariableObject(this);
  }
}
