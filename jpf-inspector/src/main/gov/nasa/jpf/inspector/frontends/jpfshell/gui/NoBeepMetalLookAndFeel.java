package gov.nasa.jpf.inspector.frontends.jpfshell.gui;

import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;

/**
 * A look and feel based on the normal look and feel, except that it prevents beeps.
 */
public class NoBeepMetalLookAndFeel extends MetalLookAndFeel {
  @Override
  public void provideErrorFeedback(Component component) {
    // Do not beep.
    //  super.provideErrorFeedback(component);
  }
}
