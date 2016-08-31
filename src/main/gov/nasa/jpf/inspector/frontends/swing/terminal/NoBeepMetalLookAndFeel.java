//
// Copyright (C) 2016 Petr Hudeček
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

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
