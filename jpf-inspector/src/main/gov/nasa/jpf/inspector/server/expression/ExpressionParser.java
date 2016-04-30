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

package gov.nasa.jpf.inspector.server.expression;

import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.interfaces.exceptions.JPFInspectorParsingErrorException;
import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionStateAssignment;
import gov.nasa.jpf.inspector.server.expression.parser.ExpressionGrammarLexer;
import gov.nasa.jpf.inspector.server.expression.parser.ExpressionGrammarParser;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.utils.parser.GenericErrorRuntimeException;
import gov.nasa.jpf.inspector.utils.parser.JPFInspectorRuntimeParsingException;
import gov.nasa.jpf.inspector.utils.parser.RecognitionRuntimeException;

import java.io.IOException;
import java.io.StringReader;

import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.RecognitionException;

public class ExpressionParser implements ExpressionParserInterface {

  private final ExpressionFactory expFactory;

  /**
   * 
   * @param inspector JPF Inspector, owner of created expressions. Can be null.
   */
  public ExpressionParser (JPFInspector inspector) {
    // this.callbacks = inspector.getCallBack();
    this.expFactory = new ExpressionFactory(inspector);
  }

  @Override
  public ExpressionStateRootNode<?> getExpressionStateInterface (String expr) throws JPFInspectorParsingErrorException, JPFInspectorGenericErrorException {
    if (expr == null) {
      return null;
    }
    expr = expr.trim();
    // We want to process empty inputs (print) commands without parameters prints local variables

    final ExpressionGrammarParser parser = getParser(expr);
    try {
      return parser.cmdStateExpression(expFactory);

    } catch (RecognitionRuntimeException e) {
      throw new JPFInspectorParsingErrorException("Invalid input - " + e.getMessage(), expr, e.getRecognitionException());

    } catch (RecognitionException e) {
      throw new JPFInspectorParsingErrorException("Invalid input - " + e.getMessage(), expr, e);

    } catch (GenericErrorRuntimeException e) {
      // Unwrap checked exception
      throw e.getWrappedException();

    } catch (JPFInspectorRuntimeParsingException e) {
      // Unwrap checked exception
      throw e.getParsingErrorException();
    }

  }

  @Override
  public ExpressionBooleanInterface getBreakpointExpression (String expr) throws JPFInspectorParsingErrorException, JPFInspectorGenericErrorException {
    if (expr == null) {
      return null;
    }

    expr = expr.trim();
    if (expr.isEmpty()) {
      return null;
    }

    final ExpressionGrammarParser parser = getParser(expr);
    try {
      return parser.cmdBreakpointsCreateParams(expFactory);

    } catch (RecognitionRuntimeException e) {
      throw new JPFInspectorParsingErrorException("Invalid input - " + e.getMessage(), expr, e.getRecognitionException());

    } catch (RecognitionException e) {
      throw new JPFInspectorParsingErrorException("Invalid input - " + e.getMessage(), expr, e);

    } catch (GenericErrorRuntimeException e) {
      // Unwrap checked exception
      throw e.getWrappedException();

    } catch (JPFInspectorRuntimeParsingException e) {
      // Unwrap checked exception
      throw e.getParsingErrorException();
    }

  }

  private ExpressionGrammarParser getParser (String expr) throws JPFInspectorParsingErrorException, JPFInspectorGenericErrorException {
    try {
      StringReader sr = new StringReader(expr);
      ANTLRReaderStream input;
      input = new ANTLRReaderStream(sr);
      Lexer lexer = new ExpressionGrammarLexer(input);
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      ExpressionGrammarParser parser = new ExpressionGrammarParser(tokens);
      return parser;
    } catch (IOException e) {
      throw new JPFInspectorParsingErrorException("IOException while parsing- " + e.getMessage(), expr, -1);
    }
  }

  /* @see gov.nasa.jpf.inspector.server.expression.ExpressionParserInterface#getExpressionAssignment(java.lang.String) */
  @Override
  public ExpressionStateAssignment getExpressionAssignment (String expr) throws JPFInspectorParsingErrorException, JPFInspectorGenericErrorException {
    if (expr == null) {
      return null;
    }
    expr = expr.trim();
    // We want to process empty inputs (print) commands without parameters prints local variables

    final ExpressionGrammarParser parser = getParser(expr);
    try {
      return parser.cmdStateAssignment(expFactory);

    } catch (RecognitionRuntimeException e) {
      throw new JPFInspectorParsingErrorException("Invalid input - " + e.getMessage(), expr, e.getRecognitionException());

    } catch (RecognitionException e) {
      throw new JPFInspectorParsingErrorException("Invalid input - " + e.getMessage(), expr, e);

    } catch (GenericErrorRuntimeException e) {
      // Unwrap checked exception
      throw e.getWrappedException();

    } catch (JPFInspectorRuntimeParsingException e) {
      // Unwrap checked exception
      throw e.getParsingErrorException();
    }
  }

  /**
   * @return Gets expression factory used during parsing
   */
  @Override
  public ExpressionFactory getExpressionFactory () {
    return expFactory;
  }
}
