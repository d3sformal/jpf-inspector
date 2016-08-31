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
 * Represents the 'record execute' command that loads a recording from a file and immediately replays it.
 * @author Alf
 */
public class CmdRecordExecute extends ClientCommand {

  private final String filename;

  public CmdRecordExecute (String filename) {
    this.filename = filename;
  }

  @Override
  public void execute(JPFInspectorClient client, JPFInspectorBackEndInterface inspector, PrintStream outStream) {

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
