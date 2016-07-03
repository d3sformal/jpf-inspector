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
package gov.nasa.jpf.inspector.common.pse;

import gov.nasa.jpf.inspector.client.commands.CmdPrint;
import gov.nasa.jpf.inspector.utils.InstructionWrapper;

import java.util.Objects;

/**
 * This is a visitor class that is used by the command {@link CmdPrint} to convert {@link ProgramStateEntry} objects
 * to text on the console.
 */
public class ValuePrinter implements PSEVisitor<StringBuilder> {

  /**
   * Maximum length of a value before it is truncated
   */
  private static final int MAX_LEN = 50;

  private final StringBuilder sb;

  public ValuePrinter() {

    this.sb = new StringBuilder();
  }

  /**
   * Prints the short-form of a value.
   */
  private void printVariableBasic (PSEVariable var) {
    sb.append('\t');
    sb.append(var.getIndex());
    sb.append(" : ");
    sb.append(var.getVarName());
    sb.append(" (");
    sb.append(var.getVarTypeName());
    sb.append(") = ");
    String varValue = var.getVarValue();
    if (varValue.length() > MAX_LEN) {
      varValue = varValue.substring(0, MAX_LEN - 3) + "...";
    }
    sb.append(varValue);
    sb.append('\n');
  }


  @Override
  public StringBuilder visitPSEHeapEntryList (PSEHeapEntryList entry) {

    for (PSEVariable var : entry.getHeapList()) {
      printVariableBasic(var);
    }
    return sb;
  }

  @Override
  public StringBuilder visitPSEMethod (PSEMethod pse) {
    InstructionWrapper.toStringBuffer(pse.getCallInstruction(), sb);
    sb.append('\n');

    PSEVariable[] localVars = pse.getLocals();
    if (localVars.length > 0) {
      sb.append("Stack slots\n");
      for (PSEVariable localVar : localVars) {
        printVariableBasic(localVar);
      }
    }

    PSEVariableObject sfThis = pse.getThis();
    if (sfThis != null) {

      PSEVariable[] fields = sfThis.getFields();
      if (fields.length > 0) {
        sb.append("Fields\n");
        for (PSEVariable field : fields) {
          printVariableBasic(field);
        }
      }
    }
    return sb;
  }


  @Override
  public StringBuilder visitPSEThread (PSEThread pse) {
    sb.append(pse.getThreadNum());
    sb.append(" : ");
    sb.append(pse.getThreadName());

    sb.append(" state=");
    sb.append(pse.getState());

    if (pse.isDaemon()) {
      sb.append(" daemon thread");
    }

    sb.append(" priority=");
    sb.append(pse.getPriority());

    // Call stack - (at least TOP call)
    sb.append('\n');

    PSEMethod[] callStack = pse.getCallStack();
    for (int i = 0; i < callStack.length; i++) {
      PSEMethod method = callStack[i];
      sb.append('\t');
      sb.append(i);
      sb.append(" : ");
      InstructionWrapper.toStringBuffer(method.getCallInstruction(), sb);
      sb.append('\n');
    }
    return sb;
  }


  @Override
  public StringBuilder visitPSEVariableArray (PSEVariableArray var) {
    sb.append(var.getVarName());
    sb.append(" (");
    sb.append(var.getVarTypeName());
    sb.append(") =");
    String varValue = var.getVarValue();
    if (varValue != null) {
      if (varValue.length() > MAX_LEN) {
        varValue = varValue.substring(0, MAX_LEN - 3) + "...";
      }
      sb.append(varValue);
    }
    sb.append(", length=");
    sb.append(var.getLength());
    sb.append('\n');

    PSEVariable[] elements = var.getArrayItems();
    for (PSEVariable element : elements) {
      printVariableBasic(element);
    }
    return sb;
  }


  @Override
  public StringBuilder visitPSEVariableObject (PSEVariableObject var) {
    sb.append(var.getVarName());
    sb.append(" (");
    sb.append(var.getVarTypeName());
    sb.append(") = ");
    String varValue = var.getVarValue();
    if (varValue != null) {
      if (varValue.length() > MAX_LEN) {
        varValue = varValue.substring(0, MAX_LEN - 3) + "...";
      }
      sb.append(varValue);
    }
    sb.append('\n');

    // TODO (elsewhere) using the "&&" command, safe mode shouldn't be so overly strict (the command "commence && wait && print null" should be permitted.


    if (!Objects.equals(var.getVarValue(), "null")) {
      PSEVariable[] fields = var.getFields();
      PSEVariable[] staticFields = var.getStaticFields();

      if (fields.length > 0) {
        sb.append("  Instance fields\n");
        for (PSEVariable field : fields) {
          printVariableBasic(field);
        }
      } else {
        if (staticFields.length > 0) {
          sb.append("  No instance fields\n");
        } else {
          sb.append("  No instance or static fields\n");
        }
      }

      if (staticFields.length > 0) {
        sb.append("  Static fields\n");
        for (PSEVariable staticField : staticFields) {
          printVariableBasic(staticField);
        }
      }
    }
    return sb;
  }

  @Override
  public StringBuilder visitPSEVariablePrimitive (PSEVariablePrimitive var) {
    printVariableBasic(var);
    return sb;
  }
}
