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

package gov.nasa.jpf.inspector.server.programstate;

import gov.nasa.jpf.JPFException;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorException;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorAssignmentOutOfRangeException;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorIncompatibleTypesException;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorNotSuperClassException;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorNullValueException;
import gov.nasa.jpf.inspector.server.expression.InspectorState;
import gov.nasa.jpf.inspector.server.programstate.client.PSEVariable;
import gov.nasa.jpf.inspector.server.programstate.client.PSEVariableArray;
import gov.nasa.jpf.inspector.server.programstate.client.PSEVariableObject;
import gov.nasa.jpf.inspector.server.programstate.client.PSEVariablePrimitive;
import gov.nasa.jpf.inspector.utils.ClassInfoCache;
import gov.nasa.jpf.jvm.ClassInfo;
import gov.nasa.jpf.jvm.DynamicElementInfo;
import gov.nasa.jpf.jvm.ElementInfo;
import gov.nasa.jpf.jvm.Heap;
import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.MJIEnv;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.StaticElementInfo;
import gov.nasa.jpf.jvm.Types;

import java.util.Arrays;

/**
 * @author Alf
 * 
 */
public abstract class StateValue extends StateNode implements StateReadableValueInterface {

  static ClassInfoCache ciCache;

  protected final ClassInfo ci;

  protected StateValue (StateNodeInterface sni, int referenceDepth, ClassInfo ci, String stateExpression) {
    super(sni, referenceDepth);

    setStateExpr(stateExpression);

    this.ci = ci;

    JVM currentVM = sni.getInspector().getJPF().getVM();
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
  protected StateValue (StateValue me, ClassInfo superClassInfo, String stateExpression) throws JPFInspectorNotSuperClassException {
    super(me, me.getReferenceDepth());
    setStateExpr(stateExpression);

    this.ci = superClassInfo;

    assert (this.ci != null);
    if (!StateValue.isPredecessor(this.ci, me.ci)) {
      throw new JPFInspectorNotSuperClassException(superClassInfo, me.ci);
    }
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#getClassInfo() */
  @Override
  public ClassInfo getClassInfo () {
    return ci;
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#isReference() */
  @Override
  public boolean isReference () {
    return !ci.isPrimitive(); // All other types (arrays, objects) are stored on the heap
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface#getReferenceValue() */
  public ElementInfo getReferenceValueImpl (int ref) {
    if (isReference() == false) {
      return null;
    }

    if (ref == MJIEnv.NULL) {
      return null;
    }

    InspectorState inspState = getInspector().getStopHolder().getInspectorState();
    Heap heap = inspState.getJVM().getHeap();
    return heap.get(ref);
  }

  // *************************************************************************
  // ** Modify represented value - infrastructure
  // *************************************************************************

  /**
   * Assigns new value to represented entry.
   * 
   * @param valueToAssign New value which should be assigned. Cannot be null.
   */
  public void assignValue (StateReadableValueInterface valueToAssign) throws JPFInspectorException {

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
        newVal = Long.valueOf(((Byte) wrappedValue).longValue());
      }

      if (ciCache.ci_Byte.equals(newValCI)) {
        if (wrappedValue == null) {
          throw new JPFInspectorNullValueException(newValCI);
        }
        assert (wrappedValue instanceof ElementInfo);
        ElementInfo eiNewVal = (ElementInfo) wrappedValue;
        byte newByteVal = eiNewVal.getByteField(ciCache.fi_val_Byte);
        newVal = Long.valueOf(newByteVal);
      }

      if (ciCache.ci_short.equals(newValCI)) {
        assert (wrappedValue instanceof Short);
        newVal = Long.valueOf(((Short) wrappedValue).longValue());
      }

      if (ciCache.ci_Short.equals(newValCI)) {
        if (wrappedValue == null) {
          throw new JPFInspectorNullValueException(newValCI);
        }
        assert (wrappedValue instanceof ElementInfo);
        ElementInfo eiNewVal = (ElementInfo) wrappedValue;
        short newShortVal = eiNewVal.getShortField(ciCache.fi_val_Short);
        newVal = Long.valueOf(newShortVal);
      }

      if (ciCache.ci_int.equals(newValCI)) {
        assert (wrappedValue instanceof Integer);
        newVal = Long.valueOf(((Integer) wrappedValue).longValue());
      }

      if (ciCache.ci_Int.equals(newValCI)) {
        if (wrappedValue == null) {
          throw new JPFInspectorNullValueException(newValCI);
        }
        assert (wrappedValue instanceof ElementInfo);
        ElementInfo eiNewVal = (ElementInfo) wrappedValue;
        int newIntVal = eiNewVal.getIntField(ciCache.fi_val_Int);
        newVal = Long.valueOf(newIntVal);
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
          if (newVal < Byte.MIN_VALUE || Byte.MAX_VALUE < newVal) {
            throw new JPFInspectorAssignmentOutOfRangeException(newVal, ci.getName());
          }

          assignValueByte(newVal.byteValue());
        }

        if (ciCache.ci_short.equals(ci)) {
          // Check if in range
          if (newVal < Short.MIN_VALUE || Short.MAX_VALUE < newVal) {
            throw new JPFInspectorAssignmentOutOfRangeException(newVal, ci.getName());
          }

          assignValueShort(newVal.shortValue());
        }

        if (ciCache.ci_int.equals(ci)) {
          // Check if in range
          if (newVal < Integer.MIN_VALUE || Integer.MAX_VALUE < newVal) {
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
        newVal = Double.valueOf(val);
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
  static public String demangleTypeName (String typeNameSignature) {
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

  static public String getSimpleMethodName (MethodInfo mi) {
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

  static public String getFullClassName (ClassInfo ci) {
    if (ci == null) {
      return "???";
    }
    return demangleTypeName(ci.getSignature());
  }

  static public String getSimpleName (ClassInfo ci) {
    if (ci == null) {
      return "???";
    }
    return getSimpleName(ci.getSignature());
  }

  static public String getSimpleName (String typeNameSignature) {
    String fullName = demangleTypeName(typeNameSignature);

    int lastDot = fullName.lastIndexOf('.');
    int lastDolar = fullName.lastIndexOf('$');

    int max = Math.max(lastDot, lastDolar);
    if ((max > 0) && (max < (fullName.length() - 1))) {
      fullName = fullName.substring(max + 1);
    }
    return fullName;
  }

  static public String elementInfo2String (ElementInfo ei) {
    if (ei == null) {
      return "null";
    }

    if (ei.isArray()) {
      // Note: Bad code practice - alternative way,
      // Create Decorating factory, decorators with used defined toSting() methods
      // Update configuration- "vm.fields_factory.class

      ClassInfo elemClassInfo = ei.getClassInfo().getComponentClassInfo();
      String elemSignature = elemClassInfo.getSignature();

      if ("Z".equals(elemSignature)) {
        return Arrays.toString(ei.asBooleanArray());
      } else if ("B".equals(elemSignature)) {
        return Arrays.toString(ei.asByteArray());
      } else if ("C".equals(elemSignature)) {
        return Arrays.toString(ei.asCharArray());
      } else if ("S".equals(elemSignature)) {
        return Arrays.toString(ei.asShortArray());
      } else if ("I".equals(elemSignature)) {
        return Arrays.toString(ei.asIntArray());
      } else if ("J".equals(elemSignature)) {
        return Arrays.toString(ei.asLongArray());
      } else if ("F".equals(elemSignature)) {
        return Arrays.toString(ei.asFloatArray());
      } else if ("D".equals(elemSignature)) {
        return Arrays.toString(ei.asDoubleArray());
      }
      if ((elemSignature != null && elemSignature.startsWith("L")) || (elemClassInfo != null && elemClassInfo.isArray())) {
        int[] refArr = ei.asReferenceArray();
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < refArr.length; i++) {
          if (i > 0) {
            sb.append(", ");
          }
          if (refArr[i] == MJIEnv.NULL) {
            sb.append("null");
          } else {
            // value stored in array at index i
            ElementInfo eiArr = JVM.getVM().getHeap().get(refArr[i]);
            assert (eiArr != null);

            sb.append(elementInfo2String(eiArr));
          }
        }
        sb.append(']');
        return sb.toString();
      } else {
        throw new RuntimeException("Invalid signature " + elemSignature);
      }
    }
    if (ClassInfo.isStringClassInfo(ei.getClassInfo()) && (ei instanceof DynamicElementInfo)) {
      DynamicElementInfo dei = (DynamicElementInfo) ei;
      return '\"' + dei.asString() + '\"';
    }

    return ei.toString();
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

  public static PSEVariable createPSEVariable (StateReadableValueInterface srvi, String name, int clientID, String varName, int index, String definedIn)
      throws JPFInspectorException {
    assert (srvi != null);

    ClassInfo ci = srvi.getClassInfo();

    String varTypeName = StateValue.demangleTypeName(ci.getSignature());

    if (ci.isPrimitive()) {
      Object wrappedValue = srvi.getValue();
      return new PSEVariablePrimitive(name, clientID, srvi, varName, varTypeName, wrappedValue.toString(), false, definedIn, index, wrappedValue);
    }

    final ElementInfo ei = srvi.getReferenceValue();
    final String varValue = StateValue.elementInfo2String(ei);
    final int referenceDepth = srvi.getReferenceDepth();

    final boolean isStatic = isStaticElementInfo(ei);

    if (ci.isArray()) {

      int arrayLen = 0;
      PSEVariable[] refArrayItems = null;

      if (ei != null) {
        // ei == null -> means null reference
        arrayLen = ei.arrayLength();

        if (referenceDepth > 0) {
          refArrayItems = new PSEVariable[arrayLen];
          for (int i = 0; i < arrayLen; i++) {
            StateValueArrayElement svae = StateValueArrayElement.createArrayElement(srvi, i, referenceDepth - 1);
            refArrayItems[i] = svae.getResultExpression(name, clientID);
          }
        }
      }
      return new PSEVariableArray(name, clientID, srvi, varName, varTypeName, varValue, false, definedIn, index, arrayLen, refArrayItems);
    } // End of array

    { // Object
      final int fields = ci.getNumberOfInstanceFields();
      final int staticFields = ci.getNumberOfStaticFields();
      PSEVariable[] refFields = null;
      PSEVariable[] refStaticFields = null;
      if (referenceDepth > 0 && ei != null) {
        // Not null and values of all fields are required

        if (isStatic == false) {
          refFields = new PSEVariable[fields];

          for (int i = 0; i < ci.getNumberOfInstanceFields(); i++) {
            StateValueElementInfoField svae = StateValueElementInfoField.createFieldFromIndex(srvi, i, referenceDepth - 1);
            refFields[i] = svae.getResultExpression(name, clientID);
          }
        } else {
          refFields = new PSEVariable[0];
        }
        // Create static fields
        refStaticFields = new PSEVariable[staticFields];
        for (int i = 0; i < staticFields; i++) {
          StateValueElementInfoField svae = StateValueElementInfoField.createStaticFieldFromIndex(srvi, i, referenceDepth - 1);
          refStaticFields[i] = svae.getResultExpression(name, clientID);
        }
      }

      return new PSEVariableObject(name, clientID, srvi, varName, varTypeName, varValue, false, definedIn, index, refFields, refStaticFields);
    }
  }

  /**
   * @return Gets true if represented value has entry with given name.
   */
  public static boolean hasNamedEntry (StateReadableValueInterface srvi, String varName) {
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

}
