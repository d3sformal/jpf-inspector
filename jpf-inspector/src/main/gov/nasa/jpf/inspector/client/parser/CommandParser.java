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

import gov.nasa.jpf.inspector.common.ThrowingErrorListener;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorParsingErrorException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

/**
 * @author Alf
 * 
 */
public abstract class CommandParser implements CommandParserInterface {

  protected ConsoleGrammarParser getParser (String expr)  {
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
