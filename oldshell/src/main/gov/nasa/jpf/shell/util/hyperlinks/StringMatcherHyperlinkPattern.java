package gov.nasa.jpf.shell.util.hyperlinks;

import java.awt.Color;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

public abstract class StringMatcherHyperlinkPattern implements HyperlinkPattern{

  private Pattern pattern;
  private Object result;

  public StringMatcherHyperlinkPattern(String str, Object res){
    this.result = res;
    this.pattern = Pattern.compile(Pattern.quote(str));
  }

  public StringMatcherHyperlinkPattern(String str){
    this(str, str);
  }

  public Pattern getPattern() {
    return pattern;
  }

  public Object getResult(String str, MatchResult m) {
    return this.result;
  }

  public abstract void onClick(Object result);

  public void setUnactiveStyle(Style style) {
    StyleConstants.setForeground(style, Color.BLUE);
  }

  public void setActiveStyle(Style style) {
    StyleConstants.setForeground(style, Color.BLUE);
    StyleConstants.setUnderline(style, true);
  }

  public String getTooltip(Object result) {
    return null;
  }

  public int getModifiers(){
    return 0;
  }
}
