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

import gov.nasa.jpf.inspector.exceptions.JPFInspectorParsingErrorException;

import java.io.IOException;
import java.io.StringReader;

import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Lexer;

/**
 * @author Alf
 * 
 */
public abstract class CommandParser implements CommandParserInterface {

  protected ConsoleGrammarParser getParser (String expr) throws JPFInspectorParsingErrorException {
    try {
      ;
      StringReader sr = new StringReader(expr);
      ANTLRReaderStream input;
      input = new ANTLRReaderStream(sr);
      Lexer lexer = new ConsoleGrammarLexer(input);
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      ConsoleGrammarParser parser = new ConsoleGrammarParser(tokens);
      return parser;
    } catch (IOException e) {
      throw new JPFInspectorParsingErrorException("IOException while parsing - " + e.getMessage(), expr, -1);
    }
  }

}
