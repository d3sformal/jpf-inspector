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
      createdBP = inspector.createBreakpoint(creationExpression);
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
