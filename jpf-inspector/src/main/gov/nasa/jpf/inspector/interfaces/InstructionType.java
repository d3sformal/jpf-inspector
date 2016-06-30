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