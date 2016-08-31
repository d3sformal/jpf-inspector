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
import gov.nasa.jpf.inspector.common.ConsoleInformation;
import gov.nasa.jpf.inspector.common.pse.PrintAttrPrinter;
import gov.nasa.jpf.inspector.common.pse.ProgramStateEntry;
import gov.nasa.jpf.inspector.common.pse.ValuePrinter;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorParsingErrorException;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;

import java.io.PrintStream;

/**
 * Represents the "print_attr" command that can be used to print attributes associated with variables.
 */
public class CmdPrintAttr extends ClientCommand {

  private final String expression;

  /**
   * @param expression
   *        Expression with with specification of the entry to show.
   */
  public CmdPrintAttr(String expression) {
    assert expression != null;
    this.expression = expression;
  }

  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {

    try {
      ProgramStateEntry pse = inspector.evaluateStateExpression(expression);
      // Result processing
      ValuePrinter printer = new PrintAttrPrinter();
      StringBuilder sb = pse.visit(printer);
      outStream.print(sb.toString());

    } catch (JPFInspectorParsingErrorException e) {
      outStream.println(e.getMessage());
      outStream.println(e.expressError(ConsoleInformation.MAX_ERROR_LINE_LENGTH));
      client.recordComment(e.getMessage());
      client.recordComment(e.expressError(ConsoleInformation.MAX_ERROR_LINE_LENGTH));
    } catch (JPFInspectorException e) {
      outStream.println(e.getMessage());
      client.recordComment(e.getMessage());
    }
  }

  @Override
  public String getNormalizedCommand () {
    return "print_attr " + expression;
  }

  @Override
  public boolean isSafeToExecuteWhenNotPaused() {
    return false;
  }

}
