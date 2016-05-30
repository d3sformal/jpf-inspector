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
import gov.nasa.jpf.inspector.migration.MigrationUtilities;
import gov.nasa.jpf.inspector.server.programstate.StateElementInfo;
import gov.nasa.jpf.inspector.server.programstate.StateNodeInterface;
import gov.nasa.jpf.inspector.server.programstate.StateReadableConstValue;
import gov.nasa.jpf.inspector.server.programstate.StateReadableValueInterface;
import gov.nasa.jpf.inspector.server.programstate.StateStackFrame;
import gov.nasa.jpf.inspector.server.programstate.StateValue;
import gov.nasa.jpf.inspector.server.programstate.StateValueElementInfoField;
import gov.nasa.jpf.inspector.server.programstate.StateValueStackSlot;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.StaticElementInfo;

/**
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

  private final String varName;

  // Note TOKEN_NAN, TOKEN_INFINITY, TOKEN_NEGATIVE_INFINITY1 and TOKEN_POSITIVE_INFINITY1 are parsed as idfField_name and ("special semantic hack") resolves
  // this
  public static final String TOKEN_IDF_NAN = "NaN"; // Has to be equal to TOKEN_NAN

  public static final String TOKEN_IDF_INFINITY = "Infinity"; // Has to be equal to TOKEN_INFINITY

  public static final String TOKEN_IDF_NEGATIVE_INFINITY1_FULL = "negative_infinity"; // Has to be equal to TOKEN_NEGATIVE_INFINITY1
  public static final String TOKEN_IDF_NEGATIVE_INFINITY1_SHORT = "neg_inf"; // Has to be equal to TOKEN_NEGATIVE_INFINITY1

  public static final String TOKEN_IDF_POSITIVE_INFINITY1_FULL = "positive_infinity"; // Has to be equal to TOKEN_POSITIVE_INFINITY1
  public static final String TOKEN_IDF_POSITIVE_INFINITY1_SHORT = "pos_inf"; // Has to be equal to TOKEN_POSITIVE_INFINITY1

  public ExpressionStateValueName (ExpressionStateValue child, String varName) {
    super(child);
    assert (varName != null);

    this.varName = varName;
  }

  /*
   * @see
   * gov.nasa.jpf.inspector.server.expression.expressions.ExpressionStateValue#getResultExpression(gov.nasa.jpf.inspector.server.programstate.
   * StateReadableValueInterface
   * )
   */
  @Override
  public StateReadableValueInterface getResultExpression (StateReadableValueInterface srvi) throws JPFInspectorException {
    assert (srvi != null);

    StateReadableValueInterface namedValue = null;

    if (StateValue.hasNamedEntry(srvi, varName)) {

      ClassInfo ci = srvi.getClassInfo();
      FieldInfo fi = StateValueElementInfoField.fieldNameJavaBasedLookup(ci, varName);

      if (fi.isStatic()) {
        namedValue = StateValueElementInfoField.createStaticNamedField(srvi, varName);
      } else {
        namedValue = StateValueElementInfoField.createInstanceNamedField(srvi, varName);
      }
    } else {
      // Try if not a predecessor class name (package is ignored)
      ClassInfo ci = StateElementInfo.isPredecessorTypeName(srvi.getClassInfo(), varName);

      if (ci != null) {
        namedValue = srvi.createPredecessorClass(ci);
      }
    }

    if (namedValue == null) {
      // Double hacks.
      namedValue = tryApplyDoubleHacks(srvi);
    }

    if (namedValue == null) {
      throw new JPFInspectorInvalidNameException(varName);
    }

    ExpressionStateValue child = getChild();
    if (child == null) {
      return namedValue;
    } else {
      return child.getResultExpression(namedValue);
    }

  }

  /**
   * @return Return null if non of double hacks can be applied, otherwise returns a representation of a double constant.
   */
  private StateReadableValueInterface tryApplyDoubleHacks (StateNodeInterface sni) {
    // Double hacks.
    if (TOKEN_IDF_NAN.equals(varName)) {
      return new StateReadableConstValue(sni.getInspector(), 1, MigrationUtilities.getResolvedClassInfo("double"), Double.valueOf(Double.NaN));
    }

    if (TOKEN_IDF_NEGATIVE_INFINITY1_FULL.equals(varName) || TOKEN_IDF_NEGATIVE_INFINITY1_SHORT.equals(varName)) {
      return new StateReadableConstValue(sni.getInspector(), 1, MigrationUtilities.getResolvedClassInfo("double"), Double.valueOf(Double.POSITIVE_INFINITY));
    }
    if (TOKEN_IDF_INFINITY.equals(varName) || TOKEN_IDF_POSITIVE_INFINITY1_FULL.equals(varName) || TOKEN_IDF_POSITIVE_INFINITY1_SHORT.equals(varName)) {
      return new StateReadableConstValue(sni.getInspector(), 1, MigrationUtilities.getResolvedClassInfo("double"), Double.valueOf(Double.POSITIVE_INFINITY));
    }

    return null;
  }

  public StateNodeInterface getResultExpression (StateStackFrame ssf) throws JPFInspectorException {
    assert (ssf != null);

    StateReadableValueInterface srvi = null;

    // Local variable or parameter
    if (ssf.namedSlotIndex(varName) != StateValueStackSlot.INVALID_SLOT_INDEX) {
      srvi = StateValueStackSlot.createNamedSlotValue(ssf, varName);
    }

    ClassInfo ci = ssf.getClassInfo();
    // For some synthetic bytecode instructions can be ClassInfo null
    // ci == null should not occur for standard user loaded bytecode
    if (ci != null) {

      // Field or static field
      if (srvi == null) {
        if (ssf.isStaticMethod()) {
          FieldInfo fi = ci.getStaticField(varName);
          if (fi != null) {
            srvi = StateValueElementInfoField.createStaticNamedField(ssf, varName);
          }
        } else {
          FieldInfo fi = StateValueElementInfoField.fieldNameJavaBasedLookup(ci, varName);
          if (fi != null) {
            // Correct not a static
            StateValueStackSlot svsf = StateValueStackSlot.createHiddenThisSlotValue(ssf);
            if (fi.isStatic()) {
              srvi = StateValueElementInfoField.createStaticNamedField(svsf, varName);
            } else {
              srvi = StateValueElementInfoField.createInstanceNamedField(svsf, varName);
            }
          }
        }
      }

      // Predecessor class Name
      if (srvi == null) {
        ClassInfo ciPred = StateElementInfo.isPredecessorTypeName(ci, varName);

        if (ciPred != null) {
          if (ssf.isStaticMethod()) {
            srvi = StateElementInfo.createStaticClass(ssf, ciPred);
          } else {
            StateValueStackSlot svsf = StateValueStackSlot.createHiddenThisSlotValue(ssf);
            srvi = svsf.createPredecessorClass(ciPred);
          }
        } else {
          // Not a predecessor class (any other class)
          StaticElementInfo sei = MigrationUtilities.getStaticElementInfoFromName(varName);

          if (sei != null) {
            // Class with given name exists
            srvi = StateElementInfo.createStaticClass(ssf, sei.getClassInfo());
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

    if (srvi == null) {
      // Double hacks.
      srvi = tryApplyDoubleHacks(ssf);
    }

    if (srvi == null) {
      throw new JPFInspectorInvalidNameException(varName);
    }

    // Childs
    ExpressionStateValue child = getChild();
    if (child == null) {
      return srvi;
    } else {
      return child.getResultExpression(srvi);
    }
  }

  /* @see gov.nasa.jpf.inspector.server.expression.ExpressionNodeInterface#getNormalizedExpression() */
  @Override
  public String getNormalizedExpression () {
    return '.' + varName + (child != null ? child.getNormalizedExpression() : "");
  }
}
