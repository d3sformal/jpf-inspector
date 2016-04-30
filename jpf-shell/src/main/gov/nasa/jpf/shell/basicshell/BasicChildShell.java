/* Copyright (C) 2007 United States Government as represented by the
 * Administrator of the National Aeronautics and Space Administration
 * (NASA).  All Rights Reserved.
 *
 * This software is distributed under the NASA Open Source Agreement
 * (NOSA), version 1.3.  The NOSA has been approved by the Open Source
 * Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
 * directory tree for the complete NOSA document.
 *
 * THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
 * KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
 * LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
 * SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
 * A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
 * THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
 * DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
 */
package gov.nasa.jpf.shell.basicshell;

import gov.nasa.jpf.shell.*;
import javax.swing.JPanel;

/**
 * This is the shell used as a child to the BasicShell it can hold tabs just
 * like BasicShell but does not have a list a of commands across the top.
 * This class is used for when a tab is dragged away from the main BasicShell.
 */
public class BasicChildShell extends BasicShell{

  /**
   * Only the main Shell contains a list of commands to be executed.
   * @param command does nothing, don't even bother.
   */
  @Override
  public void installCommand(final ShellCommand command){}

  /**
   * No need for a tool bar on child shells
   */
  @Override
  protected JPanel createToolBar(){return new JPanel();}
}
