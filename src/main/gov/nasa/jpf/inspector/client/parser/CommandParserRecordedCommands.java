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

package gov.nasa.jpf.inspector.client.parser;

import gov.nasa.jpf.inspector.client.ClientCommandInterface;
import gov.nasa.jpf.inspector.client.parser.generated.ConsoleGrammarParser;
import gov.nasa.jpf.inspector.common.AntlrParseException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorParsingErrorException;
import gov.nasa.jpf.inspector.utils.parser.RecognitionRuntimeException;

/**
 * 
 * Parses commands from user or textual representation of callbacks.
 * 
 * Note: Used in replay of the recorded execution .
 * 
 * @author Alf
 * 
 */
public class CommandParserRecordedCommands implements CommandParserInterface {

  /**
   * Parses commands from user or textual representation of callbacks.
   * 
   * Note: Used in replay of the recorded execution .
   * 
   * @param cmd Textual representation to process
   * @return Representation of command or callback
   * @throws JPFInspectorParsingErrorException In case of malformed command
   */
  @Override
  public ClientCommandInterface parseCommand(String cmd) throws JPFInspectorParsingErrorException {
    if (cmd == null) {
      return null;
    }
    cmd = cmd.trim();
    if (cmd.isEmpty()) {
      return null;
    }

    try {
      ConsoleGrammarParser parser = CommandParserFactory.getParser(cmd);
      return parser.clientCommandWithCB().value;
    } catch (RecognitionRuntimeException e) {
      throw new JPFInspectorParsingErrorException("Invalid input" + (e.getMessage() != null ? " - " + e.getMessage() : ""), cmd, e.getRecognitionException());
    } catch (AntlrParseException e) {
      throw new JPFInspectorParsingErrorException("Parse error: " + e.getMessage(), cmd, e.getColumn());
    }
  }
}
