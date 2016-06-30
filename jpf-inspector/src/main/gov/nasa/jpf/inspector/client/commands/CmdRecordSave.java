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
