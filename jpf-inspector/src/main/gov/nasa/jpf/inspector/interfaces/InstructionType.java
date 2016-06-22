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

/**
 * Represents the argument of the "instruction type = [type]" hit condition.
 */
public enum InstructionType {
  IT_ANY,
  IT_NONE,
  IT_INVOKE,
  IT_RETURN,
  IT_FIELD_ACCESS,
  IT_FIELD_READ,
  IT_FIELD_WRITE,
  IT_IFCOND,
  /**
   * MoniterEnter/MonitorExit
   */
  IT_LOCK,
  IT_ARRAY,
  /**
   * The instruction accesses a local variable.
   * (aload*, astore*, dload*, dstore*, fload*, fstore*, iload*, istore*, lload*, lstore*, ret)
   */
  LOCAL_ACCESS,
  /**
   * The instruction reads a value from a local variable.
   * (aload*, dload*, fload*, iload*, lload*, ret)
   */
  LOCAL_READ,
  /**
   * The instruction writes a value into a local variable.
   * (astore*, dstore*, fstore*, istore*, lstore*)
   */
  LOCAL_WRITE
}