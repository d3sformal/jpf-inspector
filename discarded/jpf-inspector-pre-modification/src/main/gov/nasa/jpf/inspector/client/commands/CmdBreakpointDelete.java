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

import java.io.PrintStream;

/**
 * Handles delete breakpoint INT command
 */
public class CmdBreakpointDelete extends ClientCommand {

  final String bpID; // / Identifier of the Breakpoint {@link BreakPoint#getBPID()}

  public CmdBreakpointDelete (String bpID) {
    this.bpID = bpID;
  }

  @Override
  public void executeCommands (JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    try {
      int bpIDint = Integer.parseInt(bpID);
      final boolean rc = inspector.deleteBreakPoint(bpIDint);
      if (rc == false) {
        outStream.println("Breakpoint with ID = " + bpIDint + " not exists");
      } else {
        outStream.println("Breakpoint with ID = " + bpIDint + " delete succesfully");
      }
    } catch (NumberFormatException e) {
      outStream.println("Malformated breakpoint identifier - use number (" + bpID + ")");
    }
  }

  @Override
  public String getNormalizedCommand () {
    return "delete breakpoint " + bpID;
  }

}
