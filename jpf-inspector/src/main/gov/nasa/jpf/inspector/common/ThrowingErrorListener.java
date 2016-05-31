package gov.nasa.jpf.inspector.common;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

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
