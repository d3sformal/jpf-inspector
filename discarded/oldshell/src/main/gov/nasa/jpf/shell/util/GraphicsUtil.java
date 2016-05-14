package gov.nasa.jpf.shell.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.AbstractButton;
import javax.swing.UIManager;

public class GraphicsUtil {

  public static int BUTTON_OUTLINE_THICKNESS = 1;
  public static int BUTTON_OUTLINE_ROUNDNESS = 3;

  public static BasicStroke BUTTON_OUTLINE_STROKE = new BasicStroke(1);
  public static Color BUTTON_OUTLINE_COLOR = Color.BLACK;
  public final static Color BUTTON_MOUSEPRESSED_COLOR = UIManager.getColor("Tree.selectionBackground");

  public static void drawGradientBackground(Graphics g, Color c1, Color c2){
    Graphics2D g2 = (Graphics2D) g.create();
    Rectangle clipBounds = g.getClipBounds();
    int height = (int)clipBounds.getHeight();
    GradientPaint gradientPaint = new GradientPaint(0, 0, new Color(0xf2f2f2), 0, height, new Color(0xd9d9d9));
    g2.setPaint(gradientPaint);
    g2.fill(g2.getClip());
    g2.dispose();
  }

  //http://nadeausoftware.com/articles/2008/08/java_tip_use_gradient_backgrounds_add_interest_and_polish_user_interface
  public static void drawButtonBackground(Graphics g, AbstractButton button){
    if ( !(button.getModel().isPressed() || button.getModel().isRollover()) ) {
      //The button background is clear if it's not rolledover
      return;
    }

    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int width = button.getWidth() - (2*BUTTON_OUTLINE_THICKNESS);
    int height = button.getHeight() - (2*BUTTON_OUTLINE_THICKNESS);

    //The shape of the button being drawn
    RoundRectangle2D.Double buttonShape = new RoundRectangle2D.Double(BUTTON_OUTLINE_THICKNESS, BUTTON_OUTLINE_THICKNESS,
                       width, height,
                       BUTTON_OUTLINE_ROUNDNESS, BUTTON_OUTLINE_ROUNDNESS);


    if (button.getModel().isPressed()) {
      g2.setColor(BUTTON_MOUSEPRESSED_COLOR);
    } else{ //If the button is not pressed, then it is atleast being rolledover
      GradientPaint gp = new GradientPaint(0, 0, BUTTON_MOUSEPRESSED_COLOR.brighter(), 0, height, BUTTON_MOUSEPRESSED_COLOR);
      g2.setPaint(gp);
    }

    g2.fill(buttonShape);

    g2.setColor(BUTTON_OUTLINE_COLOR);
    g2.setStroke(BUTTON_OUTLINE_STROKE);
    g2.draw(buttonShape);

    g2.dispose();
  }
}
