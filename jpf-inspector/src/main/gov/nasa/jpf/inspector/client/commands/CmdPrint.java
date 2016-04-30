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

package gov.nasa.jpf.inspector.client.commands;

import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorException;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorParsingErrorException;
import gov.nasa.jpf.inspector.server.programstate.ProgramStateEntry;
import gov.nasa.jpf.inspector.server.programstate.client.PSEHeapEntryList;
import gov.nasa.jpf.inspector.server.programstate.client.PSEMethod;
import gov.nasa.jpf.inspector.server.programstate.client.PSEThread;
import gov.nasa.jpf.inspector.server.programstate.client.PSEVariable;
import gov.nasa.jpf.inspector.server.programstate.client.PSEVariableArray;
import gov.nasa.jpf.inspector.server.programstate.client.PSEVariableObject;
import gov.nasa.jpf.inspector.server.programstate.client.PSEVariablePrimitive;
import gov.nasa.jpf.inspector.server.programstate.client.PSEVisitor;
import gov.nasa.jpf.inspector.utils.InstructionWrapper;

import java.io.PrintStream;

/**
 * Represents the print command that can be used to show variables values as well as call stack.
 */
public class CmdPrint extends ClientCommand {

  private final String expression;

  /**
   * @param expression
   *        Expression with with specification of the entry to show.
   */
  public CmdPrint (String expression) {
    assert expression != null;
    this.expression = expression;
  }

  @Override
  public void executeCommands (JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {

    try {
      ProgramStateEntry pse = inspector.evaluateStateExpression(expression);
      // Result processing
      ValuePrinter printer = new ValuePrinter();
      StringBuilder sb = pse.visit(printer);
      outStream.print(sb.toString());

    } catch (JPFInspectorParsingErrorException e) {
      outStream.println(e.getMessage());
      // TODO - Extend/replace outStream to be able to report line length - not use magic constant 50
      outStream.println(e.expressError(50));

      client.recordComment(e.getMessage());
      client.recordComment(e.expressError(JPFInspectorParsingErrorException.DEFAULT_LINE_LENGHT));
    } catch (JPFInspectorException e) {
      outStream.print(e.getMessage());
      client.recordComment(e.getMessage());
    }

  }

  @Override
  public String getNormalizedCommand () {
    return "print " + expression;
  }

  public static class ValuePrinter implements PSEVisitor<StringBuilder> {

    /**
     * Maximal printed length of source line or value
     */
    public static final int MAX_LEN = 50;

    private final StringBuilder sb;

    public ValuePrinter (StringBuilder sb) {
      super();

      this.sb = sb;
    }

    public ValuePrinter () {
      super();

      this.sb = new StringBuilder();
    }

    /**
     * @return Gets a string buffer where data from visited program state entrieas are stored.
     */
    public StringBuilder getResult () {
      return sb;
    }

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

    /*
     * @see
     * gov.nasa.jpf.inspector.server.programstate.client.PSEVisitor#visitPSEHeapEntryList(gov.nasa.jpf.inspector.server.programstate.client.PSEHeapEntryList)
     */
    @Override
    public StringBuilder visitPSEHeapEntryList (PSEHeapEntryList entry) throws JPFInspectorException {

      for (PSEVariable var : entry.getHeapList()) {
        printVariableBasic(var);
      }
      return sb;
    }

    /* @see gov.nasa.jpf.inspector.server.programstate.client.PSEVisitor#visitPSEMethod(gov.nasa.jpf.inspector.server.programstate.client.PSEMethod) */
    @Override
    public StringBuilder visitPSEMethod (PSEMethod pse) throws JPFInspectorException {
      InstructionWrapper.toStringBuffer(pse.getCallInstruction(), sb);
      sb.append('\n');

      PSEVariable[] localVars = pse.getLocals();
      if (localVars.length > 0) {
        sb.append("Stack slots\n");
        for (int i = 0; i < localVars.length; i++) {
          printVariableBasic(localVars[i]);
        }
      }

      PSEVariableObject sfThis = pse.getThis();
      if (sfThis != null) {

        PSEVariable[] fields = sfThis.getFields();
        if (fields.length > 0) {
          sb.append("Fields\n");
          for (int i = 0; i < fields.length; i++) {
            printVariableBasic(fields[i]);
          }
        }
      }
      return sb;
    }

    /* @see gov.nasa.jpf.inspector.server.programstate.client.PSEVisitor#visitPSEThread(gov.nasa.jpf.inspector.server.programstate.client.PSEThread) */
    @Override
    public StringBuilder visitPSEThread (PSEThread pse) throws JPFInspectorException {
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

    /*
     * @see
     * gov.nasa.jpf.inspector.server.programstate.client.PSEVisitor#visitPSEVariableArray(gov.nasa.jpf.inspector.server.programstate.client.PSEVariableArray)
     */
    @Override
    public StringBuilder visitPSEVariableArray (PSEVariableArray var) throws JPFInspectorException {
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
      sb.append(var.getLen());
      sb.append('\n');

      PSEVariable[] elements = var.getArrayItems();
      for (int i = 0; i < elements.length; i++) {
        printVariableBasic(elements[i]);
      }
      return sb;
    }

    /*
     * @see
     * gov.nasa.jpf.inspector.server.programstate.client.PSEVisitor#visitPSEVariableObject(gov.nasa.jpf.inspector.server.programstate.client.PSEVariableObject)
     */
    @Override
    public StringBuilder visitPSEVariableObject (PSEVariableObject var) throws JPFInspectorException {
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

      PSEVariable[] fields = var.getFields();
      PSEVariable[] staticFields = var.getStaticFields();

      if (fields.length > 0) {
        sb.append("  Instance fields\n");
        for (int i = 0; i < fields.length; i++) {
          printVariableBasic(fields[i]);
        }
      } else {
        if (staticFields.length > 0) {
          sb.append("  No instance fields\n");
        } else {
          sb.append("  No instance and static fields\n");
        }
      }

      if (staticFields.length > 0) {
        sb.append("  Static fields\n");
        for (int i = 0; i < staticFields.length; i++) {
          printVariableBasic(staticFields[i]);
        }
      }

      return sb;
    }

    /*
     * @see
     * gov.nasa.jpf.inspector.server.programstate.client.PSEVisitor#visitPSEVariablePrimitive(gov.nasa.jpf.inspector.server.programstate.client.PSEVariablePrimitive
     * )
     */
    @Override
    public StringBuilder visitPSEVariablePrimitive (PSEVariablePrimitive var) {
      printVariableBasic(var);
      return sb;
    }
  }

}
