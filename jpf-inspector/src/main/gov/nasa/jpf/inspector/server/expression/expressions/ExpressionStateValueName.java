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

package gov.nasa.jpf.inspector.server.expression.expressions;

import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorInvalidNameException;
import gov.nasa.jpf.inspector.server.programstate.*;
import gov.nasa.jpf.vm.*;

/**
 * Represents a simple member access expression.
 *
 * Name resolution order: (After Stack frame)
 * 1) slot name (local variable or parameter) - if state frame is provided
 * 2) field name (declared in this class or static) / declared in predecessor, ... (if not a static method)
 * 3) TypeName if type is predecessor of current instance then "restricted view" mode on current instance, otherwise view on Static field of given class
 * 4) Error is reported
 * 
 * Name resolution order: (Deeper in the variable expression)
 * 2) field name
 * 3) Predecessor type name
 * 
 * @author Alf
 */
public class ExpressionStateValueName extends ExpressionStateValue {

  /**
   * Name of the member.
   */
  private final String varName;

  // Note TOKEN_NAN, TOKEN_INFINITY, TOKEN_NEGATIVE_INFINITY1 and TOKEN_POSITIVE_INFINITY1 are parsed
  // as idfField_name and ("special semantic hack") resolves this

  /**
   * Has to be equal to TOKEN_NAN
   */
  private static final String TOKEN_IDF_NAN = "NaN";
  /**
   * Has to be equal to TOKEN_INFINITY
   */
  private static final String TOKEN_IDF_INFINITY = "Infinity";

  /**
   * Has to be equal to TOKEN_NEGATIVE_INFINITY1
   */
  private static final String TOKEN_IDF_NEGATIVE_INFINITY1_FULL = "negative_infinity";
  /**
   * Has to be equal to TOKEN_NEGATIVE_INFINITY1
   */
  private static final String TOKEN_IDF_NEGATIVE_INFINITY1_SHORT = "neg_inf";
  /**
   * as to be equal to TOKEN_POSITIVE_INFINITY1
   */
  private static final String TOKEN_IDF_POSITIVE_INFINITY1_FULL = "positive_infinity";
  /**
   * Has to be equal to TOKEN_POSITIVE_INFINITY1
   */
  private static final String TOKEN_IDF_POSITIVE_INFINITY1_SHORT = "pos_inf";

  public ExpressionStateValueName (ExpressionStateValue child, String varName) {
    super(child);
    assert (varName != null);

    this.varName = varName;
  }

  @Override
  public StateReadableValue toHierarchy2(StateReadableValue parent) throws JPFInspectorException {
    assert (parent != null);

    StateReadableValue namedValue = null;

    if (StateWritableValue.hasNamedEntry(parent, varName)) {

      ClassInfo ci = parent.getClassInfo();
      FieldInfo fi = StateValueElementInfoField.fieldNameJavaBasedLookup(ci, varName);

      assert fi != null;
      if (fi.isStatic()) {
        namedValue = StateValueElementInfoField.createStaticNamedField(parent, varName);
      } else {
        namedValue = StateValueElementInfoField.createInstanceNamedField(parent, varName);
      }
    } else {
      // Try if not a predecessor class name (package is ignored)
      ClassInfo ci = StateElementInfo.isPredecessorTypeName(parent.getClassInfo(), varName);

      if (ci != null) {
        namedValue = parent.createPredecessorClass(ci);
      }
    }

    if (namedValue == null) {
      // Double hacks.
      namedValue = tryApplyDoubleHacks(parent);
    }

    if (namedValue == null) {
      throw new JPFInspectorInvalidNameException(varName);
    }

    ExpressionStateValue child = getChild();
    if (child == null) {
      return namedValue;
    } else {
      return child.toHierarchy2(namedValue);
    }

  }

  /**
   * Returns null if none of the double hacks can be applied, otherwise returns a representation of a double constant.
   */
  private StateReadableValue tryApplyDoubleHacks (StateNodeInterface sni) {
    // Double hacks.
    if (TOKEN_IDF_NAN.equals(varName)) {
      return new StateReadableConstValue(sni.getInspector(), ClassLoaderInfo.getCurrentResolvedClassInfo("double"),
                                         Double.NaN);
    }

    if (TOKEN_IDF_NEGATIVE_INFINITY1_FULL.equals(varName) || TOKEN_IDF_NEGATIVE_INFINITY1_SHORT.equals(varName)) {
      return new StateReadableConstValue(sni.getInspector(), ClassLoaderInfo.getCurrentResolvedClassInfo("double"),
                                         Double.POSITIVE_INFINITY);
    }
    if (TOKEN_IDF_INFINITY.equals(varName) || TOKEN_IDF_POSITIVE_INFINITY1_FULL.equals(varName) || TOKEN_IDF_POSITIVE_INFINITY1_SHORT.equals(varName)) {
      return new StateReadableConstValue(sni.getInspector(), ClassLoaderInfo.getCurrentResolvedClassInfo("double"),
                                         Double.POSITIVE_INFINITY);
    }

    return null;
  }

  public StateNodeInterface getExpressionFromStackFrame(StateStackFrame parent) throws JPFInspectorException {
    assert (parent != null);

    StateReadableValue resultingValue = null;

    // Local variable or parameter
    if (parent.namedSlotIndex(varName) != StateValueStackSlot.INVALID_SLOT_INDEX) {
      resultingValue = StateValueStackSlot.createNamedSlotValue(parent, varName);
    }

    ClassInfo ci = parent.getClassInfo();
    // For some synthetic bytecode instructions ClassInfo can be  null
    // ci == null should not occur for standard user loaded bytecode
    if (ci != null) {
      // Field or static field
      if (resultingValue == null) {
        if (parent.isStaticMethod()) {
          FieldInfo fi = ci.getStaticField(varName);
          if (fi != null) {
            resultingValue = StateValueElementInfoField.createStaticNamedField(parent, varName);
          }
        } else {
          FieldInfo fi = StateValueElementInfoField.fieldNameJavaBasedLookup(ci, varName);
          if (fi != null) {
            // Correct not a static
            StateValueStackSlot svsf = StateValueStackSlot.createHiddenThisSlotValue(parent);
            if (fi.isStatic()) {
              resultingValue = StateValueElementInfoField.createStaticNamedField(svsf, varName);
            } else {
              resultingValue = StateValueElementInfoField.createInstanceNamedField(svsf, varName);
            }
          }
        }
      }

      // Predecessor class Name
      if (resultingValue == null) {
        ClassInfo ciPred = StateElementInfo.isPredecessorTypeName(ci, varName);

        if (ciPred != null) {
          if (parent.isStaticMethod()) {
            resultingValue = StateElementInfo.createStaticClass(parent, ciPred);
          } else {
            StateValueStackSlot svsf = StateValueStackSlot.createHiddenThisSlotValue(parent);
            resultingValue = svsf.createPredecessorClass(ciPred);
          }
        } else {
          // Not a predecessor class (any other class)
          // We are using the final, unqualified name, only. Really, this is undocumented and will stay that way for now,
          // because exploring static area is not something we are well equipped to do at this time anyway.
          StaticElementInfo sei = null;
          Statics statics = ClassLoaderInfo.getCurrentClassLoader().getStatics();
          for (int i = 0; i < statics.size(); i++) {
            StaticElementInfo aStaticClass = statics.get(i);
            if ((aStaticClass.getClassInfo().getName().endsWith("." + varName))) {
              sei = aStaticClass;
              break;
            }
          }
          if (sei != null) {
            // Class with given name exists
            resultingValue = StateElementInfo.createStaticClass(parent, sei.getClassInfo());
          }
          /*
          Code before migration:
          JPF jpf = ssf.getInspector().getJPF();
          StaticArea sa = jpf.getVM().getStaticArea();
          StaticElementInfo sei = sa.get(varName);
          if (sei != null) {
            // Class with given name exists
            srvi = StateElementInfo.createStaticClass(ssf, sei.getClassInfo());
          }
           */
        }
      }
    }

    if (resultingValue == null) {
      // Double hacks.
      resultingValue = tryApplyDoubleHacks(parent);
    }

    if (resultingValue == null) {
      throw new JPFInspectorInvalidNameException(varName);
    }

    ExpressionStateValue child = getChild();
    if (child == null) {
      return resultingValue;
    } else {
      return child.toHierarchy2(resultingValue);
    }
  }

  @Override
  public String getNormalizedExpression () {
    return '.' + varName + (getChild() != null ? getChild().getNormalizedExpression() : "");
  }
}
