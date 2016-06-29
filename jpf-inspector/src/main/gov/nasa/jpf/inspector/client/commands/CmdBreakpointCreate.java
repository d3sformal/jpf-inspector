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
import gov.nasa.jpf.inspector.common.BreakpointCreationExpression;
import gov.nasa.jpf.inspector.common.ConsoleInformation;
import gov.nasa.jpf.inspector.interfaces.BreakpointStatus;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorParsingErrorException;

import java.io.PrintStream;

/**
 * Represents the "create breakpoint" command that creates a new breakpoint.
 */
public class CmdBreakpointCreate extends ClientCommand {

  private final BreakpointCreationExpression creationExpression;
  private BreakpointStatus createdBP;

  public CmdBreakpointCreate (BreakpointCreationExpression creationExpression) {
    this.creationExpression = creationExpression;
    createdBP = null;
  }

  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    createdBP = null;
    try {
      createdBP = inspector.createBreakPoint(creationExpression);
      if (createdBP != null) {
        outStream.println("New breakpoint successfully created with ID " + createdBP.getBPID() + ".");
      }

      if (recorder != null) {
        recorder.updateCommandRecord(this);
      }

    } catch (JPFInspectorParsingErrorException e) {
      outStream.println(e.getMessage());
      outStream.println(e.expressError(ConsoleInformation.MAX_ERROR_LINE_LENGTH));
      client.recordComment(e.getMessage());
      client.recordComment(e.expressError(JPFInspectorParsingErrorException.DEFAULT_LINE_LENGTH));
    } catch (JPFInspectorGenericErrorException e) {
      outStream.println(e.getMessage());
      client.recordComment(e.getMessage());
    }

  }

  @Override
  public String getNormalizedCommand () {
    if (createdBP == null) {
      return BreakpointCreationExpression.getNormalizedExpressionPrefix(creationExpression) + ' ' + creationExpression.getBPExpression();
    } else {
      return BreakpointCreationExpression.getNormalizedExpressionPrefix(creationExpression) + ' ' + createdBP.getNormalizedBreakpointExpression();
    }
  }

  @Override
  public String toString () {
    return getNormalizedCommand();
  }

}
