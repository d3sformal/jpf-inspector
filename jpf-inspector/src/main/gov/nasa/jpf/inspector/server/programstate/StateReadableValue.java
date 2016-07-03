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

  private final boolean shouldExpandMembers;

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
  protected static PSEVariable createPSEVariable(StateReadableValue value,
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
      return new PSEVariablePrimitive(varName, varTypeName, wrappedValue.toString(),
                                      index, wrappedValue);
    }

    final ElementInfo ei = value.getReferenceValue(); // may be null
    final String varValue = elementInfo2String(ei); // short-form description of the value
    final boolean shouldExpandMembers = value.shouldExpandMembers();

    final boolean isStatic = StateWritableValue.isStaticElementInfo(ei);

    if (ci.isArray()) {
      // It's an array.

      if (ei == null) {
        // It's a null value.
        return new PSEVariableObject(varName, varTypeName, varValue, index, new PSEVariable[0], new PSEVariable[0]);
      } else if (shouldExpandMembers) {
        int arrayLen;
        PSEVariable[] refArrayItems;
        arrayLen = ei.arrayLength();
        refArrayItems = new PSEVariable[arrayLen];
        for (int i = 0; i < arrayLen; i++) {
          StateValueArrayElement svae = StateValueArrayElement.createArrayElement(value, i, false);
          refArrayItems[i] = svae.toHierarchy3();
        }
        return new PSEVariableArray(varName, varTypeName, varValue, index, arrayLen, refArrayItems);
      } else {
        return new PSEVariableShortForm(varName, varTypeName, varValue, index);

      }
    } else {
      // It's a reference object.

      final int fields = ci.getNumberOfInstanceFields();
      final int staticFields = ci.getNumberOfStaticFields();

      if (ei == null) {
        // It's a null value.
        return new PSEVariableObject(varName, varTypeName, "null",
                                     index, new PSEVariable[0],
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
                                     index, refFields,
                                     refStaticFields);
      } else {
        return new PSEVariableShortForm(varName, varTypeName, varValue, index);
      }
    }
  }

  /**
   * Returns true if, when the this instance's {@link #toHierarchy3()} method is called, the resulting
   * {@link ProgramStateEntry} should contain its fields and static fields (if object) or its elements (if array).
   * The return value of this method has no meaning for primitive types, threads, and stack frames.
   */
  protected boolean shouldExpandMembers() {
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
