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
 * Represents the 'record save' command that saves the contents of the command recorder into a file.
 * @author Alf
 */
public class CmdRecordSave extends ClientCommand {

  private final String fileName;

  public CmdRecordSave (String fileName) {
    this.fileName = fileName;
  }

  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {
    CommandRecorder rec = client.getCommandRecorder();
    if (rec.saveRecordedCommmands(fileName)) {
      // Successfully saved
      outStream.println("List of executed commands was successfully saved to the file '" + fileName + "'.");
    } else {
      // The called method already printed the error message to the console.
    }
  }

  @Override
  public String getNormalizedCommand () {
    return "record save " + fileName;
  }

  @Override
  public void recordCommand (CommandRecorder rec) {
    // We record this command only as a comment because when executing a recording,
    // we don't want the file to be overwritten, as per the documentation.
    rec.addComment("Recorded command: " + getNormalizedCommand());
  }

}
