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
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGTypes;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.utils.ChoiceGeneratorWrapper;
import gov.nasa.jpf.inspector.utils.InstructionWrapper;

import java.io.PrintStream;
import java.util.List;

/**
 * Represents the "used choice_generators" command that prints used choices.
 */
public class CmdUsedChoiceGenerators extends ClientCommand {

  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    try {
      List<ChoiceGeneratorWrapper> result = inspector.getUsedChoiceGenerators(true);
      if (result == null) {
        return;
      }
      for (int i = 0; i < result.size(); i++) {
        outStream.println(" " + i + " --> " + cgWrapper2String(result.get(i)));
      }
    } catch (JPFInspectorException e) {
      outStream.println(e.getMessage());
      client.recordComment(e.getMessage());
    }

  }

  @Override
  public String getNormalizedCommand () {
    return "used choice_generators";
  }

  private static String cgWrapper2String(ChoiceGeneratorWrapper cgw) {
    if (cgw == null) {
      return "";
    }

    StringBuilder sb = new StringBuilder();
    // CG type
    switch (cgw.getCgType()) {
    case CG_TYPE_SCHEDULING:
      sb.append("(SCHED) ");
      break;
    case CG_TYPE_DATA:
      sb.append("(DATA)  ");
      break;
    default:
      throw new RuntimeException("Unknown " + CGTypes.class.getName() + " entry " + cgw.getCgType() + ".");
    }

    sb.append(" - ");
    sb.append(cgw.getCgString());

    InstructionWrapper instw = cgw.getInstruction();
    if (instw != null) {
      sb.append("\n            ");
      InstructionWrapper.toStringBuffer(instw, sb);
    }

    return sb.toString();
  }

}
