/* Copyright (C) 2008 United States Government as represented by the
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
*/
package gov.nasa.jpf.shell.panels;

import gov.nasa.jpf.shell.*;
import java.util.*;
import javax.swing.*;

import gov.nasa.jpf.shell.listeners.VerifyCommandListener;
import gov.nasa.jpf.shell.commands.VerifyCommand;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.report.ConsolePublisher;
import gov.nasa.jpf.report.Publisher;
import gov.nasa.jpf.report.PublisherExtension;
import gov.nasa.jpf.report.Reporter;
import gov.nasa.jpf.shell.util.ProgressTrackerUI;
import gov.nasa.jpf.shell.util.HyperlinkEditorPane;
import gov.nasa.jpf.shell.util.hyperlinks.BasicHyperLinkDecorator;
import gov.nasa.jpf.shell.util.hyperlinks.JavaSourceFileHyperlinkPattern;
import gov.nasa.jpf.shell.util.hyperlinks.StacktraceHyperlinkPattern;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Map.Entry;

/**
 * Basic output panel that divides JPF's results into browseable topics. This
 * panel uses a {@link gov.nasa.jpf.shell.listeners.VerifyCommandListener} to
 * keep track of when the VerifyCommand is executed.
 */
public final class ReportPanel extends ShellPanel implements VerifyCommandListener{

  private static final String PROGRESS = "PROGRESS";
  private static final String TOPICS = "TOPICS";

	//Topics Panel
	private JLabel statusLabel = new JLabel();
	private JSplitPane splitPane;
  private HyperlinkEditorPane outputArea;
  private TopicListModel topicListModel = new TopicListModel();
  private TopicPublisher topicPublisher;
  private JList topicList = new JList(topicListModel);

  private boolean isSaveable = false;

  public ReportPanel(){
    super("Report", null, "View JPF's Output");
    
    ShellManager.getManager().addCommandListener( VerifyCommand.class, this );

    //Prepare the list of topics
    topicList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    topicList.addMouseListener(new java.awt.event.MouseAdapter(){
      @Override
      public void mousePressed(java.awt.event.MouseEvent evt) {popupMenu(evt);}
      @Override
      public void mouseReleased(java.awt.event.MouseEvent evt) {popupMenu(evt);}
    });
    topicList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
      public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
        //Make sure this doesn't get called multiple times for one event
        if (evt.getValueIsAdjusting() == false){
          updateTextArea();
        }
      }
    });

    //Prepare the text area
    outputArea = new HyperlinkEditorPane();

    JavaSourceFileHyperlinkPattern jsf = new JavaSourceFileHyperlinkPattern();
    StacktraceHyperlinkPattern sh = new StacktraceHyperlinkPattern();
    outputArea.addHyperlinkPattern(jsf);
    outputArea.addHyperlinkPattern(sh);

    //Decorate!
    BasicHyperLinkDecorator decorator = new BasicHyperLinkDecorator();
    outputArea.setHyperlinkDecorator(jsf, decorator);
    outputArea.setHyperlinkDecorator(sh, decorator);

    outputArea.setEditable(false);    
    
    JScrollPane textScroll = new JScrollPane(outputArea);
		textScroll.getViewport().setBackground(Color.white);
    textScroll.setMinimumSize(new Dimension(100,50));

    JScrollPane listScroll = new JScrollPane(topicList);
    listScroll.setMinimumSize(new Dimension(100,50));

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(statusLabel);

    splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScroll, textScroll);
    splitPane.setOneTouchExpandable(true);
    splitPane.setDividerLocation(100);
		add(splitPane);
		
    statusLabel.setText("JPF has not been running yet");
  }

  private void updateTextArea() {
    String topic = (String) topicList.getSelectedValue();
    if (topic != null){
      String text = topicListModel.getTopics().get(topic).toString();
      outputArea.setText(text); //Keep the white space
      outputArea.setCaretPosition(0);
    }
  }

  private void popupMenu(java.awt.event.MouseEvent evt) {
    if ( isSaveable && evt.isPopupTrigger() ){
      JPopupMenu popup = new JPopupMenu();
      JMenuItem item = new JMenuItem("Save Result...");
      item.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e) {
          JFileChooser chooser = new JFileChooser();
          int result = chooser.showSaveDialog(null);

          if (result != JFileChooser.APPROVE_OPTION)
            return;

          final File file = chooser.getSelectedFile();
            if ( file.exists() && !approveOverwrite(file.getName()) )
              return;

          //Output the file in a different thread
          Runnable saveFile = new Runnable(){
          public void run(){
            try {
              file.createNewFile();
              PrintWriter out = new PrintWriter(file);
              String time = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z").format(new Date());
              out.print("Created: ");
              out.println(time);
              out.print("Created By: ");
              out.println(System.getProperty("user.name"));

              for(Entry<String, String> e : topicListModel.getTopics().entrySet())
                out.println(e.getValue());

              if ( out.checkError() ){
                getShell().error("There was an error saving the results.");
              }
              out.close();
            } catch (IOException ex) {
              getShell().error("File: " + file.getName() + " could not created.");
              return;
            }
          }
        };
        new Thread(saveFile).start();
        }
      });
      popup.add(item);
      popup.show(evt.getComponent(), evt.getX(), evt.getY());
    }
  }

  private boolean approveOverwrite(String fileName){
    String message = "File: " + fileName + " already exists.\n" +
                     "Are you sure that you want to overwrite its contents?";
    return JOptionPane.showConfirmDialog(null, message, "Overwrite " + fileName + " ?", JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION;
  }

  private void showResults(Map<String, String> topics){
    reset();
    isSaveable = true;
    topicListModel.setTopics(topics);
		topicList.repaint();
    topicList.setSelectedIndex(0);
  }

  private void reset(){
    isSaveable = false;
    topicListModel.clear();
    topicList.setModel(topicListModel);
    outputArea.setText("");
  }

  /**
   * requests focus from the Shell then adds the {@link gov.nasa.jpf.shell.panels.TopicPublisher} 
   * to the jpf config.
   * @param command
   */
  public void preCommand(final VerifyCommand command) {
    requestShellFocus();
  }

  /**
   * Once JPF creates an instance of the TopicPublisher it is grabbed after
   * initialization by the tracker.
   * @param command
   */
  public void afterJPFInit(VerifyCommand command) {
    JPF jpf = command.getJPF();
    Reporter reporter = jpf.getReporter();
    Config config = ShellManager.getManager().getConfig();

    topicPublisher = new TopicPublisher(config, reporter);
    reporter.addPublisher(topicPublisher);
    
    Runnable statUpdater = new Runnable(){
      public void run(){
        StringWriter sw = new StringWriter(512);
        PrintWriter pw = new PrintWriter(sw);
        topicPublisher.printStatistics(pw);
        pw.flush();
        outputArea.setText( sw.toString());
        pw.close();
        //showResults(topicPublisher.getResults());
      }
    };
    
    reset();
    statusLabel.setText("JPF is running...");
    statUpdater.run(); // to initialize the content
    
    int interval = config.getInt("shell.update_interval", 1000);
    JPFMonitor monitor = new JPFMonitor(jpf, statUpdater, interval);
    monitor.start();
  }

  /**
   * Just show the results of the JPF verification.
   * @param command
   */
  public void postCommand(VerifyCommand command) {

		if (command.errorOccured()) {
			statusLabel.setText("An Error occured during the verify, check the Logger Panel for more details");
			statusLabel.setForeground(Color.RED);
		}else{
			statusLabel.setText("The JPF run completed successfully");
			statusLabel.setForeground(Color.BLACK);
		}

    showResults( topicPublisher.getResults());
  }
  
	public void exceptionDuringVerify(Exception ex) {
  }
  
  
  class JPFMonitor extends Thread {
    JPF jpf;
    Runnable statsUpdater;
    int interval;
    
    JPFMonitor (JPF jpf, Runnable statsUpdater, int interval){
      this.jpf = jpf;
      this.statsUpdater = statsUpdater;
      this.interval = interval;
    }
    
    long tLast;
    
    public void run() {
      tLast = System.currentTimeMillis();

      while (jpf.getStatus() != JPF.Status.DONE){
        long t = System.currentTimeMillis();
        long td = t - tLast;
        if (td > interval){
          SwingUtilities.invokeLater(statsUpdater);
          tLast = t;
        } else {
          try {
            Thread.sleep(interval - td);
          } catch (InterruptedException ix){
            // we don't care
          }
        }
      }
    }
  }

  
  class TopicListModel extends AbstractListModel {

    private Map<String, String> topics = new HashMap<String, String>();

    public void setTopics(Map<String, String> topics) {
      this.topics = topics;
      fireIntervalAdded(this, 0, topics.size());
    }

    public void clear() {
      int size = getSize();
      topics.clear();
      fireIntervalRemoved(this, 0, size < 0 ? 0 : size);
    }

    public Map<String, String> getTopics() {
      return topics;
    }

    public int getSize() {
      return topics.size();
    }

    public Object getElementAt(int index) {
      return topics.keySet().toArray()[index];
    }
  }
  
  // <2do> why is this still a inner class, there is a toplevel TopicPublisher
  class TopicPublisher extends ConsolePublisher {

    private static final String NAME = "topic";
    private LinkedHashMap<String, String> topics;
    private StringWriter output;
    private String curTopic;

    public TopicPublisher(Config config, Reporter reporter) {
      super(config, reporter);
      
      topics = new LinkedHashMap<String, String>();
      openChannel();
      
      // <2do> temp fix to copy existing ConsolePublisher extensions - this does not catch dynamic ones!
      for (Publisher p : reporter.getPublishers()){
        if (p instanceof ConsolePublisher){
          for (PublisherExtension pe : p.getExtensions()){
            addExtension(pe);
          }
        }
      }
    }

    @Override
    public String getName() {
      return NAME;
    }

    @Override
    protected void setTopics() {
      setTopics("console");
      setTopics(NAME);
    }

    public Map<String, String> getResults() {
      return topics;
    }

    @Override
    protected void openChannel() {
      if (output == null){
        output = new StringWriter();
        out = new PrintWriter(output);
      }
    }

    @Override
    protected void closeChannel() {
      try {
        out.close();
        output.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }

    @Override
    public void publishTopicStart(String topic) {
      if (topic != null) {
        StringBuffer buff = output.getBuffer();
        if (buff.length() > 0) {
          topics.put(curTopic, buff.toString());
          buff.setLength(0); //reset the output buffer
        }
      }
      curTopic = topic;
    }

    @Override
    public void publishEpilog() {
      publishTopicStart("");
    }
    
    String getStatistics(){
      return topics.get(STATISTICS_TOPIC);
    }
  }
}


