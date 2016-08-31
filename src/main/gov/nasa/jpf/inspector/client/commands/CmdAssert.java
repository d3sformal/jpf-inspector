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
import gov.nasa.jpf.inspector.interfaces.AssertCreationInformation;
import gov.nasa.jpf.inspector.interfaces.AssertStatus;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorParsingErrorException;

import java.io.PrintStream;

/**
 * Represents the "assert" command that creates a new assetion.
 */
public class CmdAssert extends ClientCommand {

  private final String position;
  private final String assertExpression;

  private AssertStatus createdAssert;

  public CmdAssert(String position, String assertExpression) {
    this.position = position;
    this.assertExpression = assertExpression;
  }

  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {

    try {

      createdAssert = inspector.createAssert(new ConsoleAssertionCreationExpression(position, assertExpression));
      if (createdAssert != null) {
        outStream.println("New assertion successfully created with ID " + createdAssert.getBPID() + ".");
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
      outStream.append(e.getMessage());
      client.recordComment(e.getMessage());
    }

  }

  @Override
  public String getNormalizedCommand () {
    if (createdAssert == null) {
      return "assert " + position + " " + assertExpression;
    } else {
      return "assert " + createdAssert.getNormalizedPosition() + " " + createdAssert.getNormalizedCondition();
    }
  }

  /**
   * Represents the arguments for the command "assert".
   */
  private static class ConsoleAssertionCreationExpression extends BreakpointCreationExpression implements AssertCreationInformation {

    private static final long serialVersionUID = -7840321111358650898L;

    private final String position;
    private final String expression;

    public ConsoleAssertionCreationExpression(String position, String expression) {
      this.position = position;
      this.expression = expression;
    }

    @Override
    public String getPosition () {
      return position;
    }

    @Override
    public String getCondition () {
      return expression;
    }

  }
}
