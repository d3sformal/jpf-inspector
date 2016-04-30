package gov.nasa.jpf.shell.util.hyperlinks;

import javax.swing.text.AttributeSet;

public interface HyperlinkDecorator {
  public AttributeSet getActiveStyle(HyperlinkPattern hpe, Object result);
  public AttributeSet getInactiveStyle(HyperlinkPattern hpe, Object result);
}
