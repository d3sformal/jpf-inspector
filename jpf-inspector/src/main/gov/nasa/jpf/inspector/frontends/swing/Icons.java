///
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
///

package gov.nasa.jpf.inspector.frontends.swing;

import javax.swing.*;
import java.net.URL;

/**
 * This class, upon static initialization, loads icons that are used from the Program State Explorer.
 */
@SuppressWarnings({"WeakerAccess", "unused"}) // The icons are placeholders, probably.
public final class Icons {
  public static final ImageIcon leaf;
  public static final ImageIcon folderOpen;
  public static final ImageIcon folderClosed;
  public static final ImageIcon instanceField;
  public static final ImageIcon staticField;
  public static final ImageIcon thread;
  public static final ImageIcon stackFrame;
  public static final ImageIcon stackSlot;
  public static final ImageIcon unknownAttachmentKind;



  static {
    leaf = loadIcon("document");
    folderOpen = loadIcon("folder-open");
    folderClosed = loadIcon("folder");
    staticField = loadIcon("Static");
    instanceField = loadIcon("Instance");
    unknownAttachmentKind = loadIcon("Unknown");
    stackFrame = loadIcon("StackFrame");
    stackSlot = loadIcon("StackSlot");
    thread = loadIcon("Thread");
  }
  private static ImageIcon loadIcon(String name) {
    URL url = InspectorPrimaryConsolePanel.class.getResource("icons/" + name + ".png");
    ImageIcon imageIcon = new ImageIcon(url);
    return imageIcon;
  }
}
