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

import gov.nasa.jpf.inspector.client.ClientCommand;
import gov.nasa.jpf.inspector.client.ClientCommandInterface;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorParsingErrorException;

/**
 * An ANTLR parser that takes a string and returns a {@link ClientCommand}.
 */
public interface CommandParserInterface {
  /**
   * Parses a command and returns its representation.
   * 
   * @param cmd String with the command to parse.
   * @return Representation of the parsed command.
   */
  ClientCommandInterface parseCommand(String cmd) throws JPFInspectorParsingErrorException;
}
