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


import javax.swing.text.*;
import gov.nasa.jpf.shell.util.EditorPanel;
import gov.nasa.jpf.shell.ShellManager;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Panel that serves as an editor for the file pointed to in the *.jpf file
 * denoted in the "jpf.app" property in
 * {@link gov.nasa.jpf.shell.ShellManager#getConfig() }. Implements basic
 * text editing features and syntax highlighting.
 */
public class PropertiesPanel extends EditorPanel{
  
  private static final String DEFAULT_TITLE = "Properties";
  private static final String DEFAULT_TIP = "Edit the Application Properties";
  private static final String MIME_TYPE = "text/jpfproperties";

  public PropertiesPanel(){
    super(DEFAULT_TITLE, null, DEFAULT_TIP);

    label.setText("Properties: ");
    
    String fname = ShellManager.getManager().getConfig().getProperty("jpf.app");
    initContents("JPF application properties", fname);    
  }

  protected PropertiesPanel (String title, Icon icon, String tip){
    super(title,icon,tip);
  }

  protected EditorKit getEditorKit() {
    return new StyledEditorKit() {
      @Override
      public Document createDefaultDocument(){
        return new PropertyDocument();
      }

      @Override
      public String getContentType(){
        return MIME_TYPE;
      }
    };

  }

	@Override
	protected void fileSaved(File f) {
		ShellManager.getManager().reloadAppProperties(loadedFile.getPath());
	}

	@Override
	protected void fileLoaded(File f) {
    if (initialized){
      // no need to rebuild if this is from the ctor
      ShellManager.getManager().reloadAppProperties(loadedFile.getPath());
    }
	}

  
  @Override
  protected FileNameExtensionFilter getFileFilter(){
    return new FileNameExtensionFilter("JPF application properties", "jpf");
  }
}

/**
 * A PropertyDocument is the document type used to represent a *.jpf file.
 * it includes the following styles:
 * <dl>
 *   <dt>key</dt><dd>The style to be applied to a property name</dd>
 *   <dt>value</dt><dd>The style to be applied to a property value</dd>
 *   <dt>comment</dt><dd>The style applied to a comment in the file.</dd>
 *   <dt>malformed</dt><dd>The style applied to a malformed line.</dd>
 *   <dt>assignment</dt><dd>The style applied to everything else (the '=' sign)</dd>
 * </dl>
 */
class PropertyDocument extends DefaultStyledDocument{

  private Style key;
  private Style value;
  private Style comment;
  private Style malformed;
  private Style assignment;

  public PropertyDocument(){
    super();

    key = addStyle("key", null);
    key.addAttribute(StyleConstants.Foreground, Color.BLUE);

    value = addStyle("value", null);
    value.addAttribute(StyleConstants.Foreground, Color.darkGray);

    comment = addStyle("comment", null);
    comment.addAttribute(StyleConstants.Foreground, Color.GRAY);

    malformed = addStyle("malformed", null);
    malformed.addAttribute(StyleConstants.Foreground, Color.RED);

    assignment = addStyle("assignment", null);
    assignment.addAttribute(StyleConstants.Foreground, Color.BLACK);
  }

  @Override
  public void insertString(int offs, String str, AttributeSet a) throws BadLocationException{
    super.insertString(offs, str, a);
    updateLines(offs, str.length());

  }

  @Override
  public void remove(int offset, int length) throws BadLocationException{
    super.remove(offset, length);
    updateLines(offset, 0);
  }

  /**
   * Checks a portion of a document's text to see what syntax highlighting 
   * should be applied.
   * @param offset which position in the document to start looking
   * @param length if the length of text that need be checked.
   * @throws BadLocationException
   */
  private void updateLines(int offset, int length) throws BadLocationException{

    int startLine = getDefaultRootElement().getElementIndex(offset);
    int endLine = getDefaultRootElement().getElementIndex(offset + length);

    for(int line = startLine; line <= endLine; line++)
      colorLine(line);

  }

  /**
   * Sets an entire line number to the correct color that it should be.
   * @param lineNum
   * @throws BadLocationException
   */
  private void colorLine(int lineNum) throws BadLocationException{
    int startOffset = getDefaultRootElement().getElement(lineNum).getStartOffset();
    int endOffset = getDefaultRootElement().getElement(lineNum).getEndOffset() - 1;
    String line = getText(startOffset, endOffset - startOffset);

    //First check if this uses the "+=" assignment operator
    int assignIndex = line.indexOf("+=");
    int assignLength = 2;

    if (assignIndex == -1){// += was not found, its probably an '=' assignment
      assignIndex = line.indexOf('=');
      assignLength = 1;
    }

    if (assignIndex > -1){//We have a key/value pair here
      //style the key
      setCharacterAttributes(startOffset, assignIndex, key, true);
      //style the value
      int valueOffset = startOffset + assignIndex + assignLength;
      setCharacterAttributes(valueOffset, line.length() - (assignIndex + assignLength), value, true);

      //Don't forget to style the '=' sign
      setCharacterAttributes(startOffset + assignIndex, assignLength, assignment, true);

    }else{//This line isn't a comment or an assignment, it's probably malformed
      setCharacterAttributes(startOffset, endOffset - startOffset + 1, malformed, true);
    }

    //And now after all of that, check to see if this has a comment in it
    int commentIndex = line.indexOf("#");
    if ( commentIndex != -1)
      setCharacterAttributes(startOffset + commentIndex, endOffset - (startOffset + commentIndex), comment, true);
  }

}
