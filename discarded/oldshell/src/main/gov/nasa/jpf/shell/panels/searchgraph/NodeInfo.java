package gov.nasa.jpf.shell.panels.searchgraph;

import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;
import gov.nasa.jpf.shell.util.hyperlinks.HyperlinkDecorator;
import gov.nasa.jpf.shell.util.hyperlinks.HyperlinkPattern;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.util.Map;

public class NodeInfo implements PathItemComponent{

  private static final int PADDING = 5;
  private String info;

  public NodeInfo(){}

  public NodeInfo(String info){
    this.info = info;
  }

  @Override
  public String toString(){
    return info;
  }

  public String getNodeText(){
    return toString();
  }

  public String getPathText(){
    return info;
  }

  public Shape getNodeShape(GraphicsDecorator gd) {
    if (gd == null) {
      throw new NullPointerException();
    }
    int width = gd.getFontMetrics().stringWidth(getNodeText());
    int height = gd.getFontMetrics().getHeight();
    return new RoundRectangle2D.Float(-(width)/2, -(height)/2, width + PADDING, height + PADDING, PADDING, PADDING);
  }

  public String getToolTip() { return null; }

  public int getThread() {
    return -1;
  }

  public Map<HyperlinkPattern, HyperlinkDecorator> getHyperlinkPatterns() {
    return null;
  }


}
