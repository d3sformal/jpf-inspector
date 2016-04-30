//
// Copyright (C) 2011 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
//
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
//
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//
package gov.nasa.jpf.shell.panels;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.util.ColumnInfo;
import gov.nasa.jpf.shell.util.PopupMenu;
import gov.nasa.jpf.shell.util.RLCTable;
import gov.nasa.jpf.shell.util.AnnotationTableModel;
import gov.nasa.jpf.shell.util.InternalRowMoveHandler;
import gov.nasa.jpf.util.FileUtils;
import gov.nasa.jpf.util.JPFSiteUtils;
import gov.nasa.jpf.util.Misc;
import gov.nasa.jpf.util.Pair;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.DropMode;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.TransferHandler;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


/**
 * shell panel to display and edit site.properties contents
 * 
 * The trick is to allow editing extensions through a table (add/remove/reorder
 * of project entries), and extensions *plus* other properties through the editor,
 * i.e. table and editor have to be synced in a reasonably intuitive way
 */
public class SitePanel extends PropertiesPanel implements TableModelListener {
  
	private static final String[] columns = {"", "name", "location"};
  
  static class ProjectEntry {
    @ColumnInfo(name="load", isEditable=true)
    Boolean isInitialized;
    
    @ColumnInfo(name="project")
    String name;
    
    @ColumnInfo(name="directory")
    String pathName;
    
    ProjectEntry (String name, String pathName, boolean isInitialized){
      this.name = name;
      this.pathName = pathName;
      this.isInitialized = isInitialized;
    }
  }
  
  static class SiteModel extends AnnotationTableModel<ProjectEntry> {
    
    String configPath;
    HashSet<String> skipList = new HashSet<String>();

    SiteModel (String pathName, Reader reader){
      configPath = pathName;
      loadSiteProperties( reader);
    }
    
    protected void loadSiteProperties (Reader reader){
      Config conf = new Config(reader);
      ArrayList<ProjectEntry> list = new ArrayList<ProjectEntry>();
      
      String[] ext = conf.getCompactTrimmedStringArray("extensions"); //  pathnames
      
      for (String projId : conf.getEntrySequence()){
        if ("extensions".equals(projId)){
          // we have to filter this out in case there is only a single project in
          // the list, in which case we find a jpf.properties under its value
          continue;
        }
        
        String v = conf.getString(projId);
        File projDir = new File(v);
        
        if (projDir.isDirectory()){
          File propFile = new File(projDir, "jpf.properties");
          if (propFile.isFile()){
            String projPathName = projDir.getPath();
            boolean isInitialized = Misc.contains(ext, projPathName);
            
            projPathName = FileUtils.asUnixPathName(projDir);
            
            ProjectEntry e = new ProjectEntry(projId, projPathName, isInitialized);
            list.add(e);
          }
        }
      }
            
      setData( list);
    }
    
    protected String checkSiteProperties(){
      ArrayList<ProjectEntry> data = getData();

      if (data.isEmpty()){
        return "empty projects list (need at least 'jpf-core')";
      }

      ProjectEntry pe = data.get(0);
      if (!"jpf-core".equals(pe.name)){
        return "first project entry has to be 'jpf-core'";
      }
      
      if (!pe.isInitialized){
        return "jpf-core needs to be loaded";
      }
      
      return null;
    }
    
    protected String writeSiteProperties(Writer writer, String editorContents) {
      PrintWriter pw = new PrintWriter(writer);
      ArrayList<String> initList = new ArrayList<String>();
      ArrayList<ProjectEntry> data = getData();
      
      pw.println("### jpf-shell generated site.properties");
      pw.println();
      
      for (ProjectEntry pe : data){
        skipList.add(pe.name);
        
        pw.print(pe.name);
        pw.print(" = ");
        pw.println( FileUtils.unixToUserPathName(pe.pathName));
        
        if (pe.isInitialized){
          initList.add(pe.name);
        }
      }
      
      pw.println();
      pw.print("extensions = ");
      skipList.add("extensions");
      
      int i=0;
      for (String s : initList){
        if (i++>0){
          pw.print(',');
        }
        pw.print("${");
        pw.print(s);
        pw.print('}');
      }
      
      pw.println();
      pw.println();
      
      // now check for any properties from siteProps that are not project entries
      // note we have to do this on the original, unexpanded source to preserve sysprop use
      if (editorContents != null && !editorContents.isEmpty()){
        try {
          for (Pair<String,String> kv : JPFSiteUtils.getRawEntries(new StringReader(editorContents))) {
            if (!skipList.contains(kv._1)) {
              pw.print(kv._1);
              pw.print(" = ");
              pw.println(kv._2);
            }
          }
        } catch (IOException iox) {
          return "error parsing editor contents as site properties";
        }
      }
      
      skipList.clear();
      return null;
    }
    
    @Override
    public void remove(int rowIdx) {
      // make sure we don't re-add this after it got removed
      ProjectEntry pe = getData().get(rowIdx);
      if (pe != null){
        skipList.add(pe.name);
      }
      super.remove(rowIdx);
    }
  }  
  
	private JTable table;
  private SiteModel siteModel;
  
  
  public SitePanel(){
    super("Site", null, "edit site properties");
    
    String sitePropLocation = getSitePropLocation();
    initContents("JPF site.properties", sitePropLocation);
  }

	@Override
	protected void fileSaved(File f) {
    setTableContents(siteModel);
		ShellManager.getManager().reloadSiteProperties(loadedFile.getPath());
	}

	@Override
	protected void fileLoaded(File f) {
    if (initialized){
      // no need to rebuild if this is from the ctor
      ShellManager.getManager().reloadSiteProperties(loadedFile.getPath());
    }
	}

  
  @Override
  protected PopupMenu createEditorPopupMenu(JEditorPane c){
    PopupMenu popup = super.createEditorPopupMenu(c);
    
    popup.addSeparator();
    popup.add( "Update Table", new AbstractAction(){
      public void actionPerformed(ActionEvent e) {
        updateTableContents();
      }      
    }, KeyStroke.getKeyStroke('T', KeyEvent.META_DOWN_MASK, true));
    
    return popup;
  }
  
  @Override
  protected void createLayout(){
    createTable();
    
    Box box = Box.createVerticalBox();
    
    box.add( createFilePanel() ); // the top text field for the site.properties path
   
    JSplitPane split = new JSplitPane( JSplitPane.VERTICAL_SPLIT, 
                             new JScrollPane(table), 
                             new JScrollPane(editor));

    split.setOneTouchExpandable(true);
    split.setDividerLocation(0.7);
    split.setResizeWeight(0.5);
    split.setMinimumSize(new Dimension(Short.MAX_VALUE,100));

    box.add( split);
    
    setLayout( new GridLayout());
    add( box);
  }
  
  protected void createTable(){
    
    table = new RLCTable();   
    
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setRowSelectionAllowed(true);
        
    //--- set popup menu
    PopupMenu popup = new PopupMenu(table);

    popup.add( "Save", new AbstractAction(){
      public void actionPerformed(ActionEvent e) {
        saveProjectList();
      }      
    });
    
    popup.add( "Save As..", new AbstractAction(){
      public void actionPerformed(ActionEvent e) {
        saveProjectListAs();
      }      
    });

    popup.addSeparator();
    
    popup.add( "Add..", new AbstractAction(){
      public void actionPerformed(ActionEvent e) {
        addProject();
      }      
    });

    popup.add( "Remove", new AbstractAction(){
      public void actionPerformed(ActionEvent e) {
        removeSelectedProject();
      }      
    });
    
    popup.addSeparator();
    
    popup.add( "Up", new AbstractAction(){
      public void actionPerformed(ActionEvent e) {
        moveSelectedProject(-1);
      }      
    }, KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.META_DOWN_MASK, true));

    popup.add( "Down", new AbstractAction(){
      public void actionPerformed(ActionEvent e) {
        moveSelectedProject(1);
      }      
    }, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.META_DOWN_MASK, true));

    //--- allow reordering by drag&drop of single rows
    table.setDragEnabled(true);
    table.setDropMode(DropMode.INSERT_ROWS);
    table.setTransferHandler( new InternalRowMoveHandler(table){
      @Override
      public boolean importData(TransferHandler.TransferSupport tfs) {
        if (super.importData(tfs)){
          updateEditorContents();
          markChange();
          return true;
        } else {
          return false;
        }
      }
    });
  } 
  
  protected void updateEditorContents(){
    StringWriter sw = new StringWriter();
    String curContents = getEditorContents();
    String errorMsg = siteModel.writeSiteProperties(sw, curContents);
    if (errorMsg != null){
      error(errorMsg);
    } else {
      setDocument(sw.toString());
    }
  }
  
  protected void updateTableContents(){
    String curContents = getEditorContents();
    StringReader reader = new StringReader(curContents);
    siteModel.loadSiteProperties(reader);
    
  }
  
  protected void addProject(){
    JFileChooser chooser = new JFileChooser(".."); // open in parent dir
    chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);

    chooser.setFileFilter( new FileFilter(){
      public boolean accept (File dir){
        return dir.isDirectory() && new File(dir, "jpf.properties").isFile();
      }
      
      public String getDescription(){
        return "JPF project directories";
      }
    });
    
    if (chooser.showDialog(getShell(), "Add Project") == JFileChooser.APPROVE_OPTION) {
      File dir = chooser.getSelectedFile();
      if (!new File(dir, "jpf.properties").isFile()){
        warning( "Not a Valid JPF Project");
      } else {
        String projectId = dir.getName();
        String projectDir = FileUtils.asUnixPathName(dir);
        ProjectEntry pe = new ProjectEntry(projectId, projectDir, false);

        siteModel.append(pe);
        
        markChange();
        updateEditorContents();
      }
    }   
  }
  
  protected void removeSelectedProject(){
    int rowIdx = table.getSelectedRow();
    if (rowIdx < 0){
      warning("please select a project");
      
    } else {
      siteModel.remove(rowIdx);
      
      markChange();
      updateEditorContents();
    }
  }
  
  protected void moveSelectedProject(int delta){
    int rowIdx = table.getSelectedRow();
    if (rowIdx < 0){
      warning("please select a project");
      
    } else {
      int newIdx = siteModel.move(rowIdx, delta);
      table.setRowSelectionInterval(newIdx, newIdx);
      
      // keep selection visible
      Rectangle rect = table.getCellRect(newIdx, 0, true);
      table.scrollRectToVisible(rect);
      
      markChange();
      updateEditorContents();
    }    
  }
  
  protected void saveTableContents(final File file){
    if (FileUtils.ensureDirs(file)){
      SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
        @Override
        public String doInBackground() {
          String errorMsg=null;
          try {
            errorMsg = siteModel.checkSiteProperties();
            if (errorMsg != null){
              error(errorMsg);
              return null;
            }
            
            /**
            StringWriter sw = new StringWriter();
            errorMsg = siteModel.writeSiteProperties(sw, getEditorContents());
            if (errorMsg != null){
              error(errorMsg);
              return null;
            }
            String contents = sw.toString();
            **/
            String contents = getEditorContents();
            
            FileUtils.setContents(file, contents);
            
            return contents;
            
          } catch (IOException iox) {
            error("failed to read file: " + file);
            return null;
          }
        }

        @Override
        protected void done() {
          try {
            String contents = get();
            if (contents != null){
              loadedFile = file;
              setPathName(file.getPath());
              setDocument(contents);
              unmarkChange();
            }
          } catch (Exception ex) {
            error("could not load new contents: " + ex);
          }
        }
      };
      
      worker.execute();
      
    } else {
      warning( "cannot create directories");
    }
  }
  
  protected void saveProjectList(){
    if (loadedFile == null){
      warning("no pathname specified");
    } else {
      saveTableContents(loadedFile);
    }
  }
  
  protected void saveProjectListAs(){
    JFileChooser chooser = new JFileChooser();
    chooser.setFileHidingEnabled(false);
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.setSelectedFile(loadedFile);
    
    FileNameExtensionFilter filter = getFileFilter();
    if (filter != null) {
      chooser.addChoosableFileFilter(filter);
    }
    
    if (chooser.showDialog(getShell(), "Save") == JFileChooser.APPROVE_OPTION) {
      saveTableContents( chooser.getSelectedFile());
    }
  }
  
  protected String getSitePropLocation(){
    Config config = ShellManager.getManager().getConfig();
    String loc = config.getString("jpf.site");
    
    if (loc == null){
      File siteProps = JPFSiteUtils.getStandardSiteProperties();
      loc = siteProps.getPath();
    }
    
    return loc;
  }

  protected void setTableContents (SiteModel newModel){
    siteModel = newModel;
    table.setModel(siteModel);
    
    siteModel.addTableModelListener(this);
    
    setColumnWidths(); // can't do that before we have a model
  }
  
  @Override
  public void tableChanged (TableModelEvent e){
    updateEditorContents();
    markChange();
  }
  
  protected void setColumnWidths (){
    TableColumnModel columnModel = table.getColumnModel();
    
    TableColumn column = columnModel.getColumn(0);
    column.setWidth(40);
    column.setResizable(false);
    
    columnModel.getColumn(1).setWidth(150);
    //columnModel.getColumn(2).setPreferredWidth(300);
  }
  
  @Override
  protected void loadFile (final File file){
    if (file.isFile()) {
      SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

        SiteModel newSiteModel;
        
        @Override
        public String doInBackground() {
          try {
            String contents = FileUtils.getContentsAsString(file);
            
            // we have to do this from the async worker part because it involves filesystem access
            StringReader reader = new StringReader(contents);
            newSiteModel = new SiteModel(file.getPath(), reader);
            
            return contents;
            
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
              textfield.setText(file.getPath());
              
              fileLoaded(file);
              
            } else {
              loadedFile = null;
              textfield.setText(null);
            }
          } catch (Exception ignore) {
            ignore.printStackTrace();
          }
          
          setTableContents(newSiteModel);
          
          unmarkChange();
        }
      };

      worker.execute();
      
    } else {
      warning("not a valid file: " + file.getPath());
    }
  }
  
  @Override
  protected FileNameExtensionFilter getFileFilter(){
    return new FileNameExtensionFilter("JPF site properties (*.properties)", "properties");
  }
  
  //--- editor save -> table update
  
  /**
   * this is executed outside of the EventDispatcher
   */
  @Override
  protected void saveContentsToFile(String contents, File file) {
    super.saveContentsToFile(contents, file);
    
    StringReader reader = new StringReader(contents);
    siteModel = new SiteModel(file.getPath(), reader);
  }
  
  
}
