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
import gov.nasa.jpf.inspector.client.CommandRecorder;
import gov.nasa.jpf.inspector.client.JPFInspectorClient;
import gov.nasa.jpf.inspector.interfaces.JPFInspectorBackEndInterface;

import java.io.PrintStream;

/**
 * Represents the 'record execute' command that loads a recording from a file and immediately replays it.
 * @author Alf
 */
public class CmdRecordExecute extends ClientCommand {

  private final String filename;

  public CmdRecordExecute (String filename) {
    super();
    this.filename = filename;
  }

  @Override
  public void executeCommands (JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {

    CommandRecorder rec = client.getCommandRecorder();
    rec.executeCommands(filename, client);

    rec.addComment("Execution of the recording '" + filename + "' has finished.");
    rec.addComment("------------------------------------------------------------------------------");

  }

  @Override
  public String getNormalizedCommand () {
    return "record execute " + filename;
  }

  @Override
  public void recordCommand (CommandRecorder rec) {
    // We record this only as a comment, otherwise we might get stuck in an infinite replay loop.
    rec.addComment("------------------------------------------------------------------------------");
    rec.addComment(getNormalizedCommand());
  }

}
