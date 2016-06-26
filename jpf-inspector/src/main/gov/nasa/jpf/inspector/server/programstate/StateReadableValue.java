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

import gov.nasa.jpf.inspector.common.pse.*;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorNotSuperClassException;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.vm.*;

import java.util.Arrays;

/**
 * Holds operation needed for reading the value.
 * 
 * Note: Not direct part of the {@link StateWritableValue} class,
 * because it is necessary to distinguish among writeable values,
 * (which represents {@link StateWritableValue}) and the read only instances which holds value
 * {#heap[1]} entries ClassName #className[]
 * 
 * @author Alf
 * 
 */
public abstract class StateReadableValue extends StateNode {

  private boolean shouldExpandMembers;

  protected StateReadableValue(JPFInspector inspector, boolean shouldExpandMembers) {
    super(inspector);
    this.shouldExpandMembers = shouldExpandMembers;
  }

  public static String elementInfo2String(ElementInfo ei) {
    if (ei == null) {
      return "null";
    }

    if (ei.isArray()) {
      // Note: Bad code practice - alternative way,
      // Create Decorating factory, decorators with used defined toSting() methods
      // Update configuration- "vm.fields_factory.class

      ClassInfo elemClassInfo = ei.getClassInfo().getComponentClassInfo();
      assert  elemClassInfo != null;
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
      if (elemSignature != null && elemSignature.startsWith("L") || elemClassInfo.isArray()) {
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
            ElementInfo eiArr = VM.getVM().getHeap().get(refArr[i]);
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

    // Normally, we would have ElementInfo.toString here. However, we want to display the heap index
    // as a decimal value, not hexadecimal.
    //     return ((ci != null ? ci.getName() : "ElementInfo") + '@' + Integer.toHexString(objRef));
    String className =  (ei.getClassInfo()!= null ? ei.getClassInfo().getName() : "ElementInfo");
    String heapIndex = Integer.toString(ei.getObjectRef(), 10);
    return className + "@" + heapIndex;
  }
  /**
   * Creates a hierarchy-3 representation of a value.
   *
   * @param value A hierarchy-2 representation of the value.
   * @param varName Name of the variable that contains the value.
   * @param index Index (stack slot, array index, field index or heap index), if any. If not, then zero.
   * @param definedIn The class this field was defined in, if field, if any. If not, then it's the empty string.
   * @return The hierarchy-3 representation.
   */
  public static PSEVariable createPSEVariable(StateReadableValue value,
                                              String varName,
                                              int index,
                                              String definedIn)
      throws JPFInspectorException {

    assert (value != null);
    assert (definedIn != null) : "Use the empty string rather than null.";
    assert (varName != null) : "Use an arbirtrary string (such as ???) rather than null.";

    ClassInfo ci = value.getClassInfo();

    String varTypeName = StateWritableValue.demangleTypeName(ci.getSignature());

    // Primitive
    if (ci.isPrimitive()) {
      Object wrappedValue = value.getValue();
      return new PSEVariablePrimitive(varName, varTypeName, wrappedValue.toString(), false,
                                      definedIn, index, wrappedValue);
    }

    final ElementInfo ei = value.getReferenceValue(); // may be null
    final String varValue = elementInfo2String(ei); // short-form description of the value
    final boolean shouldExpandMembers = value.shouldExpandMembers();

    final boolean isStatic = StateWritableValue.isStaticElementInfo(ei);

    if (ci.isArray()) {
      // It's an array.

      if (ei == null) {
        // It's a null value.
        return new PSEVariableObject(varName, varTypeName, varValue, false, definedIn, index, new PSEVariable[0], new PSEVariable[0]);
      } else if (shouldExpandMembers) {
        int arrayLen;
        PSEVariable[] refArrayItems;
        arrayLen = ei.arrayLength();
        refArrayItems = new PSEVariable[arrayLen];
        for (int i = 0; i < arrayLen; i++) {
          StateValueArrayElement svae = StateValueArrayElement.createArrayElement(value, i, false);
          refArrayItems[i] = svae.toHierarchy3();
        }
        return new PSEVariableArray(varName, varTypeName, varValue, false, definedIn, index, arrayLen, refArrayItems);
      } else {
        return new PSEVariableShortForm(varName, varTypeName, varValue, false, definedIn, index);

      }
    } else {
      // It's a reference object.

      final int fields = ci.getNumberOfInstanceFields();
      final int staticFields = ci.getNumberOfStaticFields();

      if (ei == null) {
        // It's a null value.
        return new PSEVariableObject(varName, varTypeName, "null",
                                     false, definedIn, index, new PSEVariable[0],
                                     new PSEVariable[0]);
      }
      else if (shouldExpandMembers) {
        // Not null and values of all fields are required
        PSEVariable[] refFields;
        PSEVariable[] refStaticFields;

        if (isStatic == false) {
          refFields = new PSEVariable[fields];

          for (int i = 0; i < ci.getNumberOfInstanceFields(); i++) {
            StateValueElementInfoField svae = StateValueElementInfoField.createFieldFromIndex(value,
                                                                                              i,
                                                                                              false);
            refFields[i] = svae.toHierarchy3();
          }
        } else {
          refFields = new PSEVariable[0];
        }
        // Create static fields
        refStaticFields = new PSEVariable[staticFields];
        for (int i = 0; i < staticFields; i++) {
          StateValueElementInfoField svae = StateValueElementInfoField.createStaticFieldFromIndex(value,
                                                                                                  i,
                                                                                                  false);
          refStaticFields[i] = svae.toHierarchy3();
        }
        return new PSEVariableObject(varName, varTypeName, varValue,
                                     false, definedIn, index, refFields,
                                     refStaticFields);
      } else {
        return new PSEVariableShortForm(varName, varTypeName, varValue, false, definedIn, index);
      }
    }
  }

  /**
   * Returns true if, when the this instance's {@link #toHierarchy3()} method is called, the resulting
   * {@link ProgramStateEntry} should contain its fields and static fields (if object) or its elements (if array).
   * The return value of this method has no meaning for primitive types, threads, and stack frames.
   */
  public boolean shouldExpandMembers() {
    return shouldExpandMembers;
  }

  /**
   * Gets type information about represented value.
   */
  public abstract ClassInfo getClassInfo();

  /**
   * @return Gets representation of the readable value.
   *         For primitive types gets wrapped version of the primitive type.
   *         For objects (references and arrays) gets ElementInfo.
   */
  public abstract Object getValue();

  /**
   * Indicates whether the represented object is a reference or an array and thus is represented by an {@link ElementInfo}.
   */
  public abstract boolean isReference();

  /**
   * If {@link #isReference()}  would return true, this returns the representation of referenced value.
   * Returns null otherwise or if the reference value itself is null.
   */
  public abstract ElementInfo getReferenceValue();

  /**
   * Limits accessible fields only to from the given ClassInfo.
   * (useful for access to hidden/overriden fields)
   * 
   * @return Gets value which represents only a subset of fields.
   */
  public abstract StateReadableValue createSuper() throws JPFInspectorException;


  public abstract StateReadableValue createPredecessorClass(ClassInfo ci) throws JPFInspectorNotSuperClassException;

  /**
   * Gets value that represents this instance. It means returns same "Value", but with different stateExpression. If not an instance (static,primitive type,
   * array) then throws exception)
   * 
   * @return Gets value that represents this instance.
   * @throws JPFInspectorException If not an instance (static,primitive type, array)
   */
  public abstract StateReadableValue createThisValue() throws JPFInspectorException;

}
