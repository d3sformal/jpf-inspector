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

import gov.nasa.jpf.inspector.common.AntlrParseException;
import gov.nasa.jpf.inspector.common.ThrowingErrorListener;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorParsingErrorException;
import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionStateAssignment;
import gov.nasa.jpf.inspector.server.expression.generated.ExpressionGrammarLexer;
import gov.nasa.jpf.inspector.server.expression.generated.ExpressionGrammarParser;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.utils.parser.GenericErrorRuntimeException;
import gov.nasa.jpf.inspector.utils.parser.JPFInspectorRuntimeParsingException;
import gov.nasa.jpf.inspector.utils.parser.RecognitionRuntimeException;
import org.antlr.v4.runtime.ANTLRInputStream;

/**
 * This class encapsulates the ANTLR parser for expressions.
 */
public class ExpressionParser implements ExpressionParserInterface {

  private final ExpressionFactory expFactory;

  /**   *
   * @param inspector JPF Inspector, owner of created expressions. Can be null.
   */
  public ExpressionParser (JPFInspector inspector) {
    this.expFactory = new ExpressionFactory(inspector);
  }

  @Override
  public ExpressionStateRootNode getExpressionStateInterface (String expr) throws JPFInspectorParsingErrorException, JPFInspectorGenericErrorException {
    if (expr == null) {
      return null;
    }
    expr = expr.trim();
    // We want to process empty inputs (print) commands without parameters prints local variables

    final ExpressionGrammarParser parser = getParser(expr);
    try {
      return parser.cmdStateExpression(expFactory).expr;

    } catch (RecognitionRuntimeException e) {
      throw new JPFInspectorParsingErrorException("Invalid input - " + e.getMessage(), expr, e.getRecognitionException());

    } catch (AntlrParseException e) {
      throw new JPFInspectorParsingErrorException("Parse error: " + e.getMessage(), expr, e.getColumn());
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
      return parser.cmdBreakpointsCreateParams(expFactory).bp;

    } catch (RecognitionRuntimeException e) {
      throw new JPFInspectorParsingErrorException("Invalid input - " + e.getMessage(), expr, e.getRecognitionException());
    } catch (AntlrParseException e) {
      throw new JPFInspectorParsingErrorException("Parse error: " + e.getMessage(), expr, e.getColumn());
    } catch (GenericErrorRuntimeException e) {
      // Unwrap checked exception
      throw e.getWrappedException();

    } catch (JPFInspectorRuntimeParsingException e) {
      // Unwrap checked exception
      throw e.getParsingErrorException();
    }

  }

  private ExpressionGrammarParser getParser (String expr) throws JPFInspectorParsingErrorException, JPFInspectorGenericErrorException {
      ExpressionGrammarLexer lexer = new ExpressionGrammarLexer(new ANTLRInputStream(expr));
      lexer.removeErrorListeners();
      lexer.addErrorListener(ThrowingErrorListener.getInstance());
      org.antlr.v4.runtime.CommonTokenStream tokens = new org.antlr.v4.runtime.CommonTokenStream(lexer);
      ExpressionGrammarParser parser = new ExpressionGrammarParser(tokens);
      parser.removeErrorListeners();
      parser.addErrorListener(ThrowingErrorListener.getInstance());
      return parser;
  }

  @Override
  public ExpressionStateAssignment getExpressionAssignment (String expr) throws JPFInspectorParsingErrorException, JPFInspectorGenericErrorException {
    if (expr == null) {
      return null;
    }
    expr = expr.trim();
    // We want to process empty inputs (print) commands without parameters prints local variables

    final ExpressionGrammarParser parser = getParser(expr);
    try {
      return parser.cmdStateAssignment(expFactory).expr;

    } catch (RecognitionRuntimeException e) {
      throw new JPFInspectorParsingErrorException("Invalid input - " + e.getMessage(), expr, e.getRecognitionException());

    } catch (AntlrParseException e) {
      throw new JPFInspectorParsingErrorException("Parse error: " + e.getMessage(), expr, e.getColumn());
    }  catch (GenericErrorRuntimeException e) {
      // Unwrap checked exception
      throw e.getWrappedException();

    } catch (JPFInspectorRuntimeParsingException e) {
      // Unwrap checked exception
      throw e.getParsingErrorException();
    }
  }


  @Override
  public ExpressionFactory getExpressionFactory () {
    return expFactory;
  }
}
