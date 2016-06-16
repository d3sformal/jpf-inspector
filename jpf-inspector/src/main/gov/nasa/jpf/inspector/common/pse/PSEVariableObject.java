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

/**
 * Represents a non-array, non-primitive Java object.
 */
public class PSEVariableObject extends PSEVariable {

  private static final long serialVersionUID = 5368791941110343439L;

  /**
   * Sorted by field index (ensured by creator)
   *
   * Note: Previously, this comment said "sorted by field/slot index" but I don't understand how "slots" could come
   * in play here so I removed it.
   */
  private PSEVariable[] refFields;
  /**
   * Sorted by field index (static element fields)
   */
  private PSEVariable[] refStaticFields;

  public PSEVariableObject(String varName,
                           String varTypeName, String varValue, boolean isStatic,
                           String definedIn, int index, PSEVariable[] refFields, PSEVariable[] refStaticFields) {
    super(varName, varTypeName, varValue, isStatic, definedIn, index);

    this.refFields = refFields;
    this.refStaticFields = refStaticFields;
  }

  public PSEVariable[] getFields () throws JPFInspectorException {
    return refFields;
  }

  public PSEVariable[] getStaticFields () throws JPFInspectorException {
    return refStaticFields;
  }

  @Override
  public <T> T visit (PSEVisitor<T> visitor) throws JPFInspectorException {
    return visitor.visitPSEVariableObject(this);
  }
}
