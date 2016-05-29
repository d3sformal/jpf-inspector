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

package gov.nasa.jpf.inspector.client.parser;

import gov.nasa.jpf.inspector.client.ClientCommandInterface;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorParsingErrorException;
import gov.nasa.jpf.inspector.utils.parser.RecognitionRuntimeException;

import org.antlr.runtime.RecognitionException;

/**
 * @author Alf
 * 
 */
public class CommandParserUserCommands extends CommandParser {

  /**
   * 
   * Parses commands from user.
   * 
   * @param cmd String with command to parse.
   * @return Representation of the parsed command.
   */
  @Override
  public ClientCommandInterface parseCommands (String cmd) throws JPFInspectorParsingErrorException {
    if (cmd == null) {
      return null;
    }
    cmd = cmd.trim();
    if (cmd.isEmpty()) {
      return null;
    }

    try {
      ConsoleGrammarParser parser = getParser(cmd);
      return parser.clientCommands().value;
    } catch (RecognitionRuntimeException e) {
      throw new JPFInspectorParsingErrorException("Invalid input" + (e.getMessage() != null ? " - " + e.getMessage() : ""), cmd, e.getRecognitionException());
    } catch (Exception e) {
      // TODO this used to be RecognitionException.
      /*
      throw new JPFInspectorParsingErrorException("Invalid input" + (e.getMessage() != null ? " - " + e.getMessage() : ""), cmd, e);*/
      throw e;
    }
  }

}
