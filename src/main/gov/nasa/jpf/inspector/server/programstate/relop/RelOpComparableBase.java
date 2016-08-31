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
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.server.programstate.StateReadableValue;
import gov.nasa.jpf.inspector.utils.ClassInfoCache;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.VM;

/**
 * Base class for <, <=, >, >= operators.
 * 
 * @author Alf
 * 
 */
abstract class RelOpComparableBase implements RelationOperator {

  protected static ClassInfoCache ciCache = null;

  // Checks if cache holds valid types
  protected static void validateCache (JPFInspector inspector) {
    // Update the type cache if needed
    assert (inspector.getJPF() != null); // Breakpoint cannot be evaluated if there is no JPF instance

    VM currentVM = inspector.getJPF().getVM();
    if (ciCache == null || ciCache.cacheValid(currentVM) == false) {
      ciCache = new ClassInfoCache(currentVM);
    }
  }

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

    // !!!This code in intentionally duplicated in the RelOpEqualBase.compare method.

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
    // Convert both to double and resent to child
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

    throw new JPFInspectorIncompatibleTypesException(this, leftCi, rightCi);
    // return false;
  }

  /**
   * Compares two doubles.
   */
  protected abstract boolean compare(double left, double right);

  /**
   * Compares two longs.
   */
  protected abstract boolean compare(long left, long right);

  /**
   * Compares two chars.
   */
  protected abstract boolean compare(char left, char right);

  /**
   * Compares two Strings.
   */
  protected abstract boolean compare(String left, String right);

  /**
   *
   * @return Get null, if value does not represent floating point number, otherwise gets operand value converted to double.
   */
  protected static Double tryConvertToDouble (StateReadableValue value) {

    ClassInfo valueCi = value.getClassInfo();
    if (ciCache.ci_float.equals(valueCi) || ciCache.ci_Float.equals(valueCi)) {
      Object o = value.getValue();
      assert (o != null);
      assert (o instanceof Float);
      Float f = (Float) o;
      return f.doubleValue();
    }

    if (ciCache.ci_double.equals(valueCi) || ciCache.ci_Double.equals(valueCi)) {
      Object o = value.getValue();
      assert (o != null);
      assert (o instanceof Double);
      return (Double) o;
    }

    return null;
  }

  /**
   *
   * @return Get null, if value does not represent integer type (byte, short, int, long), otherwise gets operand value converted to long.
   */
  protected static Long tryConvertToLong (StateReadableValue value) {

    ClassInfo valueCi = value.getClassInfo();

    if (ciCache.ci_byte.equals(valueCi) || ciCache.ci_Byte.equals(valueCi)) {
      Object o = value.getValue();
      assert (o != null);
      assert (o instanceof Byte);
      Byte b = (Byte) o;
      return b.longValue();
    }

    if (ciCache.ci_short.equals(valueCi) || ciCache.ci_Short.equals(valueCi)) {
      Object o = value.getValue();
      assert (o != null);
      assert (o instanceof Short);
      Short shortValue = (Short) o;
      return shortValue.longValue();
    }

    if (ciCache.ci_int.equals(valueCi) || ciCache.ci_Int.equals(valueCi)) {
      Object o = value.getValue();
      assert (o != null);
      assert (o instanceof Integer);
      Integer i = (Integer) o;
      return i.longValue();
    }

    if (ciCache.ci_long.equals(valueCi) || ciCache.ci_Long.equals(valueCi)) {
      Object o = value.getValue();
      assert (o != null);
      assert (o instanceof Long);
      return (Long) o;
    }

    return null;
  }

  /**
   * @return Get null, if value does not represent String value, otherwise gets operand value converted to String.
   */
  protected static String tryConvertToString (StateReadableValue value) {

    ClassInfo valueCi = value.getClassInfo();
    if (ciCache.ci_String.equals(valueCi)) {
      Object o = value.getValue();
      assert (o != null);
      assert (o instanceof String);
      return (String) o;
    }

    return null;
  }

  /**
   * @return Get null, if value does not represent character value, otherwise gets operand value converted to Character.
   */
  protected static Character tryConvertToChar (StateReadableValue value) {

    ClassInfo valueCi = value.getClassInfo();
    if (ciCache.ci_char.equals(valueCi) || ciCache.ci_Char.equals(valueCi)) {
      Object o = value.getValue();
      assert (o != null);
      assert (o instanceof Character);
      return (Character) o;
    }

    return null;
  }

}
