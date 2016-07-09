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

import gov.nasa.jpf.JPFException;
import gov.nasa.jpf.inspector.exceptions.*;
import gov.nasa.jpf.inspector.interfaces.attributes.StringToAttributeConverter;
import gov.nasa.jpf.inspector.server.attributes.AttributesManager;
import gov.nasa.jpf.inspector.server.attributes.attachments.AttributeAttachment;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.utils.ClassInfoCache;
import gov.nasa.jpf.vm.*;

/**
 * Represents a hierarchy-2 l-value, i.e. it is a value (and not a thread, heap entry list or a stack frame), and it
 * is assignable (i.e. it is not a constant).
 */
public abstract class StateWritableValue extends StateReadableValue {
  private static ClassInfoCache ciCache;

  /**
   * The Java type of this value.
   */
  protected final ClassInfo ci;

  protected StateWritableValue(StateNodeInterface sni, boolean expandMembers, ClassInfo ci, String stateExpression) {
    super(sni.getInspector(), expandMembers);

    setStateExpr(stateExpression);

    this.ci = ci;

    VM currentVM = sni.getInspector().getJPF().getVM();
    if (ciCache == null || ciCache.cacheValid(currentVM) == false) {
      ciCache = new ClassInfoCache(currentVM);
    }
  }

  /**
   * Construction used to create different view on the same instance.
   * Checks if used specified class is supper type of the original type
   * 
   * Should be used only to implement "this, "super", or "overcasting" purposes.
   * 
   * @throws JPFInspectorNotSuperClassException
   */
  protected StateWritableValue(StateWritableValue me, ClassInfo superClassInfo, String stateExpression) throws JPFInspectorNotSuperClassException {
    super(me.getInspector(), me.shouldExpandMembers());
    setStateExpr(stateExpression);

    this.ci = superClassInfo;

    assert (this.ci != null);
    if (!StateWritableValue.isPredecessor(this.ci, me.ci)) {
      throw new JPFInspectorNotSuperClassException(superClassInfo, me.ci);
    }
  }

  @Override
  public ClassInfo getClassInfo () {
    return ci;
  }

  @Override
  public boolean isReference () {
    return !ci.isPrimitive(); // All other types (arrays, objects) are stored on the heap
  }

  protected ElementInfo getReferenceValueImpl(int ref) {
    if (isReference() == false) {
      return null;
    }

    if (ref == MJIEnv.NULL) {
      return null;
    }

    InspectorState inspState = getInspector().getStopHolder().getInspectorState();
    Heap heap = inspState.getVM().getHeap();
    return heap.get(ref);
  }

  // *************************************************************************
  // ** Modify represented value - infrastructure
  // *************************************************************************

  /**
   * Assigns a new value to the entry represented by this instance.
   * @param valueToAssign New value which should be assigned to this instance. Cannot be null.
   */
  public void assignValue (StateReadableValue valueToAssign) throws JPFInspectorException {

    assert (valueToAssign != null);

    final ClassInfo newValCI = valueToAssign.getClassInfo();
    Object wrappedValue = valueToAssign.getValue();

    // Check according type of the represented value
    // Primitive types
    if (ciCache.ci_boolean.equals(ci)) {

      // Can be used assignValueBoolean()
      Boolean newVal = null;

      // Check if assigned type is the same/compatible
      if (ciCache.ci_boolean.equals(newValCI)) {
        assert (wrappedValue instanceof Boolean);
        newVal = (Boolean) wrappedValue;
      }

      if (ciCache.ci_Boolean.equals(newValCI)) {
        if (wrappedValue == null) {
          throw new JPFInspectorNullValueException(newValCI);
        }
        assert (wrappedValue instanceof ElementInfo);
        ElementInfo eiNewVal = (ElementInfo) wrappedValue;
        newVal = eiNewVal.getBooleanField(ciCache.fi_val_Boolean);
      }

      if (newVal != null) {
        assignValueBoolean(newVal);
        return;
      }
    }

    if (ciCache.ci_char.equals(ci)) {

      // Can be used assignValueBoolean()
      Character newVal = null;

      // Check if assigned type is the same/compatible
      if (ciCache.ci_char.equals(newValCI)) {
        assert (wrappedValue instanceof Character);
        newVal = (Character) wrappedValue;
      }

      if (ciCache.ci_Char.equals(newValCI)) {
        if (wrappedValue == null) {
          throw new JPFInspectorNullValueException(newValCI);
        }
        assert (wrappedValue instanceof ElementInfo);
        ElementInfo eiNewVal = (ElementInfo) wrappedValue;
        newVal = eiNewVal.getCharField(ciCache.fi_val_Char);
      }

      if (newVal != null) {
        assignValueChar(newVal);
        return;
      }

    }

    // Process integer values (dynamic type overcasting long->byte, ...)
    if (ciCache.ci_byte.equals(ci) || ciCache.ci_short.equals(ci) || ciCache.ci_int.equals(ci) || ciCache.ci_long.equals(ci)) {

      Long newVal = null;

      // Check if assigned type is the same/compatible
      if (ciCache.ci_byte.equals(newValCI)) {
        assert (wrappedValue instanceof Byte);
        newVal = ((Byte) wrappedValue).longValue();
      }

      if (ciCache.ci_Byte.equals(newValCI)) {
        if (wrappedValue == null) {
          throw new JPFInspectorNullValueException(newValCI);
        }
        assert (wrappedValue instanceof ElementInfo);
        ElementInfo eiNewVal = (ElementInfo) wrappedValue;
        byte newByteVal = eiNewVal.getByteField(ciCache.fi_val_Byte);
        newVal = (long) newByteVal;
      }

      if (ciCache.ci_short.equals(newValCI)) {
        assert (wrappedValue instanceof Short);
        newVal = ((Short) wrappedValue).longValue();
      }

      if (ciCache.ci_Short.equals(newValCI)) {
        if (wrappedValue == null) {
          throw new JPFInspectorNullValueException(newValCI);
        }
        assert (wrappedValue instanceof ElementInfo);
        ElementInfo eiNewVal = (ElementInfo) wrappedValue;
        short newShortVal = eiNewVal.getShortField(ciCache.fi_val_Short);
        newVal = (long) newShortVal;
      }

      if (ciCache.ci_int.equals(newValCI)) {
        assert (wrappedValue instanceof Integer);
        newVal = ((Integer) wrappedValue).longValue();
      }

      if (ciCache.ci_Int.equals(newValCI)) {
        if (wrappedValue == null) {
          throw new JPFInspectorNullValueException(newValCI);
        }
        assert (wrappedValue instanceof ElementInfo);
        ElementInfo eiNewVal = (ElementInfo) wrappedValue;
        int newIntVal = eiNewVal.getIntField(ciCache.fi_val_Int);
        newVal = (long) newIntVal;
      }

      if (ciCache.ci_long.equals(newValCI)) {
        assert (wrappedValue instanceof Long);
        newVal = (Long) wrappedValue;
      }

      if (ciCache.ci_Long.equals(newValCI)) {
        if (wrappedValue == null) {
          throw new JPFInspectorNullValueException(newValCI);
        }
        assert (wrappedValue instanceof ElementInfo);
        ElementInfo eiNewVal = (ElementInfo) wrappedValue;
        newVal = eiNewVal.getLongField(ciCache.fi_val_Long);
      }

      // Assignments part
      if (newVal != null) {
        if (ciCache.ci_byte.equals(ci)) {
          // Check if in range
          if (newVal < Byte.MIN_VALUE || newVal > Byte.MAX_VALUE) {
            throw new JPFInspectorAssignmentOutOfRangeException(newVal, ci.getName());
          }

          assignValueByte(newVal.byteValue());
        }

        if (ciCache.ci_short.equals(ci)) {
          // Check if in range
          if (newVal < Short.MIN_VALUE || newVal > Short.MAX_VALUE) {
            throw new JPFInspectorAssignmentOutOfRangeException(newVal, ci.getName());
          }

          assignValueShort(newVal.shortValue());
        }

        if (ciCache.ci_int.equals(ci)) {
          // Check if in range
          if (newVal < Integer.MIN_VALUE || newVal > Integer.MAX_VALUE) {
            throw new JPFInspectorAssignmentOutOfRangeException(newVal, ci.getName());
          }

          assignValueInt(newVal.intValue());
        }

        if (ciCache.ci_long.equals(ci)) {
          assignValueLong(newVal);
        }

        return;
      }

    } // End of integer

    if (ciCache.ci_float.equals(ci)) {

      // Can be used assignValueBoolean()
      Float newVal = null;

      // Check if assigned type is the same/compatible
      if (ciCache.ci_float.equals(newValCI)) {
        assert (wrappedValue instanceof Float);
        newVal = (Float) wrappedValue;
      }

      if (ciCache.ci_Float.equals(newValCI)) {
        if (wrappedValue == null) {
          throw new JPFInspectorNullValueException(newValCI);
        }
        assert (wrappedValue instanceof ElementInfo);
        ElementInfo eiNewVal = (ElementInfo) wrappedValue;
        newVal = eiNewVal.getFloatField(ciCache.fi_val_Float);
      }
      if (ciCache.ci_double.equals(newValCI)) {
        assert wrappedValue instanceof Double;
        Double newDoubleValue = (Double)wrappedValue;
        if (newDoubleValue.isNaN()) {
          newVal = Float.NaN;
        } else if (newDoubleValue == Double.POSITIVE_INFINITY) {
          newVal = Float.POSITIVE_INFINITY;
        } else if (newDoubleValue == Double.NEGATIVE_INFINITY) {
          newVal = Float.NEGATIVE_INFINITY;
        }
      }

      if (newVal != null) {
        assignValueFloat(newVal);
        return;
      }

    }

    if (ciCache.ci_double.equals(ci)) {

      // Can be used assignValueBoolean()
      Double newVal = null;

      // Check if assigned type is the same/compatible
      if (ciCache.ci_double.equals(newValCI)) {
        assert (wrappedValue instanceof Double);
        newVal = (Double) wrappedValue;
      }

      if (ciCache.ci_Double.equals(newValCI)) {
        if (wrappedValue == null) {
          throw new JPFInspectorNullValueException(newValCI);
        }
        assert (wrappedValue instanceof ElementInfo);
        ElementInfo eiNewVal = (ElementInfo) wrappedValue;
        newVal = eiNewVal.getDoubleField(ciCache.fi_val_Double);
      }

      // Overcasting (lossless) Float -> Double
      if (ciCache.ci_float.equals(newValCI)) {
        assert (wrappedValue instanceof Float);
        newVal = ((Float) wrappedValue).doubleValue();
      }

      if (ciCache.ci_Float.equals(newValCI)) {
        if (wrappedValue == null) {
          throw new JPFInspectorNullValueException(newValCI);
        }
        assert (wrappedValue instanceof ElementInfo);
        ElementInfo eiNewVal = (ElementInfo) wrappedValue;
        float val = eiNewVal.getFloatField(ciCache.fi_val_Float);
        newVal = (double) val;
      }

      if (newVal != null) {
        assignValueDouble(newVal);
        return;
      }

    }

    // References
    if (this.isReference()) {
      if (valueToAssign.isReference()) {

        if (wrappedValue == null) {
          assignValueRef(MJIEnv.NULL);
          return;

        } else if (newValCI.isInstanceOf(ci)) {
          assert (wrappedValue instanceof ElementInfo);
          ElementInfo eiNewVal = (ElementInfo) wrappedValue;
          assignValueRef(eiNewVal.getObjectRef());
          return;
        } else {
          // Incompatible types
        }

      }
    }

    throw new JPFInspectorIncompatibleTypesException(ci, newValCI);
  }

  // Note type and range checking is done in the assignValue method
  // Simple setters which should not cause any error
  // if error takes place in these methods, then it is implementations error
  protected abstract void assignValueBoolean (boolean newVal);

  protected abstract void assignValueChar (char newVal);

  protected abstract void assignValueByte (byte newVal);

  protected abstract void assignValueShort (short newVal);

  protected abstract void assignValueInt (int newVal);

  protected abstract void assignValueLong (long newVal);

  protected abstract void assignValueFloat (float newVal);

  protected abstract void assignValueDouble (double newVal);

  protected abstract void assignValueRef (int newValRef);

  // *************************************************************************
  // ** Helper static methods
  // *************************************************************************

  /**
   * Demangles type names from I, [Ljava.lang.Object; into user readable format.
   * 
   * <br>Note: In contrast to {@link Types#getTypeName(String)} handled incorrect type names like '?'
   * 
   * @param typeNameSignature Name of the type in the JVM internal format - "I", "[Lx/Y;" etc.
   * @return Human readable form of the type name.
   */
  public static String demangleTypeName (String typeNameSignature) {
    if (typeNameSignature == null) {
      return null;
    }
    // Ugly hack for "?" typeNames (they always causes JPFException)
    if ("?".equals(typeNameSignature)) {
      return "?";
    }
    try {
      return Types.getTypeName(typeNameSignature);
    } catch (JPFException e) {
      // Invalid type name
      return "???";
    }
  }

  public static String getSimpleMethodName (MethodInfo mi) {
    if (mi == null) {
      return "???";
    }

    ClassInfo ci = mi.getClassInfo();
    if (ci == null) {
      // Inspired by MethodInfo.getClassName()
      return "[VM]." + mi.getName();

    }
    return getSimpleName(ci) + "." + mi.getName();
  }

  public static String getFullClassName (ClassInfo ci) {
    if (ci == null) {
      return "???";
    }
    return demangleTypeName(ci.getSignature());
  }

  public static String getSimpleName (ClassInfo ci) {
    if (ci == null) {
      return "???";
    }
    return getSimpleName(ci.getSignature());
  }

  private static String getSimpleName(String typeNameSignature) {
    String fullName = demangleTypeName(typeNameSignature);

    int lastDot = fullName.lastIndexOf('.');
    int lastDolar = fullName.lastIndexOf('$');

    int max = Math.max(lastDot, lastDolar);
    if ((max > 0) && (max < (fullName.length() - 1))) {
      fullName = fullName.substring(max + 1);
    }
    return fullName;
  }


  /**
   * @return Gets true is testedCi type is predecessor (superType) is the representedValue type.
   */
  public static boolean isPredecessor (ClassInfo testedCi, ClassInfo representedValue) {
    while (representedValue != null) {
      if (representedValue.equals(testedCi)) {
        return true;
      }
      representedValue = representedValue.getSuperClass();
    }
    return false;
  }

  /**
   * @return Gets true if represented value has entry with given name.
   */
  public static boolean hasNamedEntry (StateReadableValue srvi, String varName) {
    assert (srvi != null);
    assert (varName != null);

    ClassInfo ci = srvi.getClassInfo();
    assert (ci != null);

    return (ci.getInstanceField(varName) != null) || (ci.getStaticField(varName) != null);
  }

  public static boolean isStaticElementInfo (ElementInfo ei) {
    if (ei == null) {
      return false;
    }

    // return (ei instanceof StaticElementInfo);

    ClassInfo ci = ei.getClassInfo();
    StaticElementInfo sei = ci.getStaticElementInfo();

    return ei.equals(sei);
  }

  public abstract AttributeAttachment getAttributeAttachment();
}
