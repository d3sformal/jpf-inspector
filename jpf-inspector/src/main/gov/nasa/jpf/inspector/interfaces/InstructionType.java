//
// Copyright (C) 2010 United States Government as represented by the
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

package gov.nasa.jpf.inspector.interfaces;

import gov.nasa.jpf.jvm.bytecode.*;

/**
 * Represents the argument of the "instruction type = [type]" hit condition.
 */
public enum InstructionType {
  ANY,
  NONE,
  /**
   * The instruction is an invocation instruction that starts with the letters INVOKE, such as {@link INVOKEVIRTUAL}.
   */
  INVOKE,
  /**
   * The instruction returns from a method.
   */
  RETURN,
  /**
   * The instruction accesses a field. This is the union of {@link #FIELD_READ} and {@link #FIELD_WRITE}.
   */
  FIELD_ACCESS,
  /**
   * The instruction is a {@link GETFIELD} or a {@link GETSTATIC}.
   */
  FIELD_READ,
  /**
   * The instruction is a {@link PUTFIELD} or a {@link PUTSTATIC}.
   */
  FIELD_WRITE,
  /**
   * The instruction is an if-condition instruction that starts with the letters IF, such as {@link IF_ACMPEQ} or {@link IFGT}.
   */
  IFCOND,
  /**
   * The instruction is a {@link MONITORENTER} or a {@link MONITOREXIT}.
   */
  LOCK,
  /**
   * The instruction accesses an array element.
   */
  ARRAY,
  /**
   * The instruction accesses a local variable. This is the union of {@link #LOCAL_READ} and {@link #LOCAL_WRITE}
   * instruction types.
   */
  LOCAL_ACCESS,
  /**
   * The instruction reads a value from a local variable.
   * (aload*, dload*, fload*, iload*, lload*)
   */
  LOCAL_READ,
  /**
   * The instruction writes a value into a local variable.
   * (astore*, dstore*, fstore*, istore*, lstore*)
   */
  LOCAL_WRITE
}