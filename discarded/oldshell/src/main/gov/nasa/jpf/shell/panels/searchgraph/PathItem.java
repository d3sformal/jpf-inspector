package gov.nasa.jpf.shell.panels.searchgraph;

import gov.nasa.jpf.shell.util.GraphicsUtil;
import gov.nasa.jpf.shell.util.HyperlinkEditorPane;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JTextArea;

public class PathItem<E extends PathItemComponent> extends HyperlinkEditorPane {

  private static final int ROUNDNESS = 5;
  private static final Color SELECTED_COLOR
                                    = GraphicsUtil.BUTTON_MOUSEPRESSED_COLOR;

  private E item = null;
  private boolean isSelected = false;

  public PathItem(E item){
    super();
    this.item = item;
		setEditable(false);
    addHyperlinkPatterns(item.getHyperlinkPatterns());
		setText(item.getPathText());

    Dimension prefSize = new JTextArea(item.getPathText()).getMinimumSize();

    prefSize.width += ROUNDNESS*2;
    prefSize.height += ROUNDNESS*4;
    setPreferredSize(prefSize);
    setMinimumSize(prefSize);
  }

  @Override
  public void paintComponent(Graphics g){
    Graphics2D g2 = (Graphics2D)g.create();

    RoundRectangle2D perimeter = new RoundRectangle2D.Float(0, 0,
          getWidth() - (2*GraphicsUtil.BUTTON_OUTLINE_THICKNESS) - ROUNDNESS,
          getHeight() - (2 *GraphicsUtil.BUTTON_OUTLINE_THICKNESS),
          ROUNDNESS, ROUNDNESS);

    //Color in the background
    Color from = isSelected ? SELECTED_COLOR.brighter() : new Color(0xf2f2f2);
    Color to = isSelected ? SELECTED_COLOR : new Color(0xd9d9d9);
    g2.setPaint(new GradientPaint(0,0,from,0,(float)perimeter.getHeight(),to));
    g2.fill(perimeter);

    g2.setColor(isSelected ? Color.BLACK : Color.GRAY);
    //g2.setStroke(isSelected ? new BasicStroke(3f) : new BasicStroke(1f));
    g2.draw(perimeter);

    g2.dispose();

    //Indent the text that is getting written in
    g.translate(ROUNDNESS, ROUNDNESS);

    setOpaque(false);
    super.paintComponent(g);
    setOpaque(true);

  }

  void setSelected(boolean selected) {
    isSelected = selected;
    repaint();
  }

  public E getItem() {
    return item;
  }

}
