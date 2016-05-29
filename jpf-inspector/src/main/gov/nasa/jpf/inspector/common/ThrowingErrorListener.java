package gov.nasa.jpf.inspector.common;

import gov.nasa.jpf.inspector.utils.parser.RecognitionRuntimeException;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class ThrowingErrorListener extends BaseErrorListener {
  private static final ThrowingErrorListener instance = new ThrowingErrorListener();

  public static ThrowingErrorListener getInstance() {
    return instance;
  }
  @Override
  public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
    throw new AntlrParseException(msg, charPositionInLine);
  }
}
