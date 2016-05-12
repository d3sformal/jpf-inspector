package gov.nasa.jpf.shell.util;

import gov.nasa.jpf.shell.panels.LoggingPanel;
import gov.nasa.jpf.shell.panels.LoggingPanel.ErrorType;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Receives errors and logs then to the error panel
 *
 * @author Sandro Badame <a href="mailto:s.badame@gmail.com">s.badame&amp;gmail.com</a>
 */
public class ErrorPanelHandler extends Handler {

  private LoggingPanel errpanel;
  private ErrorType category;

  public ErrorPanelHandler(ErrorType category, LoggingPanel panel){
    super();
    setLevel(Level.ALL);
    this.errpanel = panel;
    this.category = category;
  }

  @Override
  public void publish(LogRecord lr) {
    errpanel.publish(category, lr);
  }

  @Override
  public void flush() {}

  @Override
  public void close() throws SecurityException {}

}