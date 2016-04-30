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
import gov.nasa.jpf.inspector.interfaces.ChoiceGeneratorsInterface.CGTypes;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorException;
import gov.nasa.jpf.inspector.utils.ChoiceGeneratorWrapper;
import gov.nasa.jpf.inspector.utils.InstructionWrapper;

import java.io.PrintStream;
import java.util.List;

/**
 * Represents used choice_generators command - prints used choices.
 */
public class CmdUsedChoiceGenerators extends ClientCommand {

  @Override
  public void executeCommands (JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
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

  static public String cgWrapper2String (ChoiceGeneratorWrapper cgw) {
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
      throw new RuntimeException("Unknown " + CGTypes.class.getName() + " entry " + cgw.getCgType());
    }

    // CG Id
    // String id = cgw.getId();
    // if (cgw.getId() != null) {
    // sb.append(" - ");
    // // sb.append("id=");
    // sb.append(id);
    // }
    //
    // // Cg threadID
    // sb.append(" - ");
    // // sb.append("tid=");
    // sb.append(cgw.getThreadNum());

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
