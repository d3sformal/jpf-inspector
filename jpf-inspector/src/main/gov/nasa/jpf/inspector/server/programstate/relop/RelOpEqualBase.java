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

package gov.nasa.jpf.inspector.server.programstate.relop;

import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorIncompatibleTypesException;
import gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;

/**
 * Base class for == and !=.
 * 
 * @author Alf
 * 
 */
public abstract class RelOpEqualBase extends RelOpComparableBase {

  @Override
  public boolean compare (StateReadableValueInterface left, StateReadableValueInterface right) throws JPFInspectorIncompatibleTypesException {

    // Common type checking
    assert (left != null);
    assert (right != null);

    // Update the type cache if needed
    validateCache(left.getInspector());

    // Check for possible type combinations
    ClassInfo leftCi = left.getClassInfo();
    ClassInfo rightCi = right.getClassInfo();

    // Compare booleans
    Boolean leftBool = tryConvertToBoolean(left);
    if (leftBool != null) {
      Boolean rightBool = tryConvertToBoolean(right);

      if (rightBool != null) {
        return compare(leftBool, rightBool);
      }

      throw new JPFInspectorIncompatibleTypesException(this, leftCi, rightCi);
    }

    // !!!This code in intentionally duplicated in the RelOpComparableBase.compare method.

    // Compare chars
    Character leftChar = tryConvertToChar(left);
    if (leftChar != null) {
      Character rightChar = tryConvertToChar(right);

      if (rightChar != null) {
        return compare(leftChar, rightChar);
      }

      throw new JPFInspectorIncompatibleTypesException(this, leftCi, rightCi);
    }

    // Compare Strings
    String leftString = tryConvertToString(left);
    if (leftString != null) {
      String rightString = tryConvertToString(right);

      if (rightString != null) {
        return compare(leftString, rightString);
      }

      throw new JPFInspectorIncompatibleTypesException(this, leftCi, rightCi);
    }

    // Comparison of floating point numbers
    Double leftDouble = tryConvertToDouble(left);
    if (leftDouble != null) {
      Double rightDouble = tryConvertToDouble(right);

      if (rightDouble != null) {
        return compare(leftDouble, rightDouble);
      }
      throw new JPFInspectorIncompatibleTypesException(this, leftCi, rightCi);
      // return false;
    }

    Long leftLong = tryConvertToLong(left);
    if (leftLong != null) {
      Long rightLong = tryConvertToLong(right);

      if (rightLong != null) {
        return compare(leftLong, rightLong);
      }
      throw new JPFInspectorIncompatibleTypesException(this, leftCi, rightCi);
      // return false;

    }
    // !!! End of duplicated code

    // Now we know that the left operator is not does not have a primitive type (or is boxed primitive type)
    // --> check references
    // Check references (not boxed)
    if (right.isReference() == false || ciCache.isBoxedPrimitiveType(rightCi) == true || ciCache.ci_String.equals(rightCi)) {
      throw new JPFInspectorIncompatibleTypesException(this, leftCi, rightCi);
    }

    return compare(left.getReferenceValue(), right.getReferenceValue());
  }

  public abstract boolean compare (boolean left, boolean right);

  // Compare references
  public abstract boolean compare (ElementInfo left, ElementInfo right);

  @Override
  public abstract boolean compare (char left, char right);

  @Override
  public abstract boolean compare (String left, String right);

  @Override
  public abstract boolean compare (double left, double right);

  @Override
  public abstract boolean compare (long left, long right);

  @Override
  public abstract String getNormalizedText ();

  /**
   * @param value
   * @return Get null, if value does not represent bool value, otherwise gets operand value converted to Boolean.
   */
  protected static Boolean tryConvertToBoolean (StateReadableValueInterface value) {

    ClassInfo valueCi = value.getClassInfo();
    if (ciCache.ci_boolean.equals(valueCi) || ciCache.ci_Boolean.equals(valueCi)) {
      Object o = value.getValue();
      assert (o != null);
      assert (o instanceof Boolean);
      return (Boolean) o;
    }

    return null;
  }

}
