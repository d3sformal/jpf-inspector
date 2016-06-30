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
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;

import java.io.PrintStream;

/**
 * Represents the "cg select" command that forces the current choice generator to use the user-specified choice.
 */
public class CmdChoiceSelect extends ClientCommand {

  public static final int USE_CURRENT_CHOICE = -2;

  static {
    assert (USE_CURRENT_CHOICE != ChoiceGeneratorsInterface.USE_DEFAULT_CHOICE);
  }

  private final int choice;

  public CmdChoiceSelect (int choice) {
    this.choice = choice;
  }

  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    try {
      if (choice == USE_CURRENT_CHOICE) {
        throw new JPFInspectorGenericErrorException("You must specify a choice.");
        // inspector.start(); // Continue until next CG
      }
      inspector.selectChoice(choice);
    } catch (JPFInspectorException e) {
      outStream.print(e.getMessage());
      client.recordComment(e.getMessage());
    }

  }

  @Override
  public String getNormalizedCommand () {
    String result = "choice_generators select";
    // Use current choice
    if (choice == USE_CURRENT_CHOICE) {
      return result;
    }
    // Default forward trace
    if (choice == ChoiceGeneratorsInterface.USE_DEFAULT_CHOICE) {
      return result + " *";
    }
    // Use specified choice
    return "choice_generators select " + choice;
  }

}
