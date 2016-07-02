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

package gov.nasa.jpf.inspector.server.programstate.relop;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorIncompatibleTypesException;
import gov.nasa.jpf.inspector.server.programstate.StateReadableValue;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;

/**
 * Base class for == and !=.
 * 
 * @author Alf
 * 
 */
abstract class RelOpEqualBase extends RelOpComparableBase {

  @Override
  public boolean compare (StateReadableValue left, StateReadableValue right) throws JPFInspectorIncompatibleTypesException {

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

  protected abstract boolean compare(boolean left, boolean right);

  protected abstract boolean compare(ElementInfo left, ElementInfo right);

  /**
   * @return Get null, if value does not represent bool value, otherwise gets operand value converted to Boolean.
   */
  private static Boolean tryConvertToBoolean(StateReadableValue value) {

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
