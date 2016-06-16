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

import java.io.Serializable;

/**
 * Represents a Java primitive.
 */
public class PSEVariablePrimitive extends PSEVariable {

  private static final long serialVersionUID = 6680525792762309903L;

  private final Serializable wrappedValue;

  public PSEVariablePrimitive(String varName,
                              String varTypeName, String varValue, boolean isStatic,
                              String definedIn, int index, Object wrappedValue) {
    super(varName, varTypeName, varValue, isStatic, definedIn, index);

    checkPrimitiveType(wrappedValue);
    this.wrappedValue = (Serializable) wrappedValue;

  }

  /**
   * Checks if value is Wrapped primitive type
   */
  private static void checkPrimitiveType (Object wrappedValue) {
    assert (wrappedValue != null);

    assert ((wrappedValue instanceof Boolean) || (wrappedValue instanceof Character) || (wrappedValue instanceof Byte) || (wrappedValue instanceof Short)
        || (wrappedValue instanceof Integer) || (wrappedValue instanceof Long) || (wrappedValue instanceof Float) || (wrappedValue instanceof Double));

    assert wrappedValue instanceof Serializable;

  }

  /**
   * Gets the Java primitive value, for example, an {@link Integer} or a {@link Boolean}.
   */
  public Object getWrappedValue () {
    return wrappedValue;
  }

  @Override
  public <T> T visit (PSEVisitor<T> visitor) throws JPFInspectorException {
    return visitor.visitPSEVariablePrimitive(this);
  }

}
