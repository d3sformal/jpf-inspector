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
package gov.nasa.jpf.shell.util;

import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.ShellPanel;
import gov.nasa.jpf.util.FileUtils;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.Keymap;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

/**
 * A foundation class for creating text editors in the shell.
 */
public class EditorPanel extends ShellPanel {

  protected static final String CHANGE_MARK = "*";

  
  class DocumentModifierListener implements DocumentListener {
    public void insertUpdate(DocumentEvent e) {
      markChange();
    }
    public void removeUpdate(DocumentEvent e) {
      markChange();
    }
    public void changedUpdate(DocumentEvent e) {
      markChange();
    }
  }
  
  class UndoAction extends AbstractAction {

    public UndoAction() {
      super("Undo");
      setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
      try {
        undoManager.undo();
      } catch (CannotUndoException ex) {
        error("Unable to undo: " + ex);
      }
      updateUndoState();
      redoAction.updateRedoState();
    }

    protected void updateUndoState() {
      if (undoManager.canUndo()) {
        setEnabled(true);
        putValue(Action.NAME, undoManager.getUndoPresentationName());
      } else {
        setEnabled(false);
        putValue(Action.NAME, "Undo");
      }
    }
  }

  class RedoAction extends AbstractAction {

    public RedoAction() {
      super("Redo");
      setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
      try {
        undoManager.redo();
      } catch (CannotRedoException ex) {
        error("Unable to redo: " + ex);
      }
      updateRedoState();
      undoAction.updateUndoState();
    }

    protected void updateRedoState() {
      if (undoManager.canRedo()) {
        setEnabled(true);
        putValue(Action.NAME, undoManager.getRedoPresentationName());
      } else {
        setEnabled(false);
        putValue(Action.NAME, "Redo");
      }
    }
  }
  
  protected JLabel label = new JLabel("File: ", JLabel.RIGHT);
  protected JTextField textfield = new JTextField();  

  protected JEditorPane editor;
  
  //--- undo support for the editor
  protected UndoManager undoManager = new UndoManager();
  UndoAction undoAction;
  RedoAction redoAction;
  
  protected File loadedFile;
  protected boolean changed;
  protected DocumentListener modifierListener = new DocumentModifierListener();

  protected boolean initialized = false;
  
  public EditorPanel(String title, Icon icon, String tip){
    super(title, icon, tip);

    editor = createEditor();
    createLayout();
  }

  // to be called by concrete class once contents are initialized
  protected void setInitialized(boolean c){
    initialized = c;
  }
  
  /**
   * override this if you have additional components
   */
  protected void createLayout(){
    Box box = Box.createVerticalBox();
    box.add( createFilePanel() );
    box.add( new JScrollPane(editor) );
    setLayout(new GridLayout());
    add(box);    
  }

  protected JComponent createFilePanel () {
    Box box = Box.createHorizontalBox();

    setLabelSizeConstraints(label);
    box.add( label );

    textfield.addActionListener( new ActionListener() {
      public void actionPerformed (ActionEvent e) {
        fileNameEntered();
      }
    });
    setTextFieldSizeConstraints(textfield);
    box.add(textfield);

    JButton browse = new JButton("...");
    browse.addActionListener( new ActionListener() {
      public void actionPerformed (ActionEvent e) {
        chooseFileLoad();
      }
    });

    setButtonSizeConstraints(browse);
    box.add(browse);

    return box;
  }

  protected EditorKit getEditorKit(){
    return null; // means use DefaultEditorKit
  }
  
  protected JEditorPane createEditor(){
    JEditorPane editor = new JEditorPane();
        
    EditorKit editorKit = getEditorKit();
    if (editorKit != null){
      editor.setEditorKit(editorKit);
    }
    
    // so that we can set fonts
    editor.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);    
    Font font = getFont("shell.textfont", Font.MONOSPACED, 13, Font.PLAIN);
    editor.setFont(font);
    
    //--- set the popup menu
    PopupMenu popup = createEditorPopupMenu(editor);
    editor.setComponentPopupMenu(popup);
    
    return editor;
  }
  
  protected PopupMenu createEditorPopupMenu(JEditorPane c){
    PopupMenu popup = new PopupMenu(c);

    undoAction = new UndoAction();
    redoAction = new RedoAction();
        
    c.getDocument().addUndoableEditListener(new UndoableEditListener(){
      public void undoableEditHappened(UndoableEditEvent e) {
        UndoableEdit edit = e.getEdit();
        if (!(edit instanceof DefaultDocumentEvent) ||
              ((DefaultDocumentEvent) edit).getType() != DefaultDocumentEvent.EventType.CHANGE) {
          undoManager.addEdit(e.getEdit());
          undoAction.updateUndoState();
          redoAction.updateRedoState();
        }        
      }
    });
    
    popup.add("Undo", undoAction, KeyStroke.getKeyStroke('Z', KeyEvent.META_DOWN_MASK, true));
    popup.add("Redo", redoAction, KeyStroke.getKeyStroke('Y', KeyEvent.META_DOWN_MASK, true));

    popup.addSeparator();
    
    popup.add( "Save", new AbstractAction(){
      public void actionPerformed(ActionEvent e) {
       save();
      }      
    }, KeyStroke.getKeyStroke('S', KeyEvent.META_DOWN_MASK, true));

    popup.add( "Save As..", new AbstractAction(){
      public void actionPerformed(ActionEvent e) {
        chooseFileSave();
      }      
    });

    return popup;
  }
  
  protected void initContents(final String fileType, final String fileName){
    if (fileName != null){
      File file = new File(fileName);
      if (file.isFile()){
        loadFile(file);
      } else {
        /**
        // don't block construction of this pane
        SwingUtilities.invokeLater( new Runnable(){
          public void run(){
            warning("file does not exist: " + fileName);
          }
        });
        **/
      }
    } else {
      setInitialized(true);
    }
  }

  
  public File getLoadedFile(){
    return loadedFile;
  }

  public JEditorPane getEditor(){
    return editor;
  }
  
  public String getEditorContents(){
    return editor.getText();
  }
  
  protected void chooseFileSave(){
    JFileChooser chooser = new JFileChooser(loadedFile);
    chooser.setFileHidingEnabled(false);
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    
    FileNameExtensionFilter filter = getFileFilter();
    if (filter != null) {
      chooser.addChoosableFileFilter(filter);
    }
    
    if (chooser.showDialog(getShell(), "Save") == JFileChooser.APPROVE_OPTION) {
      saveFile( chooser.getSelectedFile());
    }    
  }
  
  protected void save(){
    saveFile(loadedFile);
  }

  protected Future<String> saveFile (final File file){
    if (!file.isDirectory() && (!file.exists() || file.canWrite())) {
      SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

        @Override
        public String doInBackground() {
          String contents = editor.getText();
          saveContentsToFile(contents, file);
          return contents;
        }

        @Override
        protected void done() {
          try {
            // no need to re-read the document
            fileSaved(file);
            
            loadedFile = file;
            textfield.setText(file.getPath());
            unmarkChange();
                
          } catch (Exception ignore) {
            ignore.printStackTrace();
          }
        }
      };

      worker.execute();
      return worker;
      
    } else {
      warning("not a valid file: " + file.getPath());
      return null;
    }
  }

  /**
   * override this if content to save needs post processing outside EventDispatcher 
   */
  protected void saveContentsToFile (String contents, File file){
    try {
      FileWriter writer = new FileWriter(file);
      writer.write(contents);
      writer.close();
    } catch (IOException iox) {
      error("failed to write file: " + file);
    }    
  }
  
  /**
   * override if saved file needs post processing within EventDispatcher
   */
  protected void fileSaved(File file) {
  }

  
  /**
   * override if there are special filename checks
   */
  protected void fileNameEntered(){
    loadFile( new File(textfield.getText()));
  }
  
  protected void chooseFileLoad(){
    JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
    FileNameExtensionFilter filter = getFileFilter();
    if (filter != null) {
      chooser.setFileFilter(filter);
    }
    
    if (chooser.showOpenDialog(getShell()) == JFileChooser.APPROVE_OPTION) {
      loadFile( chooser.getSelectedFile());
    }    
  }
  
  protected void loadFile (final File file){
    if (file.isFile()) {
      SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

        @Override
        public String doInBackground() {
          try {
            return FileUtils.getContentsAsString(file);
          } catch (IOException iox) {
            error("failed to read file: " + file);
            return null;
          }
        }

        @Override
        protected void done() {
          try {
            String newContents = get();
            if (setDocument(newContents)){           
              loadedFile = file;
              setPathName(file.getPath());
              
              fileLoaded(file);
              
            } else {
              loadedFile = null;
              textfield.setText(null);
            }
          } catch (Exception ignore) {
            ignore.printStackTrace();
          }
          unmarkChange();
          
          setInitialized(true);
        }
      };

      worker.execute();
      
    } else {
      warning("not a valid file: " + file.getPath());
    }
  }
  
  protected void setPathName (String pathName){
    textfield.setText(pathName);
  }
  
  /**
   * override this if there is special document processing needs
   */
  protected boolean setDocument(String newContents){
    boolean success = false;
    editor.getDocument().removeDocumentListener(modifierListener);
    
    try {
      Document doc = editor.getDocument();
      doc.remove(0, doc.getLength());
      doc.insertString(0, newContents, null);
      success = true;
      
    } catch (Exception ex) {
      error("document did not load: " + ex);
    }

    editor.getDocument().addDocumentListener(modifierListener);
    
    return success;
  }
  
  /**
   * override if there are special post processing needs
   * @param file - File source of new contents
   */  
  protected void fileLoaded (File file){
  }
  
  /**
   * override this for specific file types
   * @return FileNameExtensionFilter or null (all files)
   */
  protected FileNameExtensionFilter getFileFilter(){
    return null; // all file types
  }


  protected void markChange(){
    if (! changed ){
      setTitle(CHANGE_MARK + getTitle());
      changed = true;
    	ShellManager.getManager().updateShellPanel(this);
    }
  }

  protected void unmarkChange(){
    if (changed){
      setTitle(getTitle().substring(CHANGE_MARK.length()));
      changed = false;
      ShellManager.getManager().updateShellPanel(this);
    }
  }

  @Override
  public boolean closing(){
    if (!changed){
      return true;
    }
    
    getShell().requestFocus(this);
    //We have possible unsaved changes to the property file
    int answer =  JOptionPane.showConfirmDialog(
            getShell(),
            "The file: " + loadedFile.getName() + " has some unsaved changes.\nWould you like to save the changes?",
            "Save file?",
            JOptionPane.YES_NO_CANCEL_OPTION);

    if (answer == JOptionPane.YES_OPTION){
      Future<String> future = saveFile(loadedFile);
      if (future != null){
        try {
          future.get(3000, TimeUnit.MILLISECONDS);
        } catch (Exception cx){
          return false;
        }
        return true;     
      } else {
        return false;
      }
      
    } else if (answer == JOptionPane.NO_OPTION){
      return true;
      
    } else { // cancel close
      return false;
    }
  }


  private static void setTextFieldSizeConstraints (JTextField tf) {
    Dimension d = tf.getPreferredSize();
    tf.setMinimumSize( new Dimension(50, d.height));
    tf.setMaximumSize( new Dimension(10000, d.height));
  }

  private static void setButtonSizeConstraints (JButton btn) {
    Dimension d = btn.getPreferredSize();
    btn.setMinimumSize(d);
    btn.setMaximumSize(d);
  }

  private static void setLabelSizeConstraints(JLabel label) {
    Dimension d = label.getPreferredSize();
    Dimension fixed = new Dimension(90, d.height);
    label.setPreferredSize( fixed);
    label.setMinimumSize( fixed);
    label.setMaximumSize( fixed);
  }
}
