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
package gov.nasa.jpf.shell.panels;

import gov.nasa.jpf.shell.*;
import gov.nasa.jpf.shell.commands.VerifyCommand;
import gov.nasa.jpf.shell.listeners.VerifyCommandListener;
import gov.nasa.jpf.shell.util.EditorPanel;
import java.io.File;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.StyledEditorKit;

/**
 * Panel that allows for scripts to be edited.
 */
public class ScriptPanel extends EditorPanel implements VerifyCommandListener{

  private static final String DEFAULT_TITLE = "Script";
  private static final String DEFAULT_TIP = "Edit the script file";

  public ScriptPanel(){
    super(DEFAULT_TITLE, null, DEFAULT_TIP);
    editor.setEditorKitForContentType("text/jpfscript", getKit());
    editor.setContentType("text/jpfscript");
    label.setText("Script: ");

    String scriptFile = getScriptFile();
    initContents("JPF script file", scriptFile);
  }

  @Override
  public void addedToShell(){
    super.addedToShell();
    ShellManager.getManager().addCommandListener(VerifyCommand.class, this);
  }

  @Override
  public void removedFromShell(){
    super.removedFromShell();
    ShellManager.getManager().removeCommandListener(this);
  }

  private EditorKit getKit() {
    return new StyledEditorKit() {
      @Override
      public Document createDefaultDocument(){
        return new ScriptDocument();
      }
    };
  }

  private String getScriptFile(){
    String text = textfield.getText();
    if ( text == null || text.isEmpty() || !new File(text).exists() )
      return ShellManager.getManager().getConfig().getProperty("awt.script");
    else
      return textfield.getText();
  }



  public void preCommand(VerifyCommand command) {
    String awt_script = getScriptFile();
    if ( awt_script != null )
      ShellManager.getManager().getConfig().setProperty("awt.script", awt_script);
  }

  public void postCommand(VerifyCommand command) {
    //Nothing to do here
  }

  public void afterJPFInit(VerifyCommand command) {
    //Nothing to do here
  }

	public void exceptionDuringVerify(Exception ex) {
		//No idea
	}
}

class ScriptDocument extends DefaultStyledDocument{
    
}
