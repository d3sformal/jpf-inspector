package gov.nasa.jpf.inspector.frontends.swing.terminal;

import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;

/**
 * A look and feel based on the normal look and feel, except that it prevents beeps.
 */
public class NoBeepMetalLookAndFeel extends MetalLookAndFeel {
  private static final long serialVersionUID = 8032572446478662742L;

  @Override
  public void provideErrorFeedback(Component component) {
    // Do not beep.
    //  super.provideErrorFeedback(component);
  }
}
