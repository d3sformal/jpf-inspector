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
package gov.nasa.jpf.shell.basicshell.images;

import java.net.URL;
import java.util.HashMap;
import javax.swing.ImageIcon;

/**
 * Class to help manage the icon resources of jpf-shell and basicshell. Use this
 * class to load images as needed
 */
public class JPFShell_Images {

  //--------------- Command Icons -------------------------------------
  public enum CommandIcon{ Test, Verify, }
  
  private static HashMap<CommandIcon, URL> cimap= new HashMap<CommandIcon, URL>(){{
    put(CommandIcon.Test, getClass().getResource("test.png"));
    put(CommandIcon.Verify, getClass().getResource("verify.png"));
  }};

  /**
   * Returns the icon for the given command
   * @param ci The enum representing the command
   * @param s The description wanted for the ImageIcon returned
   * @return an ImageIcon with the given icon and description
   */
  public static ImageIcon getCommandIcon(CommandIcon ci, String description){
    return new ImageIcon(cimap.get(ci), description);
  }


  //--------------- Status Icons -------------------------------------
  public enum StatusIcon{ TestOn, TestOff, VerifyOn, VerifyOff, Working}

  private static HashMap<StatusIcon, URL> simap= new HashMap<StatusIcon, URL>(){{
    put(StatusIcon.TestOn, getClass().getResource("teststatus_on.png"));
    put(StatusIcon.TestOff, getClass().getResource("teststatus_off.png"));
    put(StatusIcon.VerifyOn, getClass().getResource("verifystatus_on.png"));
    put(StatusIcon.VerifyOff, getClass().getResource("verifystatus_off.png"));
    put(StatusIcon.Working, getClass().getResource("working.gif"));
  }};

  public static ImageIcon getStatusIcon(StatusIcon si, String description){
    return new ImageIcon(simap.get(si), description);
  }

}
