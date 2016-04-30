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
package gov.nasa.jpf.shell.listeners;

import gov.nasa.jpf.shell.*;
import gov.nasa.jpf.shell.commands.VerifyCommand;
import gov.nasa.jpf.JPF;

/**
 * Command listener for the {@link gov.nasa.jpf.shell.commands.VerifyCommand}
 * class. The {@link #preCommand(gov.nasa.jpf.shell.ShellCommand)} executes 
 * before a JPF instance is created. {@link #afterJPFInit(gov.nasa.jpf.JPF) }
 * is run after JPF instance is created but before the {@link gov.nasa.jpf.JPF#run()}
 * method is run. The {@link #postCommand(gov.nasa.jpf.shell.ShellCommand)}
 * method is run after the jpf search comes to end for whatever reason.
 */
public interface VerifyCommandListener extends 
	ShellCommandListener<VerifyCommand>{

  /**
   * Called after the JPF instance is created but, before it is run. This is
   * when publishers and listeners can be added to JPF.
   * @param jpf
   */
  public void afterJPFInit(VerifyCommand command);

	public void exceptionDuringVerify(Exception ex);

}
