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

package gov.nasa.jpf.inspector.utils;

import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ClassLoaderInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.VM;

/**
 * Caches ClassInfos of primitive types and wrappers.
 * 
 * Note: Each instance of JVM have new ClassInfos for these types.
 *
 *
 * As a historical note, previously, in JPF6, all instace of the call
 *
 * ClassLoaderInfo.getSystemResolvedClassInfo("type")
 *
 * were instead:
 *
 * ClassInfo.getResolvedClassInfo("arg")
 *
 * We are confident that the replacement is correct, though. If you feel the same way, then I guess you may remove
 * this comment.
 */
public class ClassInfoCache {
  private final VM vm; // VM to which cached values are related to.

  // Primitive types
  public final ClassInfo ci_boolean;
  public final ClassInfo ci_byte;
  public final ClassInfo ci_char;
  public final ClassInfo ci_short;
  public final ClassInfo ci_int;
  public final ClassInfo ci_long;
  public final ClassInfo ci_float;
  public final ClassInfo ci_double;

  // Wrappers
  public final ClassInfo ci_Boolean;
  public final ClassInfo ci_Byte;
  public final ClassInfo ci_Char;
  public final ClassInfo ci_Short;
  public final ClassInfo ci_Int;
  public final ClassInfo ci_Long;
  public final ClassInfo ci_Float;
  public final ClassInfo ci_Double;

  // Wrappers value fields (field which holds the wrapped primitive type)
  public final FieldInfo fi_val_Boolean;
  public final FieldInfo fi_val_Byte;
  public final FieldInfo fi_val_Char;
  public final FieldInfo fi_val_Short;
  public final FieldInfo fi_val_Int;
  public final FieldInfo fi_val_Long;
  public final FieldInfo fi_val_Float;
  public final FieldInfo fi_val_Double;

  public final ClassInfo ci_String;

  public ClassInfoCache (VM vm) {
    assert (vm != null);

    this.vm = vm;

    ci_boolean = ClassLoaderInfo.getSystemResolvedClassInfo("boolean");
    ci_byte = ClassLoaderInfo.getSystemResolvedClassInfo("byte");
    ci_char = ClassLoaderInfo.getSystemResolvedClassInfo("char");
    ci_short = ClassLoaderInfo.getSystemResolvedClassInfo("short");
    ci_int = ClassLoaderInfo.getSystemResolvedClassInfo("int");
    ci_long = ClassLoaderInfo.getSystemResolvedClassInfo("long");
    ci_float = ClassLoaderInfo.getSystemResolvedClassInfo("float");
    ci_double = ClassLoaderInfo.getSystemResolvedClassInfo("double");

    assert (ci_boolean != null);
    assert (ci_byte != null);
    assert (ci_char != null);
    assert (ci_short != null);
    assert (ci_int != null);
    assert (ci_long != null);
    assert (ci_float != null);
    assert (ci_double != null);

    ci_Boolean = ClassLoaderInfo.getSystemResolvedClassInfo("java.lang.Boolean");
    ci_Byte = ClassLoaderInfo.getSystemResolvedClassInfo("java.lang.Byte");
    ci_Char = ClassLoaderInfo.getSystemResolvedClassInfo("java.lang.Character");
    ci_Short = ClassLoaderInfo.getSystemResolvedClassInfo("java.lang.Short");
    ci_Int = ClassLoaderInfo.getSystemResolvedClassInfo("java.lang.Integer");
    ci_Long = ClassLoaderInfo.getSystemResolvedClassInfo("java.lang.Long");
    ci_Float = ClassLoaderInfo.getSystemResolvedClassInfo("java.lang.Float");
    ci_Double = ClassLoaderInfo.getSystemResolvedClassInfo("java.lang.Double");

    assert (ci_Boolean != null);
    assert (ci_Byte != null);
    assert (ci_Char != null);
    assert (ci_Short != null);
    assert (ci_Int != null);
    assert (ci_Long != null);
    assert (ci_Float != null);
    assert (ci_Double != null);

    fi_val_Boolean = ci_Boolean.getInstanceField("value");
    fi_val_Byte = ci_Byte.getInstanceField("value");
    fi_val_Char = ci_Char.getInstanceField("value");
    fi_val_Short = ci_Short.getInstanceField("value");
    fi_val_Int = ci_Int.getInstanceField("value");
    fi_val_Long = ci_Long.getInstanceField("value");
    fi_val_Float = ci_Float.getInstanceField("value");
    fi_val_Double = ci_Double.getInstanceField("value");

    ci_String = ClassLoaderInfo.getSystemResolvedClassInfo("java.lang.String");

    assert (ci_String != null);
  }

  public boolean isBoxedPrimitiveType (ClassInfo ci) {
    return (ci_Boolean.equals(ci) || ci_Byte.equals(ci) || ci_Char.equals(ci) || ci_Short.equals(ci) || ci_Int.equals(ci) || ci_Long.equals(ci)
        || ci_Float.equals(ci) || ci_Double.equals(ci));
  }

  /**
   * @return Gets true if cache is valid (holds proper instances of ClassInfos) for given instance of JVM.
   */
  public boolean cacheValid (VM vm) {
    return vm == this.vm;
  }

  @Override
  public String toString () {
    return "ClassInfoCache [ci_boolean=" + ci_boolean + ", ci_byte=" + ci_byte + ", ci_char=" + ci_char + ", ci_short=" + ci_short + ", ci_int=" + ci_int
        + ", ci_long=" + ci_long + ", ci_float=" + ci_float + ", ci_double=" + ci_double + ", ci_Boolean=" + ci_Boolean + ", ci_Byte=" + ci_Byte + ", ci_Char="
        + ci_Char + ", ci_Short=" + ci_Short + ", ci_Int=" + ci_Int + ", ci_Long=" + ci_Long + ", ci_Float=" + ci_Float + ", ci_Double=" + ci_Double
        + ", ci_String=" + ci_String + "]";
  }

}
