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

package gov.nasa.jpf.inspector.server.expression;

import gov.nasa.jpf.inspector.common.AntlrParseException;
import gov.nasa.jpf.inspector.common.ThrowingErrorListener;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorGenericErrorException;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorParsingErrorException;
import gov.nasa.jpf.inspector.server.expression.expressions.ExpressionStateAssignment;
import gov.nasa.jpf.inspector.server.expression.generated.ExpressionGrammarLexer;
import gov.nasa.jpf.inspector.server.expression.generated.ExpressionGrammarParser;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.utils.expressions.FieldName;
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
  public FieldName getFieldName (String expr) throws JPFInspectorParsingErrorException, JPFInspectorGenericErrorException {
    if (expr == null) {
      return null;
    }
    expr = expr.trim();
    // We want to process empty inputs (print) commands without parameters prints local variables

    final ExpressionGrammarParser parser = getParser(expr);
    try {
      return parser.classAndFieldName().fn;

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
  private static ExpressionGrammarParser getParser(String expr) {
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
  public ExpressionFactory getExpressionFactory () {
    return expFactory;
  }
}
