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

import gov.nasa.jpf.Config;
import gov.nasa.jpf.report.ConsolePublisher;
import gov.nasa.jpf.report.Publisher;
import gov.nasa.jpf.report.PublisherExtension;
import gov.nasa.jpf.report.Reporter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Does everything that the ConsolePublisher does but stores it all into a 
 * HashMap with topic names as keys. This is used by ReportPanel to retrieve results
 * from JPF.
 * 
 * <2do> this should be a replacement for the ConsolePublisher, not an additional publisher.
 * If not, chances are we miss PublisherExtensions, and we do a lot of redundant output. Fix it!!
 * 
 * @see ReportPanel
 */
public class TopicPublisher extends ConsolePublisher{

  private static final String NAME = "topic";

  private LinkedHashMap<String, String> topics;
  private StringWriter output;

  private String curTopic;

  public TopicPublisher(Config config, Reporter reporter){
    super(config, reporter);
    topics = new LinkedHashMap<String, String>();
    
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
  public String getName(){
    return NAME;
  }

  @Override
  protected void setTopics(){
    setTopics("console");
    setTopics(NAME);
  }

  public Map<String,String> getResults(){
    return topics;
  }

  @Override
  protected void openChannel(){
      output = new StringWriter();
      out = new PrintWriter(output);
  }

  @Override
  protected void closeChannel(){
    try {
      out.close();
      output.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void publishTopicStart (String topic){
    if (topic != null){
      StringBuffer buff = output.getBuffer();
      if (buff.length() > 0){
        topics.put(curTopic, buff.toString() );
        buff.setLength(0); //reset the output buffer
      }
    }
    curTopic = topic;
  }

  @Override
  public void publishEpilog(){
    publishTopicStart("");
  }

}