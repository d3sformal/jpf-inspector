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
