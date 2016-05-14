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

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.jvm.ClassInfo;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.report.Statistics;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class ProgressTrackerUI extends Box implements Runnable {
  
  static final int INTERVAL = 2000; // update every 2sec
  
  class JPFMonitor extends Thread {

    long tLast;
    
    public void run() {
      tLast = System.currentTimeMillis();

      while (jpf.getStatus() != JPF.Status.DONE){
        long t = System.currentTimeMillis();
        long td = t - tLast;
        if (td > INTERVAL){
          SwingUtilities.invokeLater(ProgressTrackerUI.this);
          tLast = t;
        } else {
          try {
            Thread.sleep(INTERVAL - td);
          } catch (InterruptedException ix){
            // we don't care
          }
        }
      }
    }
  }

  
  protected boolean stop;
  protected boolean foundErrors;

  protected String application;

  //--- the statistics data labels
  protected JLabel lStatus;
  protected JLabel lTime;
  protected JLabel lInsn;
  protected JLabel lMaxMem;
  
  protected JLabel lNewStates;
  protected JLabel lVisitedStates;
  protected JLabel lBacktrackedStates;
  protected JLabel lEndStates;
  
  protected JLabel lMaxDepth;
  protected JLabel lConstraints;
  
  protected JLabel lSignalCG;
  protected JLabel lLockCG;
  protected JLabel lSharedFieldCG;
  protected JLabel lDataCG;

  protected JLabel lMaxLiveObjects;
  protected JLabel lNewObjects;
  protected JLabel lReleasedObjects;

  protected JLabel lClasses;
  protected JLabel lMethods;
  
  protected JLabel lResult;
  
  JLabel[] dataFields;
  
  JPF jpf;
  Statistics stats;
  JPFMonitor monitor;
  long tStart;
  
  public ProgressTrackerUI(){
    super(BoxLayout.X_AXIS);
		createLabels();
  }

  // note this is called from the thread that runs JPF (not the EventDispatcher)
  public void attachJPF(JPF jpf){
    this.jpf = jpf;
    application = jpf.getConfig().getTarget();
    stats = jpf.getReporter().getRegisteredStatistics();
    monitor = new JPFMonitor();
    
    monitor.start();
    tStart = System.currentTimeMillis();
  }

  static final int VERTICAL_SPACING  = 5;
  static final int HORIZONTAL_SPACING  = 5;
  
  
  private void createLabels() {
    Box lBox = Box.createVerticalBox();
    Box rBox = Box.createVerticalBox();
    
    add( Box.createVerticalGlue());
    add( lBox);
    add( Box.createHorizontalStrut(HORIZONTAL_SPACING));
    add( rBox);
    add( Box.createVerticalGlue());
    
    // enforce equal left/right Box width
    Dimension fillerDim = new Dimension(300,5);
    lBox.add( new Box.Filler(null, fillerDim, fillerDim));
    rBox.add( new Box.Filler(null, fillerDim, fillerDim));
    
    //--- our statistics field names
    lBox.add( new RightAlignedLabel("status:"));
    lBox.add( new RightAlignedLabel("elapsed time:"));
    lBox.add( new RightAlignedLabel("instructions:"));
    lBox.add( new RightAlignedLabel("max memory (MB):"));

    lBox.add( Box.createVerticalStrut(VERTICAL_SPACING));
    lBox.add( new RightAlignedLabel("new states:"));
    lBox.add( new RightAlignedLabel("visited states:"));
    lBox.add( new RightAlignedLabel("backtracked states:"));
    lBox.add( new RightAlignedLabel("end states:"));

    lBox.add( Box.createVerticalStrut(VERTICAL_SPACING));
    lBox.add( new RightAlignedLabel("max search depth:"));
    lBox.add( new RightAlignedLabel("search constraints:"));

    lBox.add( Box.createVerticalStrut(VERTICAL_SPACING));    
    lBox.add( new RightAlignedLabel("signal CG:"));
    lBox.add( new RightAlignedLabel("lock CG:"));
    lBox.add( new RightAlignedLabel("shared field CG:"));
    lBox.add( new RightAlignedLabel("data CG:"));

    lBox.add( Box.createVerticalStrut(VERTICAL_SPACING));    
    lBox.add( new RightAlignedLabel("max live objects:"));
    lBox.add( new RightAlignedLabel("new objects:"));
    lBox.add( new RightAlignedLabel("released objects:"));

    lBox.add( Box.createVerticalStrut(VERTICAL_SPACING));
    lBox.add( new RightAlignedLabel("classes:"));
    lBox.add( new RightAlignedLabel("methods:"));

    
    //--- the statistics data 
    rBox.add( (lStatus = new JLabel("")));
    rBox.add( (lTime = new JLabel("")));
    rBox.add( (lInsn = new JLabel("")));
    rBox.add( (lMaxMem = new JLabel("")));

    rBox.add( Box.createVerticalStrut(VERTICAL_SPACING));
    rBox.add( (lNewStates = new JLabel("")));
    rBox.add( (lVisitedStates = new JLabel("")));
    rBox.add( (lBacktrackedStates = new JLabel("")));
    rBox.add( (lEndStates = new JLabel("")));

    rBox.add( Box.createVerticalStrut(VERTICAL_SPACING));
    rBox.add( (lMaxDepth = new JLabel("")));
    rBox.add( (lConstraints = new JLabel("")));

    rBox.add( Box.createVerticalStrut(VERTICAL_SPACING));
    rBox.add( (lSignalCG = new JLabel("")));
    rBox.add( (lLockCG = new JLabel("")));
    rBox.add( (lSharedFieldCG = new JLabel("")));
    rBox.add( (lDataCG = new JLabel("")));

    rBox.add( Box.createVerticalStrut(VERTICAL_SPACING));
    rBox.add( (lMaxLiveObjects = new JLabel("")));
    rBox.add( (lNewObjects = new JLabel("")));
    rBox.add( (lReleasedObjects = new JLabel("")));

    rBox.add( Box.createVerticalStrut(VERTICAL_SPACING));
    rBox.add( (lClasses = new JLabel("")));
    rBox.add( (lMethods = new JLabel("")));
    
    dataFields = new JLabel[]{ lStatus, lTime, lInsn, lMaxMem, lNewStates, lVisitedStates,
                   lBacktrackedStates, lEndStates, lMaxDepth, lConstraints,
                   lSignalCG, lLockCG, lSharedFieldCG, lDataCG, lMaxLiveObjects,
                   lNewObjects, lReleasedObjects, lClasses, lMethods };
    for (JLabel lbl : dataFields){
      lbl.setForeground(Color.BLUE);
    }
  }

	public void resetFields(){
    for (JLabel lbl : dataFields){
      lbl.setText(null);
    }
	}

  // to be called from the EventDispatchThread
  public void run(){

    updateTime();
    
    lStatus.setText( jpf.getStatus().name());
    lInsn.setText( Long.toString(stats.insns));
    lMaxMem.setText( Long.toString(stats.maxUsed >> 20));
  
    lNewStates.setText( Long.toString(stats.newStates));
    lVisitedStates.setText( Long.toString(stats.visitedStates));
    lBacktrackedStates.setText( Long.toString(stats.backtracked));
    lEndStates.setText( Long.toString(stats.endStates));
  
    lMaxDepth.setText( Integer.toString( stats.maxDepth));
    lConstraints.setText( Integer.toString( stats.constraints));
  
    lSignalCG.setText(Integer.toString( stats.signalCGs));
    lLockCG.setText( Integer.toString( stats.monitorCGs));
    lSharedFieldCG.setText( Integer.toString( stats.sharedAccessCGs));
    lDataCG.setText( Integer.toString( stats.dataCGs));

    lMaxLiveObjects.setText( Integer.toString( stats.maxLiveObjects));
    lNewObjects.setText( Long.toString( stats.nNewObjects));
    lReleasedObjects.setText( Long.toString(stats.nReleasedObjects));
    
    lClasses.setText( Integer.toString(ClassInfo.getNumberOfLoadedClasses()));
    lMethods.setText( Integer.toString(MethodInfo.getNumberOfLoadedMethods()));
  }
  
  static char[] tBuf = { '0', '0', ':', '0', '0', ':', '0', '0' };
  
  void updateTime() {
    long td = System.currentTimeMillis() - tStart;
    
    int h = (int) (td / 3600000);
    int m = (int) ((td / 60000) % 60);
    int s = (int) ((td / 1000) % 60);
    
    tBuf[0] = (char) ('0' + (h / 10));
    tBuf[1] = (char) ('0' + (h % 10));
    
    tBuf[3] = (char) ('0' + (m / 10));
    tBuf[4] = (char) ('0' + (m % 10));
    
    tBuf[6] = (char) ('0' + (s / 10));
    tBuf[7] = (char) ('0' + (s % 10));
    
    lTime.setText(new String(tBuf));
  }
  
}