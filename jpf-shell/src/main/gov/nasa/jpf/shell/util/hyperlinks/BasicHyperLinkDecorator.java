package gov.nasa.jpf.shell.util.hyperlinks;

import java.awt.Color;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class BasicHyperLinkDecorator implements HyperlinkDecorator{

  private Color color = Color.BLUE;

  public BasicHyperLinkDecorator(){ }

  public BasicHyperLinkDecorator(Color c){
    this.color = c;
  }

  public AttributeSet getActiveStyle(HyperlinkPattern hpe, Object result) {
    SimpleAttributeSet as = new SimpleAttributeSet();
    StyleConstants.setForeground(as, color);
    StyleConstants.setUnderline(as, true);
    return as;
  }

  public AttributeSet getInactiveStyle(HyperlinkPattern hpe, Object result) {
    SimpleAttributeSet as = new SimpleAttributeSet();
    StyleConstants.setForeground(as, color);
    return as;
  }

}
