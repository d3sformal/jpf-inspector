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

import gov.nasa.jpf.inspector.client.parser.generated.ConsoleGrammarLexer;
import gov.nasa.jpf.inspector.client.parser.generated.ConsoleGrammarParser;
import gov.nasa.jpf.inspector.common.ThrowingErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 * This class is used to create an ANTLR parser for client commands.
 */
public final class CommandParserFactory {

  /**
   * Returns a new parser for parsing commands directly from the user.
   */
  public static CommandParserInterface getClientCommandParser () {
    return new CommandParserUserCommands();
  }

  /**
   * Returns a new parser for parsing commands replayed by the recorder (which includes the hidden callback commands).
   */
  public static CommandParserInterface getRecordCommandParser () {
    return new CommandParserRecordedCommands();
  }

  /**
   * Package-private method that creates an ANTLR parser from a command string to be parsed.
   * @param expr Command as a string (that should be parsed).
   * @return The ANTLR parser.
   */
  static ConsoleGrammarParser getParser(String expr)  {
    org.antlr.v4.runtime.Lexer lexer = new ConsoleGrammarLexer(new ANTLRInputStream(expr));
    lexer.removeErrorListeners();
    lexer.addErrorListener(ThrowingErrorListener.getInstance());
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    ConsoleGrammarParser parser = new ConsoleGrammarParser(tokens);
    parser.removeErrorListeners();
    parser.addErrorListener(ThrowingErrorListener.getInstance());
    return parser;
  }
}
